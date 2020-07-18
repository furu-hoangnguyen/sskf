import _ from 'lodash';
export default {
  methods: {
    roundNumberMixins(number, round){
      if(round){
        return _.round(number,round)
      }else {
        return _.round(number)
      }
    }
  }
}