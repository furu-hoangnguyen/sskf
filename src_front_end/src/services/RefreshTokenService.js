import http from "../http-common";
import VueJwtDecode from "vue-jwt-decode";
import TokenService from "@/services/TokenService";
const b64DecodeUnicode = (str) => {
  // Going backwards: from bytestream, to percent-encoding, to original string.
  return decodeURIComponent(atob(str).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));
}
const RefreshTokenService = {
  async callRefreshToken(originalRequest, failureCb) {
    let refreshToken = TokenService.getRefreshToken();
    return http.post("/refresh-token", {
      "refreshToken": refreshToken
    })
    .then(response => {
      if(response.status === 201) {
        var decodeToken = VueJwtDecode.decode(response.data.accessToken);
        decodeToken.shainNm = b64DecodeUnicode(decodeToken.shainNm);
        decodeToken.bumonNm = b64DecodeUnicode(decodeToken.bumonNm);
        TokenService.saveUserLogin(decodeToken);
        TokenService.saveToken(response.data.accessToken);
        TokenService.saveRefreshToken(response.data.refreshToken);
        // Change Authorization header
        http.defaults.headers.common["Authorization"] = "Bearer " + TokenService.getToken();
        if(originalRequest) {
          // Call originalRequest object with Axios.
          return http(originalRequest);
        } else {
          return Promise.resolve({result: true});
        }
      }
    })
    .catch(err => {
      TokenService.removeRefreshToken();
      if(typeof failureCb == "function") {
        failureCb();
      } else {
        return Promise.reject({result: false});
      }
    });
  }
}
export default RefreshTokenService;