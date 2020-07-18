package sskf.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MasterControllerTest {

    @InjectMocks
    private MasterController masterController;

    @Mock
    private ImportedSituationService importedSituationService;

    @Mock
    private DropdownService dropdownService;

    @Mock
    private MstStoreServices mstStoreServices;

    @Mock
    private MstTantoServices mstTantoServices;

    @Mock
    private MstApprovalFlowsServices mstApprovalFlowsServices;

    @Mock
    private MstApprovalFlowDetailsServices mstApprovalFlowDetailsServices;

    @Test
    public void listTest() {
        BaseSearchRequest request = new BaseSearchRequest();
        Page<ImportedMasterStatusEntity> importedMasterStatusEntities = Page.empty();

        Mockito.when(importedSituationService.list(request)).thenReturn(importedMasterStatusEntities);

        ResponseEntity<?> response = masterController.list(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(importedMasterStatusEntities);
    }

    @Test
    public void getShainFilterSelectOptionTest() {
        List<MstYakushokuResponse> positions = new ArrayList<>();
        List<BumonResponse> departments = new ArrayList<>();

        Mockito.when(dropdownService.listPosition()).thenReturn(positions);
        Mockito.when(dropdownService.listDepartment()).thenReturn(departments);

        ResponseEntity<?> response = masterController.getShainFilterSelectOption();
        FilterSelectOptionResponse filterSelectOptionResponseActual= (FilterSelectOptionResponse) response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(filterSelectOptionResponseActual.getPositions()).isEqualTo(positions);
        Assertions.assertThat(filterSelectOptionResponseActual.getDepartments()).isEqualTo(departments);
    }

    @Test
    public void getStoresTest() {
        BaseSearchRequest request = new BaseSearchRequest();
        Page<MstStoreResponse> stores = Page.empty();

        Mockito.when(mstStoreServices.getStores(request)).thenReturn(stores);

        ResponseEntity<?> response = masterController.getStores(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(stores);
    }

    @Test
    public void getTantosTest() {
        BaseSearchRequest request = new BaseSearchRequest();
        Page<MstTantoResponse> tantos = Page.empty();

        Mockito.when(mstTantoServices.getTantos(request)).thenReturn(tantos);

        ResponseEntity<?> response = masterController.getTantos(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(tantos);
    }

    @Test
    public void getApprovalFlowsTest() {
        BaseSearchRequest request = new BaseSearchRequest();
        Page<MstApprovalFlowsResponse> approvalFlows = Page.empty();

        Mockito.when(mstApprovalFlowsServices.getApprovalFlows(request)).thenReturn(approvalFlows);

        ResponseEntity<?> response = masterController.getApprovalFlows(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(approvalFlows);
    }

    @Test
    public void getApprovalFlowsGroupTest() {
        String mstApprovalFlowCd = "1234";
        MstApprovalFlowForMaster approvalFlowsGroup = new MstApprovalFlowForMaster();

        Mockito.when(mstApprovalFlowDetailsServices.findApprovalFlowsGroup(mstApprovalFlowCd)).thenReturn(approvalFlowsGroup);

        ResponseEntity<?> response = masterController.getApprovalFlowsGroup(mstApprovalFlowCd);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(approvalFlowsGroup);
    }

}
