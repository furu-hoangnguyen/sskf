import Vue from "vue";
import App from "./App.vue";
import store from "./store/index";
import { BootstrapVue, BootstrapVueIcons } from "bootstrap-vue";
import router from "./router";
import VueCookie from "vue-cookie";
import PrintPage from "@/plugin/print";
import GlobalMixin from "@/mixins/GlobalMixin";
import 'vue-select/dist/vue-select.css';
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
require("./assets/scss/main.scss");

import "./plugin";
Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons);
Vue.use(VueCookie);
const printOpts = {
  name: '_blank',
  specs: [
    'fullscreen=yes',
    'scrollbars=yes'
  ],
  replace : false,
  styles: [
    "/css/request-detail.css"
  ]
};
Vue.use(PrintPage, printOpts);
Vue.use(require('vue-moment'));
Vue.config.productionTip = false;
Vue.config.devtools = true;

new Vue({
  mixin:[GlobalMixin],
  store,
  router,
  render: (h) => h(App),
}).$mount("#app");
