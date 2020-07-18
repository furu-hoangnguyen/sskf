package sskf.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import sskf.common.Constants;
import sskf.common.enums.EnumRequestStatus;
import sskf.common.enums.EnumRequestType;
import sskf.exception.SSKFException;
import sskf.mapper.ApprovalFlowDetailsMapper;
import sskf.mapper.MstRequestStatusesMapper;
import sskf.mapper.MstRequestTypesMapper;
import sskf.mapper.RequestAttachmentsMapper;
import sskf.mapper.RequestMapper;
import sskf.model.EmailModel;
import sskf.model.FileModel;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestAttachmentsEntity;
import sskf.model.entity.RequestNumbersEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.FilterRequest;
import sskf.model.request.RequestModel;
import sskf.model.request.RequestModelForApply;
import sskf.model.request.RequestModelForApproval;
import sskf.model.request.WrapperRequest;
import sskf.model.response.ApprovalPerson;
import sskf.model.response.ApprovalPersonsResponse;
import sskf.model.response.CountRequestResponse;
import sskf.model.response.FilterSelectResponse;
import sskf.model.response.LockModelResponse;
import sskf.model.response.MstRequestStatusesResponse;
import sskf.model.response.MstRequestTypesResponse;
import sskf.model.response.RequestResponse;
import sskf.repository.ApprovalFlowDetailsRepository;
import sskf.repository.MstApprovalFlowsRepository;
import sskf.repository.MstRequestCommentActionsRepository;
import sskf.repository.MstRequestStatusesRepository;
import sskf.repository.MstRequestTypeRepository;
import sskf.repository.OperationHistoriesRepository;
import sskf.repository.RequestAttachmentsRepository;
import sskf.repository.RequestNumbersRepository;
import sskf.repository.RequestsRepository;
import sskf.repository.ShainAddressRepository;
import sskf.repository.ShainRepository;
import sskf.service.ApprovalFlowDetailsService;
import sskf.service.AwsS3FileService;
import sskf.service.CommentsForDetailsService;
import sskf.service.EmailServices;
import sskf.service.OperationHistoriesService;
import sskf.service.RequestService;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;
import sskf.util.ConversionUtil;
import sskf.util.ObjectUtil;
import sskf.util.StringUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static sskf.specs.RequestSpecification.MST_REQUEST_STATUSES_NAME;
import static sskf.specs.RequestSpecification.filterRequestAll;
import static sskf.specs.RequestSpecification.filterRequestInProgress;
import static sskf.specs.RequestSpecification.filterRequestReject;
import static sskf.specs.RequestSpecification.filterRequestSendBack;
import static sskf.specs.RequestSpecification.hasAmount;
import static sskf.specs.RequestSpecification.hasAppliedAt;
import static sskf.specs.RequestSpecification.hasApplyNumber;
import static sskf.specs.RequestSpecification.hasNotDeleted;
import static sskf.specs.RequestSpecification.hasNumberOfStagnancyDay;
import static sskf.specs.RequestSpecification.hasPaymentScheduledDate;
import static sskf.specs.RequestSpecification.hasRequestExhibitionPromotions;
import static sskf.specs.RequestSpecification.hasRequestMannequinPromotions;
import static sskf.specs.RequestSpecification.hasRequestType;
import static sskf.specs.RequestSpecification.hasSettlementNumber;
import static sskf.specs.RequestSpecification.hasStatusCd;
import static sskf.specs.RequestSpecification.hasSupplierName;
import static sskf.specs.RequestSpecification.requestListApplyOrApprove;
import static sskf.specs.RequestSpecification.requestListConfirm;
import static sskf.specs.RequestSpecification.requestListCreate;

@Slf4j
@Service
public class RequestServiceImpl extends BaseServiceHasSearchRSQL<RequestsEntity> implements RequestService {

    @Autowired
    private AwsS3FileService awsS3FileService;

    @Autowired
    private MstRequestStatusesRepository mstRequestStatusesRepository;

    @Autowired
    private MstRequestTypeRepository mstRequestTypeRepository;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private ApprovalFlowDetailsService approvalFlowDetailsService;

    @Autowired
    private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;

    @Autowired
    private RequestAttachmentsMapper requestAttachmentsMapper;

    @Autowired
    private RequestAttachmentsRepository requestAttachmentsRepository;

    @Autowired
    private MstRequestTypesMapper mstRequestTypesMapper;

    @Autowired
    private MstRequestStatusesMapper mstRequestStatusesMapper;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainService shainService;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    @Autowired
    private CommentsForDetailsService commentsForDetailsService;

    @Autowired
    private RequestNumbersRepository requestNumbersRepository;

    @Autowired
    private MstApprovalFlowsRepository mstApprovalFlowsRepository;

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private ApprovalFlowDetailsMapper approvalFlowDetailsMapper;

    @Autowired
    private MstRequestCommentActionsRepository mstRequestCommentActionsRepository;

    @Autowired
    private OperationHistoriesRepository operationHistoriesRepository;

    @Autowired
    private ShainAddressRepository shainAddressRepository;
    @Override
    public CountRequestResponse countRequest() {
        try {
            String shainCd = shainService.getUserNameByToken();

            log.info("Begin service: countRequest");

            int countRequestCreate = requestsRepository.countRequestCreate(shainCd);

            int countRequestWaitingConfirm = requestsRepository.countRequestWaitingConfirm(shainCd);

            int countRequestWaitingApply = requestsRepository.countRequestWaitingApply(shainCd);

            int countRequestWaitingApprove = requestsRepository.countRequestWaitingApprove(shainCd);

            int countRequestWaitingConfirmSettlement = requestsRepository.countRequestWaitingConfirmSettlement(shainCd);

            CountRequestResponse countRequestResponse = new CountRequestResponse(countRequestCreate,
                    countRequestWaitingConfirm, countRequestWaitingApply, countRequestWaitingApprove, countRequestWaitingConfirmSettlement);

            return countRequestResponse;
        } finally {
            log.info("End service: countRequest");
        }
    }

