package sskf.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import sskf.mapper.RequestExhibitionPromotionsMapper;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.model.request.RequestModel;
import sskf.repository.RequestExhibitionPromotionsRepository;
import sskf.repository.RequestsRepository;
import sskf.service.impl.RequestExhibitionPromotionsServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class RequestExhibitionPromotionsServiceTest {

    @InjectMocks
    private RequestExhibitionPromotionsServiceImpl requestExhibitionPromotionsService;

    @Mock
    private RequestService requestService;

    @Mock
    private RequestExhibitionPromotionsRepository requestExhibitionPromotionsRepository;

    @Mock
    private RequestExhibitionPromotionsMapper requestExhibitionPromotionsMapper;

    @Mock
    private HoldingsForRequestExhibitionPromotionService holdingsForRequestExhibitionPromotionService;

    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private EmailServices emailServices;

    @Mock
    private ShainAddressService shainAddressService;

    @Mock
    private OperationHistoriesService operationHistoriesService;


    @Test
    public void createTest() {
        ExhibitionPromotionsRequest exhibitionPromotionsRequest = new ExhibitionPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        exhibitionPromotionsRequest.setCd(1L);
        exhibitionPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.create(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);
        RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = new RequestExhibitionPromotionsEntity();
        exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
        exhibitionPromotionsEntity.setCd(1L);
        Mockito.when(requestExhibitionPromotionsMapper.toEntity(Mockito.any())).thenReturn(exhibitionPromotionsEntity);
        Mockito.when(requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity)).thenReturn(exhibitionPromotionsEntity);
        Mockito.doNothing().when(holdingsForRequestExhibitionPromotionService).setHoldingsForRequestExhibition(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestExhibitionPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(exhibitionPromotionsRequest);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);
        MultipartFile[] files = null;
        ExhibitionPromotionsRequest actual = requestExhibitionPromotionsService.create(exhibitionPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), exhibitionPromotionsRequest.getCd());
    }

    @Test
    public void updateCreateTest() {
        ExhibitionPromotionsRequest exhibitionPromotionsRequest = new ExhibitionPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        exhibitionPromotionsRequest.setCd(1L);
        exhibitionPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.updateCreate(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);

        RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = new RequestExhibitionPromotionsEntity();
        exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
        exhibitionPromotionsEntity.setCd(1L);
        Mockito.when(requestExhibitionPromotionsRepository.getOne(Mockito.anyLong())).thenReturn(exhibitionPromotionsEntity);
        Mockito.when(requestExhibitionPromotionsMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(exhibitionPromotionsEntity);
        Mockito.when(requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity)).thenReturn(exhibitionPromotionsEntity);
        Mockito.doNothing().when(holdingsForRequestExhibitionPromotionService).setHoldingsForRequestExhibition(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestExhibitionPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(exhibitionPromotionsRequest);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);
        MultipartFile[] files = null;
        ExhibitionPromotionsRequest actual = requestExhibitionPromotionsService.updateCreate(exhibitionPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), exhibitionPromotionsRequest.getCd());
    }

    @Test
    public void updateTest() {
        ExhibitionPromotionsRequest exhibitionPromotionsRequest = new ExhibitionPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        exhibitionPromotionsRequest.setCd(1L);
        exhibitionPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.update(Mockito.any(), Mockito.any())).thenReturn(requestsEntity);

        RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = new RequestExhibitionPromotionsEntity();
        exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
        exhibitionPromotionsEntity.setCd(1L);
        Mockito.when(requestExhibitionPromotionsRepository.getOne(Mockito.anyLong())).thenReturn(exhibitionPromotionsEntity);
        Mockito.when(requestExhibitionPromotionsMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(exhibitionPromotionsEntity);
        Mockito.when(requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity)).thenReturn(exhibitionPromotionsEntity);
        Mockito.doNothing().when(holdingsForRequestExhibitionPromotionService).setHoldingsForRequestExhibition(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestExhibitionPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(exhibitionPromotionsRequest);
        MultipartFile[] files = null;
        ExhibitionPromotionsRequest actual = requestExhibitionPromotionsService.update(exhibitionPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), exhibitionPromotionsRequest.getCd());
    }

    @Test
    public void getByRequestIdTest() {
        ExhibitionPromotionsRequest exhibitionPromotionsRequest = new ExhibitionPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        exhibitionPromotionsRequest.setCd(1L);
        exhibitionPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Set<RequestExhibitionPromotionsEntity> requestExhibitionPromotionsEntities = new HashSet<>();

        RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity = new RequestExhibitionPromotionsEntity();
        requestExhibitionPromotionsEntity.setCd(1L);
        requestExhibitionPromotionsEntities.add(requestExhibitionPromotionsEntity);
        requestsEntity.setRequestExhibitionPromotionsEntities(requestExhibitionPromotionsEntities);
        Mockito.when(requestsRepository.findByCdAndIsDeleted(Mockito.anyLong(), Mockito.anyByte())).thenReturn(requestsEntity);

        Mockito.when(requestExhibitionPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(exhibitionPromotionsRequest);

        ExhibitionPromotionsRequest actual = requestExhibitionPromotionsService.getByRequestId(1L, "edit");
        Assert.assertEquals(actual.getCd(), exhibitionPromotionsRequest.getCd());
    }
}
