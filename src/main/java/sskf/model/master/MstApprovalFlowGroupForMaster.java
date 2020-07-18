package sskf.model.master;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MstApprovalFlowGroupForMaster {
    private String bumonNm;
    private String yakushokuNm;

    public MstApprovalFlowGroupForMaster(String bumonNm, String yakushokuNm) {
        this.bumonNm = bumonNm;
        this.yakushokuNm = yakushokuNm;
    }
}
