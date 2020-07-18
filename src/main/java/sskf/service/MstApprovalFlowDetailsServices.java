package sskf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstApprovalFlowDetailsEntity;
import sskf.model.master.MstApprovalFlowForMaster;
import sskf.model.master.MstApprovalFlowGroupForMaster;
import sskf.repository.MstApprovalFlowDetailsRepository;
import sskf.service.impl.BaseServiceHasSearchRSQL;
import java.util.List;

@Service
@Slf4j
public class MstApprovalFlowDetailsServices extends BaseServiceHasSearchRSQL<MstApprovalFlowDetailsEntity> {
    @Autowired
    private MstApprovalFlowDetailsRepository mstApprovalFlowDetailsRepository;

    public MstApprovalFlowForMaster findApprovalFlowsGroup(String mstApprovalFlowCd) {
        BaseSearchRequest request = new BaseSearchRequest();
        request.setKeyword("mstApprovalFlowsEntity.cd=='" + mstApprovalFlowCd + "'");

        List<MstApprovalFlowDetailsEntity> entities = listRSQL(mstApprovalFlowDetailsRepository,
                request, MstApprovalFlowDetailsEntity.class);;

        MstApprovalFlowForMaster result = new MstApprovalFlowForMaster();

        for (MstApprovalFlowDetailsEntity entity: entities) {
            MstApprovalFlowGroupForMaster groupItem = new MstApprovalFlowGroupForMaster(entity.getMstBumonEntity().getBumonNm(), entity.getMstYakushokuEntity().getName());
            if (entity.getStepNumber() == 1) { //apply
                if (entity.getIsDeputy() == 0) {
                    result.getApplyGroup().add(groupItem);
                }
            } else if (entity.getStepNumber() == 2) { //1st approval
                if (entity.getIsDeputy() == 0) {
                    result.getFirstApprovalGroup().add(groupItem);
                } else {
                    result.getFirstDeputyGroup().add(groupItem);
                }
            } else if (entity.getStepNumber() == 3) { //2st approval
                if (entity.getIsDeputy() == 0) {
                    result.getSecondApprovalGroup().add(groupItem);
                } else {
                    result.getSecondDeputyGroup().add(groupItem);
                }
            } else if (entity.getStepNumber() == 4) { //3st approval
                if (entity.getIsDeputy() == 0) {
                    result.getThirdApprovalGroup().add(groupItem);
                } else {
                    result.getThirdDeputyGroup().add(groupItem);
                }
            } else if (entity.getStepNumber() == 5) { //settlement
                if (entity.getIsDeputy() == 0) {
                    result.getSettlementGroup().add(groupItem);
                }
            }
        }

        return result;
    }


}
