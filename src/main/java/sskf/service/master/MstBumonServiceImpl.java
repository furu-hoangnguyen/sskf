package sskf.service.master;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.common.Constants;
import sskf.mapper.MstBumonMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstBumonEntity;
import sskf.model.response.BumonResponse;
import sskf.repository.MstBumonRepository;
import sskf.service.MstBumonService;
import sskf.service.impl.BaseServiceHasSearchRSQL;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MstBumonServiceImpl extends BaseServiceHasSearchRSQL<MstBumonEntity> implements MstBumonService {

    @Autowired
    private MstBumonRepository mstBumonRepository;

    @Autowired
    private MstBumonMapper mstBumonMapper;

    @Override
    public List<BumonResponse> listDepartmentForItem(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Log begin service MstBumonServiceImpl: listDepartmentForItem");
            List<MstBumonEntity> result = listRSQL(mstBumonRepository, baseSearchRequest, MstBumonEntity.class);
            return result.stream().map(mstBumonMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service MstBumonServiceImpl: listDepartmentForItem");
        }

    }

    @Override
    public List<BumonResponse> listDepartmentByStepNumber(Byte stepNumber) {
        try {
            log.info("Log begin service MstBumonServiceImpl: listDepartmentByStepNumber");
            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
            String searchKey = "bumonCdNk!=null";
            if (stepNumber != 1) {
                searchKey = searchKey + Constants.AND_RSQL + "mstApprovalFlowDetailsEntity.isDeputy==1" +
                Constants.AND_RSQL + "mstApprovalFlowDetailsEntity.stepNumber==" + stepNumber;
            }

            baseSearchRequest.setKeyword(searchKey);
            List<MstBumonEntity> result = listDistinctRSQL(mstBumonRepository, baseSearchRequest, MstBumonEntity.class);
            return result.stream().map(mstBumonMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service MstBumonServiceImpl: listDepartmentByStepNumber");
        }

    }
}
