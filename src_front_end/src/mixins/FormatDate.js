import Vue from "vue";
export default {
  filters: {
    $_mixins_formatDate(date,formatStr){
      if(!date) {
        return;
      }
      if(!formatStr){
        formatStr = 'YYYY/MM';
      }
      return Vue.moment(date).format(formatStr);
    }
  }
}