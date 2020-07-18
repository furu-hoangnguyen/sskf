import AccountReceivableCreateService from "@/services/AccountReceivableCreateService";
import VueJwtDecode from 'vue-jwt-decode';
import _ from 'lodash';
import cookie from "vue-cookie";
import getMaxItemList from "@/helper/getMaxItemList";
import router from "../../router";
import {
  DetailsForAccountsReceivablesRequest,
  DetailsForPromotionalExpensesRequestLogisticFees,
  DetailsForPromotionalExpensesRequestTax8Percent,
  DetailsForPromotionalExpensesRequestTax10Percent,
  AccountReceivablesRequest
} from "@/models/AccountReceivablesRequestModel"; 
const state = {
  maxItemNumberDetailRequestList: null,
  maxItemNumberLogisticFeesList: null,
  maxItemNumberDetailTax10PercentList: null,
  maxItemNumberDetailTax8PercentList: null,
  showNormalProcess: false,
  showErrorDialog: false,
  modeAccountReceivable: '',
  useTypesList: [],
  commissionTypeList: [],
  applicationList: [],
  departmentList: [],
  paymentPlaceList: [],
  paymentMethodList: [],
  listItem: [],
  listPromotionExpenses: [],
  listInputType: [],
  listClassification: [],
  listBrandClassification: [],
  listCategory: [],
  listChargeName: [],
  modelNotReuseAccountReceivable: ['targetOn', 'scheduledPaymentOn', 'paymentOn'],
  resetAccountReceivableCreateData: new AccountReceivablesRequest(),
  accountReceivableCreateData: new AccountReceivablesRequest(),
  itemForAccountsReceivablesRequestList: new DetailsForAccountsReceivablesRequest(),
  itemForPromotionalExpensesRequestLogisticFeesList: new DetailsForPromotionalExpensesRequestLogisticFees(),
  itemForPromotionalExpensesRequestTax10PercentList: new DetailsForPromotionalExpensesRequestTax10Percent(),
  itemForPromotionalExpensesRequestTax8PercentList: new DetailsForPromotionalExpensesRequestTax8Percent(),
  attach_file: [],
  currentItemNumber: null
};

