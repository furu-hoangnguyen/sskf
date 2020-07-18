import Vue from "vue";
import {
  ValidationProvider,
  ValidationObserver,
  setInteractionMode,
  extend,
} from "vee-validate";
setInteractionMode('passive');
import length from "./validators/lenght";
import validNumber from './validators/validNumber';
import * as Rules from "vee-validate/dist/rules";

Vue.component("ValidationProvider", ValidationProvider);
Vue.component("ValidationObserver", ValidationObserver);

for (const rule in Rules) {
  extend(rule, Rules[rule]);
}
extend("length", length);
extend('validNumber', validNumber);