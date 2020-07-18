package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.DetailsForAccountsReceivablesMapper;
import sskf.mapper.DetailsForPromotionalExpensesMapper;
import sskf.mapper.RequestAccountsReceivablesMapper;
import sskf.model.EmailModel;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;
import sskf.model.entity.DetailsForPromotionalExpensesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainAddressEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.AccountReceivablesConfirmRequest;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.DetailsForAccountsReceivablesRequest;
import sskf.model.request.DetailsForPromotionalExpensesRequest;
import sskf.model.request.RequestModel;
import sskf.model.request.SalesChargeEditItemRequest;
import sskf.model.request.SalesChargeEditRequest;
import sskf.repository.CommentsForDetailsRepository;
import sskf.repository.RequestAccountReceivablesRepository;
import sskf.repository.RequestsRepository;
import sskf.repository.ShainAddressRepository;
import sskf.repository.ShainRepository;
import sskf.service.CommentsForDetailsService;
import sskf.service.EmailServices;
import sskf.service.OperationHistoriesService;
import sskf.service.RequestAccountReceivablesService;
import sskf.service.RequestAccountsReceivableDetailsService;
import sskf.service.RequestService;
import sskf.service.ShainAddressService;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestAccountReceivablesServiceImpl implements RequestAccountReceivablesService {

    @Autowired
    private RequestAccountReceivablesRepository requestAccountReceivablesRepository;

    @Autowired
    private RequestAccountsReceivablesMapper requestAccountsReceivablesMapper;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestAccountsReceivableDetailsService requestAccountsReceivableDetailsService;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainAddressRepository shainAddressRepository;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    @Autowired
    private DetailsForAccountsReceivablesMapper detailsForAccountsReceivablesMapper;

    @Autowired
    private DetailsForPromotionalExpensesMapper detailsForPromotionalExpensesMapper;

    @Autowired
    private ShainService shainService;

    @Autowired
    private CommentsForDetailsRepository commentsForDetailsRepository;

    @Autowired
    private ShainAddressService shainAddressService;

    @Autowired
    private CommentsForDetailsService commentsForDetailsService;

    @Override
    @Transactional
    public AccountReceivablesRequest create(AccountReceivablesRequest receivablesRequest, MultipartFile[] files) {
        try {
            log.info("Begin service: create RequestAccountsReceivables");
            RequestModel requestModel = receivablesRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.create(requestModel, files, Constants.ACCOUNT_RECEIVABLE);

            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountsReceivablesMapper.toEntity(receivablesRequest);
            requestAccountsReceivablesEntity.setRequestsEntity(requestsEntity);
            requestAccountsReceivablesEntity = requestAccountReceivablesRepository.save(requestAccountsReceivablesEntity);
            OperationHistoriesEntity operationHistoriesEntity = null;
            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
            }
            requestAccountsReceivableDetailsService.create(requestAccountsReceivablesEntity, receivablesRequest, "作成中");

            if (!receivablesRequest.getRequestModel().getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(receivablesRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatedAccountReceivable(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity);
        } finally {
            log.info("End service: create RequestAccountsReceivables");
        }
    }

    @Override
    @Transactional
    public AccountReceivablesRequest updateCreate(AccountReceivablesRequest receivablesRequest, MultipartFile[] files) {
        try {
            log.info("Begin service: update RequestAccountsReceivables");
            if (ObjectUtils.isEmpty(receivablesRequest.getCd())) {
                throw new SSKFException("400", "AccountReceivablesRequest cd required!");
            }
            RequestModel requestModel = receivablesRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.updateCreate(requestModel, files, Constants.ACCOUNT_RECEIVABLE);

            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = update(receivablesRequest, requestsEntity);
            OperationHistoriesEntity operationHistoriesEntity = null;
            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
                operationHistoriesService.updateParent(requestsEntity.getCd(), "作成途中保存", operationHistoriesEntity);
            }
            requestAccountsReceivableDetailsService.update(requestAccountsReceivablesEntity, receivablesRequest, "作成中");
            if (!receivablesRequest.getRequestModel().getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(receivablesRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatedAccountReceivable(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity);
        } finally {
            log.info("End service: update RequestAccountsReceivables");
        }
    }

    private RequestAccountsReceivablesEntity update(AccountReceivablesRequest receivablesRequest, RequestsEntity requestsEntity) {
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestAccountReceivablesRepository.getOne(receivablesRequest.getCd());
        requestAccountsReceivablesEntity = requestAccountsReceivablesMapper.toEntityUpdated(requestAccountsReceivablesEntity, receivablesRequest);
        requestAccountsReceivablesEntity.setRequestsEntity(requestsEntity);
        requestAccountsReceivablesEntity.setUpdatedAt(LocalDateTime.now());
        return requestAccountReceivablesRepository.save(requestAccountsReceivablesEntity);

    }

    @Override
    public AccountReceivablesRequest getByRequestId(Long requestId, String mode) {
        try {
            log.info("Begin service: get RequestAccountsReceivables");

            RequestsEntity requestsEntity = requestsRepository.findByCdAndIsDeleted(requestId, Constants.NOT_DELETED);
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "RequestsEntity not existed!");
            }
            if (CollectionUtil.isEmpty(requestsEntity.getRequestAccountsReceivablesEntities())) {
                throw new SSKFException("404", "RequestAccountsReceivables not existed!");
            }

            if (Constants.EDIT.equals(mode)) {
                requestService.lockRequest(requestsEntity);
            }
            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestsEntity.getRequestAccountsReceivablesEntities().stream().findFirst().get();
            Set<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntities();
            HashMap<Long, RequestAccountsReceivableDetailsEntity> mapHasComment = new HashMap<>();
            if (CollectionUtil.isNotEmpty(requestAccountsReceivableDetailsEntities)) {
                List<String> cdRequestAccountsReceivables = requestAccountsReceivableDetailsEntities.stream().map(e -> e.getCd().toString()).collect(Collectors.toList());
                String cdKey = String.join(",", cdRequestAccountsReceivables);
                for (RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity : requestAccountsReceivableDetailsEntities) {
                    cdKey = cdKey + "," + requestAccountsReceivableDetailsEntity.getCd();
                }
                BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
                String keySearch = "cd=in=(" + cdKey + ");commentsForDetailsEntities!=null;commentsForDetailsEntities.isDeleted==0";
                baseSearchRequest.setKeyword(keySearch);
                List<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsHasComment = requestAccountsReceivableDetailsService.list(baseSearchRequest);
                mapHasComment = requestAccountsReceivableDetailsHasComment.stream()
                        .collect(Collectors.toMap(RequestAccountsReceivableDetailsEntity::getCd, e -> e, (a, b) -> b, HashMap::new));

            }

            AccountReceivablesRequest result = requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity);
            return setListItemForRequest(result, requestAccountsReceivableDetailsEntities, mapHasComment);
        } finally {
            log.info("End service: get RequestAccountsReceivables");
        }
    }

    private AccountReceivablesRequest setListItemForRequest(AccountReceivablesRequest response, Set<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntitySet,
                                                            HashMap<Long, RequestAccountsReceivableDetailsEntity> mapHasComment) {
        List<DetailsForAccountsReceivablesRequest> detailsForAccountsReceivablesRequestList = new ArrayList<>();

        List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax8PercentList = new ArrayList<>();

        List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax10PercentList = new ArrayList<>();

        List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestLogisticFeesList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(requestAccountsReceivableDetailsEntitySet)) {
            for (RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity : requestAccountsReceivableDetailsEntitySet) {

                if (requestAccountsReceivableDetailsEntity.getItemNumber().startsWith("1")) {
                    Set<DetailsForAccountsReceivablesEntity> detailsForAccountsReceivablesEntities = requestAccountsReceivableDetailsEntity.getDetailsForAccountsReceivablesEntities();
                    if (CollectionUtil.isNotEmpty(detailsForAccountsReceivablesEntities)) {
                        DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity = detailsForAccountsReceivablesEntities.stream().findFirst().get();
                        DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest = detailsForAccountsReceivablesMapper.toResponse(detailsForAccountsReceivablesEntity);
                        if (ObjectUtil.isNotEmpty(mapHasComment.get(detailsForAccountsReceivablesRequest.getAccountReceivableDetailCd())))
                            detailsForAccountsReceivablesRequest.setHasComment(true);
                        detailsForAccountsReceivablesRequestList.add(detailsForAccountsReceivablesRequest);
                    }

                } else if (requestAccountsReceivableDetailsEntity.getItemNumber().startsWith("2")) {
                    Set<DetailsForPromotionalExpensesEntity> detailsForPromotionalExpensesEntities = requestAccountsReceivableDetailsEntity.getDetailsForPromotionalExpensesEntities();
                    if (CollectionUtil.isNotEmpty(detailsForPromotionalExpensesEntities)) {
                        DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity = detailsForPromotionalExpensesEntities.stream().findFirst().get();
                        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = detailsForPromotionalExpensesMapper.toResponse(detailsForPromotionalExpensesEntity);
                        if (ObjectUtil.isNotEmpty(mapHasComment.get(detailsForPromotionalExpensesRequest.getAccountReceivableDetailCd())))
                            detailsForPromotionalExpensesRequest.setHasComment(true);
                        detailsForPromotionalExpensesRequestTax8PercentList.add(detailsForPromotionalExpensesRequest);
                    }

                } else if (requestAccountsReceivableDetailsEntity.getItemNumber().startsWith("3")) {
                    Set<DetailsForPromotionalExpensesEntity> detailsForPromotionalExpensesEntities = requestAccountsReceivableDetailsEntity.getDetailsForPromotionalExpensesEntities();
                    if (CollectionUtil.isNotEmpty(detailsForPromotionalExpensesEntities)) {
                        DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity = detailsForPromotionalExpensesEntities.stream().findFirst().get();
                        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = detailsForPromotionalExpensesMapper.toResponse(detailsForPromotionalExpensesEntity);
                        if (ObjectUtil.isNotEmpty(mapHasComment.get(detailsForPromotionalExpensesRequest.getAccountReceivableDetailCd())))
                            detailsForPromotionalExpensesRequest.setHasComment(true);
                        detailsForPromotionalExpensesRequestTax10PercentList.add(detailsForPromotionalExpensesRequest);
                    }
                } else if (requestAccountsReceivableDetailsEntity.getItemNumber().startsWith("4")) {
                    Set<DetailsForPromotionalExpensesEntity> detailsForPromotionalExpensesEntities = requestAccountsReceivableDetailsEntity.getDetailsForPromotionalExpensesEntities();
                    if (CollectionUtil.isNotEmpty(detailsForPromotionalExpensesEntities)) {
                        DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity = detailsForPromotionalExpensesEntities.stream().findFirst().get();
                        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = detailsForPromotionalExpensesMapper.toResponse(detailsForPromotionalExpensesEntity);
                        if (ObjectUtil.isNotEmpty(mapHasComment.get(detailsForPromotionalExpensesRequest.getAccountReceivableDetailCd())))
                            detailsForPromotionalExpensesRequest.setHasComment(true);
                        detailsForPromotionalExpensesRequestLogisticFeesList.add(detailsForPromotionalExpensesRequest);
                    }
                }
            }

            response.setDetailsForAccountsReceivablesRequestList(detailsForAccountsReceivablesRequestList);
            response.setDetailsForPromotionalExpensesRequestTax8PercentList(detailsForPromotionalExpensesRequestTax8PercentList);
            response.setDetailsForPromotionalExpensesRequestTax10PercentList(detailsForPromotionalExpensesRequestTax10PercentList);
            response.setDetailsForPromotionalExpensesRequestLogisticFeesList(detailsForPromotionalExpensesRequestLogisticFeesList);
        }
        return response;
    }

    @Override
    @Transactional
    public AccountReceivablesRequest confirm(AccountReceivablesConfirmRequest receivablesConfirmRequest) {
        try {
            log.info("Begin service: confirm RequestAccountsReceivables");

            if (ObjectUtils.isEmpty(receivablesConfirmRequest.getRequestCd())) {
                throw new SSKFException("400", "Request cd  required!");
            }

            RequestsEntity requestsEntity = requestsRepository.findByCdAndIsDeleted(receivablesConfirmRequest.getRequestCd(), (byte) 0);
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "Requests not existed!");
            }

            if (requestService.checkLockRequest(requestsEntity)) {
                throw new SSKFException("403", "Request is locked");
            }


            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestsEntity.getRequestAccountsReceivablesEntities().stream().findFirst().get();

            operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "明細確認", receivablesConfirmRequest.getComment(), false, null);

            requestAccountsReceivableDetailsService.confirm(receivablesConfirmRequest);

            Set<RequestAccountsReceivableDetailsEntity> listItem = requestAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntities();
            Boolean itemNotFullCheck = listItem.stream().anyMatch(e -> e.getIsChecked() == 0);
            if (!itemNotFullCheck) {
                requestsEntity = requestService.confirm(requestsEntity);
                OperationHistoriesEntity parent = operationHistoriesService.setOperationHistoriesBySystem(requestsEntity, "確認", null);
                operationHistoriesService.updateParent(requestsEntity.getCd(), "明細確認", parent);
                List<ApprovalFlowDetailsEntity> applicants = requestsEntity.getApprovalFlowDetailsEntities().stream().filter(e -> (e.getStepNumber().equals(1))).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(applicants)) {
                    List<String> shainCd = new ArrayList<>();
                    for (ApprovalFlowDetailsEntity applicant : applicants) {
                        shainCd.add(applicant.getShainEntity().getShainAddressEntity().getShainCd());
                    }
                    Set<String> emails = shainAddressRepository.findAllById(shainCd).stream().map(e -> e.getMailAddress()).collect(Collectors.toSet());
                    if (CollectionUtil.isNotEmpty(emails)) {
                        EmailModel emailModel = new EmailModel(emails).buildEmailForConfirm(requestsEntity.getCd());
                        emailServices.sendEmail(emailModel);
                    }
                }
                commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), parent.getCd());
            }
            requestService.unLockRequest(requestsEntity.getCd());
            return requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity);
        } finally {
            log.info("End service: confirm RequestAccountsReceivables");
        }
    }

    @Override
    @Transactional
    public AccountReceivablesRequest update(AccountReceivablesRequest receivablesRequest, MultipartFile[] files) {
        try {
            log.info("Begin service: update RequestAccountsReceivables");
            if (ObjectUtils.isEmpty(receivablesRequest.getCd())) {
                throw new SSKFException("400", "AccountReceivablesRequest cd required!");
            }
            RequestModel requestModel = receivablesRequest.getRequestModel();
            requestModel.setRequestTypeName("販売未収金");
            RequestsEntity requestsEntity = requestService.update(requestModel, files);
            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = update(receivablesRequest, requestsEntity);
            requestAccountsReceivableDetailsService.update(requestAccountsReceivablesEntity, receivablesRequest, requestsEntity.getMstRequestStatusesEntity().getName());

            return requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity);
        } finally {
            log.info("End service: update RequestAccountsReceivables");
        }
    }

    @Override
    @Transactional
    public void requestSalesChargeEdit(SalesChargeEditRequest salesChargeEditRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesServiceImpl requestSalesChargeEdit");
            List<SalesChargeEditItemRequest> salesChargeEditItemRequests = salesChargeEditRequest.getSalesChargeEditItemRequests();
            RequestsEntity requestsEntity = requestService.requestSalesChargeEdit(salesChargeEditRequest.getRequestCd());

            OperationHistoriesEntity operationHistoriesEntity = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "修正依頼", salesChargeEditRequest.getComment(), false, null);
            List<String> accountReceivableDetailCds = salesChargeEditItemRequests.stream().map(e -> e.getAccountReceivableDetailCd().toString()).collect(Collectors.toList());

            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
            String searchKey = "cd=in=(" + String.join(",", accountReceivableDetailCds) + ")";
            baseSearchRequest.setKeyword(searchKey);
            List<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities = requestAccountsReceivableDetailsService.list(baseSearchRequest);

            for (RequestAccountsReceivableDetailsEntity detail : requestAccountsReceivableDetailsEntities) {
                detail.setIsChecked((byte) 0);

            }

            Set<String> emailsConfirmPersons = getEmailsConfirmPersonWhenRequestSalesEdit(salesChargeEditRequest);
            if (CollectionUtil.isNotEmpty(emailsConfirmPersons)) {
                EmailModel emailConfirmPerson = new EmailModel(emailsConfirmPersons).buildEmailForConfirmPersonWhenRequestSalesEdit(salesChargeEditRequest.getRequestCd());
                emailServices.sendEmail(emailConfirmPerson);
            }

            Set<String> emailsApplicants = getEmailApplicantsWhenRequestSalesEdit(salesChargeEditRequest);
            if (CollectionUtil.isNotEmpty(emailsApplicants)) {
                EmailModel emailsApplicant = new EmailModel(emailsApplicants).buildEmailForApplicantWhenRequestSalesEdit(salesChargeEditRequest.getRequestCd());
                emailServices.sendEmail(emailsApplicant);

            }
            RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = requestsEntity.getRequestAccountsReceivablesEntities().stream().findFirst().get();
            commentsForDetailsService.updateIsCapableOfBeingDeleted(requestAccountsReceivablesEntity.getCd(), operationHistoriesEntity.getCd());
        } finally {
            log.info("Log end API: RequestAccountReceivablesServiceImpl requestSalesChargeEdit");
        }
    }
    private Set<String> getEmailsConfirmPersonWhenRequestSalesEdit(SalesChargeEditRequest salesChargeEditRequest) {

        StringBuilder listShainCd = new StringBuilder();

        if (CollectionUtil.isNotEmpty(salesChargeEditRequest.getSalesChargeEditItemRequests())) {
            for (SalesChargeEditItemRequest salesChargeEditItemRequest : salesChargeEditRequest.getSalesChargeEditItemRequests()) {
                listShainCd.append(salesChargeEditItemRequest.getShainCdItem() + ",");
            }
        }
        listShainCd.deleteCharAt(listShainCd.length() - 1);
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String searchKey = "shainCd=in=(" + listShainCd + ")";
        baseSearchRequest.setKeyword(searchKey);

        return shainAddressService.listEmail(baseSearchRequest);
    }

    private Set<String> getEmailApplicantsWhenRequestSalesEdit(SalesChargeEditRequest salesChargeEditRequest) {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String userLogin = shainService.getUserNameByToken();
        String searchKey = "approvalFlowDetailsEntities.stepNumber==1" + Constants.AND_RSQL + "isAlertedForModificationRequest==1"
                + Constants.AND_RSQL + "approvalFlowDetailsEntities.requestsEntity.cd==" + salesChargeEditRequest.getRequestCd() + "";
        baseSearchRequest.setKeyword(searchKey);
        List<ShainEntity> shainEntityList = shainService.getListShains(baseSearchRequest);
        if (CollectionUtil.isNotEmpty(shainEntityList)) {
            List<String> shainCdList = shainEntityList.stream().map(e -> e.getShainCd()).collect(Collectors.toList());
            searchKey = "shainCd=in=(" + String.join(",", shainCdList) + ")" + ";shainCd!=" + userLogin;
            baseSearchRequest.setKeyword(searchKey);
            return shainAddressService.listEmail(baseSearchRequest);
        }
        return null;
    }

    private Set<String> getEmailsWhenCompleteCreate(AccountReceivablesRequest receivablesRequest) {
        Set<String> emails = new HashSet<>();
        List<String> listShainCd = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(receivablesRequest.getDetailsForAccountsReceivablesRequestList())) {
            for (DetailsForAccountsReceivablesRequest detail : receivablesRequest.getDetailsForAccountsReceivablesRequestList()) {
                listShainCd.add(detail.getShainCd());
            }
        }
        if (CollectionUtil.isNotEmpty(receivablesRequest.getDetailsForPromotionalExpensesRequestTax8PercentList())) {
            for (DetailsForPromotionalExpensesRequest detail : receivablesRequest.getDetailsForPromotionalExpensesRequestTax8PercentList()) {
                listShainCd.add(detail.getShainCd());
            }
        }
        if (CollectionUtil.isNotEmpty(receivablesRequest.getDetailsForPromotionalExpensesRequestTax10PercentList())) {
            for (DetailsForPromotionalExpensesRequest detail : receivablesRequest.getDetailsForPromotionalExpensesRequestTax10PercentList()) {
                listShainCd.add(detail.getShainCd());
            }
        }
        if (CollectionUtil.isNotEmpty(receivablesRequest.getDetailsForPromotionalExpensesRequestLogisticFeesList())) {
            for (DetailsForPromotionalExpensesRequest detail : receivablesRequest.getDetailsForPromotionalExpensesRequestLogisticFeesList()) {
                listShainCd.add(detail.getShainCd());
            }
        }

        List<ShainAddressEntity> shainAddressEntities = shainAddressRepository.findAllById(listShainCd);
        if (CollectionUtil.isNotEmpty(shainAddressEntities)) {
            emails = shainAddressEntities.stream().map(e -> e.getMailAddress()).collect(Collectors.toSet());
        }
        return emails;
    }
}
