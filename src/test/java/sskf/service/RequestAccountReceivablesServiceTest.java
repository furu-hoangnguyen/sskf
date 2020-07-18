package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.RequestAccountsReceivablesMapper;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.RequestModel;
import sskf.repository.ApprovalFlowDetailsRepository;
import sskf.repository.RequestAccountReceivablesRepository;
import sskf.repository.RequestsRepository;
import sskf.service.impl.RequestAccountReceivablesServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class RequestAccountReceivablesServiceTest {

    @InjectMocks
    private RequestAccountReceivablesServiceImpl requestAccountReceivablesService;

    @Mock
    private RequestAccountReceivablesRepository requestAccountReceivablesRepository;

    @Mock
    private RequestAccountsReceivablesMapper requestAccountsReceivablesMapper;

    @Mock
    private RequestService requestService;

    @Mock
    private RequestAccountsReceivableDetailsService requestAccountsReceivableDetailsService;

    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;

    @Mock
    private ShainService shainService;

    @Mock
    private OperationHistoriesService operationHistoriesService;

    @Test
    public void createTest() {
        AccountReceivablesRequest receivablesRequest = new AccountReceivablesRequest();
        receivablesRequest.setRemarks("AAA");
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(true);
        receivablesRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();
        Mockito.when(requestService.create(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(requestAccountsReceivablesMapper.toEntity(receivablesRequest)).thenReturn(requestAccountsReceivablesEntity);
        Mockito.when(requestAccountReceivablesRepository.save(requestAccountsReceivablesEntity)).thenReturn(requestAccountsReceivablesEntity);
        Mockito.doNothing().when(requestAccountsReceivableDetailsService).create(requestAccountsReceivablesEntity, receivablesRequest, "作成中");
        Mockito.when(requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity)).thenReturn(receivablesRequest);
        AccountReceivablesRequest result = requestAccountReceivablesService.create(receivablesRequest, null);
        Assert.assertEquals(receivablesRequest.getRemarks(), result.getRemarks());
    }

    @Test
    public void updateTest() {
        AccountReceivablesRequest receivablesRequest = new AccountReceivablesRequest();
        receivablesRequest.setRemarks("AAA");
        receivablesRequest.setCd(1L);
        RequestModel requestModel = new RequestModel();
        requestModel.setIsTemp(true);
        receivablesRequest.setRequestModel(requestModel);
        RequestsEntity requestsEntity = new RequestsEntity();
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();
        requestAccountsReceivablesEntity.setCd(1L);
        Mockito.when(requestService.updateCreate(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(requestsEntity);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(requestAccountReceivablesRepository.getOne(receivablesRequest.getCd())).thenReturn(requestAccountsReceivablesEntity);
        Mockito.when(requestAccountsReceivablesMapper.toEntityUpdated(requestAccountsReceivablesEntity, receivablesRequest)).thenReturn(requestAccountsReceivablesEntity);
        Mockito.when(requestAccountReceivablesRepository.save(requestAccountsReceivablesEntity)).thenReturn(requestAccountsReceivablesEntity);
        Mockito.doNothing().when(requestAccountsReceivableDetailsService).create(requestAccountsReceivablesEntity, receivablesRequest, "作成中");
        Mockito.when(requestAccountsReceivablesMapper.toResponse(requestAccountsReceivablesEntity)).thenReturn(receivablesRequest);
        AccountReceivablesRequest result = requestAccountReceivablesService.updateCreate(receivablesRequest, null);
        Assert.assertEquals(receivablesRequest.getRemarks(), result.getRemarks());
    }

    @Test
    public void getByRequestIdRequestsEntityNotExistedTest() {
        RequestsEntity requestsEntity = new RequestsEntity();
        Mockito.when(requestsRepository.findByCdAndIsDeleted(1L, Constants.NOT_DELETED)).thenReturn(null);

        SSKFException exception = Assert.expectThrows(SSKFException.class, () -> {
            requestAccountReceivablesService.getByRequestId(1L, "view");
        });
        Assert.assertEquals(exception.getMessage(), "RequestsEntity not existed!");
    }

    @Test
    public void getByRequestIdRequestAccountsReceivablesNotExistedTest() {
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);

        Mockito.when(requestsRepository.findByCdAndIsDeleted(1L, Constants.NOT_DELETED)).thenReturn(requestsEntity);

        SSKFException exception = Assert.expectThrows(SSKFException.class, () -> {
            requestAccountReceivablesService.getByRequestId(1L, "view");
        });
        Assert.assertEquals(exception.getMessage(), "RequestAccountsReceivables not existed!");
    }


    @Test
    public void getByRequestIdTestSuccess() {
        String userName = "NgocCD";
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        Set<RequestAccountsReceivablesEntity> requestAccountsReceivablesEntitySet = new HashSet<>();
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();
        requestAccountsReceivablesEntity.setCd(1L);
        requestAccountsReceivablesEntitySet.add(requestAccountsReceivablesEntity);
        requestsEntity.setRequestAccountsReceivablesEntities(requestAccountsReceivablesEntitySet);
        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("作成中");
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        Mockito.when(requestsRepository.findByCdAndIsDeleted(1L, Constants.NOT_DELETED)).thenReturn(requestsEntity);

        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntityList = new ArrayList<>();
        ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();
        approvalFlowDetailsEntity.setStepNumber((byte)1);
        approvalFlowDetailsEntityList.add(approvalFlowDetailsEntity);
        Mockito.when(shainService.getUserNameByToken()).thenReturn(userName);
        Mockito.when(approvalFlowDetailsRepository
                .findByRequestsEntityAndStepNumberAndShainEntity_ShainCd(requestsEntity, (byte) 1, "NgocCD")).thenReturn(approvalFlowDetailsEntityList);
        AccountReceivablesRequest accountReceivablesRequest = new AccountReceivablesRequest();
        accountReceivablesRequest.setCd(1L);
        Mockito.when(requestAccountsReceivablesMapper.toResponse(requestsEntity.getRequestAccountsReceivablesEntities().stream().findFirst().get())).thenReturn(accountReceivablesRequest);

        requestAccountReceivablesService.getByRequestId(1L, "view");
        Assert.assertEquals(accountReceivablesRequest.getCd(), requestsEntity.getCd());
    }
}
