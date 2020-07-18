import http from "../http-common";

const LoginService = {
    authentication(data){
        return http.post("/authenticate", data);
    },
    logout(param){
        return http.post("/log-out", param);
    }
}

export default LoginService;