const actions = {
  showSlideNormalProcess({commit}) {
    let promise = new Promise(resolve => {
      commit("SET_SHOW_NORMAL_PROCESS_DIALOG", true);
      setTimeout(() => {
        resolve()
      }, 2650);
    })
    return promise
  },
  showSlideErrorProcess({commit}) {
    let promise = new Promise(resolve => {
      commit("SET_SHOW_ERROR_PROCESS_DIALOG", true);
      setTimeout(() => {
        resolve()
      }, 2650);
    })
    return promise
  },
  resetAttachFile({commit}) {
    commit("RESET_ATTACH_FILE");
  },
  resetDataAccountReceivable({dispatch, commit}) {
    dispatch('AuthenticationModule/setLockScreen', null, {root: true});
    commit("RESET_DATA_ACCOUNT_RECEIVABLE");
    dispatch("resetAttachFile");
    dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
  },
  setModeAccountReceivableCreate({commit}, payload) {
    commit("SET_MODE_ACCOUNT_RECEIVABLE_CREATE", payload)
  },
  getUseTypes({commit}) {
    AccountReceivableCreateService.listUseTypes().then(res => {
      commit("SET_USE_TYPES", res.data)
    }).catch(err => {
      alert(err.message);
      console.log(err)
    })

  },
  getCommissionTypes({commit}) {
    AccountReceivableCreateService.listCommissionType().then(res => {
      if (res.data) {
        commit("SET_COMMISSION_TYPES", res.data)
      }
    }).catch(err => {
      alert(err.message);
      console.log(err)
    })
  },
  getListApplication({commit, state}, payload) {
    return AccountReceivableCreateService.listApplication(payload).then(res => {
      if (res.data) {
        commit("SET_LIST_APPLICATION", res.data)
        if (res.data.length && state.modeAccountReceivable == 'create') {
          commit("SET_DEFAULT_APPLICATION", res.data[0]['shainCd'])
        }
        return res.data
      }

    }).catch(err => {
      alert(err.message);
      console.log(err)
    })
  },
  getListDepartment({commit, state, dispatch}) {
    var decodeToken = VueJwtDecode.decode(cookie.get('accessToken') ? cookie.get('accessToken') : '');
    return AccountReceivableCreateService.listDepartment().then(res => {
      if (res.data) {
        commit("SET_LIST_DEPARTMENT", res.data);
        if (state.modeAccountReceivable == 'create' && !state.accountReceivableCreateData.requestModel.approvalFlowDetailsRequest.bumonCd) {
          res.data.forEach(elm => {
            if (elm['bumonCd'] == decodeToken['bumonCd']) {
              commit('SET_DEFAULT_DEPARTMENT', decodeToken['bumonCd'])
            }
          });
          dispatch('getListApplication', decodeToken ? decodeToken['bumonCd'] : '').then(() => {
            commit('SET_DEFAULT_APPLICATION', decodeToken ? decodeToken['sub'] : '');
          });
        }
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err)
    })
  },
  getListPaymentPlace({commit}) {
    return AccountReceivableCreateService.listPaymentPlace().then(res => {
      if (res.data) {
        commit("SET_LIST_PAYMENT_PLACE", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err)
    });
  },
  getListPaymentMethod({commit}) {
    return AccountReceivableCreateService.listPaymentMethod().then(res => {
      if (res.data) {
        commit("SET_LIST_PAYMENT_METHOD", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err)
    });
  },
  getListItem({commit}) {
    return AccountReceivableCreateService.listItem().then(res => {
      if (res.data) {
        commit("SET_LIST_ITEM", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });

  },
  getListPromotionExpenses({commit}) {
    return AccountReceivableCreateService.listPromotionExpenses().then(res => {
      if (res.data) {
        commit("SET_LIST_PROMOTION_EXPENSES", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  getListInputType({commit}) {
    return AccountReceivableCreateService.listInputType().then(res => {
      if (res.data) {
        commit("SET_LIST_INPUT_TYPE", res.data);
        return res.data;
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  getListClassifications({commit}) {
    return AccountReceivableCreateService.listClassification().then(res => {
      if (res.data) {
        commit("SET_LIST_CLASSIFICATIONS", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  getListBrandClassification({commit}) {
    return AccountReceivableCreateService.listBrandClassification().then(res => {
      if (res.data) {
        commit("SET_LIST_BRAND_CLASSIFICATIONS", res.data);
        return res.data;
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  getListCategory({commit}, payload) {
    return AccountReceivableCreateService.listCategory(payload).then(res => {
      if (res.data) {
        commit("SET_LIST_CATEGORY", res.data);
        return res.data;
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  getListChargeName({commit}, payload) {
    return AccountReceivableCreateService.listChargeName(payload).then(res => {
      if (res.data) {
        commit("SET_LIST_CHARGE_NAME", res.data);
        return res.data
      }
    }).catch(err => {
      alert(err.message);
      console.log(err);
    });
  },
  createAccountReceivables({state, commit, dispatch}, payload) {
    state.accountReceivableCreateData.requestModel.isTemp = payload;
    let user = JSON.parse(cookie.get('user'));
    state.accountReceivableCreateData.requestModel.shainCd = user.sub;
    state.accountReceivableCreateData.requestModel.bumonNm = user.bumonNm;
    commit("RESET_SORT_FIELD");
    //control update and save draft
    if (state.accountReceivableCreateData.requestModel.cd) {
      dispatch("updateAccountReceivableDraft");
    } else {
      dispatch("createAccountReceivableDraft");
    }
  },
  createAccountReceivableDraft({state, commit, dispatch}) {
    if (state.modeAccountReceivable !== 'edit') {
      commit("SET_INITIAL_FOR_All_TABLE");
    }
    dispatch('AuthenticationModule/setLockScreen', null, {root: true});
    AccountReceivableCreateService.accountReceivables(state.attach_file, state.accountReceivableCreateData).then(res => {
      dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
      dispatch("showSlideNormalProcess").then(() => {
        if (state.modeAccountReceivable == 'create') {
          router.push(`/account-receivables/create/${res.data.requestModel.cd}/edit`);
          dispatch('setModeAccountReceivableCreate', 'edit');
          dispatch('getDetailAccountReceivable', {cd: res.data.requestModel.cd, mode: 'edit'});
        } else {
          router.push('/request/create');
        }
      });
    }).catch(err => {
      dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
      dispatch("showSlideErrorProcess")
    });
  },
  deleteAccountReceivable({state, dispatch}) {
    if (state.accountReceivableCreateData.requestModel.cd && state.modeAccountReceivable == 'edit') {
      dispatch('AuthenticationModule/setLockScreen', null, {root: true});
      AccountReceivableCreateService.deleteRequest(state.accountReceivableCreateData.requestModel.cd).then(res => {
        if (res.data) {
          dispatch("showSlideNormalProcess").then(() => {
            dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
            router.push('/request/create');
          });

        }
      }).catch(err => {
        dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
        dispatch("showSlideErrorProcess")
      })
    }
  },
  updateAccountReceivableDraft({state, dispatch}) {
    dispatch('AuthenticationModule/setLockScreen', null, {root: true});
    AccountReceivableCreateService.accountReceivablesUpdate(state.attach_file, state.accountReceivableCreateData).then(res => {
      if (res.data) {
        dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
        dispatch("showSlideNormalProcess")
      }
    }).catch(err => {
      dispatch('AuthenticationModule/setUnlockScreen', null, {root: true});
      dispatch("showSlideErrorProcess")
    });
  },
  getDetailAccountReceivable({state, commit}, payload) {
    return AccountReceivableCreateService.getDetailAccountReceivableByRequestId(payload).then(res => {
      if (res.data) {
        console.log(res)
        let list = res.data;
        if (list.requestModel.fileModelList) {
          commit("RESET_ATTACH_FILE")
          res.data.requestModel.fileModelList.forEach(item => {
            commit("SET_ATTACH_FILE", item)
          });
          delete list.requestModel.fileModelList;

        }
        commit("SET_DATA_ACCOUNT_RECEIVABLE", list);
        let maxNumRequesList = null;
        let maxNumLogisticFeesList = null;
        let maxNumTax10PercentList = null;
        let maxNumTax8PercentList = null;
        if(!_.isEmpty(res.data.detailsForAccountsReceivablesRequestList)){
          maxNumRequesList = getMaxItemList(res.data.detailsForAccountsReceivablesRequestList, 'itemNumber');
        }
        if(!_.isEmpty(res.data.detailsForPromotionalExpensesRequestLogisticFeesList)){
          maxNumLogisticFeesList = getMaxItemList(res.data.detailsForPromotionalExpensesRequestLogisticFeesList, 'itemNumber');
        }
        if(!_.isEmpty(res.data.detailsForPromotionalExpensesRequestTax10PercentList)){
          maxNumTax10PercentList = getMaxItemList(res.data.detailsForPromotionalExpensesRequestTax10PercentList, 'itemNumber');
        }
        if(!_.isEmpty(res.data.detailsForPromotionalExpensesRequestTax8PercentList)){
          maxNumTax8PercentList = getMaxItemList(res.data.detailsForPromotionalExpensesRequestTax8PercentList, 'itemNumber');
        }
        commit("SET_MAX_ITEM_NUMBER", {numberRequestlist: maxNumRequesList, numberLogisticFeesList : maxNumLogisticFeesList , numberTax10PercentList : maxNumTax10PercentList, numberTax8PercentList : maxNumTax8PercentList})
        return res.data
      }
    }).catch(err => {
      alert("You Do Not Have Access");
      router.push('/request/create');
      console.log(err);
    });
  },
  getDetailAccountReceivableReuse({state, commit, dispatch}, cd) {
    dispatch("getDetailAccountReceivable", cd).then((res) => {
      console.log(res)
      let data = state.accountReceivableCreateData;
      state.modelNotReuseAccountReceivable.forEach(item => {
        data[item] = '';
      });
      data.cd = null;
      data.requestModel.cd = null;
      data.requestModel.requestedAt = null;
      data.requestModel.settlementNumber = null;
      if (!_.isEmpty(data.detailsForAccountsReceivablesRequestList)) {
        data.detailsForAccountsReceivablesRequestList.forEach(item => {
          item.cd = null;
          item.isChecked = false;
          item.commentDetailsRequests = [];
        })
      }

      if (!_.isEmpty(data.detailsForPromotionalExpensesRequestTax8PercentList)) {
        data.detailsForPromotionalExpensesRequestTax8PercentList.forEach(item => {
          item.cd = null;
          item.isChecked = false;
          item.commentDetailsRequests = [];
        })
      }

      if (!_.isEmpty(data.detailsForPromotionalExpensesRequestTax10PercentList)) {
        data.detailsForPromotionalExpensesRequestTax10PercentList.forEach(item => {
          item.cd = null;
          item.isChecked = false;
          item.commentDetailsRequests = [];
        })
      }

      if (!_.isEmpty(data.detailsForPromotionalExpensesRequestLogisticFeesList)) {
        data.detailsForPromotionalExpensesRequestLogisticFeesList.forEach(item => {
          item.cd = null;
          item.isChecked = false;
          item.commentDetailsRequests = [];
        })
      }

      commit("SET_DATA_ACCOUNT_RECEIVABLE", data)
    })
  },
  deleteAttachFile({commit},payload) {
    commit("DELETE_ATTACH_FILE",payload)
  },
  setAttachFileForRequest({commit}, payload) {
    commit("SET_ATTACH_FILE", payload)
  },
  //table actions

  async addItemListManageItem({commit, dispatch, state}, payload) {
    let maxItemCode = 1000;
    let newItem = await dispatch('calculateSortItemNumber', {
      itemName: 'itemForAccountsReceivablesRequestList',
      listName: 'detailsForAccountsReceivablesRequestList',
      maxItemCode: maxItemCode,
      isClone: payload.isClone,
      index: payload.index
    });
    commit("ADD_ITEM_LIST_MANAGE_ITEM", {index: payload.index, newItem: newItem});
    commit("RESET_MAXITEMNUMBER",{field: 'maxItemNumberDetailRequestList', value: newItem.itemNumber});
  },
  async addPromotions8Percents({commit, dispatch, state}, payload) {
    let maxItemCode = 2000;
    let newItem = await dispatch('calculateSortItemNumber', {
      itemName: 'itemForPromotionalExpensesRequestTax8PercentList',
      listName: 'detailsForPromotionalExpensesRequestTax8PercentList',
      maxItemCode: maxItemCode,
      isClone: payload.isClone,
      index: payload.index
    });
    commit("ADD_NEW_ITEM_PROMOTIONS_8_PERCENT", {index: payload.index, newItem: newItem});
    commit("RESET_MAXITEMNUMBER",{field: 'maxItemNumberDetailTax8PercentList', value: newItem.itemNumber});
  },
  async addPromotions10Percents({commit, state, dispatch}, payload) {
    let maxItemCode = 3000;
    let newItem = await dispatch('calculateSortItemNumber', {
      itemName: 'itemForPromotionalExpensesRequestTax10PercentList',
      listName: 'detailsForPromotionalExpensesRequestTax10PercentList',
      maxItemCode: maxItemCode,
      isClone: payload.isClone,
      index: payload.index
    });
    commit("ADD_NEW_ITEM_PROMOTIONS_10_PERCENT", {index: payload.index, newItem: newItem});
    commit("RESET_MAXITEMNUMBER",{field: 'maxItemNumberDetailTax10PercentList', value: newItem.itemNumber});
  },
  async addPromotionsCostLogistics({commit, dispatch}, payload) {
    let maxItemCode = 4000;
    let newItem = await dispatch('calculateSortItemNumber', {
      listName: 'detailsForPromotionalExpensesRequestLogisticFeesList',
      itemName: 'itemForPromotionalExpensesRequestLogisticFeesList',
      maxItemCode: maxItemCode,
      isClone: payload.isClone,
      index: payload.index
    });
    commit("ADD_NEW_ITEM_PROMOTIONS_COST_LOGISTICS", {index: payload.index, newItem: newItem});
    commit("RESET_MAXITEMNUMBER",{field: 'maxItemNumberLogisticFeesList', value: newItem.itemNumber});
  },
  calculateSortItemNumber({state}, payload) {
    let maxItemCode = payload.maxItemCode
    let list = state.accountReceivableCreateData[payload.listName];
    let maxItemNumber = null;
    switch (payload.listName) {
      case 'detailsForAccountsReceivablesRequestList':
        maxItemNumber = state.maxItemNumberDetailRequestList;
        break;
      case 'detailsForPromotionalExpensesRequestLogisticFeesList':
        maxItemNumber = state.maxItemNumberLogisticFeesList;
        break;
      case 'detailsForPromotionalExpensesRequestTax10PercentList':
        maxItemNumber = state.maxItemNumberDetailTax10PercentList;
        break;
      case 'itemForPromotionalExpensesRequestTax8PercentList':
        maxItemNumber = state.maxItemNumberDetailTax8PercentList;
        break;
      default:
        break;
    }
    if (!_.isEmpty(list)) {
      maxItemCode = getMaxItemList(list, 'itemNumber') ; 
    }
    let newItem = null;
    if (payload.isClone) {
      newItem = _.cloneDeep(state.accountReceivableCreateData[payload.listName][(payload.index - 1)])
    } else {
      newItem = _.cloneDeep(state[payload.itemName]);
    }
    newItem.itemNumber = (maxItemNumber > maxItemCode ? maxItemNumber : maxItemCode) + 1;
    return newItem
  },
  deleteItemAccountReceivablesDetailRequestList({commit}, payload) {
    commit("DELETE_ITEM_ACCOUNT_RECEIVABLE_DETAIL_REQUEST_LIST", payload)
  },
  setChargeNameForRequestTable({commit}, payload) {
    commit('SET_CHARGE_NAME_FOR_REQUEST_TABLE', payload)
  },
  setDepartmentNameForRequestTable({commit}, payload) {
    commit('SET_DEPARTMENT_NAME_FOR_REQUEST_TABLE', payload)
  },
  //comment
  addItemComment({commit}, payload) {
    console.log(payload)
    if(!_.isEmpty(payload.comment.comment)){
      commit("ADD_COMMENT_SAME_KIND", payload)
    }
  },
  //Product
  addItemAccountReceivablesProduct({commit, dispatch}, payload) {
    commit("ADD_PRODUCT_FIELD_ACCOUNT_RECEIVABLE_DETAIL_REQUEST_LIST", payload);
    if (payload.selectedItem.listName == 'detailsForAccountsReceivablesRequestList') {
      dispatch("setValueDenpyoNet", {index: payload.selectedItem.index, listName: payload.selectedItem.listName});
    }
  },
  //field 57
  setValueDenpyoNet({commit, state}, payload) {
    let _mfYen = state.accountReceivableCreateData[payload.listName][payload.index].mfYen;
    let _csdiscount = state.accountReceivableCreateData[payload.listName][payload.index].csdiscount;
    let _irisu = state.accountReceivableCreateData[payload.listName][payload.index].irisu;
    let _commission = state.accountReceivableCreateData[payload.listName][payload.index].commission;
    let value = 0;
    if (state.accountReceivableCreateData.commissionType == "内口銭") {
      value = _.round(((_mfYen - (_csdiscount / _irisu)) * (100 - _commission) / 100), 2)
    } else {
      if (state.accountReceivableCreateData.commissionType == "外口銭") {
        value = _.round((_mfYen * (100 - _commission) / 100 - (_csdiscount / _irisu)), 2)
      }
    }
    commit("SET_VALUE_DEMPYONET", {index: payload.index, listName: payload.listName, value: value})
  },
  //59
  final_take_unit_price({state, commit}, payload) {
    let _mfYen = state.accountReceivableCreateData[payload.listName][payload.index].mfYen;
    let _accruedUnitPrice = state.accountReceivableCreateData[payload.listName][payload.index].accruedUnitPrice;
    let itemValue = _.round((_mfYen - _accruedUnitPrice), 2);
    commit('SET_FINAL_TAKE_UNIT_PRICE', {value: itemValue, index: payload.index, listName: payload.listName})
  },
  //62
  accrued_amount({state, commit}, payload) {
    let _accruedUnitPrice = state.accountReceivableCreateData[payload.listName][payload.index].accruedUnitPrice;
    let _quantityOfSoldItems = state.accountReceivableCreateData[payload.listName][payload.index].quantityOfSoldItems
    let itemValue = _.round((_accruedUnitPrice * _quantityOfSoldItems))
    commit("ACCRUED_AMOUNT", {value: itemValue, index: payload.index, listName: payload.listName})
  },
  //63
  sales_amount({state, commit}, payload) {
    let _mfYen = state.accountReceivableCreateData[payload.listName][payload.index].mfYen;
    let _accruedUnitPrice = state.accountReceivableCreateData[payload.listName][payload.index].accruedUnitPrice;
    let _quantityOfSoldItems = state.accountReceivableCreateData[payload.listName][payload.index].quantityOfSoldItems
    let itemValue = _.round(((_mfYen - _accruedUnitPrice) * _quantityOfSoldItems));
    commit("SALES_AMOUNT", {value: itemValue, index: payload.index, listName: payload.listName})
  },
  //64
  final_marginal_profit({state, commit}, payload) {
    let _mfYen = state.accountReceivableCreateData[payload.listName][payload.index].mfYen;
    let _accruedUnitPrice = state.accountReceivableCreateData[payload.listName][payload.index].accruedUnitPrice;
    let _hyojyunYen = state.accountReceivableCreateData[payload.listName][payload.index].hyojyunYen;
    let _quantityOfSoldItems = state.accountReceivableCreateData[payload.listName][payload.index].quantityOfSoldItems
    let itemValue = _.round(((_mfYen - _accruedUnitPrice - _hyojyunYen) * _quantityOfSoldItems));
    commit("FINAL_MARGINAL_PROFIT", {value: itemValue, index: payload.index, listName: payload.listName})
  },
  //65
  final_marginal_profit_ratio({state, commit}, payload) {
    let itemValue = 0;
    let _sales_amount = state.accountReceivableCreateData[payload.listName][payload.index].salesAmount;
    let _mfYen = state.accountReceivableCreateData[payload.listName][payload.index].mfYen;
    let _quantityOfSoldItems = state.accountReceivableCreateData[payload.listName][payload.index].quantityOfSoldItems;
    let _hyojyunYen = state.accountReceivableCreateData[payload.listName][payload.index].hyojyunYen;
    let _accruedUnitPrice = state.accountReceivableCreateData[payload.listName][payload.index].accruedUnitPrice;
    if (_sales_amount) {
      itemValue = _.round(((((_mfYen - _accruedUnitPrice - _hyojyunYen) * _quantityOfSoldItems) / _sales_amount) * 100), 2)
    }
    commit("FINAL_MARGINAL_PROFIT_RATIO", {value: itemValue, index: payload.index, listName: payload.listName})
  }
};
const getters = {};
const mutations = {
  SET_SHOW_ERROR_PROCESS_DIALOG(state, payload) {
    state.showErrorDialog = payload
  },
  SET_SHOW_NORMAL_PROCESS_DIALOG(state, payload) {
    state.showNormalProcess = payload;
  },
  SET_DATA_ACCOUNT_RECEIVABLE(state, payload) {
    state.accountReceivableCreateData = payload
  },
  RESET_DATA_ACCOUNT_RECEIVABLE(state) {
    state.accountReceivableCreateData = _.cloneDeep(state.resetAccountReceivableCreateData);
  },
  SET_MODE_ACCOUNT_RECEIVABLE_CREATE(state, payload) {
    state.modeAccountReceivable = payload
  },
  SET_USE_TYPES(state, payload) {
    state.accountReceivableCreateData.purpose = payload[0];
    state.useTypesList = payload;
  },
  SET_COMMISSION_TYPES(state, payload) {
    state.accountReceivableCreateData.commissionType = payload[0];
    state.commissionTypeList = payload;
  },
  SET_LIST_APPLICATION(state, payload) {
    state.applicationList = payload;
  },
  SET_LIST_DEPARTMENT(state, payload) {
    state.departmentList = payload;
  },
  SET_DEFAULT_DEPARTMENT(state, payload) {
    state.accountReceivableCreateData.requestModel.approvalFlowDetailsRequest.bumonCd = payload;
  },
  SET_DEFAULT_APPLICATION(state, payload) {
    state.accountReceivableCreateData.requestModel.approvalFlowDetailsRequest.shainCd = payload;
  },
  SET_LIST_PAYMENT_PLACE(state, payload) {
    state.paymentPlaceList = payload;
    state.accountReceivableCreateData.requestModel.paymentPlace = payload[0].bumonNm
  },
  SET_LIST_PAYMENT_METHOD(state, payload) {
    state.paymentMethodList = payload;
    state.accountReceivableCreateData.requestModel.paymentMethod = payload[0]
  },
  SET_ATTACH_FILE(state, payload) {
    console.log(payload)
    state.attach_file.push(payload);
  },
  RESET_ATTACH_FILE(state) {
    state.attach_file = [];
  },
  SET_LIST_ITEM(state, payload) {
    state.listItem = payload;
  },
  SET_LIST_PROMOTION_EXPENSES(state, payload) {
    state.listPromotionExpenses = payload;
  },
  SET_LIST_INPUT_TYPE(state, payload) {
    state.listInputType = payload;
  },
  SET_LIST_CLASSIFICATIONS(state, payload) {
    state.listClassification = payload
  },
  SET_LIST_BRAND_CLASSIFICATIONS(state, payload) {
    state.listBrandClassification = payload;
  },
  SET_LIST_CATEGORY(state, payload) {
    state.listCategory = payload;
  },
  SET_MAX_ITEM_NUMBER(state, payload){
    state.maxItemNumberDetailRequestList = payload.numberRequestlist;
    state.maxItemNumberLogisticFeesList = payload.numberLogisticFeesList;
    state.maxItemNumberDetailTax10PercentList = payload.numberTax10PercentList;
    state.maxItemNumberDetailTax8PercentList = payload.numberTax8PercentList;
  },
  RESET_MAXITEMNUMBER(state,payload){
    state[payload.field] = payload.value
  },
  SET_INITIAL_FOR_All_TABLE(state) {
    state.accountReceivableCreateData.requestModel.initialItemTotalForEightPercent = state.accountReceivableCreateData.requestModel.itemTotalForEightPercent;
    state.accountReceivableCreateData.requestModel.initialItemTotalForTenPercent = state.accountReceivableCreateData.requestModel.itemTotalForTenPercent;
    state.accountReceivableCreateData.requestModel.initialConsumptionTaxTotalForEightPercent = state.accountReceivableCreateData.requestModel.consumptionTaxTotalForEightPercent;
    state.accountReceivableCreateData.requestModel.initialConsumptionTaxTotalForTenPercent = state.accountReceivableCreateData.requestModel.consumptionTaxTotalForTenPercent;
    state.accountReceivableCreateData.requestModel.initialItemTotal = state.accountReceivableCreateData.requestModel.itemTotal;
    state.accountReceivableCreateData.requestModel.initialConsumptionTaxTotal = state.accountReceivableCreateData.requestModel.consumptionTaxTotal;
    state.accountReceivableCreateData.requestModel.initialTotalForEightPercent = state.accountReceivableCreateData.requestModel.totalForEightPercent;
    state.accountReceivableCreateData.requestModel.initialTotalForTenPercent = state.accountReceivableCreateData.requestModel.totalForTenPercent;
    state.accountReceivableCreateData.requestModel.initialTotal = state.accountReceivableCreateData.requestModel.total;
    
    if (!_.isEmpty(state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList)) {
      state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList.forEach((item, index) => {
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialAccruedUnitPrice = item.accruedUnitPrice;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialCSDiscount = item.csdiscount;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialCommission = item.commission;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialHyojyunYen = item.hyojyunYen;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialIrisu = item.irisu;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialAccruedAmount = item.initialAccruedAmount;
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[index].initialQuantityOfSoldItems = item.quantityOfSoldItems;
      });
    }
    if (!_.isEmpty(state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList)) {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList.forEach((item, index) => {
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList[index].initialAccruedAmount = item.initialAccruedAmount;
    })}
    if (!_.isEmpty(state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestLogisticFees)) {
      state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestLogisticFees.forEach((item, index) => {
        state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestLogisticFees[index].initialAccruedAmount = item.initialAccruedAmount;
    })}
    if (!_.isEmpty(state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestTax10Percent)) {
      state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestTax10Percent.forEach((item, index) => {
        state.accountReceivableCreateData.DetailsForPromotionalExpensesRequestTax10Percent[index].initialAccruedAmount = item.initialAccruedAmount;
    })}
  },
  DELETE_ATTACH_FILE(state, payload) {
    if (!state.accountReceivableCreateData.requestModel.filesIsDeleted && state.attach_file[payload].requestAttachmentCd) {
      state.accountReceivableCreateData.requestModel['filesIsDeleted'] = []
    }
    if (state.attach_file[payload].requestAttachmentCd) {
      state.accountReceivableCreateData.requestModel.filesIsDeleted.push(state.attach_file[payload].requestAttachmentCd);
    }
    state.attach_file.splice(payload, 1)
  },
  SET_LIST_CHARGE_NAME(state, payload) {
    state.listChargeName = payload;
  },
  SET_SUPPLIER_NAME_FOR_TABLE(state, payload) {
    state.accountReceivableCreateData.requestModel.torihikiNm = payload.torihikiNm;
    state.accountReceivableCreateData.requestModel.mstTorihikiCd = payload.torihikiCd;
  },
  SET_STORREGNM(state,payload){
    state.accountReceivableCreateData[payload.listName][payload.index].storeGCd = payload.value.storeGCd
  },
  //table account

  //set charge name for table
  SET_CHARGE_NAME_FOR_REQUEST_TABLE(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].shainCd = payload.shainCd;
    state.accountReceivableCreateData[payload.listName][payload.index].shainNm = payload.shainNm
  },
  //set department name for table
  SET_DEPARTMENT_NAME_FOR_REQUEST_TABLE(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].bumonNm = payload.bumonNm;
    state.accountReceivableCreateData[payload.listName][payload.index].bumonCd = payload.bumonCd
  },
  RESET_SORT_FIELD(state) {
    if (state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList) {
      for (let i = 0; i < state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList.length; i++) {
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[i].sortNumber = i
      }
    }
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList) {
      for (let i = 0; i < state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList.length; i++) {
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList[i].sortNumber = i
      }
    }
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList) {
      for (let i = 0; i < state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList.length; i++) {
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList[i].sortNumber = i
      }
    }
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList) {
      for (let i = 0; i < state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList.length; i++) {
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList[i].sortNumber = i
      }
    }
  },
  // ---------------------
  ADD_ITEM_LIST_MANAGE_ITEM(state, payload) {
    if (state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList) {
      state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList.splice((payload.index), 0, payload.newItem);
    } else {
      state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList = [payload.newItem]
    }
  },
  ADD_NEW_ITEM_PROMOTIONS_8_PERCENT(state, payload) {
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList) {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList.splice((payload.index), 0, payload.newItem);
    } else {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList = [payload.newItem]
    }
  },
  ADD_NEW_ITEM_PROMOTIONS_10_PERCENT(state, payload) {
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList) {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList.splice((payload.index), 0, payload.newItem);
    } else {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList = [payload.newItem]
    }
  },
  ADD_NEW_ITEM_PROMOTIONS_COST_LOGISTICS(state, payload) {
    if (state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList) {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList.splice((payload.index), 0, payload.newItem);
    } else {
      state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList = [payload.newItem]
    }
  },
  DELETE_ITEM_ACCOUNT_RECEIVABLE_DETAIL_REQUEST_LIST(state, payload) {
    if (state.modeAccountReceivable == 'edit' && _.isEmpty(state.accountReceivableCreateData['accountReceivableDetailIsDeleted'])) {
      state.accountReceivableCreateData['accountReceivableDetailIsDeleted'] = []
    }
    switch (payload.listName) {
      case 'detailsForAccountsReceivablesRequestList':
        if (state.modeAccountReceivable == 'edit' && state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[(payload.index - 1)].cd) {
          state.accountReceivableCreateData.accountReceivableDetailIsDeleted.push(state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList[(payload.index - 1)].accountReceivableDetailCd)
        }
        state.accountReceivableCreateData.detailsForAccountsReceivablesRequestList.splice((payload.index - 1), 1);
        break;
      case 'detailsForPromotionalExpensesRequestTax8PercentList':
        if (state.modeAccountReceivable == 'edit' && state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList[(payload.index - 1)].cd) {
          state.accountReceivableCreateData.accountReceivableDetailIsDeleted.push(state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList[(payload.index - 1)].accountReceivableDetailCd)
        }
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax8PercentList.splice((payload.index - 1), 1);
        break;
      case 'detailsForPromotionalExpensesRequestTax10PercentList':
        if (state.modeAccountReceivable == 'edit' && state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList[(payload.index - 1)].cd) {
          state.accountReceivableCreateData.accountReceivableDetailIsDeleted.push(state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList[(payload.index - 1)].accountReceivableDetailCd)
        }
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestTax10PercentList.splice((payload.index - 1), 1);
        break;
      case 'detailsForPromotionalExpensesRequestLogisticFeesList':
        if (state.modeAccountReceivable == 'edit' && state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList[(payload.index - 1)].cd) {
          state.accountReceivableCreateData.accountReceivableDetailIsDeleted.push(state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList[(payload.index - 1)].accountReceivableDetailCd)
        }
        state.accountReceivableCreateData.detailsForPromotionalExpensesRequestLogisticFeesList.splice((payload.index - 1), 1);
        break;
    }
  },
  ADD_COMMENT_SAME_KIND(state, payload) {
    let storeName = state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].storeGNm;
    let chargeName = state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].shainNm;
    if(!state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests ){
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests = [{
        comment: '',
        isDeputy: 0
      }];
    }
    if (payload.comment.isSameStoreName || payload.comment.isSameShain || payload.comment.isSameShainExclamation) {
      state.accountReceivableCreateData[payload.selectedItem.listName].forEach((item, index) => {
        if (item.storeGNm == storeName && payload.comment.isSameStoreName && state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests) {
          state.accountReceivableCreateData[payload.selectedItem.listName][index].commentDetailsRequests[0].comment = payload.comment.comment
        } else {
          if (item.shainNm == chargeName && payload.comment.isSameShain && state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests) {
            state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests[0].comment = payload.comment.comment;
          } else {
            state.accountReceivableCreateData[payload.selectedItem.listName][index].commentDetailsRequests = [{
              comment: payload.comment.comment,
              isDeputy: 0
            }];
          }
        }

      })
    } else {
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].commentDetailsRequests[0].comment = payload.comment.comment
    }
  },
  //product
  ADD_PRODUCT_FIELD_ACCOUNT_RECEIVABLE_DETAIL_REQUEST_LIST(state, payload) {
    //field common 4 table
    state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].hinmokuCd = payload.product.hinmokuCd;
    //51
    state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].hinmokuRnm = payload.product.hinmokuRnm;

    //control field 2 type table (have 2 type table)
    if (payload.selectedItem.listName == 'detailsForAccountsReceivablesRequestList') {
      //52
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].yoryo = payload.product.kikaku ? payload.product.kikaku : "" + ' ' + payload.product.yoryoTani ? payload.product.yoryoTani : "";
      //53
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].irisu = payload.product.irisu;
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].initialIrisu = payload.product.irisu;
      //54
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].mfYen = payload.product.mfYen;
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].initialMfYen = payload.product.mfYen ? payload.product.mfYen : state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].mfYen ;
    } else {
      //77
      state.accountReceivableCreateData[payload.selectedItem.listName][payload.selectedItem.index].nisugata = payload.product.nisugata;
    }
    //50

  },
  //Field need to calculate
  RESET_STATEMENT(state,payload){
    state.accountReceivableCreateData[payload.listName][payload.index].hinmokuCd = "";
    state.accountReceivableCreateData[payload.listName][payload.index].hinmokuRnm = "";
    state.accountReceivableCreateData[payload.listName][payload.index].nisugata = "";
    state.accountReceivableCreateData[payload.listName][payload.index].accruedAmount = 1;
  },
  RESET_STATEMENT1(state,payload){
    state.accountReceivableCreateData[payload.listName][payload.index].classification = "";
    state.accountReceivableCreateData[payload.listName][payload.index].brandClassification = "";
    state.accountReceivableCreateData[payload.listName][payload.index].categoryName = "";
  },
  SET_VALUE_DEMPYONET(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].denpyoNet = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialDenpyoNet = payload.value;
    }
  },
  SET_FINAL_TAKE_UNIT_PRICE(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].finalTakeUnitPrice = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialFinalTakeUnitPrice = payload.value;
    }
  },
  ACCRUED_AMOUNT(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].accruedAmount = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialAccruedAmount = payload.value;
    }
  },
  SALES_AMOUNT(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].salesAmount = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialSalesAmount = payload.value;
    }
  },
  FINAL_MARGINAL_PROFIT(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].finalMarginalProfit = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialFinalMarginalProfit = payload.value;
    }
  },
  FINAL_MARGINAL_PROFIT_RATIO(state, payload) {
    state.accountReceivableCreateData[payload.listName][payload.index].finalMarginalProfitRatio = payload.value;
    if(state.modeAccountReceivable !== 'edit'){
      state.accountReceivableCreateData[payload.listName][payload.index].initialFinalMarginalProfitRatio = payload.value;
    }
  },

};
export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}