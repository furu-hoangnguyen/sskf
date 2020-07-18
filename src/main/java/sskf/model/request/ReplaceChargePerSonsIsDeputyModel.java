package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class ReplaceChargePerSonsIsDeputyModel {

    @NotNull(message = "approvalFlowDetailMain is required!")
    private Long approvalFlowDetailMain;

    @NotNull(message = "approvalFlowDetailIsDeputy is required!")
    private Long approvalFlowDetailIsDeputy;
}
