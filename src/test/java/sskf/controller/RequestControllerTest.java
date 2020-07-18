package sskf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.model.response.CountRequestResponse;
import sskf.model.response.FilterSelectResponse;
import sskf.model.response.RequestResponse;
import sskf.service.RequestService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestControllerTest {

    @InjectMocks
    private RequestController requestController;

    @Mock
    private RequestService requestService;

    @Test
    public void countRequest_With_Success() {
        CountRequestResponse countRequestResponse = new CountRequestResponse(1, 2, 3, 4, 5);
        when(requestService.countRequest()).thenReturn(countRequestResponse);

        ResponseEntity<CountRequestResponse> response = requestController.countRequest();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(countRequestResponse);
    }

    @Test
    public void countRequest_With_Fail() {

        try {
            when(requestService.countRequest()).thenThrow(new RuntimeException("count request-list fail."));

            ResponseEntity<CountRequestResponse> response = requestController.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list fail.");
        }
    }

    @Test
    public void getRequestFilter_With_Success() throws JsonProcessingException {

        String wrapperRequestJson = "{\"type\":3,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"asc\",\"limit\":20,\"filterRequest\":{}}";

        Page expectedPage = new PageImpl(new ArrayList<RequestResponse>());

        when(requestService.getRequests(any(String.class))).thenReturn(expectedPage);

        ResponseEntity<Page<RequestResponse>> actual = requestController.getRequests(wrapperRequestJson);

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isEqualTo(expectedPage);
    }

    @Test
    public void getRequestFilter_With_Fail() {

        try {
            when(requestService.getRequests(any(String.class))).thenThrow(new RuntimeException("Error."));
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessage("Error.");
        }
    }

    @Test
    public void getFilterSelect_With_Success() {
        FilterSelectResponse expected = new FilterSelectResponse();
        when(requestService.getFilterSelect()).thenReturn(expected);

        ResponseEntity<FilterSelectResponse> actual = requestController.getFilterSelect();

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isEqualTo(expected);
    }

    @Test
    public void getFilterSelect_With_Fail() {
        try {
            when(requestService.getFilterSelect()).thenThrow(new RuntimeException("Error."));
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessage("Error.");
        }
    }

    @Test
    public void countRequestListDefault_With_Success() {
        List<Long> expected = new ArrayList<>();
        expected.add(Long.valueOf(3));
        expected.add(Long.valueOf(3));
        expected.add(Long.valueOf(3));

        when(requestService.countRequestListDefault()).thenReturn(expected);

        ResponseEntity<List<Long>> actual = requestController.countRequestListDefault();

        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual.getBody()).isEqualTo(expected);
    }

    @Test
    public void countRequestListDefault_With_Fail() {
        try {
            when(requestService.countRequestListDefault()).thenThrow(new RuntimeException("Error"));
            ResponseEntity<List<Long>> actual = requestController.countRequestListDefault();
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessage("Error");
        }
    }

}
