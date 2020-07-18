package sskf.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CountRequestResponse {

    private int countRequestCreate;

    private int countRequestWaitingConfirm;

    private int countRequestWaitingApply;

    private int countRequestWaitingApprove;

    private int countRequestWaitingConfirmSettlement;

}
