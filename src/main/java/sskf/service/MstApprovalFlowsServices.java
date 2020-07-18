package sskf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sskf.mapper.MstApprovalFlowsMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstApprovalFlowsEntity;
import sskf.model.response.MstApprovalFlowsResponse;
import sskf.repository.MstApprovalFlowsRepository;
import sskf.service.impl.BaseServiceHasSearchRSQL;

@Service
@Slf4j
public class MstApprovalFlowsServices extends BaseServiceHasSearchRSQL<MstApprovalFlowsEntity> {
    @Autowired
    private MstApprovalFlowsRepository mstApprovalFlowsRepository;

    @Autowired
    private MstApprovalFlowsMapper mstApprovalFlowsMapper;

    public Page<MstApprovalFlowsResponse> getApprovalFlows(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: getApprovalFlows");
            Page<MstApprovalFlowsEntity> entities = searchDistinctRSQL(mstApprovalFlowsRepository,
                    searchRequest, MstApprovalFlowsEntity.class, "name");
            return entities.map(mstApprovalFlowsMapper::toResponse);
        } finally {
            log.info("Log End service: getApprovalFlows");
        }
    }
}
