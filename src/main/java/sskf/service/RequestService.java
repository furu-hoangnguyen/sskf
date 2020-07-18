package sskf.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.RequestAttachmentsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.RequestModel;
import sskf.model.request.RequestModelForApply;
import sskf.model.request.RequestModelForApproval;
import sskf.model.response.ApprovalPersonsResponse;
import sskf.model.response.CountRequestResponse;
import sskf.model.response.FilterSelectResponse;
import sskf.model.response.LockModelResponse;
import sskf.model.response.RequestResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {

    CountRequestResponse countRequest();

    List<RequestResponse> getRequestListReuse(BaseSearchRequest request);

    Page<RequestResponse> getRequests(String wrapperRequestJson) throws JsonProcessingException;

    RequestsEntity create(RequestModel requestModel, MultipartFile[] files, String requestType);

    RequestsEntity updateCreate(RequestModel requestModel, MultipartFile[] files, String requestType);

    RequestsEntity update(RequestModel requestModel, MultipartFile[] files);

    RequestsEntity requestSalesChargeEdit(Long requestCd);

    RequestsEntity confirm(RequestsEntity requestsEntity);

    RequestsEntity getRequestByCd(Long requestCd);

    FilterSelectResponse getFilterSelect();

    List<Long> countRequestListDefault();

    void deleteRequest(Long cd);

    void lockRequest(RequestsEntity requestsEntity);

    void unLockRequest(Long cd);

    Boolean checkPermissionUserForApply(Long requestCd);

    List<ApprovalFlowDetailsEntity> checkPermissionUserForApproval(Long requestCd, Byte stepNumber);

    ApprovalPersonsResponse findApprovalPersons(String mstApprovalFlowCd, String requestCd);

    ApprovalPersonsResponse findSavedApprovalPersons(String requestCd);

    void apply(RequestModelForApply requestModelForApply);

    Boolean checkLockRequest(RequestsEntity requestsEntity);

    void reject(RequestModelForApproval requestModel);

    void sendBack(Long requestCd, int targetStep, String comment);

    void confirmSendBack(Long requestCd);

    void approve(Long requestCd, String comment, Byte approvingStep);

    void settlement(Long requestCd, String comment);

    LockModelResponse checkUpdated(Long requestCd, LocalDateTime updatedAt);

    List<RequestAttachmentsEntity> uploadFileForRequests(MultipartFile[] files, RequestsEntity requestsEntity, Boolean writeLogFile);

    RequestsEntity setSettlementNumber(RequestsEntity requestsEntity, RequestModel requestModel);

    void deleteRequestAttachment(Long cd);

    String getRequestDetails(Long cd);

}
