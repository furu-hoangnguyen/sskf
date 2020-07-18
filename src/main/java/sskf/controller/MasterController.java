package sskf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ImportedMasterStatusEntity;
import sskf.model.master.MstApprovalFlowForMaster;
import sskf.model.response.BumonResponse;
import sskf.model.response.FilterSelectOptionResponse;
import sskf.model.response.MstApprovalFlowsResponse;
import sskf.model.response.MstStoreResponse;
import sskf.model.response.MstTantoResponse;
import sskf.model.response.MstYakushokuResponse;
import sskf.service.DropdownService;
import sskf.service.MstApprovalFlowDetailsServices;
import sskf.service.MstApprovalFlowsServices;
import sskf.service.MstStoreServices;
import sskf.service.MstTantoServices;
import sskf.service.master.ImportedSituationService;

import java.util.List;

@RestController
@RequestMapping("/api/master")
public class MasterController {
    @Autowired
    private ImportedSituationService importedSituationService;

    @Autowired
    private DropdownService dropdownService;

    @Autowired
    private MstStoreServices mstStoreServices;

    @Autowired
    private MstTantoServices mstTantoServices;

    @Autowired
    private MstApprovalFlowsServices mstApprovalFlowsServices;

    @Autowired
    private MstApprovalFlowDetailsServices mstApprovalFlowDetailsServices;

    @GetMapping("/get-imported-situation-status")
    public ResponseEntity<?> list(BaseSearchRequest request) {
        Page<ImportedMasterStatusEntity> importedMasterStatusEntities = importedSituationService.list(request);
        return ResponseEntity.ok(importedMasterStatusEntities);
    }

    @GetMapping("/get-shain-filter-select-option")
    public ResponseEntity<?> getShainFilterSelectOption() {
        List<MstYakushokuResponse> positions = dropdownService.listPosition();
        List<BumonResponse> departments = dropdownService.listDepartment();
        return ResponseEntity.ok(new FilterSelectOptionResponse(positions, departments));
    }

    @GetMapping("/get-stores")
    public ResponseEntity<?> getStores(BaseSearchRequest request) {
        Page<MstStoreResponse> stores = mstStoreServices.getStores(request);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/get-tantos")
    public ResponseEntity<?> getTantos(BaseSearchRequest request) {
        Page<MstTantoResponse> tantos = mstTantoServices.getTantos(request);
        return ResponseEntity.ok(tantos);
    }

    @GetMapping("/get-approval-flows")
    public ResponseEntity<?> getApprovalFlows(BaseSearchRequest request) {
        Page<MstApprovalFlowsResponse> approvalFlows = mstApprovalFlowsServices.getApprovalFlows(request);
        return ResponseEntity.ok(approvalFlows);
    }

    @GetMapping("/get-approval-flows-group")
    public ResponseEntity<?> getApprovalFlowsGroup(String mstApprovalFlowCd) {
        MstApprovalFlowForMaster approvalFlowsGroup = mstApprovalFlowDetailsServices.findApprovalFlowsGroup(mstApprovalFlowCd);
        return ResponseEntity.ok(approvalFlowsGroup);
    }

}
