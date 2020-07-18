import RequestModel from './RequestModel';
class ImplementationStoresForRequestMannequinPromotionRequestItem {
    constructor() {
        this.cd = 0;
        this.runEventOn = undefined;
        this.storeNameOfEvent = undefined;
        this.sortNumber = undefined;
    }
}
class RequestMannequinPromotionDetailsRequestListItem {
    constructor() {
            this.accruedAmount = undefined;
            this.cd = null;
            this.costsOfMannequin = undefined;
            this.finalMarginalProfit = undefined;
            this.hinmokuCd = undefined;
            this.hinmokuRnm = undefined;
            this.hyojyunYen = undefined;
            this.initialAccruedAmount = undefined;
            this.initialCostsOfMannequin = undefined;
            this.initialFinalMarginalProfit = undefined;
            this.initialHyojyunYen = undefined;
            this.initialIrisu = undefined;
            this.initialMfYen = undefined;
            this.initialNumbersOfCases = undefined;
            this.initialSalesOfSeihan = undefined;
            this.initialStandardMarginalProfit = undefined;
            this.irisu = undefined;
            this.mfYen = undefined;
            this.nisugata = undefined;
            this.numbersOfCases = undefined;
            this.salesOfSeihan = undefined;
            this.sortNumber = undefined;
            this.standardMarginalProfit = undefined;
    }
}
class MannequinPromotionsRequestModel {
    constructor() {
        this.cd = null;
        this.contentOfImplementationStores = undefined;
        this.implementationStoresForRequestMannequinPromotionRequestIsDeletedList = [];
        this.implementationStoresForRequestMannequinPromotionRequestList = [];
        this.requestMannequinPromotionDetailsRequestIsDeletedList = [];
        this.requestMannequinPromotionDetailsRequestList = [];
        this.requestModel = new RequestModel();
        this.storeGCd = undefined;
        this.storeGNm = "";
        this.subject = undefined;
    }
}

export {
    MannequinPromotionsRequestModel,
    ImplementationStoresForRequestMannequinPromotionRequestItem,
    RequestMannequinPromotionDetailsRequestListItem
};
export default MannequinPromotionsRequestModel;
