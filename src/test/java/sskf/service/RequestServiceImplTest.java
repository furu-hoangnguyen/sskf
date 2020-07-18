package sskf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.mapper.MstRequestStatusesMapper;
import sskf.mapper.MstRequestTypesMapper;
import sskf.mapper.RequestMapper;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.response.CountRequestResponse;
import sskf.model.response.FilterSelectResponse;
import sskf.model.response.MstRequestStatusesResponse;
import sskf.model.response.MstRequestTypesResponse;
import sskf.model.response.RequestResponse;
import sskf.repository.MstRequestStatusesRepository;
import sskf.repository.MstRequestTypeRepository;
import sskf.repository.RequestsRepository;
import sskf.service.impl.RequestServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private MstRequestTypeRepository mstRequestTypeRepository;

    @Mock
    private MstRequestStatusesRepository mstRequestStatusesRepository;

    @Mock
    private MstRequestTypesMapper mstRequestTypesMapper;

    @Mock
    private MstRequestStatusesMapper mstRequestStatusesMapper;

    @Mock
    private ShainService shainService;

    @Test
    public void countRequest_With_Success() {

        String shainCd = "cd_1";
        when(shainService.getUserNameByToken()).thenReturn(shainCd);

        when(requestsRepository.countRequestCreate(shainCd)).thenReturn(2);
        when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenReturn(3);
        when(requestsRepository.countRequestWaitingApply(shainCd)).thenReturn(4);
        when(requestsRepository.countRequestWaitingApprove(shainCd)).thenReturn(1);
        when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenReturn(0);

        CountRequestResponse countRequestResponse = requestService.countRequest();

        Assertions.assertThat(countRequestResponse.getCountRequestCreate()).isEqualTo(2);
        Assertions.assertThat(countRequestResponse.getCountRequestWaitingConfirm()).isEqualTo(3);
        Assertions.assertThat(countRequestResponse.getCountRequestWaitingApply()).isEqualTo(4);
        Assertions.assertThat(countRequestResponse.getCountRequestWaitingApprove()).isEqualTo(1);
        Assertions.assertThat(countRequestResponse.getCountRequestWaitingConfirmSettlement()).isEqualTo(0);
    }

    @Test
    public void countRequestList_With_CountRequestCreate_Fail() {
        try {
            String shainCd = "cd_1";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.countRequestCreate(shainCd)).thenThrow(new RuntimeException("count request-list create fail."));
            when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenReturn(3);
            when(requestsRepository.countRequestWaitingApply(shainCd)).thenReturn(4);
            when(requestsRepository.countRequestWaitingApprove(shainCd)).thenReturn(1);
            when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenReturn(0);

            CountRequestResponse countRequestResponse = requestService.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list create fail.");
        }
    }

    @Test
    public void countRequestList_With_CountRequestWaitingConfirm_Fail() {
        try {
            String shainCd = "cd_1";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.countRequestCreate(shainCd)).thenReturn(2);
            when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenThrow(new RuntimeException("count request-list waiting confirm fail."));
            when(requestsRepository.countRequestWaitingApply(shainCd)).thenReturn(4);
            when(requestsRepository.countRequestWaitingApprove(shainCd)).thenReturn(1);
            when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenReturn(0);

            CountRequestResponse countRequestResponse = requestService.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list waiting confirm fail.");
        }
    }

    @Test
    public void countRequestList_With_CountRequestWaitingApply_Fail() {
        try {
            String shainCd = "cd_1";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.countRequestCreate(shainCd)).thenReturn(2);
            when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenReturn(3);
            when(requestsRepository.countRequestWaitingApply(shainCd)).thenThrow(new RuntimeException("count request-list waiting apply fail."));
            when(requestsRepository.countRequestWaitingApprove(shainCd)).thenReturn(1);
            when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenReturn(0);

            CountRequestResponse countRequestResponse = requestService.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list waiting apply fail.");
        }
    }

    @Test
    public void countRequestList_With_CountRequestWaitingApprove_Fail() {
        try {
            String shainCd = "cd_1";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.countRequestCreate(shainCd)).thenReturn(2);
            when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenReturn(3);
            when(requestsRepository.countRequestWaitingApply(shainCd)).thenReturn(4);
            when(requestsRepository.countRequestWaitingApprove(shainCd)).thenThrow(new RuntimeException("count request-list waiting approve fail."));
            when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenReturn(0);

            CountRequestResponse countRequestResponse = requestService.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list waiting approve fail.");
        }
    }

    @Test
    public void countRequestList_With_CountRequestWaitingConfirmSettlement_Fail() {
        try {
            String shainCd = "cd_1";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.countRequestCreate(shainCd)).thenReturn(2);
            when(requestsRepository.countRequestWaitingConfirm(shainCd)).thenReturn(3);
            when(requestsRepository.countRequestWaitingApply(shainCd)).thenReturn(4);
            when(requestsRepository.countRequestWaitingApprove(shainCd)).thenReturn(1);
            when(requestsRepository.countRequestWaitingConfirmSettlement(shainCd)).thenThrow(new RuntimeException("count request-list waiting confirm settlement fail."));

            CountRequestResponse countRequestResponse = requestService.countRequest();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("count request-list waiting confirm settlement fail.");
        }
    }

    @Test
    public void getRequestFilter_With_Success_InProgress_1() throws JsonProcessingException {
        String field = "requestedAt";
        String wrapperRequestJson = "{\"type\":3,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"asc\",\"limit\":20,\"filterRequest\":{}}";
        String shainCd = "118602";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    public void getRequestFilter_With_Success_InProgress_2() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":3,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{" +
                "\"settlementNumber\":\"1234\",\"supplierName\":\"abc\",\"appliedAtMin\":\"2020/06/02 09:30:56\",\"appliedAtMax\":\"2020/06/02 09:30:56\",\"paymentScheduledDateMin\":\"2020/06/02\"," +
                "\"paymentScheduledDateMax\":\"2020/06/02\",\"numberOfStagnancyDay\":3,\"amountMin\":100,\"amountMax\":1000}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }


    @Test
    public void getRequestFilter_With_Success_InProgress_3() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":2,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestFilter_With_Fail_InProgress() {
        try {
            String shainCd = "118602";
            String wrapperRequestJson = "{\"type\":2,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

            when(shainService.getUserNameByToken()).thenReturn(shainCd);

            when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenThrow(new RuntimeException("Error"));

            Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("Error");
        }
    }

    @Test
    public void getFilterSelect_With_Success() {

        when(mstRequestTypeRepository.findAll()).thenReturn(new ArrayList<MstRequestTypesEntity>());
        when(mstRequestStatusesRepository.findAll()).thenReturn(new ArrayList<MstRequestStatusesEntity>());

        when(mstRequestTypesMapper.toResponseList(any(List.class))).thenReturn(new ArrayList<MstRequestTypesResponse>());
        when(mstRequestStatusesMapper.toResponseList(any(List.class))).thenReturn(new ArrayList<MstRequestStatusesResponse>());

        FilterSelectResponse expected = new FilterSelectResponse(new ArrayList<MstRequestTypesResponse>(), new ArrayList<MstRequestStatusesResponse>());

        FilterSelectResponse actual = requestService.getFilterSelect();

        Assertions.assertThat(actual.getMstRequestStatusesResponses()).isEqualTo(expected.getMstRequestStatusesResponses());
        Assertions.assertThat(actual.getMstRequestTypesResponses()).isEqualTo(expected.getMstRequestTypesResponses());
    }

    @Test
    public void getFilterSelect_With_Fail() {
        try {
            when(mstRequestTypeRepository.findAll()).thenReturn(new ArrayList<MstRequestTypesEntity>());
            when(mstRequestStatusesRepository.findAll()).thenThrow(new RuntimeException("Error"));

            FilterSelectResponse actual = requestService.getFilterSelect();
        } catch (Exception e) {

            Assertions.assertThat(e).hasMessage("Error");
        }
    }

    @Test
    public void countRequestListDefault_With_Success() {
        String shainCd = "118602";
        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        when(requestsRepository.count(any(Specification.class))).thenReturn(Long.valueOf(3));

        List<Long> expected = new ArrayList<>();
        expected.add(Long.valueOf(3));
        expected.add(Long.valueOf(3));
        expected.add(Long.valueOf(3));

        List<Long> actual = requestService.countRequestListDefault();

        Assertions.assertThat(actual.size()).isEqualTo(3);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void countRequestListDefault_With_Fail() {
        try {
            String shainCd = "118602";
            when(shainService.getUserNameByToken()).thenReturn(shainCd);
            when(requestsRepository.count(any(Specification.class))).thenThrow(new RuntimeException("Error"));
            List<Long> actual = requestService.countRequestListDefault();
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessage("Error");
        }
    }

    @Test
    public void getRequestFilter_With_Success_Create() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":7,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestFilter_With_Success_Confirm() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":8,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestFilter_With_Success_Apply() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":9,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestFilter_With_Success_Approve() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":10,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestFilter_With_Success_ConfirmSettlement() throws JsonProcessingException {
        String field = "requestedAt";
        String shainCd = "118602";
        String wrapperRequestJson = "{\"type\":11,\"page\":0,\"field\":\"requestedAt\",\"orderBy\":\"desc\",\"limit\":20,\"filterRequest\":{}}";

        when(shainService.getUserNameByToken()).thenReturn(shainCd);
        List<RequestResponse> expectedResponse = new ArrayList<>();
        Page expectedPage = new PageImpl(new ArrayList<RequestsEntity>());
        when(requestsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(requestMapper.toResponseList(any(List.class))).thenReturn(expectedResponse);

        Page<RequestResponse> requestResponses = requestService.getRequests(wrapperRequestJson);

        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(requestsRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());
        verifyNoMoreInteractions(requestsRepository);

        Pageable pageSpecification = pageArgument.getValue();

        Assertions.assertThat(requestResponses.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(requestResponses.getContent()).isEqualTo(expectedResponse);
        Assertions.assertThat(pageSpecification.getPageSize()).isEqualTo(20);
        Assertions.assertThat(pageSpecification.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageSpecification.getSort().getOrderFor(field).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    public void getRequestDetails_With_Success() {
        MstRequestTypesEntity mstRequestTypesEntity = new MstRequestTypesEntity();
        mstRequestTypesEntity.setName("販売未収金");

        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("確認待ち");

        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setMstRequestTypesEntity(mstRequestTypesEntity);
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        Long cd = Long.valueOf("77");

        Mockito.when(requestsRepository.getOne(cd)).thenReturn(requestsEntity);

        String url = requestService.getRequestDetails(cd);
        Assertions.assertThat(url).isEqualTo("account-receivables/confirm-create/77/view");
    }

}