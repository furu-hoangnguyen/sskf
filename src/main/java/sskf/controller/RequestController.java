package sskf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sskf.common.Constants;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.RequestModelForApply;
import sskf.model.request.RequestModelForApproval;
import sskf.model.request.RequestModelForSendBack;
import sskf.model.response.ApprovalPersonsResponse;
import sskf.model.response.CountRequestResponse;
import sskf.model.response.FilterSelectResponse;
import sskf.model.response.LockModelResponse;
import sskf.model.response.RequestResponse;
import sskf.service.RequestService;
import sskf.util.CollectionUtil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping(value = "/api/request")
@RestController
@Slf4j
public class RequestController {

    @Autowired
    private RequestService requestService;


    @GetMapping("/count")
    public ResponseEntity<CountRequestResponse> countRequest() {

        CountRequestResponse countRequestResponse = requestService.countRequest();
        return ResponseEntity.ok(countRequestResponse);
    }

    @GetMapping("/get-requests/re-use")
    public ResponseEntity<List<RequestResponse>> getRequestListReuse(BaseSearchRequest request) {
        List<RequestResponse> responses = requestService.getRequestListReuse(request);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-requests")
    public ResponseEntity<Page<RequestResponse>> getRequests(@RequestParam(name = "wrapperRequest") String wrapperRequestJson) throws JsonProcessingException {
        Page<RequestResponse> response = requestService.getRequests(wrapperRequestJson);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/select")
    public ResponseEntity<FilterSelectResponse> getFilterSelect() {

        FilterSelectResponse filterSelectResponse = requestService.getFilterSelect();
        return ResponseEntity.ok(filterSelectResponse);
    }


    @GetMapping("/get-requests/count")
    public ResponseEntity<List<Long>> countRequestListDefault() {

        List<Long> list = requestService.countRequestListDefault();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping(value = "/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long requestId) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController deleteRequest");
            requestService.deleteRequest(requestId);
            return ResponseEntity.ok("Delete success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController deleteRequest");
        }

    }

    @PutMapping("/unlock-request/{requestId}")
    public ResponseEntity<?> unLockRequest(@PathVariable Long requestId) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController unLockRequest");
            requestService.unLockRequest(requestId);
            return ResponseEntity.ok("UnLockRequest success!");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController unLockRequest");
        }
    }

    @GetMapping("/get-approval-persons")
    public ResponseEntity<?> getApprovalPersons(@RequestParam(name = "mstApprovalFlowCd") String mstApprovalFlowCd, @RequestParam(name = "requestCd") String requestCd) {
        try {
            log.info("Log begin API: get-approval-persons");
            ApprovalPersonsResponse approvalPersons = requestService.findApprovalPersons(mstApprovalFlowCd, requestCd);
            return ResponseEntity.ok(approvalPersons);
        } finally {
            log.info("Log end API: get-approval-persons");
        }
    }

    @GetMapping("/get-saved-approval-persons")
    public ResponseEntity<?> getSavedApprovalPersons(@RequestParam(name = "requestCd") String requestCd) {
        try {
            log.info("Log begin API: get-saved-approval-persons");

            ApprovalPersonsResponse approvalPerons = requestService.findSavedApprovalPersons(requestCd);
            return ResponseEntity.ok(approvalPerons);
        } finally {
            log.info("Log end API: get-saved-approval-persons");
        }
    }

    @PutMapping(value = "/apply")
    public ResponseEntity<?> apply(@RequestBody @Valid RequestModelForApply requestModelForApply) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController apply");
            requestService.apply(requestModelForApply);
            return ResponseEntity.ok("apply success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController apply");
        }

    }

    @PutMapping(value = "/reject")
    public ResponseEntity<?> reject(@RequestBody @Valid RequestModelForApproval requestModel) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController reject");
            requestService.reject(requestModel);
            return ResponseEntity.ok("reject success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController reject");
        }
    }

    @PutMapping(value = "/approver-send-back")
    public ResponseEntity<?> sendBack(@RequestBody @Valid RequestModelForSendBack requestModel) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController sendBack");
            requestService.sendBack(requestModel.getRequestCd(), requestModel.getTargetStep(), requestModel.getComment());
            return ResponseEntity.ok("sendBack success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController sendBack");
        }
    }

    @PutMapping(value = "/applier-send-back/{requestCd}")
    public ResponseEntity<?> confirmSendBack(@PathVariable Long requestCd) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController applierSendBack");
            requestService.confirmSendBack(requestCd);
            return ResponseEntity.ok("applierSendBack success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController applierSendBack");
        }
    }

    @GetMapping(value = "/validate-for-approval-actions/{requestCd}")
    public ResponseEntity<?> validateForApprovalActions(@PathVariable Long requestCd, @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt) {
        try {
            RequestsEntity requestsEntity = requestService.getRequestByCd(requestCd);
            List<ApprovalFlowDetailsEntity> grantedPermissions = requestService.checkPermissionUserForApproval(requestCd, requestsEntity.getStepNumber());
            if (CollectionUtil.isEmpty(grantedPermissions)) {
                return ResponseEntity.ok("no-permission");
            }

            Boolean isUpdated = requestService.checkUpdated(requestCd, updatedAt).getIsUpdated();
            if (isUpdated) {
                return ResponseEntity.ok("record-has-been-modified");
            }
            return ResponseEntity.ok("ok");

        } finally {
            log.info("Log end API: validateForApprovalActions");
        }
    }

    @PutMapping(value = "/approval")
    public ResponseEntity<?> approval(@RequestBody @Valid RequestModelForApproval requestModel) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController requestApproval");
            requestService.approve(requestModel.getRequestCd(), requestModel.getComment(), requestModel.getApprovingStep());
            return ResponseEntity.ok("approval success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController requestApproval");
        }
    }

    @PutMapping(value = "/settlement")
    @PreAuthorize("hasAuthority('"+ Constants.ROLE_ACCOUNTING_CHARGE +"')")
    public ResponseEntity<?> settlement(@RequestBody @Valid RequestModelForApproval requestModel) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController settlement");
            requestService.settlement(requestModel.getRequestCd(), requestModel.getComment());
            return ResponseEntity.ok("settlement success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController settlement");
        }
    }

    @GetMapping(value = "/check-updated/{requestCd}")
    public ResponseEntity<?> checkUpdated(@PathVariable Long requestCd, @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController checkUpdated");
            LockModelResponse isUpdated = requestService.checkUpdated(requestCd, updatedAt);
            return ResponseEntity.ok(isUpdated);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController checkUpdated");
        }
    }
}
