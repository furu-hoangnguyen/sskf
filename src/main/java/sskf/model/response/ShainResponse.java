package sskf.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ShainResponse {

    private String shainCd;

    private String shainCdNk;

    private String shainNm;

    private Byte isAlertedForApplication;

    private Byte isAlertedForModificationRequest;

    private Byte isAlertedForRejection;

    private Byte isAlertedForApproval;

    private Byte isAlertedForSendingRequestBack;

    private Byte isAlertedForConfirmingSendRequestBack;

    private Byte isAlertedForInputPaymentDate;

    private Byte isAlertedForConfirmingSettlement;

    private Byte isAlertedForApplicationDeputy;

    private Byte isAlertedForApprovalDeputy;

    private Byte isAlertedForChangingCharge;

    private Byte isAlertedForBeingCreated;

    private Byte isAlertedForWaitingConfirmation;

    private Byte isAlertedForWaitingApplication;

    private Byte isAlertedForWaitingApproval;

    private Byte isAlertedForWaitingApplicationOnSendingBack;

    private Byte isAlertedForWaitingApprovalOnSendingBack;

    private Byte isAlertedForWaitingConfirmingSettlement;

    private Byte isAlertedForUpdatingDatabase;

    private BumonResponse mstBumonResponse;

    private String password;

    private Set<MstRelYakushokuShainResponse> mstRelYakushokuShainResponses;

}
