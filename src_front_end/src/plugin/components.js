import Vue from "vue";
import upperFirst from "lodash/upperFirst";
import camelCase from "lodash/camelCase";
import vSelect from 'vue-select';
import "vue-select/src/scss/vue-select.scss";
import VueNumeric from '@/components/Input/NumericSSkf';
import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
const requireComponent = require.context("@/components", true, /.+\.(vue|js)$/);

requireComponent.keys().forEach((fileName) => {
  const componentConfig = requireComponent(fileName);
  const componentName = upperFirst(
    camelCase(
      fileName
        .split("/")
        .pop()
        .replace(/\.\w+$/, "")
    )
  );
  Vue.component('date-picker', DatePicker);
  Vue.component('v-numeric', VueNumeric);
  Vue.component('v-select', vSelect);
  Vue.component(componentName,componentConfig.default || componentConfig);
});