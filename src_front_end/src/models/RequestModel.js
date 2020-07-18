import { ApprovalFlowDetail } from "./ApprovalFlowModel";
class MstApprovalFlows {
  constructor() {
    this.cd = undefined;
    this.name = undefined;
    this.bumonCd = undefined;
    this.isDeleted = 0;
  }
}

class MstRequestStatusesResponse {
    constructor() {
        this.cd = undefined;
        this.name = undefined;
    }
}
class RequestNumberResponse{
  constructor(){
    this.cd = undefined;
    this.createdAt = undefined;
  }
}
class RequestModel {
  constructor () {
    this.mstApprovalFlows = new MstApprovalFlows();
    this.approvalFlowDetailsRequest = new ApprovalFlowDetail();
    this.bankAccountName = undefined;
    this.bankAccountNumber = undefined;
    this.bankName = undefined;
    this.billingAmount = undefined;
    this.billingOn = undefined;
    this.cd = undefined;
    this.comment = undefined;
    this.consumptionTaxTotal = undefined;
    this.consumptionTaxTotalForEightPercent = undefined;
    this.consumptionTaxTotalForTenPercent = undefined;
    this.fileModelList = undefined;
    this.filesIsDeleted = undefined;
    this.isSentBack = undefined;
    this.isTemp = undefined;
    this.itemTotal = undefined;
    this.itemTotalForEightPercent = undefined;
    this.itemTotalForTenPercent = undefined;
    this.mstRequestStatusesResponse = new MstRequestStatusesResponse();
    this.mstTorihikiCd = undefined;
    this.paymentDestination = undefined;
    this.paymentMethod = undefined;
    this.paymentOn = undefined;
    this.paymentOtherMethod = undefined;
    this.paymentPlace = undefined;
    this.scheduledPaymentOn = undefined;
    this.torihikiNm = undefined;
    this.total = undefined;
    this.totalForEightPercent = undefined;
    this.totalForTenPercent = undefined;
    this.editShainCd = undefined;
    this.editShainName = undefined;
    this.startedEditAt = undefined;
    this.requestedAt = undefined;
    this.requestNumberResponse = new RequestNumberResponse();
    this.stepNumber = 1;
    this.settlementNumber = undefined;
    this.updatedAt = undefined;
    this.initialItemTotalForEightPercent = undefined,
    this.initialItemTotalForTenPercent = undefined,
    this.initialConsumptionTaxTotalForEightPercent = undefined,
    this.initialConsumptionTaxTotalForTenPercent = undefined,
    this.initialItemTotal = undefined,
    this.initialConsumptionTaxTotal = undefined,
    this.initialTotalForEightPercent = undefined,
    this.initialTotalForTenPercent = undefined,
    this.initialTotal = undefined
  }
}

export {MstApprovalFlows, MstRequestStatusesResponse, RequestModel};
export default RequestModel;