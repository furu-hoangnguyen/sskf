import DropdownListService from "@/services/DropdownListService";
import convertOptionsVueSelect from '@/helper/convertOptionsVueSelect';

const state = {
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
};

const actions = {
  getUseTypes({commit}) {
    return DropdownListService.listUseTypes()
    .then(response => {
      commit("SET_USE_TYPE_LIST", (response.data || []));
      return response.data || [];
    }).catch(err => {
      console.log(err);
      return [];
    });
  },
  getListChargeName(context, payload) {
    return DropdownListService.listChargeName(payload)
    .then(response => {
      return response.data.reduce((options, item) => {
        return [...options, ...[{text: item.shainNm, value: item}]]
      }, [])
    })
    .catch(err => {
      console.log(err)
      return [];
    });
  },
  getListDepartment({commit}) {
    return DropdownListService.listDepartment()
    .then(response => {
      commit("SET_DEPARTMENT_LIST", (response.data || []));
      return response.data || [];
    }).catch(err => {
      console.log(err)
      return [];
    });
  },

  listDepartmentForItem(context, payload) {
    return DropdownListService.listDepartmentForItem(payload)
    .then(response => {
        return response.data.reduce((options, item) => {
          return [...options, ...[{text: item.bumonNm, value: item}]]
        }, [])
    })
    .catch(err => {
      console.log(err)
      return [];
    });
  },

  getListStoreGroup(context, payload) {
    return DropdownListService.listStoreGroup(payload)
    .then(response => {
        return response.data.reduce((options, item) => {
          console.log(item)
          return [...options, ...[{text: item.storeGNm, value: item}]]
        }, [])
    })
    .catch(err => {
      console.log("DropdownListService.listStoreGroup: ", err);
      return [];
    });
  },
  getListPromotionExpenses() {
    return DropdownListService.listPromotionExpenses()
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.listPromotionExpenses: ", err);
      return [];
    });
  },
  getListInputType(){
    return DropdownListService.listInputType()
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.getListInputType: ", err);
      return [];
    });
  },
  getListItem(){
    return DropdownListService.listItem()
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.listItem: ", err);
      return [];
    });
  },
  getListClassifications(){
    return DropdownListService.listClassification()
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.listClassification: ", err);
      return [];
    });
  },
  getListBrandClassification(){
    return DropdownListService.listBrandClassification()
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.listClassification: ", err);
      return [];
    });
  },
  getListCategory(context, payload) {
    return DropdownListService.listCategory(payload)
    .then(response => {
      return convertOptionsVueSelect.convertOptionsArray(response.data || []);
    })
    .catch(err => {
      console.log("DropdownListService.listCategory: ", err);
      return [];
    });
  },
  getListApprovalFlows(context, payload) {
    return DropdownListService.listApprovalFlows(payload)
    .then(response => {
      if(response.data.content) {
        return response.data.content;
      } else {
        return response.data;
      }
    })
    .catch(err => {
      console.log("DropdownListService.listApprovalFlows: ", err);
      return [];
    });
  },
  getListApplication({commit}, payload) {
    return DropdownListService.listApplication(payload)
    .then(response => {
      commit("SET_APPLICANT_LIST", response.data || []);
      return response.data || [];
    }).catch(err => {
      console.log("DropdownListService.listApplication: ", err);
      return [];
    })
  },
  getCommissionTypes({commit}) {
    return DropdownListService.listCommissionType()
    .then(response => {
      commit("SET_COMMISSION_TYPE_LIST", response.data);
      return response.data || [];
    }).catch(err => {
      console.log("DropdownListService.listCommissionType: ", err);
      return [];
    })
  },
  getListPaymentPlace({commit}) {
    return DropdownListService.listPaymentPlace()
    .then(response => {
      commit("SET_PAYMENT_PLACE_LIST", response.data);
      return response.data || [];
    }).catch(err => {
      console.log("DropdownListService.listPaymentPlace: ", err);
      return [];
    });
  },
  getListPaymentMethod({commit}) {
    return DropdownListService.listPaymentMethod()
    .then(response => {
      commit("SET_PAYMENT_METHOD_LIST", response.data);
      return response.data || [];
    }).catch(err => {
      console.log("DropdownListService.listPaymentMethod: ", err);
      return [];
    });
  },
};

const mutations = {
  SET_USE_TYPE_LIST(state, payload) {
    state.useTypesList = payload;
  },
  SET_COMMISSION_TYPE_LIST(state, payload) {
    state.commissionTypeList = payload;
  },
  SET_DEPARTMENT_LIST(state, payload) {
    state.departmentList = payload;
  },
  SET_APPLICANT_LIST(state, payload) {
    state.applicationList = payload;
  },
  SET_PAYMENT_METHOD_LIST(state, payload) {
    state.paymentMethodList = payload;
  },
  SET_PAYMENT_PLACE_LIST(state, payload) {
    state.paymentPlaceList = payload;
  },
  SET_CHARGE_NAME_LIST(state, payload) {
    state.listChargeName = payload;
  },
  SET_ITEM_LIST(state, payload) {
    state.listItem = payload;
  },
  SET_CLASSIFICATION_LIST(state, payload) {
    state.listClassification = payload;
  }

};

const getters = {}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}