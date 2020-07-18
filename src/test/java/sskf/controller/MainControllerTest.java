package sskf.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.service.RequestService;

@ExtendWith(SpringExtension.class)
public class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private RequestService requestService;

    @Test
    public void getErrorPathTest() {
        String path = mainController.getErrorPath();
        Assertions.assertThat(path).isEqualTo("/error");
    }

    @Test
    public void goToVueJsAppTest() {
        String path = mainController.goToVueJsApp(null);
        Assertions.assertThat(path).isEqualTo("index");
    }

    @Test
    public void getRequestDetailsTest() {
        Long cd = Long.valueOf("77");

        Mockito.when(requestService.getRequestDetails(cd)).thenReturn("account-receivables/confirm-create/77/view");
        String path = mainController.getRequestDetails(cd);

        Assertions.assertThat(path).isEqualTo("redirect:/account-receivables/confirm-create/77/view");
    }

}
