import http from "../http-common";

const MstBumonService = {
  listDepartmentByStepNumber(stepNumber, mstApprovalflowCd) {
    return http.get(`/mst-bumon?stepNumber=${stepNumber}`);
  }

}

export default MstBumonService;
