class ApprovalFlowDetail {
  constructor() {
    this.cd = undefined;
    this.isDeputy = undefined;
    this.stepNumber = undefined;
    this.bumonCd = undefined;
    this.bumonNm = undefined;
    this.shainCd = undefined;
    this.shainNm = undefined;
  }
}

class ApprovalFlowModel {
  constructor() {
    this.applyPersons = [];
    this.firstApprovalPersons = [];
    this.secondApprovalPersons = [];
    this.thirdApprovalPersons = [];
    this.settlementApprovalPersons = [];
  }
}

export { ApprovalFlowDetail, ApprovalFlowModel}
export default ApprovalFlowModel;