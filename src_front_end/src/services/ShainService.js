import http from '@/http-common';

const ShainService = {

  getInformation() {
    return http.get('/shain/information');
  },
  listShiansForApproval(stepNumber, bumonCd, requestCd) {
    return http.get(`/shain/get-shain-for-approval?stepNumber=${stepNumber}&bumonCd=${bumonCd}&requestCd=${requestCd}`)
  },
  updateShainInformation(shainResponse) {
    return http.post("/shain/update", shainResponse);
  }
}

export default ShainService;