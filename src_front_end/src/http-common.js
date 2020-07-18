import axios from "axios";
import TokenService from "@/services/TokenService";
import RefreshTokenService from "@/services/RefreshTokenService";
import router from "@/router";
import store from "@/store";
const CancelToken = axios.CancelToken;
let cancel;

function handleToLoginPage() {
  let refreshToken = TokenService.getRefreshToken();
  if(refreshToken) {
    TokenService.removeToken();
    store.dispatch('AuthenticationModule/setAccessToken');
  }
  if (router.app._route.path !== "/login") {
    router.push("/login");
  }
}

var http = axios.create({
  baseURL: 'http://192.168.1.118:8080' + "/api",
  headers: {
    "Content-type": "application/json"
  }
});

http.interceptors.request.use(function (config) {
  let token = TokenService.getToken();
  config.cancelToken =  new CancelToken(function executor(c)
  {
    cancel = c;
  });
  if(!token && !config.url.includes("/authenticate") && !config.url.includes("/refresh-token")) {
    let refreshToken = TokenService.getRefreshToken();
    if(!refreshToken) {
      handleToLoginPage();
      cancel();
    }
  } else if (token && (!config.url.includes("/authenticate") && !config.url.includes("/refresh-token"))) {
    config.headers["Authorization"] = "Bearer " + token;
    return config;
  } else {
    return config;
  }
}, function (error) {
  return Promise.reject(error);
});

http.interceptors.response.use((response) => {
  // Extend time of accessToken
  let accessToken = TokenService.getToken();
  TokenService.saveToken(accessToken);
  return response;
}, function (error) {
  window.console.error(error);
  const originalRequest = error.config;
  if(error.response && error.response.status === 401 && 
    (originalRequest.url.includes("/authenticate") || 
    originalRequest.url.includes("/refresh-token"))
  ) {
    handleToLoginPage();
    return Promise.reject(error);
  } else if(error.response && error.response.status !== 401) {
    let accessToken = TokenService.getToken();
    if(accessToken) {
      TokenService.saveToken(accessToken);
    }
  }
  if (error.response && error.response.status === 401 && !originalRequest._retry) {
    originalRequest._retry = true;
    let refreshToken = TokenService.getRefreshToken();
    if (refreshToken) {
      return RefreshTokenService.callRefreshToken(originalRequest, handleToLoginPage);
    } else {
      handleToLoginPage();
      return Promise.reject(error);
    }
  }
  // return Error object with Promise
  return Promise.reject(error);
});
export default http;
