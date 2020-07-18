import router from "../../router";
import LoginService from "@/services/LoginService";
import TokenService from "@/services/TokenService";
import VueJwtDecode from "vue-jwt-decode";
const b64DecodeUnicode = (str) => {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(atob(str).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
} 
const state = {
  data : {
    username: "",
    password: "",
    rememberMe: false,
    notifyError: ""
  },
  lockScreen:false,
  isRefreshToken: false,
  accessToken: TokenService.getToken(),
  loginUser: TokenService.getUserLogin()
};

const actions = {
  authentication({dispatch, state}) {
    dispatch("setLockScreen");
    LoginService.authentication(state.data)
        .then(response => {
          dispatch("setUnlockScreen");
          if(response) {
            var decodeToken = VueJwtDecode.decode(response.data.accessToken);
            decodeToken.shainNm = b64DecodeUnicode(decodeToken.shainNm);
            decodeToken.bumonNm = b64DecodeUnicode(decodeToken.bumonNm);
            TokenService.saveToken(response.data.accessToken, "4h");
            TokenService.saveUserLogin(decodeToken);
            if (state.data.rememberMe == true) {
              TokenService.saveRefreshToken(response.data.refreshToken);
            } else {
              TokenService.removeRefreshToken();
            }
            dispatch("setAccessToken");
            dispatch("clearInforUserLoginSuccess");
            router.push("/");
          }
        }).catch(e => {
          dispatch("clearInforUserLoginFail");
          dispatch("setUnlockScreen");
        });
  },
  logout({dispatch}){
    let data={
      accessToken: TokenService.getToken() ? TokenService.getToken() : null ,
      refreshToken: TokenService.getRefreshToken() ? TokenService.getRefreshToken() : null
    };
    LoginService.logout(data)
        .then(res => {
          TokenService.removeToken();
          TokenService.removeRefreshToken();
          dispatch("setAccessToken");
          router.push("/login");
        }).catch(e => {
          TokenService.removeToken();
          TokenService.removeRefreshToken();
          dispatch("setAccessToken");
          router.push("/login");
    })
  },
  setLockScreen({commit}){
    commit("LOCK_SCREEN");
  },
  setUnlockScreen({commit}){
    commit("UNLOCK_SCREEN");
  },
  setIsRefreshToken({ commit }) {
    commit("SET_IS_REFRESH_TOKEN");
  },
  setAccessToken({commit}){
    commit("SET_ACCESS_TOKEN",TokenService.getToken());
  },
  clearInforUserLoginSuccess({ commit }) {
    commit("CLEAR_INFOR_USER_LOGIN_SUCCESS");
  },
  clearInforUserLoginFail({ commit }) {
    commit("CLEAR_INFOR_USER_LOGIN_FAIL");
  },
  setLoginUser({commit}, payload) {
    commit("SET_LOGIN_USER", payload);
  }
};
const getters = {
  isLogged: state => {
    return state.accessToken ? true : false;
  }
};
const mutations = {
  LOCK_SCREEN(state){
    state.lockScreen = true;
  },
  UNLOCK_SCREEN(state){
    state.lockScreen = false;
  },
  SET_IS_REFRESH_TOKEN(state) {
    state.isRefreshToken = !state.isRefreshToken;
  },
  SET_ACCESS_TOKEN(state,payload){
    state.accessToken = payload;
  },
  CLEAR_INFOR_USER_LOGIN_SUCCESS( state ) {
    state.data.username = "";
    state.data.password = "";
    state.data.notifyError = "";
  },
  CLEAR_INFOR_USER_LOGIN_FAIL( state ) {
    state.data.password = "";
    state.data.notifyError = "IDまたはPasswordが正しくありません";
  },
  SET_LOGIN_USER(state, payload) {
    let user = payload || TokenService.getUserLogin();
    state.loginUser = user;
  }
};
export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}