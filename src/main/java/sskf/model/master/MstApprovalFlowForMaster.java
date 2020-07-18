package sskf.model.master;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MstApprovalFlowForMaster {
    List<MstApprovalFlowGroupForMaster> applyGroup;
    List<MstApprovalFlowGroupForMaster> firstApprovalGroup;
    List<MstApprovalFlowGroupForMaster> secondApprovalGroup;
    List<MstApprovalFlowGroupForMaster> thirdApprovalGroup;
    List<MstApprovalFlowGroupForMaster> settlementGroup;
    List<MstApprovalFlowGroupForMaster> firstDeputyGroup;
    List<MstApprovalFlowGroupForMaster> secondDeputyGroup;
    List<MstApprovalFlowGroupForMaster> thirdDeputyGroup;

    public MstApprovalFlowForMaster() {
        applyGroup = new ArrayList<>();
        firstApprovalGroup = new ArrayList<>();
        secondApprovalGroup = new ArrayList<>();
        thirdApprovalGroup = new ArrayList<>();
        settlementGroup = new ArrayList<>();
        firstDeputyGroup = new ArrayList<>();
        secondDeputyGroup = new ArrayList<>();
        thirdDeputyGroup = new ArrayList<>();
    }
}
