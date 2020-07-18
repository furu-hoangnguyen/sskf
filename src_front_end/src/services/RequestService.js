import http from '@/http-common';
import cookie from 'vue-cookie';

const RequestService = {

  countRequest() {
    return http.get('/request/count');
  },

  getRequestList(wrapperRequest) {

    return http.get('/request/get-requests/', {
      params: {
        wrapperRequest: JSON.stringify(wrapperRequest)
      }
    });
  },

  getFilterSelect() {
    return http.get('/request/filter/select');
  },

  countRequestListDefault() {
    return http.get('/request/get-requests/count');
  },

  getTypeSearchBeforeInput(){
    if (cookie.get('accessToken')) {
      return http.get('/mst-request-type/list?sortField=cd%3D%3Dasc', {
        headers: {
          Authorization: `Bearer ${cookie.get('accessToken')}`
        }
      });
    } else {
      return new Promise(
          function (resolve, reject) {
            let reason = new Error(`Don't exist access token`);
            reject(reason);
          }
      );
    }
  },

  deleteRequest(requestCd) {
    return http.delete(`/request/${requestCd}`);
  },

  getApprovalFlowDetail(approvalCd, requestCd) {
    return http.get(`/request/get-approval-persons?mstApprovalFlowCd=${approvalCd}&requestCd=${requestCd}`);
  },

  getSavedApprovalPersons(requestCd) {
    return http.get(`/request/get-saved-approval-persons?requestCd=${requestCd}`);
  },

  applyRequest(params) {
    return http.put(`/request/apply`, params);
  },

  getDeputyUser(approvalCd, requestCd) {
    return http.get(`/request/get-deputy-users?mstApprovalFlowCd=${approvalCd}&requestCd=${requestCd}`);
  },

  checkRequestUpdated(requestCd, updatedAt) {
    return http.get(`/request/check-updated/${requestCd}?updatedAt=${updatedAt}`)
  },

  approvalRequest(params) {
    return http.put(`/request/approval`, params);
  },

  sendBack(params) {
    return http.put(`/request/approver-send-back`, params);
  },

  confirmSendBack(requestCd) {
    return http.put(`/request/applier-send-back/${requestCd}`);
  },

  reject(params) {
    return http.put(`/request/reject`, params);
  },
  
  settlementRequest(params) {
    return http.put(`/request/settlement`, params);
  },

  validateForApproval(requestCd, updatedAt) {
    return http.get(`/request/validate-for-approval-actions/${requestCd}?updatedAt=${updatedAt}`)
  },

  unlockRequest(requestCd) {
    // return http.put(`/request/unlock-request/${requestCd}`);
    return Promise.resolve({status: 200})
  }

}

export default RequestService;