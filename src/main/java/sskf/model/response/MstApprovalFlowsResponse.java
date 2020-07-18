package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class MstApprovalFlowsResponse {

    private String cd;

    private String name;

    private String bumonNm;

    private Byte isDeleted;

}
