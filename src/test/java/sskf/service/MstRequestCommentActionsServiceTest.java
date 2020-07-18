package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.common.Constants;
import sskf.mapper.OperationHistoriesMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.response.OperationHistoriesOfMstCommentResponse;
import sskf.service.master.MstRequestCommentActionsServiceImpl;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstRequestCommentActionsServiceTest {

    @InjectMocks
    MstRequestCommentActionsServiceImpl mstRequestCommentActionsService;

    @Mock
    private OperationHistoriesService operationHistoriesService;

    @Mock
    private OperationHistoriesMapper operationHistoriesMapper;

    @Test
    public void listTest() {
        BaseSearchRequest baseOperation = new BaseSearchRequest();
        Long requestCd = 1L;
        String searchKey = "requestsEntity.cd==" + requestCd + Constants.AND_RSQL + "mstRequestCommentActionsEntity.cd!=3" + Constants.AND_RSQL + "mstRequestCommentActionsEntity.cd!=1" ;
        baseOperation.setKeyword(searchKey);

        List<OperationHistoriesEntity> operationHistoriesEntityList = new ArrayList<>();
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setCd(2L);
        operationHistoriesEntityList.add(operationHistoriesEntity);
        Mockito.when(operationHistoriesService.list(Mockito.any())).thenReturn(operationHistoriesEntityList);

        OperationHistoriesOfMstCommentResponse operationHistoriesOfMstCommentResponse = new OperationHistoriesOfMstCommentResponse();
        operationHistoriesOfMstCommentResponse.setCd(2L);
        Mockito.when(operationHistoriesMapper.toResponseForMstCommentActions(operationHistoriesEntity)).thenReturn(operationHistoriesOfMstCommentResponse);
        List<OperationHistoriesOfMstCommentResponse> actual = mstRequestCommentActionsService.list(requestCd);
        Assert.assertEquals(actual.size(), operationHistoriesEntityList.size());

    }

}
