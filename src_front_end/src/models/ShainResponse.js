export default class ShainResponse {

  constructor () {
    this.shainCd = undefined;

    this.shainCdNk = undefined;

    this.shainNm = undefined;

    this.password = undefined;

    //11
    this.isAlertedForApplication = undefined;

    //12
    this.isAlertedForModificationRequest = undefined;

    //13
    this.isAlertedForRejection = undefined;

    //14
    this.isAlertedForApproval = undefined;

    //15
    this.isAlertedForSendingRequestBack = undefined;

    //16
    this.isAlertedForConfirmingSendRequestBack = undefined;

    //17
    this.isAlertedForInputPaymentDate = undefined;

    //18
    this.isAlertedForConfirmingSettlement = undefined;

    //19
    this.isAlertedForApplicationDeputy = undefined;

    //20
    this.isAlertedForApprovalDeputy = undefined;

    //21
    this.isAlertedForChangingCharge = undefined;

    //23
    this.isAlertedForBeingCreated = undefined;

    //24
    this.isAlertedForWaitingConfirmation = undefined;

    //25
    this.isAlertedForWaitingApplication = undefined;

    //26
    this.isAlertedForWaitingApproval = undefined;

    //27
    this.isAlertedForWaitingApplicationOnSendingBack = undefined;

    //28
    this.isAlertedForWaitingApprovalOnSendingBack = undefined;

    //29
    this.isAlertedForWaitingConfirmingSettlement = undefined;

    //30
    this.isAlertedForUpdatingDatabase = undefined;

    /**
     * "mstBumonResponse": {
     *  "bumonCd": "4000",
     *   "bumonCdNk": "4000",
     *   "bumonNm": "管理本部 管理課",
     *   "bumonKnm": "ｶﾝﾘﾎﾝﾌﾞ ｶﾝﾘｶ",
     *   "bumonRnm": "管理本部"
     *   }
     */
    this.mstBumonResponse = undefined;

    /**
     * "mstRelYakushokuShainResponses": [
        {
            "mstYakushokuResponse": {
                "cd": "1",
                "isDeleted": null,
                "name": "副社長"
            }
        },
        {
            "mstYakushokuResponse": {
                "cd": "3",
                "isDeleted": null,
                "name": "部長"
            }
        }
    ]
     */
    this.mstRelYakushokuShainResponses = undefined;
  }

}