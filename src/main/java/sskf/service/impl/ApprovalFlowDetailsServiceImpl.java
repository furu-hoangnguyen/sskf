package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.ApprovalFlowDetailsMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.ReplaceChargePerSonsIsDeputyModel;
import sskf.model.response.ApprovalPerson;
import sskf.repository.ApprovalFlowDetailsRepository;
import sskf.repository.MstBumonRepository;
import sskf.repository.RequestsRepository;
import sskf.repository.ShainRepository;
import sskf.service.ApprovalFlowDetailsService;
import sskf.service.OperationHistoriesService;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApprovalFlowDetailsServiceImpl extends BaseServiceHasSearchRSQL<ApprovalFlowDetailsEntity> implements ApprovalFlowDetailsService {

    @Autowired
    private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private MstBumonRepository mstBumonRepository;

    @Autowired
    private ApprovalFlowDetailsMapper approvalFlowDetailsMapper;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private ShainService shainService;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    @Override
    @Transactional
    public ApprovalFlowDetailsEntity createUserApply(ApprovalFlowDetailsRequest approvalFlowDetailsRequest, RequestsEntity requestsEntity) {
        try {
            log.info("Log begin service ApprovalFlowDetailsServiceImpl: createUserApply");
            ShainEntity shainEntity = shainRepository.getOne(approvalFlowDetailsRequest.getShainCd());
            if (ObjectUtils.isEmpty(shainEntity)) {
                throw new SSKFException("Shain", "Shain not found!");
            }

            MstBumonEntity mstBumonEntity = mstBumonRepository.getOne(approvalFlowDetailsRequest.getBumonCd());

            if (ObjectUtils.isEmpty(mstBumonEntity)) {
                throw new SSKFException("Bumon", "Bumon not found!");
            }
            ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();
            approvalFlowDetailsEntity.setBumonNm(mstBumonEntity.getBumonNm());
            approvalFlowDetailsEntity.setMstBumonEntity(mstBumonEntity);
            approvalFlowDetailsEntity.setShainNm(shainEntity.getShainNm());
            approvalFlowDetailsEntity.setShainEntity(shainEntity);
            approvalFlowDetailsEntity.setRequestsEntity(requestsEntity);
            approvalFlowDetailsEntity.setIsDeputy(false);
            approvalFlowDetailsEntity.setStepNumber((byte) 1);

            return approvalFlowDetailsRepository.save(approvalFlowDetailsEntity);

        } finally {
            log.info("Log end service ApprovalFlowDetailsServiceImpl: createUserApply");
        }
    }

    @Override
    public ApprovalFlowDetailsEntity updateUserApply(ApprovalFlowDetailsRequest approvalFlowDetailsRequest, RequestsEntity requestsEntity) {
        try {
            log.info("Log begin service ApprovalFlowDetailsServiceImpl: createUserApply");
            ShainEntity shainEntity = shainRepository.getOne(approvalFlowDetailsRequest.getShainCd());
            if (ObjectUtils.isEmpty(shainEntity)) {
                throw new SSKFException("Shain", "Shain not found!");
            }

            MstBumonEntity mstBumonEntity = mstBumonRepository.getOne(approvalFlowDetailsRequest.getBumonCd());

            if (ObjectUtils.isEmpty(mstBumonEntity)) {
                throw new SSKFException("Bumon", "Bumon not found!");
            }

            ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();

            Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntitySet = requestsEntity.getApprovalFlowDetailsEntities();
            if (CollectionUtil.isNotEmpty(approvalFlowDetailsEntitySet)) {
                for (ApprovalFlowDetailsEntity detailsEntity : approvalFlowDetailsEntitySet) {
                    if (detailsEntity.getStepNumber().equals((byte) 1) && detailsEntity.getIsDeputy() == false) {
                        approvalFlowDetailsEntity = detailsEntity;
                        break;
                    }
                }
            }

            approvalFlowDetailsEntity.setBumonNm(mstBumonEntity.getBumonNm());
            approvalFlowDetailsEntity.setMstBumonEntity(mstBumonEntity);
            approvalFlowDetailsEntity.setShainNm(shainEntity.getShainNm());
            approvalFlowDetailsEntity.setShainEntity(shainEntity);
            approvalFlowDetailsEntity.setRequestsEntity(requestsEntity);
            approvalFlowDetailsEntity.setIsDeputy(false);
            approvalFlowDetailsEntity.setStepNumber((byte) 1);

            return approvalFlowDetailsRepository.save(approvalFlowDetailsEntity);

        } finally {
            log.info("Log end service ApprovalFlowDetailsServiceImpl: createUserApply");
        }
    }

    @Override
    public List<ApprovalFlowDetailsEntity> list(BaseSearchRequest request) {
        try {
            log.info("Log begin service ApprovalFlowDetailsServiceImpl: list");
            List<ApprovalFlowDetailsEntity> result = listRSQL(approvalFlowDetailsRepository,
                    request, ApprovalFlowDetailsEntity.class);
            return result;
        } finally {
            log.info("Log end service ApprovalFlowDetailsServiceImpl: list");
        }
    }

    @Override
    public List<ApprovalPerson> listResponse(BaseSearchRequest request) {
        try {
            log.info("Log begin service ApprovalFlowDetailsServiceImpl: listResponse");
            List<ApprovalFlowDetailsEntity> result = list(request);
            return result.stream().map(approvalFlowDetailsMapper :: toResponseApprovalPerson).collect(Collectors.toList());
        } finally {
            log.info("Log end service ApprovalFlowDetailsServiceImpl: listResponse");
        }
    }

    @Override
    public Set<String> getAlertEmailForApplierAndApproval(Long requestCd, Byte stepNumber, String enableFlag) {
        String userNameByToken = shainService.getUserNameByToken();
        String searchQuery = "requestsEntity.cd==" + requestCd+ ";shainEntity.shainCd!='" + userNameByToken + "';stepNumber<=" + stepNumber +";"+ enableFlag;
        return findActionEmail(searchQuery);
    }

    @Override
    public Set<String> getAlertEmailForApproverTarget(Long requestCd, Byte stepNumber) {
        String searchQuery = "requestsEntity.cd==" + requestCd + ";" + "stepNumber==" + stepNumber;
        return findActionEmail(searchQuery);
    }

    @Override
    public Set<String> getAlertEmailForApproverIsSkipped(Long requestCd, String enableFlag, Byte stepNumberMin, Byte stepNumberMax) {
        String userNameByToken = shainService.getUserNameByToken();
        String searchQuery = "requestsEntity.cd==" + requestCd+ ";shainEntity.shainCd!='" + userNameByToken + "';stepNumber>" + stepNumberMin + ";stepNumber<" + stepNumberMax + Constants.AND_RSQL + enableFlag;
        return findActionEmail(searchQuery);
    }

    @Override
    public Set<String> getAlertEmailForApproverSameRole(Long requestCd, String enableFlag, Byte stepNumberSource) {
        String userNameByToken = shainService.getUserNameByToken();
        String searchQuery = "requestsEntity.cd==" + requestCd + Constants.AND_RSQL + "shainEntity.shainCd!='" + userNameByToken + "';stepNumber==" + stepNumberSource + Constants.AND_RSQL + enableFlag;
        return findActionEmail(searchQuery);
    }

    @Override
    @Transactional
    public void replaceChargePersons(ReplaceChargePerSonsIsDeputyModel request) {
        try {
            log.info("Log begin service  ApprovalFlowDetailsServiceImpl: replaceChargePersons");
            ApprovalFlowDetailsEntity chargePersonMain = approvalFlowDetailsRepository.getOne(request.getApprovalFlowDetailMain());
            if (ObjectUtil.isEmpty(chargePersonMain) || Boolean.TRUE.equals(chargePersonMain.getIsDeputy())) {
                throw new SSKFException("ApprovalFlowDetailsEntity", "chargePersonMain not found!");
            }

            chargePersonMain.setIsDeputy(Boolean.TRUE);

            ApprovalFlowDetailsEntity chargePersonIsDeputy = approvalFlowDetailsRepository.getOne(request.getApprovalFlowDetailIsDeputy());
            if (ObjectUtil.isEmpty(chargePersonIsDeputy) || !Boolean.TRUE.equals(chargePersonIsDeputy.getIsDeputy())) {
                throw new SSKFException("ApprovalFlowDetailsEntity", "chargePersonIsDeputy not found!");
            }
            chargePersonIsDeputy.setIsDeputy(Boolean.FALSE);

            if (chargePersonMain.getRequestsEntity().getCd() != chargePersonIsDeputy.getRequestsEntity().getCd() || chargePersonMain.getStepNumber() != chargePersonIsDeputy.getStepNumber()) {
                throw new SSKFException("replaceChargePersons", "Can not replaceChargePersons!");
            }
            checkPermissionToChangeCharge(chargePersonMain.getRequestsEntity().getCd());
            List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntityList = Arrays.asList(chargePersonMain, chargePersonIsDeputy);
            approvalFlowDetailsRepository.saveAll(approvalFlowDetailsEntityList);
            operationHistoriesService.setOperationHistoriesForApprovalFlowsDeputy(chargePersonIsDeputy.getRequestsEntity(), "担当交代", chargePersonIsDeputy.getStepNumber(), chargePersonIsDeputy.getShainNm());
        } finally {
            log.info("Log end service  ApprovalFlowDetailsServiceImpl: replaceChargePersons");
        }
    }

    @Override
    @Transactional
    public void deleteChargeIsDeputy(Long approvalFlowCd) {
        try {
            log.info("Log begin service  ApprovalFlowDetailsServiceImpl: deleteChargeIsDeputy");
            ApprovalFlowDetailsEntity chargePersonIsDeputy = approvalFlowDetailsRepository.getOne(approvalFlowCd);
            RequestsEntity requestsEntity = chargePersonIsDeputy.getRequestsEntity();

            Boolean requestAtNotNull = ObjectUtil.isNotEmpty(requestsEntity.getRequestedAt());
            if (!statusValidForAction(requestsEntity.getMstRequestStatusesEntity().getName(), requestAtNotNull)) {
                throw new SSKFException("deleteChargeIsDeputy", "Not permission!");
            }

            if (ObjectUtil.isEmpty(chargePersonIsDeputy) || !Boolean.TRUE.equals(chargePersonIsDeputy.getIsDeputy())) {
                throw new SSKFException("ApprovalFlowDetailsEntity", "chargePersonIsDeputy not found!");
            }

            if (chargePersonIsDeputy.getStepNumber() != 1) {
                String userLogin = shainService.getUserNameByToken();
                List<Byte> stepNumbers = Arrays.asList((byte)2, (byte) 3, (byte) 4);
                List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntity = approvalFlowDetailsRepository.findByRequestsEntityAndStepNumberInAndShainEntity_ShainCd(requestsEntity, stepNumbers, userLogin);
                if (CollectionUtil.isEmpty(approvalFlowDetailsEntity)) {
                    throw new SSKFException("deleteChargeIsDeputy", "Not permission!");
                }
            }

            approvalFlowDetailsRepository.delete(chargePersonIsDeputy);
            operationHistoriesService.setOperationHistoriesForApprovalFlowsDeputy(chargePersonIsDeputy.getRequestsEntity(), "代理人削除", chargePersonIsDeputy.getStepNumber(), chargePersonIsDeputy.getShainNm());
        } finally {
            log.info("Log end service  ApprovalFlowDetailsServiceImpl: deleteChargeIsDeputy");
        }
    }

    @Override
    @Transactional
    public void insert(Long requestCd, ApprovalPerson request) {
        try {
            log.info("Log begin service  ApprovalFlowDetailsServiceImpl: insert");
            RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
            if (ObjectUtil.isEmpty(request)) {
                throw new SSKFException("requestsEntity", "requestsEntity not existed!");
            }

            ShainEntity shainEntity = shainRepository.getOne(request.getShainCd());
            if (ObjectUtil.isEmpty(shainEntity)) {
                throw new SSKFException("shainEntity", "shainEntity not existed!");
            }

            MstBumonEntity mstBumonEntity = mstBumonRepository.getOne(request.getBumonCd());
            if (ObjectUtil.isEmpty(mstBumonEntity)) {
                throw new SSKFException("mstBumonEntity", "mstBumonEntity not existed!");
            }

            if (request.getStepNumber() != 1) {
                String userLogin = shainService.getUserNameByToken();
                List<Byte> stepNumbers = Arrays.asList((byte)2, (byte) 3, (byte) 4);
                List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntity = approvalFlowDetailsRepository.findByRequestsEntityAndStepNumberInAndShainEntity_ShainCd(requestsEntity, stepNumbers, userLogin);
                if (CollectionUtil.isEmpty(approvalFlowDetailsEntity)) {
                    throw new SSKFException("insert charge", "Not permission!");
                }
            }

            Boolean requestAtNotNull = ObjectUtil.isNotEmpty(requestsEntity.getRequestedAt());
            if (!statusValidForAction(requestsEntity.getMstRequestStatusesEntity().getName(), requestAtNotNull)) {
                throw new SSKFException("insert charge", "Not permission!");
            }

            ApprovalFlowDetailsEntity entity = new ApprovalFlowDetailsEntity();
            entity.setRequestsEntity(requestsEntity);
            entity.setShainNm(shainEntity.getShainNm());
            entity.setShainEntity(shainEntity);
            entity.setBumonNm(mstBumonEntity.getBumonNm());
            entity.setMstBumonEntity(mstBumonEntity);
            entity.setIsDeputy(true);
            entity.setStepNumber(request.getStepNumber());
            approvalFlowDetailsRepository.save(entity);
            operationHistoriesService.setOperationHistoriesForApprovalFlowsDeputy(requestsEntity, "代理人追加", entity.getStepNumber(), entity.getShainNm());
        } finally {
            log.info("Log end service  ApprovalFlowDetailsServiceImpl: insert");
        }
    }

    public List<ApprovalPerson> findApplyPersons (String requestCd) {
        BaseSearchRequest request = new BaseSearchRequest();
        request.setKeyword("requestsEntity.cd=='" + requestCd + "';isDeputy==0;stepNumber==1");
        request.setLimitNumber(100);
        request.setPageNumber(0);

        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities = list(request);
        List<ApprovalPerson> applier = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(approvalFlowDetailsEntities)) {
            applier = approvalFlowDetailsEntities.stream().map(approvalFlowDetailsMapper ::toResponseApprovalPerson).collect(Collectors.toList());
        }
        return applier;
    }

    private Set<String> findActionEmail(String searchQuery) {
        BaseSearchRequest request = new BaseSearchRequest();
        request.setKeyword(searchQuery);
        request.setLimitNumber(100);
        request.setPageNumber(0);

        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities = list(request);
        Set<String> emails = new HashSet<>();
        if (CollectionUtil.isNotEmpty(approvalFlowDetailsEntities)) {
            for (ApprovalFlowDetailsEntity entity: approvalFlowDetailsEntities) {
                if (entity.getShainEntity() != null && entity.getShainEntity().getShainAddressEntity() != null && entity.getShainEntity().getShainAddressEntity().getMailAddress() != null) {
                    emails.add(entity.getShainEntity().getShainAddressEntity().getMailAddress());
                }
            }
        }
        return emails;
    }

    private void checkPermissionToChangeCharge(Long requestCd) {
        RequestsEntity requestsEntity = requestsRepository.getOne(requestCd);
        String userLogin = shainService.getUserNameByToken();
        List<Byte> stepsNumber = Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4);
        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntity = approvalFlowDetailsRepository.findByRequestsEntityAndStepNumberInAndShainEntity_ShainCd(requestsEntity, stepsNumber, userLogin);
        if (CollectionUtil.isEmpty(approvalFlowDetailsEntity)) {
            throw new SSKFException("Change charge", "Not permission!");
        }

        Boolean requestAtNotNull = ObjectUtil.isNotEmpty(requestsEntity.getRequestedAt());
        if (!statusValidForAction(requestsEntity.getMstRequestStatusesEntity().getName(), requestAtNotNull)) {
            throw new SSKFException("Change charge", "Not permission!");
        }
    }

    private Boolean statusValidForAction(String status, Boolean requestAtNotNull) {
        Boolean isValid = Boolean.FALSE;
        switch (status) {
            case "確認待ち":
            case "申請待ち":
                if (requestAtNotNull)
                    isValid = Boolean.TRUE;
                break;
            case "承認待ち":
                isValid = Boolean.TRUE;
                break;
        }
        return isValid;
    }
}
