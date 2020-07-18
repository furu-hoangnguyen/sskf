package sskf.service.master;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.common.Constants;
import sskf.mapper.OperationHistoriesMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.response.OperationHistoriesOfMstCommentResponse;
import sskf.service.MstRequestCommentActionsService;
import sskf.service.OperationHistoriesService;
import sskf.service.impl.BaseServiceHasSearchRSQL;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MstRequestCommentActionsServiceImpl extends BaseServiceHasSearchRSQL<MstRequestCommentActionsEntity> implements MstRequestCommentActionsService {

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    @Autowired
    private OperationHistoriesMapper operationHistoriesMapper;

    @Override
    public List<OperationHistoriesOfMstCommentResponse> list(Long requestCd) {
        try {
            log.info("Log begin MstRequestCommentActionsServiceImpl: list");
            BaseSearchRequest baseOperation = new BaseSearchRequest();
            String searchKey = "requestsEntity.cd==" + requestCd + Constants.AND_RSQL + "mstRequestCommentActionsEntity.cd!=3" + Constants.AND_RSQL + "mstRequestCommentActionsEntity.cd!=1" ;
            baseOperation.setKeyword(searchKey);
            List<OperationHistoriesEntity> operationHistoriesEntityList = operationHistoriesService.list(baseOperation);
            return operationHistoriesEntityList.stream().map(operationHistoriesMapper :: toResponseForMstCommentActions).collect(Collectors.toList());
        } finally {
            log.info("Log end MstRequestCommentActionsServiceImpl: list");
        }
    }
}
