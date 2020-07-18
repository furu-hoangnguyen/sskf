package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.request.ReplaceChargePerSonsIsDeputyModel;
import sskf.model.response.ApprovalPerson;
import sskf.service.ApprovalFlowDetailsService;

import java.util.List;

@RestController
@RequestMapping("/api/approval-flow-details")
@Slf4j
public class ApprovalFlowDetailsController {

    @Autowired
    private ApprovalFlowDetailsService approvalFlowDetailsService;

    @GetMapping
    public ResponseEntity<?> list(BaseSearchRequest request) {
        List<ApprovalPerson> productPrice = approvalFlowDetailsService.listResponse(request);
        return ResponseEntity.ok(productPrice);
    }

    @PutMapping("/replace-charge-persons")
    public ResponseEntity<?> replaceChargePersons(@RequestBody ReplaceChargePerSonsIsDeputyModel request) {
        try {
            log.info("Log begin API ApprovalFlowDetailsController: replaceChargePersons");
            approvalFlowDetailsService.replaceChargePersons(request);
            return ResponseEntity.ok("Replace success");
        } finally {
            log.info("Log end API ApprovalFlowDetailsController: replaceChargePersons");
        }

    }

    @DeleteMapping("/delete-charge-is-deputy/{approvalFlowCd}")
    public ResponseEntity<?> deleteChargeIsDeputy(@PathVariable Long approvalFlowCd) {
        try {
            log.info("Log begin API ApprovalFlowDetailsController: replaceChargePersons");
            approvalFlowDetailsService.deleteChargeIsDeputy(approvalFlowCd);
            return ResponseEntity.ok("Replace success");
        } finally {
            log.info("Log end API ApprovalFlowDetailsController: replaceChargePersons");
        }

    }

    @PostMapping("/{requestCd}")
    public ResponseEntity<?> insert(@PathVariable Long requestCd, @RequestBody ApprovalPerson request) {
        try {
            log.info("Log begin API ApprovalFlowDetailsController: insert");
            approvalFlowDetailsService.insert(requestCd, request);
            return ResponseEntity.ok("insert success");
        } finally {
            log.info("Log end API ApprovalFlowDetailsController: insert");
        }

    }

}
