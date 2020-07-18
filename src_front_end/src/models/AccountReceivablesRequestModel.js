import RequestModel from './RequestModel';

class CommentDetailsRequests {
  constructor() {
    this.comment = undefined;
    this.isDeputy = 0;
  }
}

class DetailsForAccountsReceivablesRequest {
  constructor() {
    this.accountReceivableDetailCd = undefined;
    this.accruedAmount = undefined;
    this.accruedUnitPrice = undefined;
    this.bumonCd = undefined;
    this.bumonNm = undefined;
    this.cd = undefined;
    this.commentDetailsRequests = undefined;
    this.commission = undefined;
    this.csdiscount = undefined;
    this.denpyoNet = undefined;
    this.finalMarginalProfit = undefined;
    this.finalMarginalProfitRatio = undefined;
    this.finalTakeUnitPrice = undefined;
    this.hasComment = undefined;
    this.hinmokuCd = undefined;
    this.hinmokuRnm = undefined;
    this.hyojyunYen = undefined;
    this.initialAccruedAmount = undefined;
    this.initialAccruedUnitPrice = undefined;
    this.initialCSDiscount = undefined;
    this.initialCommission = undefined;
    this.initialDenpyoNet = undefined;
    this.initialFinalMarginalProfit = undefined;
    this.initialFinalMarginalProfitRatio = undefined;
    this.initialFinalTakeUnitPrice = undefined;
    this.initialHyojyunYen = undefined;
    this.initialIrisu = undefined;
    this.initialMfYen = undefined;
    this.initialQuantityOfSoldItems = undefined;
    this.initialSalesAmount = undefined;
    this.irisu = undefined;
    this.isChecked = undefined;
    this.isDeleted = undefined;
    this.itemNumber = undefined;
    this.mfYen = undefined;
    this.quantityOfSoldItems = undefined;
    this.salesAmount = undefined;
    this.shainCd = undefined;
    this.shainNm = undefined;
    this.sortNumber = undefined;
    this.storeGNm = undefined;
    this.storeGCd = undefined;
    this.typeOfItem = undefined;
    this.yoryo = undefined;
  }
}

class DetailsForPromotionalExpensesRequestLogisticFees {
  constructor() {
    this.accountReceivableDetailCd = undefined;
    this.accruedAmount = undefined;
    this.brandClassification = undefined;
    this.bumonCd = undefined;
    this.bumonNm = undefined;
    this.categoryName = undefined;
    this.cd = undefined;
    this.classification = undefined;
    this.commentDetailsRequests = undefined;
    this.hasComment = undefined;
    this.hinmokuCd = undefined;
    this.hinmokuRnm = undefined;
    this.initialAccruedAmount = undefined;
    this.isChecked = undefined;
    this.isDeleted = 0;
    this.itemNumber = undefined;
    this.nisugata = undefined;
    this.shainCd = undefined;
    this.shainNm = undefined;
    this.sortNumber = undefined;
    this.storeGNm = undefined;
    this.storeGCd = undefined;
    this.typeOfInput = undefined;
    this.typeOfPromotionalExpenses = undefined;
  }
}

class DetailsForPromotionalExpensesRequestTax8Percent {
  constructor() {
    this.accountReceivableDetailCd = undefined;
    this.accruedAmount = undefined;
    this.brandClassification = undefined;
    this.bumonCd = undefined;
    this.bumonNm = undefined;
    this.categoryName = undefined;
    this.cd = undefined;
    this.classification = undefined;
    this.commentDetailsRequests = undefined;
    this.hasComment = undefined;
    this.hinmokuCd = undefined;
    this.hinmokuRnm = undefined;
    this.initialAccruedAmount = undefined;
    this.isChecked = undefined;
    this.isDeleted = undefined;
    this.itemNumber = undefined;
    this.nisugata = undefined;
    this.shainCd = undefined;
    this.shainNm = undefined;
    this.sortNumber = undefined;
    this.storeGNm = undefined;
    this.storeGCd = undefined;
    this.typeOfInput = undefined;
    this.typeOfPromotionalExpenses = undefined;
  }
}

class DetailsForPromotionalExpensesRequestTax10Percent {
  constructor () {
    this.accountReceivableDetailCd = undefined;
    this.accruedAmount = undefined;
    this.brandClassification = undefined;
    this.bumonCd = undefined;
    this.bumonNm = undefined;
    this.categoryName = undefined;
    this.cd = undefined;
    this.classification = undefined;
    this.commentDetailsRequests = undefined;
    this.hasComment = undefined;
    this.hinmokuCd = undefined;
    this.hinmokuRnm = undefined;
    this.initialAccruedAmount = undefined;
    this.isChecked = undefined;
    this.isDeleted = undefined;
    this.itemNumber = undefined;
    this.nisugata = undefined;
    this.shainCd = undefined;
    this.shainNm = undefined;
    this.sortNumber = undefined;
    this.storeGNm = undefined;
    this.storeGCd = undefined;
    this.typeOfInput = undefined;
    this.typeOfPromotionalExpenses = undefined;
  }
}

class AccountReceivablesRequest {
  constructor() {
    this.accountReceivableDetailIsDeleted = [];
    this.cd = undefined;
    this.commissionType = undefined;
    this.detailsForAccountsReceivablesRequestList = [];
    this.detailsForPromotionalExpensesRequestLogisticFeesList = [];
    this.detailsForPromotionalExpensesRequestTax8PercentList = [];
    this.detailsForPromotionalExpensesRequestTax10PercentList = [];
    this.purpose = undefined;
    this.purposeOfOthers = undefined;
    this.remarks = undefined;
    this.requestModel = new RequestModel();
    this.targetOn = undefined;
  }
}
export {
  CommentDetailsRequests,
  DetailsForAccountsReceivablesRequest,
  DetailsForPromotionalExpensesRequestLogisticFees,
  DetailsForPromotionalExpensesRequestTax8Percent,
  DetailsForPromotionalExpensesRequestTax10Percent,
  AccountReceivablesRequest
}

export default AccountReceivablesRequest;