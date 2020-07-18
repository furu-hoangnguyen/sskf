import router from '@/router';
import RequestService from '@/services/RequestService';
const state = {
  countRequest: {
    create: 0,
    waitingConfirm: 0,
    waitingApply: 0,
    waitingApprove: 0,
    waitingConfirmSettlement: 0
  }
};

const actions = {
  setCountRequest({ commit }, countRequest) {
    commit('SET_COUNT_REQUEST', countRequest);
  },
  countRequestList({dispatch}) {
    RequestService.countRequest()
      .then(res => {
        dispatch('setCountRequest',res.data);
      })
      .catch(err => {
        // alert('countRequest: ' + err);
      });
  }

};
const getters = {

};
const mutations = {
  SET_COUNT_REQUEST(state, countRequest) {
    state.countRequest.create = countRequest.countRequestCreate;
    state.countRequest.waitingConfirm = countRequest.countRequestWaitingConfirm;
    state.countRequest.waitingApply = countRequest.countRequestWaitingApply;
    state.countRequest.waitingApprove = countRequest.countRequestWaitingApprove;
    state.countRequest.waitingConfirmSettlement = countRequest.countRequestWaitingConfirmSettlement;
  },
};
export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}