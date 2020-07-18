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
import sskf.model.response.ShainResponse;
import sskf.service.ShainService;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class ShainControllerTest {

    @InjectMocks
    private ShainController shainController;

    @Mock
    private ShainService shainService;

    @Test
    public void getInformationTest() {
        ShainResponse shainResponse = new ShainResponse();
        Mockito.when(shainService.getInformation()).thenReturn(shainResponse);

        ResponseEntity<ShainResponse> response = shainController.getInformation();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(shainResponse);
    }

    @Test
    public void listShainsTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Page<ShainResponse> shainResponse = Page.empty();

        Mockito.when(shainService.listShains(baseSearchRequest)).thenReturn(shainResponse);

        ResponseEntity<?> response = shainController.listShains(baseSearchRequest);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(shainResponse);
    }

    @Test
    public void listShainsMasterTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Page<ShainResponse> shainResponse = Page.empty();

        Mockito.when(shainService.listShainsMaster(baseSearchRequest)).thenReturn(shainResponse);

        ResponseEntity<?> response = shainController.listShainsMaster(baseSearchRequest);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(shainResponse);
    }

    @Test
    public void listShainForApprovalTest() {
        List<ShainResponse> responsesExpect = new ArrayList<>();
        Byte stepNumber = 13;
        String bumonCd = "4000";
        Long requestCd = Long.valueOf(13);

        Mockito.when(shainService.listShainForApproval(stepNumber, bumonCd, requestCd)).thenReturn(responsesExpect);

        ResponseEntity<?> response = shainController.listShainForApproval(stepNumber, bumonCd, requestCd);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(responsesExpect);
    }

    @Test
    public void updateShainInformationTest() {
        ShainResponse shainResponse = new ShainResponse();
        ShainResponse shainResponseEx = new ShainResponse();

        Mockito.when(shainService.updateShainInformation(shainResponse)).thenReturn(shainResponseEx);

        ResponseEntity<ShainResponse> response = shainController.updateShainInformation(shainResponse);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(shainResponseEx);
    }

}
