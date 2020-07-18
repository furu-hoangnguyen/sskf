import http from "../http-common";
import TokenService from "@/services/TokenService";
import some from "lodash/some";
import isArray from "lodash/isArray";

var AccountReceivableCreateService = {
  listUseTypes(){
      return http.get("dropdown/use-type");
  },
  listCommissionType(){
      return http.get("dropdown/commission-type");
  },
  listDepartment(){
      return http.get("dropdown/department");
  },
  listDepartmentForItem(param){
    return http.get("dropdown/department-for-item?shainCd="+`${param}`);
  },

  listApplication(param){
      return http.get("dropdown/application", {
        params: {bumonCd: param},
      });
  },
  listPaymentPlace(){
      return http.get("dropdown/payment-place");
  },
  listPaymentMethod(){
      return http.get("/dropdown/payment-method");
  },
  listItem(){
    return http.get("dropdown/items");
  },
  listPromotionExpenses(){
    return http.get("dropdown/promotion-expenses");
  },
  listStoreGroupName(param){
    return http.get("dropdown/store-group?storeGnmForSearch="+`${param}`);
  },
  listInputType(){
    return http.get("dropdown/type-input");
  },
  listClassification(){
    return http.get("dropdown/classification");
  },
  listBrandClassification(){
    return http.get("dropdown/brand-classification");
  },
  listCategory(param){
    return http.get("dropdown/category?typeInput=" + `${param}`);
  },
  listChargeName(param){
    return http.get("dropdown/charge-name?chargeName=" + `${param}`);
  },
  getDetailAccountReceivableByRequestId(param){
    return http.get("account-receivables/" + `${param.cd}` + "?mode=" + `${param.mode}`)
    .then(response => {
      return response;
    })
    .catch(err => {
      console.log(err)
      return null;
    });
  },
  accountReceivables(files,param){
    var jsonse = JSON.stringify(param);
    var blob = new Blob([jsonse], {type: "application/json"});
    let formData = new FormData();
    formData.append('receivablesRequest', blob);
    if(files){
      files.forEach(elm=>{
        formData.append('files', elm);
      })
    }
      return http.post("/account-receivables", formData);
  },
  accountReceivablesUpdate(files,param){
    var jsonse = JSON.stringify(param);
    var blob = new Blob([jsonse], {type: "application/json"});
    let formData = new FormData();
    if(files){
      files.forEach(elm=>{
        formData.append('files', elm);
      })
    }
    formData.append('receivablesRequest', blob);
    return http.put("/account-receivables", formData, {
    });
  },
  deleteRequest(id){
    return http.delete(`/request/` + `${id}`)
  },
  saveAccountReceivableConfirmRequest(files, request) {
    return http.put(`/account-receivables/confirm`, request);
  },

  updateAccountReceivableConfirmRequest(files, request) {
    var jsonse = JSON.stringify(request);
    var blob = new Blob([jsonse], {type: "application/json"});
    let formData = new FormData();
    if(files){
      files.forEach(elm=>{
        formData.append('files', elm);
      })
    }
    formData.append('receivablesRequest', blob);
    return http.put(`/account-receivables/update`, formData);
  },

  requestSalesChargeEdit(salesChargeEditRequest) {
    return http.put('/account-receivables/request-sales-charge-edit', salesChargeEditRequest);
  }
}

export default AccountReceivableCreateService;
