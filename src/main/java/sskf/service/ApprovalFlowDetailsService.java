package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.ReplaceChargePerSonsIsDeputyModel;
import sskf.model.response.ApprovalPerson;

import java.util.List;
import java.util.Set;

public interface ApprovalFlowDetailsService {

    ApprovalFlowDetailsEntity createUserApply(ApprovalFlowDetailsRequest approvalFlowDetailsRequest, RequestsEntity requestsEntity);

    ApprovalFlowDetailsEntity updateUserApply(ApprovalFlowDetailsRequest approvalFlowDetailsRequest, RequestsEntity requestsEntity);

    List<ApprovalFlowDetailsEntity> list(BaseSearchRequest request);

    List<ApprovalPerson> listResponse(BaseSearchRequest request);

    Set<String> getAlertEmailForApplierAndApproval(Long requestCd, Byte stepNumber, String enableFlag);

    Set<String> getAlertEmailForApproverTarget(Long requestCd, Byte stepNumber);

    Set<String> getAlertEmailForApproverIsSkipped(Long requestCd, String enableFlag, Byte stepNumberMin, Byte stepNumberMax);

    Set<String> getAlertEmailForApproverSameRole(Long requestCd, String enableFlag, Byte stepNumberSource);

    void replaceChargePersons(ReplaceChargePerSonsIsDeputyModel request);

    void deleteChargeIsDeputy(Long approvalFlowCd);

    void insert(Long requestCd,  ApprovalPerson request);

    List<ApprovalPerson> findApplyPersons (String requestCd);
}
