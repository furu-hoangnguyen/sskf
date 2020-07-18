package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import sskf.mapper.RequestMannequinPromotionsMapper;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestModel;
import sskf.repository.RequestMannequinPromotionsRepository;
import sskf.repository.RequestsRepository;
import sskf.service.impl.RequestMannequinPromotionsServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class RequestMannequinPromotionsServiceTest {

    @InjectMocks
    private RequestMannequinPromotionsServiceImpl requestMannequinPromotionsService;

    @Mock
    private RequestMannequinPromotionsRepository requestMannequinPromotionsRepository;

    @Mock
    private RequestService requestService;

    @Mock
    private RequestMannequinPromotionsMapper requestMannequinPromotionsMapper;

    @Mock
    private RequestMannequinPromotionDetailsService requestMannequinPromotionDetailsService;

    @Mock
    private ImplementationStoresForRequestMannequinPromotionService implementationStoresForRequestMannequinPromotionService;

    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private ShainAddressService shainAddressService;

    @Mock
    private EmailServices emailServices;

    @Mock
    private OperationHistoriesService operationHistoriesService;

    @Test
    public void createTest() {
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        mannequinPromotionsRequest.setCd(1L);
        mannequinPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.create(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);
        RequestMannequinPromotionsEntity mannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        mannequinPromotionsEntity.setRequestsEntity(requestsEntity);
        mannequinPromotionsEntity.setCd(1L);
        Mockito.when(requestMannequinPromotionsMapper.toEntity(Mockito.any())).thenReturn(mannequinPromotionsEntity);
        Mockito.when(requestMannequinPromotionsRepository.save(mannequinPromotionsEntity)).thenReturn(mannequinPromotionsEntity);
        Mockito.doNothing().when(implementationStoresForRequestMannequinPromotionService).setImplementationStoresForMannequin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(requestMannequinPromotionDetailsService).setRequestMannequinPromotionDetailsForMannequin(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestMannequinPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(mannequinPromotionsRequest);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);
        MultipartFile[] files = null;
        MannequinPromotionsRequest actual = requestMannequinPromotionsService.create(mannequinPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), mannequinPromotionsRequest.getCd());
    }

    @Test
    public void updateCreateTest() {
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        mannequinPromotionsRequest.setCd(1L);
        mannequinPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.updateCreate(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);

        RequestMannequinPromotionsEntity mannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        mannequinPromotionsEntity.setRequestsEntity(requestsEntity);
        mannequinPromotionsEntity.setCd(1L);
        Mockito.when(requestMannequinPromotionsRepository.getOne(Mockito.anyLong())).thenReturn(mannequinPromotionsEntity);
        Mockito.when(requestMannequinPromotionsMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(mannequinPromotionsEntity);
        Mockito.when(requestMannequinPromotionsRepository.save(mannequinPromotionsEntity)).thenReturn(mannequinPromotionsEntity);
        Mockito.doNothing().when(implementationStoresForRequestMannequinPromotionService).setImplementationStoresForMannequin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(requestMannequinPromotionDetailsService).setRequestMannequinPromotionDetailsForMannequin(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestMannequinPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(mannequinPromotionsRequest);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);
        MultipartFile[] files = null;
        MannequinPromotionsRequest actual = requestMannequinPromotionsService.updateCreate(mannequinPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), mannequinPromotionsRequest.getCd());
    }

    @Test
    public void updateTest() {
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        mannequinPromotionsRequest.setCd(1L);
        mannequinPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Mockito.when(requestService.update(Mockito.any(), Mockito.any())).thenReturn(requestsEntity);

        RequestMannequinPromotionsEntity mannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        mannequinPromotionsEntity.setRequestsEntity(requestsEntity);
        mannequinPromotionsEntity.setCd(1L);
        Mockito.when(requestMannequinPromotionsRepository.getOne(Mockito.anyLong())).thenReturn(mannequinPromotionsEntity);
        Mockito.when(requestMannequinPromotionsMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(mannequinPromotionsEntity);
        Mockito.when(requestMannequinPromotionsRepository.save(mannequinPromotionsEntity)).thenReturn(mannequinPromotionsEntity);
        Mockito.doNothing().when(implementationStoresForRequestMannequinPromotionService).setImplementationStoresForMannequin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(requestMannequinPromotionDetailsService).setRequestMannequinPromotionDetailsForMannequin(Mockito.any(), Mockito.any());

        Set<String> emails = new HashSet<>(Arrays.asList("AAA"));
        Mockito.when(shainAddressService.listEmail(Mockito.any())).thenReturn(emails);
        Mockito.doNothing().when(emailServices).sendEmail(Mockito.any());
        Mockito.when(requestMannequinPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(mannequinPromotionsRequest);
        MultipartFile[] files = null;
        MannequinPromotionsRequest actual = requestMannequinPromotionsService.update(mannequinPromotionsRequest, files);
        Assert.assertEquals(actual.getCd(), mannequinPromotionsRequest.getCd());
    }

    @Test
    public void getByRequestIdTest() {
        MannequinPromotionsRequest mannequinPromotionsRequest = new MannequinPromotionsRequest();
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(false);
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
        mannequinPromotionsRequest.setCd(1L);
        mannequinPromotionsRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Set<RequestMannequinPromotionsEntity> mannequinPromotionsEntities = new HashSet<>();

        RequestMannequinPromotionsEntity mannequinPromotionsEntity = new RequestMannequinPromotionsEntity();
        mannequinPromotionsEntity.setCd(1L);
        mannequinPromotionsEntities.add(mannequinPromotionsEntity);
        requestsEntity.setRequestMannequinPromotionsEntities(mannequinPromotionsEntities);
        Mockito.when(requestsRepository.findByCdAndIsDeleted(Mockito.anyLong(), Mockito.anyByte())).thenReturn(requestsEntity);

        Mockito.when(requestMannequinPromotionsMapper.toResponseModel(Mockito.any())).thenReturn(mannequinPromotionsRequest);

        MannequinPromotionsRequest actual = requestMannequinPromotionsService.getByRequestId(1L, "edit");
        Assert.assertEquals(actual.getCd(), mannequinPromotionsRequest.getCd());
    }

}
