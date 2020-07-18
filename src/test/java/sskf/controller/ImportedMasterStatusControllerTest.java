package sskf.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.model.response.ImportedMasterStatusResponse;
import sskf.service.master.ImportedSituationService;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class ImportedMasterStatusControllerTest {

    @InjectMocks
    private ImportedMasterStatusController importedMasterStatusController;

    @Mock
    private ImportedSituationService importedSituationService;

    @Test
    public void getRequestStatusTest() {
        List<ImportedMasterStatusResponse> importedMasterStatusResponses = new ArrayList<>();

        Mockito.when(importedSituationService.getRequestStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(importedMasterStatusResponses);

        ResponseEntity<?> response = importedMasterStatusController.getRequestStatus("mst_rel_yakushoku_shain", "3", "2020-07-01", "2020-07-01");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(importedMasterStatusResponses);
        Assertions.assertThat(response.getHeaders().containsKey("request_status")).isEqualTo(true);
        Assertions.assertThat(response.getHeaders().get("request_status").get(0)).isEqualTo("200");
    }
}
