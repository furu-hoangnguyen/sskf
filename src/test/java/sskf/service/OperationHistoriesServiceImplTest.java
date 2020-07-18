package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.exception.SSKFException;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.repository.MstRequestCommentActionsRepository;
import sskf.repository.OperationHistoriesRepository;
import sskf.repository.ShainRepository;
import sskf.service.impl.OperationHistoriesServiceImpl;

@ExtendWith(SpringExtension.class)
public class OperationHistoriesServiceImplTest {

    @InjectMocks
    private OperationHistoriesServiceImpl operationHistoriesService;

    @Mock
    private MstRequestCommentActionsRepository mstRequestCommentActionsRepository;

    @Mock
    private OperationHistoriesRepository operationHistoriesRepository;

    @Mock
    private ShainRepository shainRepository;

    @Mock
    private ShainService shainService;

    @Test
    public void setOperationHistoriesForRequestsTest() {

        Mockito.when(shainService.getUserNameByToken()).thenReturn("Admin");
        MstRequestCommentActionsEntity mstRequestCommentActionsEntity = new MstRequestCommentActionsEntity();
        mstRequestCommentActionsEntity.setCd(1L);
        Mockito.when(mstRequestCommentActionsRepository.findByName(Mockito.anyString())).thenReturn(mstRequestCommentActionsEntity);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setCd(1L);
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("Admin");
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonNm("bumonName");
        mstBumonEntity.setBumonCd("bumonCd");
        shainEntity.setMstBumonEntity(mstBumonEntity);
        operationHistoriesEntity.setShainEntity(shainEntity);
        Mockito.when(shainRepository.findByShainCd(Mockito.anyString())).thenReturn(shainEntity);
        Mockito.when(operationHistoriesRepository.save(Mockito.any())).thenReturn(operationHistoriesEntity);

        RequestsEntity requestsEntity = new RequestsEntity();
        String commentAction = "A", content = "A";
        OperationHistoriesEntity actual = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, commentAction, content, false, null);
        Assert.assertEquals(actual.getCd(), operationHistoriesEntity.getCd());
    }

    @Test
    public void setOperationHistoriesForRequestsWithmstRequestCommentActionsEntityNullTest() {

        Mockito.when(shainService.getUserNameByToken()).thenReturn("Admin");
        MstRequestCommentActionsEntity mstRequestCommentActionsEntity = new MstRequestCommentActionsEntity();
        mstRequestCommentActionsEntity.setCd(1L);
        Mockito.when(mstRequestCommentActionsRepository.findByName(Mockito.anyString())).thenReturn(null);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setCd(1L);
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("Admin");
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonNm("bumonName");
        mstBumonEntity.setBumonCd("bumonCd");
        shainEntity.setMstBumonEntity(mstBumonEntity);
        operationHistoriesEntity.setShainEntity(shainEntity);
        Mockito.when(shainRepository.findByShainCd(Mockito.anyString())).thenReturn(shainEntity);
        Mockito.when(operationHistoriesRepository.save(Mockito.any())).thenReturn(operationHistoriesEntity);

        RequestsEntity requestsEntity = new RequestsEntity();
        String commentAction = "A", content = "A";
        SSKFException exception = Assert.expectThrows(SSKFException.class, () -> {
            operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, commentAction, content, false, null);
        });
        Assert.assertEquals(exception.getMessage(), "Not found comment action!");
    }

    @Test
    public void setOperationHistoriesForRequestsNoContentTest() {

        Mockito.when(shainService.getUserNameByToken()).thenReturn("Admin");
        MstRequestCommentActionsEntity mstRequestCommentActionsEntity = new MstRequestCommentActionsEntity();
        mstRequestCommentActionsEntity.setCd(1L);
        Mockito.when(mstRequestCommentActionsRepository.findByName(Mockito.anyString())).thenReturn(mstRequestCommentActionsEntity);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setCd(1L);
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("Admin");
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonNm("bumonName");
        mstBumonEntity.setBumonCd("bumonCd");
        shainEntity.setMstBumonEntity(mstBumonEntity);
        operationHistoriesEntity.setShainEntity(shainEntity);
        Mockito.when(shainRepository.findByShainCd(Mockito.anyString())).thenReturn(shainEntity);
        Mockito.when(operationHistoriesRepository.save(Mockito.any())).thenReturn(operationHistoriesEntity);

        RequestsEntity requestsEntity = new RequestsEntity();
        String commentAction = "A", content = null;
        OperationHistoriesEntity actual = operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, commentAction, content, false, null);
        Assert.assertEquals(actual.getCd(), operationHistoriesEntity.getCd());
    }
}