    @Override
    public List<RequestResponse> getRequestListReuse(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: getRequestListReuse");
            String searchCondition = "isDeleted==0";
            if (StringUtil.isNotEmpty(searchRequest.getKeyword())) {
                searchCondition = searchCondition + ";" + searchRequest.getKeyword();
            }

            searchRequest.setKeyword(searchCondition);
            List<RequestsEntity> requestsEntityList = listRSQL(requestsRepository, searchRequest, RequestsEntity.class);

            return requestMapper.toResponseList(requestsEntityList);
        } finally {
            log.info("End service: getRequestListReuse");
        }
    }

    @Override
    public Page<RequestResponse> getRequests(String wrapperRequestJson) throws JsonProcessingException {
        try {
            log.info("Begin service: getRequests");

            String shainCd = shainService.getUserNameByToken();
            WrapperRequest wrapperRequest = ConversionUtil.convertJsonToWrapperRequest(wrapperRequestJson);
            Pageable pageable = buildPageable(wrapperRequest.getPage(), wrapperRequest.getField(),
                    wrapperRequest.getOrderBy(), wrapperRequest.getLimit());

            Specification<RequestsEntity> filterConditions = buildFilterConditions(shainCd, wrapperRequest.getType(), wrapperRequest.getFilterRequest());

            Page<RequestsEntity> requestsEntities = requestsRepository.findAll(filterConditions, pageable);

            return requestsEntities.map(requestMapper::toResponse);

        } finally {
            log.info("End service: getRequests");
        }
    }


    private Pageable buildPageable(int page, String field, String orderBy, int limit) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (orderBy.equals(Constants.SORT_TYPE_DESC)) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, field);

        Pageable pageable = PageRequest.of(page, limit, sort);

        return pageable;
    }

    private Specification<RequestsEntity> buildFilterConditions(String shainCd, Integer type, FilterRequest filterRequest) {

        Specification<RequestsEntity> specification = null;

        if (type == 3) { // In-progress
            specification = filterRequestInProgress(shainCd, filterRequest);
        } else if (type == 4) { // All
            specification = filterRequestAll(shainCd, filterRequest);
        } else if (type == 5) { // Sendback
            specification = filterRequestSendBack(shainCd, filterRequest);
        } else if (type == 6) { // Reject
            specification = filterRequestReject(shainCd, filterRequest);
        } else if (type == 7) { // Create
            specification = requestListCreate(shainCd).and(hasStatusCd(MST_REQUEST_STATUSES_NAME.get(0)));
        } else if (type == 8) { // Confirm
            specification = requestListConfirm(shainCd).and(hasStatusCd(MST_REQUEST_STATUSES_NAME.get(1)));
        } else if (type == 9) { // Apply
            specification = requestListApplyOrApprove(shainCd).and(hasStatusCd(MST_REQUEST_STATUSES_NAME.get(2)));
        } else if (type == 10) { // Approve
            specification = requestListApplyOrApprove(shainCd).and(hasStatusCd(MST_REQUEST_STATUSES_NAME.get(3)));
        } else if (type == 11) { // Confirm settlement
            specification = hasStatusCd(MST_REQUEST_STATUSES_NAME.get(5));
        }

        if (ObjectUtils.isEmpty(specification)) {
            return buildQueryCondtionByFixedFilter(filterRequest);
        }

        return specification.and(buildQueryCondtionByFixedFilter(filterRequest));
    }

    private Specification<RequestsEntity> buildQueryCondtionByFixedFilter(FilterRequest filterRequest) {
        Specification<RequestsEntity> specification = getBaseFilterQuery();
        if (!ObjectUtils.isEmpty(filterRequest)) {
            specification = specification.and(hasApplyNumber(filterRequest.getApplyNumber()))
                    .and(hasSettlementNumber(filterRequest.getSettlementNumber()))
                    .and(hasRequestType(filterRequest.getRequestType()))
                    .and(hasSupplierName(filterRequest.getSupplierName()))
                    .and(hasAppliedAt(filterRequest.getAppliedAtMin(), filterRequest.getAppliedAtMax()))
                    .and(hasPaymentScheduledDate(filterRequest.getPaymentScheduledDateMin(), filterRequest.getPaymentScheduledDateMax()))
                    .and(hasNumberOfStagnancyDay(filterRequest.getNumberOfStagnancyDay()))
                    .and(hasAmount(filterRequest.getAmountMin(), filterRequest.getAmountMax()));
        }
        return specification;
    }

    private Specification<RequestsEntity> getBaseFilterQuery() {
        return hasNotDeleted().and(hasRequestExhibitionPromotions()).and(hasRequestMannequinPromotions());
    }

    @Override
    @Transactional
    public RequestsEntity create(RequestModel requestModel, MultipartFile[] files, String requestType) {
        try {
            log.info("Begin service: create Requests");
            RequestsEntity requestsEntity = requestMapper.toEntity(requestModel);
            requestsEntity.setInitialValue();
            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                setStatusForRequest(requestsEntity, "作成中");
            } else {
                if (Constants.ACCOUNT_RECEIVABLE.equals(requestType)) {
                    setStatusForRequest(requestsEntity, "確認待ち");
                } else {
                    setStatusForRequest(requestsEntity, "申請待ち");
                }
                requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
                requestsEntity.setIsAlertedForRemind((byte) 0);
            }
            MstRequestTypesEntity mstRequestTypesEntity = mstRequestTypeRepository.findByName(requestType);
            if (ObjectUtils.isEmpty(mstRequestTypesEntity)) {
                throw new SSKFException("Request type", "Request type invalid~");
            }

            requestsEntity.setMstRequestTypesEntity(mstRequestTypesEntity);
            requestsEntity = requestsRepository.save(requestsEntity);

            ApprovalFlowDetailsEntity approvalFlowDetailsEntity = approvalFlowDetailsService.createUserApply(requestModel.getApprovalFlowDetailsRequest(), requestsEntity);
            Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntitieSet = new HashSet<>();
            approvalFlowDetailsEntitieSet.add(approvalFlowDetailsEntity);
            requestsEntity.setApprovalFlowDetailsEntities(approvalFlowDetailsEntitieSet);
            return requestsEntity;
        } finally {
            log.info("End service: create Requests");
        }

    }

    @Override
    @Transactional
    public RequestsEntity updateCreate(RequestModel requestModel, MultipartFile[] files, String requestType) {
        try {
            log.info("Begin service: updateCreate Requests");
            if (ObjectUtils.isEmpty(requestModel.getCd())) {
                throw new SSKFException("400", "Request cd  required!");
            }
            RequestsEntity requestsEntity = requestsRepository.getOne(requestModel.getCd());
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "Requests not existed!");
            }
            if (!requestsEntity.getMstRequestStatusesEntity().getName().equals("作成中")) {
                throw new SSKFException("403", "Cannot complete create request!");
            }
            if (checkLockRequest(requestsEntity)) {
                throw new SSKFException("403", "Request is locked");
            }

            requestsEntity = requestMapper.toEntityUpdated(requestsEntity, requestModel);
            requestsEntity.setStepNumber((byte) 1);
            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                setStatusForRequest(requestsEntity, "作成中");
            } else {
                if (Constants.ACCOUNT_RECEIVABLE.equals(requestType)) {
                    setStatusForRequest(requestsEntity, "確認待ち");
                } else {
                    setStatusForRequest(requestsEntity, "申請待ち");
                }
                requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
                requestsEntity.setIsAlertedForRemind((byte) 0);
            }
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsEntity = requestsRepository.save(requestsEntity);
            ApprovalFlowDetailsEntity approvalFlowDetailsEntity = approvalFlowDetailsService.updateUserApply(requestModel.getApprovalFlowDetailsRequest(), requestsEntity);
            Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntitieSet = new HashSet<>();
            approvalFlowDetailsEntitieSet.add(approvalFlowDetailsEntity);
            requestsEntity.setApprovalFlowDetailsEntities(approvalFlowDetailsEntitieSet);

            return requestsEntity;
        } finally {
            log.info("End service: updateCreate Requests");
        }

    }

    @Override
    public RequestsEntity update(RequestModel requestModel, MultipartFile[] files) {
        try {
            log.info("Begin service: update Requests");
            if (ObjectUtils.isEmpty(requestModel.getCd())) {
                throw new SSKFException("400", "Request cd  required!");
            }
            RequestsEntity requestsEntity = requestsRepository.getOne(requestModel.getCd());
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "Requests not existed!");
            }
            if (checkLockRequest(requestsEntity)) {
                throw new SSKFException("403", "Request is locked");
            }

            requestsEntity = requestMapper.toEntityUpdated(requestsEntity, requestModel);
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            if (ObjectUtil.isNotEmpty(requestModel.getPaymentOn())) {
                requestsEntity = setSettlementNumber(requestsEntity, requestModel);
            }
            requestsEntity = requestsRepository.save(requestsEntity);
            approvalFlowDetailsService.updateUserApply(requestModel.getApprovalFlowDetailsRequest(), requestsEntity);
            uploadFileForRequests(files, requestsEntity, Boolean.TRUE);
            return requestsEntity;
        } finally {
            log.info("End service: update Requests");
        }
    }

    @Override
    @Transactional
    public RequestsEntity requestSalesChargeEdit(Long requestCd) {
        try {
            log.info("Begin service RequestServiceImpl: update requestSalesChargeEdit");
            RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
            if (ObjectUtil.isEmpty(requestsEntity)) {
                throw new SSKFException("Request", "Request not found!");
            }

            if (checkLockRequest(requestsEntity)) {
                throw new SSKFException("403", "Request is locked!");
            }
            if (ObjectUtil.isNotEmpty(requestsEntity.getRequestedAt()) && CollectionUtil.isEmpty(checkPermissionUserForApproval(requestsEntity.getCd(), (byte) 1))) {
                throw new SSKFException("401", "Cannot request sales charge edit! Not permission");
            }

            setStatusForRequest(requestsEntity, "確認待ち");
            requestsEntity.setIsSentBack((byte) 0);
            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsEntity = requestsRepository.save(requestsEntity);

            return requestsEntity;
        } finally {
            log.info("End service RequestServiceImpl: update requestSalesChargeEdit");
        }
    }

    @Override
    @Transactional
    public RequestsEntity confirm(RequestsEntity requestsEntity) {
        try {
            log.info("Begin service RequestServiceImpl: confirm Requests");
            requestsEntity.setStepNumber((byte) 1);
            setStatusForRequest(requestsEntity, "申請待ち");
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsEntity = requestsRepository.save(requestsEntity);
            return requestsEntity;
        } finally {
            log.info("End service RequestServiceImpl: confirm Requests");
        }
    }

    @Override
    public RequestsEntity getRequestByCd(Long requestCd) {
        return requestsRepository.getOne(requestCd);
    }

    @Override
    public FilterSelectResponse getFilterSelect() {

        try {
            log.info("Begin service: getFilterSelect");

            List<MstRequestTypesEntity> mstRequestTypesEntities = mstRequestTypeRepository.findAll();
            List<MstRequestStatusesEntity> mstRequestStatusesEntities = mstRequestStatusesRepository.findAll();

            List<MstRequestTypesResponse> mstRequestTypesResponses = mstRequestTypesMapper.toResponseList(mstRequestTypesEntities);
            List<MstRequestStatusesResponse> mstRequestStatusesResponses = mstRequestStatusesMapper.toResponseList(mstRequestStatusesEntities);

            return new FilterSelectResponse(mstRequestTypesResponses, mstRequestStatusesResponses);

        } finally {
            log.info("End service: getFilterSelect");
        }

    }

    @Override
    public List<Long> countRequestListDefault() {
        try {
            String shainCd = shainService.getUserNameByToken();

            log.info("Begin service: countRequestListDefault");

            List<Long> list = new ArrayList<>();
            for (int i = 4; i < 7; i++) {
                Specification<RequestsEntity> filterConditions = buildFilterConditions(shainCd, i, null);
                list.add(requestsRepository.count(filterConditions));
            }

            return list;
        } finally {
            log.info("End service: countRequestListDefault");
        }
    }

    @Override
    @Transactional
    public void deleteRequest(Long cd) {
        try {
            log.info("Begin service RequestServiceImpl: deleteRequest");
            RequestsEntity requestsEntity = requestsRepository.findByCdAndIsDeleted(cd, Constants.NOT_DELETED);
            if (ObjectUtil.isNotEmpty(requestsEntity)) {
                if (checkLockRequest(requestsEntity)) {
                    throw new SSKFException("403", "Request is locked");
                }
                MstRequestStatusesEntity mstRequestStatusesEntity = requestsEntity.getMstRequestStatusesEntity();
                if (!mstRequestStatusesEntity.getName().equals("作成中")) {
                    if ( ObjectUtil.isNotEmpty(requestsEntity.getRequestedAt()) || CollectionUtil.isEmpty(checkPermissionUserForApproval(requestsEntity.getCd(), (byte) 1))) {
                        throw new SSKFException("403 ", "Access denied!");
                    }
                }

                requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
                requestsEntity.setIsDeleted(Constants.IS_DELETED);
                requestsEntity.setEditShainEntity(null);
                requestsEntity.setStartedEditAt(null);
                requestsEntity = requestsRepository.save(requestsEntity);
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "削除", null, false, null);
            }
        } finally {
            log.info("End service RequestServiceImpl: deleteRequest");
        }
    }

    @Override
    @Transactional
    public void lockRequest(RequestsEntity requestsEntity) {
        try {
            log.info("Begin service RequestServiceImpl: lockRequest");
            String userLogin = shainService.getUserNameByToken();
            if (!checkLockRequest(requestsEntity)) {
                ShainEntity editUserUpdated = shainRepository.getOne(userLogin);
                requestsEntity.setEditShainEntity(editUserUpdated);
                requestsEntity.setStartedEditAt(LocalDateTime.now());
                requestsRepository.save(requestsEntity);
            }
        } finally {
            log.info("End service RequestServiceImpl: lockRequest");
        }
    }

    @Override
    @Transactional
    public void unLockRequest(Long cd) {
        try {
            log.info("Begin service RequestServiceImpl: unLockRequest");
            String userLogin = shainService.getUserNameByToken();
            RequestsEntity requestsEntity = requestsRepository.getOne(cd);
            ShainEntity editUser = requestsEntity.getEditShainEntity();
            if (ObjectUtil.isNotEmpty(editUser)) {
                if (StringUtil.isNotEmpty(editUser.getShainCd()) && !editUser.getShainCd().equals(userLogin)) {
                    throw new SSKFException("403", "Cannot unlock request!");
                }
            }
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsRepository.save(requestsEntity);
        } finally {
            log.info("End service RequestServiceImpl: unLockRequest");
        }
    }

    @Override
    public Boolean checkPermissionUserForApply(Long requestCd) {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String searchKey = "shainEntity.shainCd=='" + shainService.getUserNameByToken() + "';requestsEntity.cd==" + requestCd + ";stepNumber==1";
        baseSearchRequest.setKeyword(searchKey);
        return CollectionUtil.isNotEmpty(approvalFlowDetailsService.list(baseSearchRequest));
    }

    @Override
    public List<ApprovalFlowDetailsEntity> checkPermissionUserForApproval(Long requestCd, Byte stepNumber) {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String searchKey = "shainEntity.shainCd=='" + shainService.getUserNameByToken() + "';requestsEntity.cd==" + requestCd + ";stepNumber>=" + stepNumber;
        baseSearchRequest.setKeyword(searchKey);
        return approvalFlowDetailsService.list(baseSearchRequest);
    }


    @Override
    public ApprovalPersonsResponse findApprovalPersons(String mstApprovalFlowCd, String requestCd) {

        ApprovalPersonsResponse approvalPersons =new ApprovalPersonsResponse();

        List<String> shainCdForApprovalStep2 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowCd, 2);
        List<ShainEntity> shainForApprovalStep2 = shainRepository.findAllById(shainCdForApprovalStep2);
        approvalPersons.setFirstApprovalPersons(mappingApprovalPersonList(shainForApprovalStep2, (byte) 2));

        List<String> shainCdForApprovalStep3 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowCd, 3);
        List<ShainEntity> shainForApprovalStep3 = shainRepository.findAllById(shainCdForApprovalStep3);
        approvalPersons.setSecondApprovalPersons(mappingApprovalPersonList(shainForApprovalStep3, (byte) 3));

        List<String> shainCdForApprovalStep4 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowCd, 4);
        List<ShainEntity> shainForApprovalStep4 = shainRepository.findAllById(shainCdForApprovalStep4);
        approvalPersons.setThirdApprovalPersons(mappingApprovalPersonList(shainForApprovalStep4, (byte) 4));

        List<String> shainCdForApprovalStep5 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowCd, 5);
        List<ShainEntity> shainForApprovalStep5 = shainRepository.findAllById(shainCdForApprovalStep5);
        approvalPersons.setSettlementApprovalPersons(mappingApprovalPersonList(shainForApprovalStep5, (byte) 5));

        approvalPersons.setApplyPersons(approvalFlowDetailsService.findApplyPersons(requestCd));
        return approvalPersons;
    }

    private List<ApprovalPerson> mappingApprovalPersonList(List<ShainEntity> shainEntityList, Byte stepNumber) {
        List<ApprovalPerson> approvalPersonList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(shainEntityList)) {
            approvalPersonList = shainEntityList.stream().map(e -> approvalFlowDetailsMapper.toResponseApprovalPerson(e, stepNumber, false)).collect(Collectors.toList());
        }
        return approvalPersonList;
    }


    @Override
    public ApprovalPersonsResponse findSavedApprovalPersons(String requestCd) {

        BaseSearchRequest request = new BaseSearchRequest();
        String searchKey = "requestsEntity.cd==" + requestCd;
        request.setKeyword(searchKey + ";stepNumber==1");
        List<ApprovalPerson> applyPersons = approvalFlowDetailsService.listResponse(request);
        request.setKeyword(searchKey + ";stepNumber==2");
        List<ApprovalPerson> firstApprovalPersons = approvalFlowDetailsService.listResponse(request);
        request.setKeyword(searchKey + ";stepNumber==3");
        List<ApprovalPerson> secondApprovalPersons = approvalFlowDetailsService.listResponse(request);
        request.setKeyword(searchKey + ";stepNumber==4");
        List<ApprovalPerson> thirdApprovalPersons = approvalFlowDetailsService.listResponse(request);
        request.setKeyword(searchKey + ";stepNumber==5");
        List<ApprovalPerson> settlementApprovalPersons= approvalFlowDetailsService.listResponse(request);
        return new ApprovalPersonsResponse(applyPersons, firstApprovalPersons, secondApprovalPersons, thirdApprovalPersons, settlementApprovalPersons);
    }

    @Override
    @Transactional
    public void apply(RequestModelForApply requestModelForApply) {
        try {
            log.info("Begin service RequestServiceImpl: apply");
            RequestsEntity requestsEntity = requestsRepository.getOne(requestModelForApply.getRequestCd());
            Boolean isFirstApply = ObjectUtil.isEmpty(requestsEntity.getRequestedAt());

            List<ApprovalFlowDetailsEntity> grantedPermissions = checkPermissionUserForApproval(requestsEntity.getCd(), (byte) 1);

            Boolean isRightApplyPerson = CollectionUtil.isNotEmpty(grantedPermissions);
            Boolean isOperationHistoryDeputy = isRightApplyPerson ? isDeputyUser(grantedPermissions, requestsEntity.getStepNumber()) : true;
            if (!isFirstApply && !isRightApplyPerson) {
                throw new SSKFException("401", "Cannot request sales charge edit! Not permission");
            }

            setStatusForRequest(requestsEntity, "承認待ち");
            requestsEntity.setStepNumber((byte) 2);
            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setUpdatedAt(LocalDateTime.now());
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setIsSentBack((byte) 0);
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsEntity = requestsRepository.save(requestsEntity);

            if (isFirstApply) {
                requestsEntity.setRequestedAt(LocalDateTime.now());
                requestsEntity.setMstApprovalFlowsEntity(mstApprovalFlowsRepository.findFirstByCd(requestModelForApply.getMstApprovalFlowsCd()));
                RequestNumbersEntity requestNumbersEntity = new RequestNumbersEntity();
                requestNumbersEntity.setRequestsEntity(requestsEntity);
                requestNumbersRepository.save(requestNumbersEntity);
                findAndInsertApprovalDetailsWhenApplied(requestsEntity, requestModelForApply.getMstApprovalFlowsCd(), isRightApplyPerson); // insert approval detail

            }

            if (!isRightApplyPerson) {
                String shainNm = shainService.getInformation().getShainNm();
                String comment = "申請者の代理人に$shain_nm$を追加しました。";
                comment = comment.replace("$shain_nm$", shainNm);
                operationHistoriesService.setOperationHistoriesBySystem(requestsEntity, "代理人追加", comment);
            }

            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "申請", requestModelForApply.getComment(), isOperationHistoryDeputy, (byte) 1);

            Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
                RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
            sendAlertEmailForRequestApply(requestsEntity.getCd());

        } finally {
            log.info("End service RequestServiceImpl: apply");
        }
    }

    private Boolean isDeputyUser(List<ApprovalFlowDetailsEntity> grantedPermissions, Byte stepNumber) {
        String shainCd = shainService.getUserNameByToken();
        for (ApprovalFlowDetailsEntity apd: grantedPermissions) {
            if (apd.getStepNumber() == stepNumber && apd.getShainEntity().getShainCd().equals(shainCd) && apd.getIsDeputy()) {
                return true;
            }
        }
        return false;
    }

    private void sendAlertEmailForRequestApply(Long requestCd) {
        //alert 3 for applier other
        Set<String> applierSameRoleEmails = approvalFlowDetailsService.getAlertEmailForApplierAndApproval(requestCd, (byte) 1, "shainEntity.isAlertedForApplication==1");
        if (CollectionUtil.isNotEmpty(applierSameRoleEmails)) {
            emailServices.sendEmail(new EmailModel(applierSameRoleEmails).buildEmailForApplierWhenApplyRequest(requestCd));
        }
        // for approved
        Set<String> approvalEmails = approvalFlowDetailsService.getAlertEmailForApproverTarget(requestCd, (byte) 2);
        if (CollectionUtil.isNotEmpty(approvalEmails)) {
            emailServices.sendEmail(new EmailModel(approvalEmails).buildEmailForApprovedWhenApplyRequest(requestCd));
        }
    }

    private void findAndInsertApprovalDetailsWhenApplied(RequestsEntity requestsEntity, String mstApprovalFlowsCd, Boolean isRightApplyPerson) {
        List<ApprovalFlowDetailsEntity> approvalDetails = new ArrayList<>();
        if (!isRightApplyPerson) {
            ShainEntity shain = shainService.getLoggedInShainEntity();
            ApprovalFlowDetailsEntity deputyUser = new ApprovalFlowDetailsEntity();
            deputyUser.setRequestsEntity(requestsEntity);
            deputyUser.setStepNumber((byte) 1);
            deputyUser.setIsDeputy(true);
            deputyUser.setShainEntity(shain);
            deputyUser.setShainNm(shain.getShainNm());
            deputyUser.setMstBumonEntity(shain.getMstBumonEntity());
            deputyUser.setBumonNm(shain.getMstBumonEntity().getBumonNm());
            approvalDetails.add(deputyUser);
        }


        List<String> shainCdForApprovalStep2 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowsCd, 2);
        List<ShainEntity> shainForApprovalStep2 = shainRepository.findAllById(shainCdForApprovalStep2);
        if (CollectionUtil.isNotEmpty(shainForApprovalStep2)) {
            for (ShainEntity shainEntity : shainForApprovalStep2) {
                ApprovalFlowDetailsEntity approvalFlowDetail = new ApprovalFlowDetailsEntity();
                approvalFlowDetail.setStepNumber((byte) 2);
                approvalFlowDetail.setIsDeputy(false);
                approvalFlowDetail.setShainEntity(shainEntity);
                approvalFlowDetail.setShainNm(shainEntity.getShainNm());
                approvalFlowDetail.setMstBumonEntity(shainEntity.getMstBumonEntity());
                approvalFlowDetail.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
                approvalFlowDetail.setRequestsEntity(requestsEntity);
                approvalDetails.add(approvalFlowDetail);
            }

        }
        List<String> shainCdForApprovalStep3 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowsCd, 3);
        List<ShainEntity> shainForApprovalStep3 = shainRepository.findAllById(shainCdForApprovalStep3);
        if (CollectionUtil.isNotEmpty(shainForApprovalStep3)) {
            for (ShainEntity shainEntity : shainForApprovalStep3) {
                ApprovalFlowDetailsEntity approvalFlowDetail = new ApprovalFlowDetailsEntity();
                approvalFlowDetail.setStepNumber((byte) 3);
                approvalFlowDetail.setIsDeputy(false);
                approvalFlowDetail.setShainEntity(shainEntity);
                approvalFlowDetail.setShainNm(shainEntity.getShainNm());
                approvalFlowDetail.setMstBumonEntity(shainEntity.getMstBumonEntity());
                approvalFlowDetail.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
                approvalFlowDetail.setRequestsEntity(requestsEntity);
                approvalDetails.add(approvalFlowDetail);
            }
        }

        List<String> shainCdForApprovalStep4 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowsCd, 4);
        List<ShainEntity> shainForApprovalStep4 = shainRepository.findAllById(shainCdForApprovalStep4);
        if (CollectionUtil.isNotEmpty(shainForApprovalStep4)) {
            for (ShainEntity shainEntity : shainForApprovalStep4) {
                ApprovalFlowDetailsEntity approvalFlowDetail = new ApprovalFlowDetailsEntity();
                approvalFlowDetail.setStepNumber((byte) 4);
                approvalFlowDetail.setIsDeputy(false);
                approvalFlowDetail.setShainEntity(shainEntity);
                approvalFlowDetail.setShainNm(shainEntity.getShainNm());
                approvalFlowDetail.setMstBumonEntity(shainEntity.getMstBumonEntity());
                approvalFlowDetail.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
                approvalFlowDetail.setRequestsEntity(requestsEntity);
                approvalDetails.add(approvalFlowDetail);
            }
        }

        List<String> shainCdForApprovalStep5 = shainRepository.listShainCdApproveByMstApprovalFlowsCdAndStepNumber(mstApprovalFlowsCd, 5);
        List<ShainEntity> shainForApprovalStep5 = shainRepository.findAllById(shainCdForApprovalStep5);
        if (CollectionUtil.isNotEmpty(shainForApprovalStep5)) {
            for (ShainEntity shainEntity : shainForApprovalStep5) {
                ApprovalFlowDetailsEntity approvalFlowDetail = new ApprovalFlowDetailsEntity();
                approvalFlowDetail.setStepNumber((byte) 5);
                approvalFlowDetail.setIsDeputy(false);
                approvalFlowDetail.setShainEntity(shainEntity);
                approvalFlowDetail.setShainNm(shainEntity.getShainNm());
                approvalFlowDetail.setMstBumonEntity(shainEntity.getMstBumonEntity());
                approvalFlowDetail.setBumonNm(shainEntity.getMstBumonEntity().getBumonNm());
                approvalFlowDetail.setRequestsEntity(requestsEntity);
                approvalDetails.add(approvalFlowDetail);
            }
        }

        approvalFlowDetailsRepository.saveAll(approvalDetails);
    }

    @Override
    public Boolean checkLockRequest(RequestsEntity requestsEntity) {
        Boolean requestIsLocked = false;
        String userLogin = shainService.getUserNameByToken();
        ShainEntity editUser = requestsEntity.getEditShainEntity();
        LocalDateTime editAt = requestsEntity.getStartedEditAt();
        LocalDateTime currentTime = LocalDateTime.now();
        if (ObjectUtil.isNotEmpty(editUser) && ObjectUtil.isNotEmpty(editAt)) {
            if (StringUtil.isNotEmpty(editUser.getShainCd()) && !editUser.getShainCd().equals(userLogin) && editAt.isAfter(currentTime.plusHours(-1))) {
                requestIsLocked = true;
            }
        }
        return requestIsLocked;
    }

    @Override
    @Transactional
    public void reject(RequestModelForApproval requestModel) {
        Byte stepNumber = requestModel.getApprovingStep();
        if (ObjectUtils.isEmpty(stepNumber)) {
            throw new SSKFException("Step Number", "Approving step is required!");
        }
        RequestsEntity requestsEntity = requestsRepository.getOne(requestModel.getRequestCd());
        if (ObjectUtils.isEmpty(requestsEntity)) {
            throw new SSKFException("Request", "Request not found!");
        }
        List<ApprovalFlowDetailsEntity> grantedPermissions = checkPermissionUserForApproval(requestsEntity.getCd(), requestsEntity.getStepNumber());

        if (CollectionUtil.isEmpty(grantedPermissions)) {
            throw new SSKFException("Not Permission", "User not permission!");
        }

        requestsEntity.setIsAlertedForRemind((byte) 0);
        requestsEntity.setIsSentBack((byte) 0);
        requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
        requestsEntity.setUpdatedAt(LocalDateTime.now());
        setStatusForRequest(requestsEntity, "却下");
        requestsEntity.setEditShainEntity(null);
        requestsEntity.setStartedEditAt(null);
        Set<String> applierAndApproverEmails = new HashSet<>();
        if (requestModel.getApprovingStep() != 5 && requestsEntity.getIsSentBack() == 1) {
            stepNumber = operationHistoriesRepository.getStepNumberMaxWhenSendBack(requestsEntity.getCd());
        }
        if (requestModel.getApprovingStep() == 5) {
            stepNumber = 4;
        }
        applierAndApproverEmails = approvalFlowDetailsService.getAlertEmailForApplierAndApproval(requestsEntity.getCd(), stepNumber, "shainEntity.isAlertedForRejection==1");
        if (CollectionUtil.isNotEmpty(applierAndApproverEmails)) {
            emailServices.sendEmail(new EmailModel(applierAndApproverEmails).buildEmailForRejectedRequest(requestsEntity.getCd()));
        }
        requestsRepository.save(requestsEntity);

        Boolean isDeputyUser = isDeputyUser(grantedPermissions, requestsEntity.getStepNumber());
        OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "却下", requestModel.getComment(), isDeputyUser, requestsEntity.getStepNumber());
        Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();

        if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
            commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
        }

    }


    @Override
    @Transactional
    public void sendBack(Long requestCd, int targetStep, String comment) {
        RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
        List<ApprovalFlowDetailsEntity> grantedPermissions = checkPermissionUserForApproval(requestCd, requestsEntity.getStepNumber());
        if (CollectionUtil.isNotEmpty(grantedPermissions)) {
            Boolean isDeputyUser = isDeputyUser(grantedPermissions, requestsEntity.getStepNumber());
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setIsSentBack((byte) 1);

            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setUpdatedAt(LocalDateTime.now());
            Byte stepNumber = requestsEntity.getStepNumber();
            if (targetStep >= stepNumber) {
                throw new SSKFException("sendBack step", "sendBack step invalid!");
            }
            if (targetStep == 1) {
                requestsEntity.setStepNumber((byte) 1);
                setStatusForRequest(requestsEntity, "申請待ち");
            } else {
                requestsEntity.setStepNumber((byte) targetStep);
                setStatusForRequest(requestsEntity, "承認待ち");
            }

            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "差し戻し", comment, isDeputyUser, stepNumber);

            Set<String> approverTargertEmails = approvalFlowDetailsService.getAlertEmailForApproverTarget(requestCd, (byte) targetStep);
            if (CollectionUtil.isNotEmpty(approverTargertEmails)) {
                emailServices.sendEmail(new EmailModel(approverTargertEmails).buildEmailForApproverSendBackRequest(requestCd));
            }

            Set<String> approverIsSkippedEmails = approvalFlowDetailsService.getAlertEmailForApproverIsSkipped(requestCd, "shainEntity.isAlertedForSendingRequestBack==1", (byte) targetStep, requestsEntity.getStepNumber());
            if (CollectionUtil.isNotEmpty(approverIsSkippedEmails)) {
                emailServices.sendEmail(new EmailModel(approverIsSkippedEmails).buildEmailForApproverIsSkippedSendBackRequest(requestCd));
            }

            Set<String> approverSameRoleEmails = approvalFlowDetailsService.getAlertEmailForApproverSameRole(requestCd, "shainEntity.isAlertedForSendingRequestBack==1", requestsEntity.getStepNumber());
            if (CollectionUtil.isNotEmpty(approverSameRoleEmails)) {
                emailServices.sendEmail(new EmailModel(approverSameRoleEmails).buildEmailForApproverOtherSendBackRequest(requestCd));
            }
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsRepository.save(requestsEntity);
            Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
                RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
        }
    }

    @Override
    @Transactional
    public void confirmSendBack(Long requestCd) {
        RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
        Boolean isApplier = checkPermissionUserForApply(requestCd);

        if (isApplier) {
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setIsSentBack((byte) 0);

            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setUpdatedAt(LocalDateTime.now());
            requestsEntity.setStepNumber((byte) 1);
            setStatusForRequest(requestsEntity, "申請待ち");

            int skippedNumber = requestsEntity.getStepNumber();
            String skipNumString = String.valueOf(skippedNumber);
            for (int i = requestsEntity.getStepNumber(); i > 1; i--) {
                skipNumString = skipNumString + "・" + (skippedNumber - i);
            }
            String commentSkipping = "申請者が差し戻し確認を行った為、第$skipped_number$承認者がスキップされました";
            commentSkipping = commentSkipping.replace("$skipped_number$", skipNumString);
            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesBySystem(requestsEntity, "差し戻し確認", commentSkipping);

            Set<String> approvalIsSkippedEmails = approvalFlowDetailsService.getAlertEmailForApproverIsSkipped(requestCd,"shainEntity.isAlertedForConfirmingSendRequestBack==1", (byte)1, (byte) (skippedNumber + 1));
            if (CollectionUtil.isNotEmpty(approvalIsSkippedEmails)) {
                emailServices.sendEmail(new EmailModel(approvalIsSkippedEmails).buildEmailForApproverWhenConfirmSendBack(requestCd));
            }

            Set<String> approverSameRoleEmails = approvalFlowDetailsService.getAlertEmailForApproverSameRole(requestCd,"shainEntity.isAlertedForConfirmingSendRequestBack==1", (byte)1);
            if (CollectionUtil.isNotEmpty(approverSameRoleEmails)) {
                emailServices.sendEmail(new EmailModel(approverSameRoleEmails).buildEmailForConfirmSendBack(requestCd));
            }
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsRepository.save(requestsEntity);
            Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
                RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
        }
    }

    @Override
    @Transactional
    public void approve(Long requestCd, String comment, Byte approvingStep) {
        RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);

        List<ApprovalFlowDetailsEntity> grantedPermissions = checkPermissionUserForApproval(requestsEntity.getCd(), approvingStep);

        if (CollectionUtil.isNotEmpty(grantedPermissions)) {
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setIsSentBack((byte) 0);
            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setUpdatedAt(LocalDateTime.now());

            int amountSkip = approvingStep - requestsEntity.getStepNumber();

            Boolean isApprovalSkipping = (amountSkip > 0);
            sendAlertEmailWhenApproverApproved(requestsEntity.getCd(), requestsEntity.getStepNumber(), approvingStep);

            Boolean isDeputyUser = isDeputyUser(grantedPermissions, requestsEntity.getStepNumber());
            if (isApprovalSkipping) {
                String role = "";
                switch (approvingStep) {
                    case 3:
                        role = ":第2承認者";
                        break;
                    case 4:
                        role = "第3承認者";
                        break;
                    case 5:
                        role = "決裁者";
                        break;
                }
                int skippedNumber = requestsEntity.getStepNumber() - 1;
                String skipNumString = String.valueOf(skippedNumber);
                for (int i = 1; i < amountSkip; i++) {
                    skipNumString = skipNumString + "・" + (skippedNumber + i);
                }
                String commentSkipping = "$role$が承認を行った為、第$skipped_number$承認者がスキップされました";
                commentSkipping = commentSkipping.replace("$role$", role).replace("$skipped_number$", skipNumString);
                operationHistoriesService.setOperationHistoriesBySystem(requestsEntity, "承認(スキップ)", commentSkipping);
            }

            if (requestsEntity.getStepNumber() < 5 && ObjectUtil.isNotEmpty(approvingStep) && approvingStep < 5) {
                int newStep = approvingStep + 1;
                requestsEntity.setStepNumber((byte) newStep);
            } else {
                requestsEntity.setStepNumber((byte) 5);
                setStatusForRequest(requestsEntity, "決済確認待ち");
            }

            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "承認", comment, isDeputyUser, approvingStep.byteValue());
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);
            requestsRepository.save(requestsEntity);
            Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
                RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
        }

    }

    @Override
    @Transactional
    public void settlement(Long requestCd, String comment) {
        RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
        String bumonCd = shainService.getBumonByToken();
        if (Constants.USER_ROLE_ACCOUNTING_CHARGE_BUMON.equals(bumonCd)) {
            requestsEntity.setIsAlertedForRemind((byte) 0);
            requestsEntity.setIsSentBack((byte) 0);
            requestsEntity.setUpdatedStatusAt(LocalDateTime.now());
            requestsEntity.setUpdatedAt(LocalDateTime.now());
            setStatusForRequest(requestsEntity, "支払済");
            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "決済確認", comment, false, null);
            Set<String> settlementPersonEmails = approvalFlowDetailsService.getAlertEmailForApplierAndApproval(requestCd, (byte) 4, "shainEntity.isAlertedForConfirmingSettlement==1");
            if (CollectionUtil.isNotEmpty(settlementPersonEmails)) {
                emailServices.sendEmail(new EmailModel(settlementPersonEmails).buildEmailForSettlementedRequest(requestCd));
            }
            requestsEntity.setEditShainEntity(null);
            requestsEntity.setStartedEditAt(null);

            requestsRepository.save(requestsEntity);
            Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntities = requestsEntity.getRequestAccountsReceivablesEntities();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivablesEntities)) {
                RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesEntities.stream().findFirst().get();
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
        } else {
            throw new SSKFException("settlement", "settlement not permission!");
        }
    }

    @Override
    public LockModelResponse checkUpdated(Long requestCd, LocalDateTime updatedAt) {
        try {
            log.info("Begin service RequestServiceImpl: checkUpdated");
            LockModelResponse lockModelResponse = new LockModelResponse();
            RequestsEntity requestEntity = requestsRepository.getOne(requestCd);
            ShainEntity editUser = requestEntity.getEditShainEntity();
            lockModelResponse.setIsUpdated(false);
            if (checkLockRequest(requestEntity)) {
                if (ObjectUtil.isNotEmpty(requestEntity.getStartedEditAt())) {
                    Long timeLocked = Timestamp.valueOf(requestEntity.getStartedEditAt().plusHours(1)).getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime();
                    lockModelResponse.setStartedEditAt(timeLocked);
                }
                if (ObjectUtil.isNotEmpty(editUser)) {
                    lockModelResponse.setEditShainCd(editUser.getShainCd());
                    lockModelResponse.setEditShainName(editUser.getShainNm());
                }
            }

            if (ObjectUtil.isNotEmpty(requestEntity)) {
                int nano = updatedAt.getNano();
                if (nano >= 500000000) {
                    updatedAt = updatedAt.plusSeconds(1);
                }
                updatedAt = updatedAt.withNano(0);
                LocalDateTime updatedRequest = requestEntity.getUpdatedAt().withNano(0);
                lockModelResponse.setIsUpdated(!updatedRequest.equals(updatedAt));
            }
            return lockModelResponse;
        } finally {
            log.info("End service RequestServiceImpl: checkUpdated");
        }

    }


    private void sendAlertEmailWhenApproverApproved(Long requestCd, Byte stepNumberCurrent, Byte targetStepNumber) {
       if (targetStepNumber == 5) {
           Set<String> approverIsSkippedEmails = approvalFlowDetailsService.getAlertEmailForApproverIsSkipped(requestCd, "shainEntity.isAlertedForApproval==1", (byte)1, targetStepNumber);
           if (CollectionUtil.isNotEmpty(approverIsSkippedEmails)) {
               emailServices.sendEmail(new EmailModel(approverIsSkippedEmails).buildEmailForApplicantWhenSettlementApproved(requestCd));
           }
           Set<String> emailAccountingCharge = shainAddressRepository.listEmailAccountingCharge().stream().collect(Collectors.toSet());
           if (CollectionUtil.isNotEmpty(emailAccountingCharge)) {
               emailServices.sendEmail(new EmailModel(emailAccountingCharge).buildEmailForAccountingCharge(requestCd));
           }
       } else {
           //send email to higher level
           //send email to same role
           if (targetStepNumber > stepNumberCurrent) {
               Set<String> approverIsSkippedEmails = approvalFlowDetailsService.getAlertEmailForApproverIsSkipped(requestCd, "shainEntity.isAlertedForApproval==1", stepNumberCurrent, targetStepNumber);
               if (CollectionUtil.isNotEmpty(approverIsSkippedEmails)) {
                   emailServices.sendEmail(new EmailModel(approverIsSkippedEmails).buildEmailForNormalApproverApprovedToLowerSkipLevel(requestCd));
               }
           }
           Set<String> approverSameRoleEmails = approvalFlowDetailsService.getAlertEmailForApproverSameRole(requestCd, "shainEntity.isAlertedForApproval==1", targetStepNumber);
           if (CollectionUtil.isNotEmpty(approverSameRoleEmails)) {
               emailServices.sendEmail(new EmailModel(approverSameRoleEmails).buildEmailForNormalApproverApprovedToSameLevel(requestCd));
           }
           int stepNumberHighLevel = targetStepNumber + 1;
           Set<String> higherRoleEmails = approvalFlowDetailsService.getAlertEmailForApproverTarget(requestCd, (byte) stepNumberHighLevel);
           if (CollectionUtil.isNotEmpty(higherRoleEmails)) {
               emailServices.sendEmail(new EmailModel(higherRoleEmails).buildEmailForNormalApproverApprovedToHigherLevel(requestCd));
           }
        }
    }

    private void setStatusForRequest(RequestsEntity entity, String statusName) {
        MstRequestStatusesEntity statusesEntity = mstRequestStatusesRepository.findByName(statusName);
        if (ObjectUtils.isEmpty(statusesEntity)) {
            throw new SSKFException("Find status", "Not found status!");
        }

        entity.setMstRequestStatusesEntity(statusesEntity);

    }

    public List<RequestAttachmentsEntity> uploadFileForRequests(MultipartFile[] files, RequestsEntity requestsEntity, Boolean writeLogFile) {
        List<FileModel> fileModels = awsS3FileService.uploadFile(Arrays.asList(files), "request_" + requestsEntity.getCd());
        List<String> filesOverride = new ArrayList<>();
        List<RequestAttachmentsEntity> requestAttachmentsEntities = new ArrayList<>();
        Map<String, String> filesOverrideMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(fileModels)) {
            requestAttachmentsEntities = fileModels.stream().map(requestAttachmentsMapper::toEntity).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(requestAttachmentsEntities)) {
                for (RequestAttachmentsEntity e : requestAttachmentsEntities) {
                    e.setRequestsEntity(requestsEntity);
                    filesOverride.add(e.getDisplayedFileName());
                    filesOverrideMap.put(e.getDisplayedFileName(), e.getDisplayedFileName());
                }
            }
        }

        List<RequestAttachmentsEntity> filesDeleteByName = requestAttachmentsRepository.findAllByDisplayedFileNameIn(filesOverride);

        List<OperationHistoriesEntity> operationHistoriesEntityList = new ArrayList<>();

        MstRequestCommentActionsEntity mstRequestCommentActionsEntity = mstRequestCommentActionsRepository.findByName("ファイル変更");
        String commentOperationHistory = mstRequestCommentActionsEntity.getAutoComment();
        ShainEntity userAction = shainService.getLoggedInShainEntity();
        MstBumonEntity bumon = userAction.getMstBumonEntity();

        ApprovalFlowDetailsEntity approvalFlowDetailsEntityList = approvalFlowDetailsRepository
                .findByRequestsEntityAndStepNumberAndAndShainEntityAndIsDeputy(requestsEntity, requestsEntity.getStepNumber(), userAction, false);
        Boolean isDeputyActioning = true;
        if (ObjectUtil.isNotEmpty(approvalFlowDetailsEntityList)) {
            isDeputyActioning = false;
        }

        if (CollectionUtil.isNotEmpty(filesDeleteByName)) {
            if (writeLogFile) {
                for(RequestAttachmentsEntity deletedByName : filesDeleteByName) {
                    filesOverrideMap.remove(deletedByName.getDisplayedFileName());
                    OperationHistoriesEntity operationHistoriesEntity = setOperationHistoriesEntityForUploadFile(userAction, requestsEntity,
                            mstRequestCommentActionsEntity, bumon.getBumonNm(), isDeputyActioning);
                    String commentForChangeFile = commentOperationHistory.replace("$displayed_file_name$", deletedByName.getDisplayedFileName()).replace("$action$", "変更");
                    operationHistoriesEntity.setComment(commentForChangeFile);
                    operationHistoriesEntityList.add(operationHistoriesEntity);
                }
            }
            requestAttachmentsRepository.deleteAll(filesDeleteByName);

        }

        for (String key : filesOverrideMap.keySet()) {
            if (writeLogFile) {
                OperationHistoriesEntity operationHistoriesEntity = setOperationHistoriesEntityForUploadFile(userAction, requestsEntity,
                        mstRequestCommentActionsEntity, bumon.getBumonNm(), isDeputyActioning);
                String commentForInsertFile = commentOperationHistory.replace("$displayed_file_name$", key).replace("$action$", "追加");
                operationHistoriesEntity.setComment(commentForInsertFile);
                operationHistoriesEntityList.add(operationHistoriesEntity);
            }

        }
        if (writeLogFile) {
            operationHistoriesService.saveList(operationHistoriesEntityList);
        }
        return requestAttachmentsRepository.saveAll(requestAttachmentsEntities);
    }

    @Override
    @Transactional
    public RequestsEntity setSettlementNumber(RequestsEntity requestsEntity, RequestModel requestModel) {
        ShainEntity shainEntity = shainService.getLoggedInShainEntity();
        if (requestsEntity.getStepNumber() > 3 && shainEntity.getMstBumonEntity().getBumonCd().equals("4000")) {
            if (StringUtil.isEmpty(requestModel.getRequestTypeName()))
                throw new SSKFException("Request type name", "Request type name is required!");
            Date paymentOn = requestsEntity.getPaymentOn();
            LocalDateTime currentDate = LocalDateTime.now();
            if (ObjectUtil.isEmpty(paymentOn) && ObjectUtil.isNotEmpty(requestModel.getPaymentOn())) {

                requestsEntity.setUpdatedAtAfterAccountingCheck(currentDate);
                MstRequestTypesEntity mstRequestTypesEntity = mstRequestTypeRepository.findByName(requestModel.getRequestTypeName());
                Integer numberRequestType =  mstRequestTypesEntity.getNumber();
                if (ObjectUtil.isEmpty(mstRequestTypesEntity)) {
                    throw new SSKFException("MstRequestTypesEntity", "Request Type name not" + requestModel.getRequestTypeName() + " existed!");
                }

                String typeCd = mstRequestTypesEntity.getCd().toString();
                int lengthType = typeCd.length();
                if (lengthType < 2) {
                    typeCd = "0" + typeCd;
                }
                String numberType = numberRequestType.toString();
                int lengthNumber = numberType.length();
                if (lengthNumber == 1) {
                    numberType = "00" + numberType;
                } else if (lengthNumber == 2) {
                    numberType = "0" + numberType;
                }
                String year = String.valueOf(currentDate.getYear());
                String month = (currentDate.getMonth().getValue() < 10) ? ("0" + currentDate.getMonth().getValue()) : String.valueOf(currentDate.getMonth().getValue());
                String day = (currentDate.getDayOfMonth() < 10) ?  ("0" + currentDate.getDayOfMonth()) : String.valueOf(currentDate.getDayOfMonth());
                String settlementNumber =  year + month + day + "_" + typeCd + "_" + numberType;
                requestsEntity.setSettlementNumber(settlementNumber);
                if (numberRequestType < 999) {
                    mstRequestTypesEntity.setNumber(numberRequestType +1);
                } else {
                    mstRequestTypesEntity.setNumber(1);
                }

                operationHistoriesService.setOperationHistoriesBySystem(requestsEntity, "支払日入力", null);
            } else if (ObjectUtil.isNotEmpty(paymentOn) && !paymentOn.equals(requestModel.getPaymentOn())) {
                requestsEntity.setUpdatedAtAfterAccountingCheck(currentDate);
            }
        }
        requestsEntity.setPaymentOn(requestModel.getPaymentOn());
        return requestsEntity;
    }

    @Override
    public void deleteRequestAttachment(Long cd) {
        try {
            log.info("Begin service RequestServiceImpl: deleteRequestAttachment");
            RequestAttachmentsEntity requestAttachmentsEntity = requestAttachmentsRepository.getOne(cd);
            if (ObjectUtil.isEmpty(requestAttachmentsEntity)) {
                throw new SSKFException("File", "File not existed!");
            }
            MstRequestCommentActionsEntity mstRequestCommentActionsEntity = mstRequestCommentActionsRepository.findByName("ファイル変更");
            String commentOperationHistory = mstRequestCommentActionsEntity.getAutoComment();
            RequestsEntity requestsEntity = requestAttachmentsEntity.getRequestsEntity();
            ShainEntity userAction = shainService.getLoggedInShainEntity();
            Boolean writeLogFile = false;
            if (!requestsEntity.getMstRequestStatusesEntity().getName().equals("作成中")) {
                writeLogFile = true;
            }

            ApprovalFlowDetailsEntity approvalFlowDetailsEntityList = approvalFlowDetailsRepository
                    .findByRequestsEntityAndStepNumberAndAndShainEntityAndIsDeputy(requestsEntity, requestsEntity.getStepNumber(), userAction, false);
            Boolean isDeputyActioning = true;
            if (ObjectUtil.isNotEmpty(approvalFlowDetailsEntityList)) {
                isDeputyActioning = false;
            }
            List<OperationHistoriesEntity> operationHistoriesEntityList = new ArrayList();
            MstBumonEntity bumon = userAction.getMstBumonEntity();
            awsS3FileService.deleteObject(requestAttachmentsEntity.getAttachmentPath());
            requestAttachmentsRepository.delete(requestAttachmentsEntity);
            if (writeLogFile) {
                OperationHistoriesEntity operationHistoriesEntity = setOperationHistoriesEntityForUploadFile(userAction, requestsEntity,
                        mstRequestCommentActionsEntity, bumon.getBumonNm(), isDeputyActioning);
                String commentForDeleteFile = commentOperationHistory.replace("$displayed_file_name$", requestAttachmentsEntity.getDisplayedFileName()).replace("$action$", "削除");
                operationHistoriesEntity.setComment(commentForDeleteFile);
                operationHistoriesEntityList.add(operationHistoriesEntity);
                operationHistoriesService.saveList(operationHistoriesEntityList);
            }

        } finally {
            log.info("Begin service RequestServiceImpl: deleteRequestAttachment");
        }
    }

    @Override
    public String getRequestDetails(Long cd) {
        try {
            log.info("Begin service RequestServiceImpl: getRequestDetails");

            RequestsEntity requestsEntity = requestsRepository.getOne(cd);
            MstRequestTypesEntity mstRequestTypesEntity = requestsEntity.getMstRequestTypesEntity();
            MstRequestStatusesEntity mstRequestStatusesEntity = requestsEntity.getMstRequestStatusesEntity();

            String requestType = EnumRequestType.getValueByLabel(mstRequestTypesEntity.getName());
            String requestStatus = EnumRequestStatus.getValueByLabel(mstRequestStatusesEntity.getName());

            return requestType + "/" + requestStatus + "/" + cd + "/view";
        } finally {
            log.info("End service RequestServiceImpl: getRequestDetails");
        }
    }

    private OperationHistoriesEntity setOperationHistoriesEntityForUploadFile(ShainEntity userAction, RequestsEntity requestsEntity, MstRequestCommentActionsEntity mstRequestCommentActionsEntity, String bumonNm, Boolean isDeputyActioning) {
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setShainEntity(userAction);
        operationHistoriesEntity.setShainNm(userAction.getShainNm());
        operationHistoriesEntity.setRequestsEntity(requestsEntity);
        operationHistoriesEntity.setMstRequestCommentActionsEntity(mstRequestCommentActionsEntity);
        operationHistoriesEntity.setIsDoneBySystem((byte) 0);
        operationHistoriesEntity.setBumonNm(bumonNm);
        operationHistoriesEntity.setIsDeputy(isDeputyActioning);
        return operationHistoriesEntity;
    }
}
