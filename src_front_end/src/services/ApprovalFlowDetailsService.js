import http from "../http-common";

const ApprovalFlowDetailsService = {
  replaceChargePersons(request) {
    return http.put(`/approval-flow-details/replace-charge-persons`, request);
  },
  insertDeputyPerson(requestCd, request) {
    return http.post(`approval-flow-details/${requestCd}`, request);
  },
  deleteChargeIsDeputy(approvalFlowCd) {
    return http.delete(`/approval-flow-details/delete-charge-is-deputy/${approvalFlowCd}`)
  }

}

export default ApprovalFlowDetailsService;
