package sskf.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sskf.model.basemodel.BaseListRequest;

@Getter
@Setter
@AllArgsConstructor
public class ShainMasterFilterRequest extends BaseListRequest {
    private String shainCd;
    private String shainNm;
    private String bumonCd;
    private String positionCd;
    private Boolean includeDeleted;

    public ShainMasterFilterRequest() {
        super();
    }
}
