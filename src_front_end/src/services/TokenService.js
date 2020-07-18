import cookie from "vue-cookie";
import store from "@/store";

const TokenService = {
  getToken() {
    var v = cookie.get("accessToken");
    var session = cookie.get("accessToken_session");

    return v && session ? v : null;
  },
  getRefreshToken() {
    var v = cookie.get("refreshToken");
    return v ? v : null;
  },

  getExpireTime() {
    return parseInt(cookie.get("expireTime"));
  },

  saveToken(accessToken, expiredAt) {
    if(!expiredAt) {
      expiredAt = 0;
    }
    cookie.set("accessToken", accessToken, {expires: "4h"});
    cookie.set("accessToken_session", accessToken, null);
    store.dispatch('AuthenticationModule/setAccessToken');
  },

  saveRefreshToken(accessRefreshToken) {
    cookie.set("refreshToken", accessRefreshToken, {expires: "14D"});
  },

  saveUserLogin(user) {
    cookie.set("user",JSON.stringify(user));
    store.dispatch('AuthenticationModule/setLoginUser');
  },

  removeToken() {
    cookie.delete("accessToken");
    cookie.delete("accessToken_session");
  },

  removeRefreshToken() {
    cookie.delete("refreshToken");
  },

  getUserLogin() {
    return JSON.parse(cookie.get("user"));
  },
  
  resetRolesForRequest() {
    let user = this.getUserLogin();
    user.roles = [];
    this.saveUserLogin(user);
  },

  isUserAccountingCharge() {
    let user = this.getUserLogin();
    return user && user.bumonCd === "4000";
  }
};

export default TokenService;