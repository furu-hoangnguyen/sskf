package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
@Setter
public class ApprovalFlowDetailsRequest {

    @NotEmpty(message = "bumonCd is required")
    private String bumonCd;

    private String bumonNm;

    @NotEmpty(message = "shainCd is required")
    private String shainCd;

    private String shainNm;
}
