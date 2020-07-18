
import TokenService from "@/services/TokenService";
import some from "lodash/some";
import forEach from "lodash/forEach";
import isArray from "lodash/isArray";
import includes from "lodash/includes";
import filter from "lodash/filter";
const userRole = (flow, request) => {
  let user = TokenService.getUserLogin();
  let roles = user.roles || [];
  if(user.bumonCd == 4000 && !includes(roles, "confirmSettlementPersons")){
    roles.push("confirmSettlementPersons");
  }
  // set role for login user based on request info
  if(request && user && user.sub && user.sub != "") {
    if(user.bumonCd == 4000) {
      roles.push("confirmSettlementPersons");
    }
    if(request.requestModel && request.requestModel.createdBy == user.sub && !roles.includes("createPersons")) {
      roles.push("createPersons");
    }
    let isConfirmUser = false;
    // detailsForAccountsReceivablesRequestList
    if(!isConfirmUser &&
      request.detailsForAccountsReceivablesRequestList &&
      isArray(request.detailsForAccountsReceivablesRequestList)
    ){
      isConfirmUser = some(request.detailsForAccountsReceivablesRequestList, o => {
        return o.shainCd == user.sub;
      });
    }
    // detailsForPromotionalExpensesRequestTax8PercentList
    if(!isConfirmUser &&
      request.detailsForPromotionalExpensesRequestTax8PercentList &&
      isArray(request.detailsForPromotionalExpensesRequestTax8PercentList)
    ){
      isConfirmUser = some(request.detailsForPromotionalExpensesRequestTax8PercentList, o => {
        return o.shainCd == user.sub;
      });
    }
    // detailsForPromotionalExpensesRequestTax10PercentList
    if(!isConfirmUser &&
      request.detailsForPromotionalExpensesRequestTax10PercentList &&
      isArray(request.detailsForPromotionalExpensesRequestTax10PercentList)
    ){
      isConfirmUser = some(request.detailsForPromotionalExpensesRequestTax10PercentList, o => {
        return o.shainCd == user.sub;
      });
    }
    // detailsForPromotionalExpensesRequestLogisticFeesList
    if(!isConfirmUser &&
      request.detailsForPromotionalExpensesRequestLogisticFeesList &&
      isArray(request.detailsForPromotionalExpensesRequestLogisticFeesList)
    ){
      isConfirmUser = some(request.detailsForPromotionalExpensesRequestLogisticFeesList, o => {
        return o.shainCd == user.sub;
      });
    }
    if(isConfirmUser && !roles.includes("confirmPersons")) {
      roles.push("confirmPersons");
    }
    // Apply Person
    if(request.requestModel &&
      !request.requestModel.requestedAt &&
      request.requestModel.approvalFlowDetailsRequest &&
      request.requestModel.approvalFlowDetailsRequest.shainCd == user.sub &&
      !roles.includes("applyPersons")
    ) {
      roles.push("applyPersons");
    }
  } else if(flow && user && user.sub && user.sub != "") {
    forEach(flow, (value, key) => {
      if(isArray(value)){
        let hasRole = some(value, o => {
          return user && o.shainCd == user.sub;
        });
        if(hasRole && !includes(roles, key)) {
          roles.push(key);
        } else if(!hasRole && includes(roles, key)) {
          // remove
          roles = filter(roles, value => {
            return value != key;
          })
        }
      }
    });
  }
  user.roles = roles;
  TokenService.saveUserLogin(user);
}

export default userRole;