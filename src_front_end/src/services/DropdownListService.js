import http from "../http-common";

const DropdownListService = {
  listDepartment(){
    return http.get("dropdown/department");
  },
  listDepartmentForItem(param){
    return http.get("dropdown/department-for-item?shainCd="+`${param}`);
  },
  listUseTypes(){
    return http.get("dropdown/use-type");
  },
  listCommissionType(){
    return http.get("dropdown/commission-type");
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
    return http.get("/dropdown/items");
  },
  listPromotionExpenses(){
    return http.get("/dropdown/promotion-expenses");
  },
  listStoreGroupName(param){
    return http.get("/dropdown/store-group%20?storeGnmForSearch="+`${param}`);
  },
  listInputType(){
    return http.get("/dropdown/type-input");
  },
  listClassification(){
    return http.get("/dropdown/classification");
  },
  listBrandClassification(){
    return http.get("/dropdown/brand-classification");
  },
  listCategory(param){
    return http.get("/dropdown/category?typeInput=" + `${param}`);
  },
  listChargeName(param){
    return http.get("dropdown/charge-name?chargeName=" + `${param}`);
  },
  listStoreGroup(param){
    return http.get("/dropdown/store-group?storeGnmForSearch=" + `${param}`);
  },
  listApprovalFlows(bumonCd) {
      return http.get(`/dropdown/approval-flows?bumonCd=${bumonCd}`);
  }
}

export default DropdownListService;
