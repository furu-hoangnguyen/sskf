import Vue from 'vue'

export default {
  methods: {
    $_mixins_formatCurrentcy(number){
      return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(number);
    }
  },
}