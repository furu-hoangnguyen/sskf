import Vue from "vue";
import Vuex from "vuex";
import mutations from './Mutations'
// import  RequestModule from './module/RequestModule'
// import AuthenticationModule from './module/AuthenticationModule';
// import CountRequestModule from './module/CountRequestModule';
// import AccountReceivableCreateModule from './module/AccountReceivableCreateModule'
import modules from "./module"
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {},
  getters: {},
  mutations: {
   ...mutations
  },
  actions: {},
  modules: modules
});

export default store;
