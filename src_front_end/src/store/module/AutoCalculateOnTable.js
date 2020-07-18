import CommissionType from "@/constants/CommissionType";
import isEmpty from "lodash/isEmpty";
import round from "lodash/round";
const state = {};
const actions = {
  calculateDenpyoNet(context = null, payload) {
    let item = payload.item;
    let commissionType = payload.commissionType;
    let precision = payload.precision || 2;
    if(!item.irisu) {
      return item;
    }
    if(commissionType == CommissionType.internal) {
      let t = round((((item.mfYen|| 0)-((item.csdiscount || 0)/(item.irisu || 1)))*(100-(item.commission || 0))/ 100), precision);
      item.denpyoNet = isNaN(t) ? undefined : t ;
    } else {
      let y = round((item.mfYen|| 0)*(100-(item.commission || 0))/ 100 -((item.csdiscount || 0)/(item.irisu || 1)), precision);
      item.denpyoNet = isNaN(y) ? undefined : y
    }
    if(payload.initialField) {
      item["initialDenpyoNet"] = item.denpyoNet;
    }
    return item;
  },
  calculateFinalTakeUnitPrice(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t = round((item.mfYen || 0) - (item.accruedUnitPrice || 0),precision);
    item.finalTakeUnitPrice = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialFinalTakeUnitPrice"] = item.finalTakeUnitPrice;
    }
    return item;
  },
  calculateAccruedAmount(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t = round((item.accruedUnitPrice || 0) * (item.quantityOfSoldItems || 0),precision);
    item.accruedAmount = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialAccruedAmount"] = item.accruedAmount;
    }
    return item;
  },
  calculateSalesAmount(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t= round(((item.mfYen|| 0) - (item.accruedUnitPrice || 0)) * (item.quantityOfSoldItems || 0),precision);
    item.salesAmount = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialSalesAmount"] = item.salesAmount;
    }
    return item;
  },
  calculateFinalMarginalProfit(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t = round((((item.mfYen|| 0) - (item.accruedUnitPrice || 0) - (item.hyojyunYen || 0)) * (item.quantityOfSoldItems || 0)),precision);
    item.finalMarginalProfit = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialFinalMarginalProfit"] = item.finalMarginalProfit;
    }
    return item;
  },
  calculateFinalMarginalProfitRatio(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    if(item.salesAmount) {
      let t = round((((((item.mfYen|| 0) - 
      (item.accruedUnitPrice || 0) - 
      (item.hyojyunYen || 0)) * (item.quantityOfSoldItems || 0)) / (item.salesAmount)) * 100),precision);
      item.finalMarginalProfitRatio = isNaN(t) ? undefined : t ;
    }
    if(payload.initialField) {
      item["initialFinalMarginalProfitRatio"] = item.finalMarginalProfitRatio;
    }
    return item;
  },
  calculateStandardMarginalProfitRatio(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t = round((((item.mfYen || 0) - (item.hyojyunYen || 0)) * (item.irisu || 0) * (item.numbersOfCases || 0)),precision);
    item.standardMarginalProfit = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialStandardMarginalProfit"] = item.standardMarginalProfit;
    }
    return item;
  },
  calculateSalesOfSeihan(context = null, payload) {
    let item = payload.data;
    let precision = payload.precision || 0;
    let t = round(((item.mfYen || 0) * (item.irisu || 0) * (item.numbersOfCases || 0)),precision) ;
    item.salesOfSeihan = isNaN(t) ? undefined : t ;
    if(payload.initialField) {
      item["initialSalesOfSeihan"] = item.salesOfSeihan;
    }
    return item;
  }
};

const mutations = {};

const getters = {}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}