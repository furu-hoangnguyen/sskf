import cookie from "vue-cookie";
import http from '@/http-common';
import router from "@/router";

const actions = {

  refreshToken() {
    // var promise = new Promise((resolve, reject) => {
    //   let _refreshToken = cookie.get('refreshToken');
    //   if (_refreshToken) {
    //     http.post('/refresh-token', {
    //       refreshToken: _refreshToken
    //     }).then(res => {
    //       cookie.set('accessToken', res.data.accessToken);
    //       cookie.set('refreshToken', res.data.refreshToken, {expires: 14});
    //       resolve();
    //     }).catch(err => {
    //       router.push('/login');
    //     });
    //   } else {
    //     reject(new Error(`Don't exist refresh token`))
    //   }
    //
    // });
    // return promise;
  },
  
};
export default actions