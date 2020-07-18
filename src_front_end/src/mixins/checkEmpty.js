import _ from 'lodash';
export default {
  methods: {
    checkEmpty(item){
      if(_.isEmpty(item)){
        return true;
      }else {
        let isHasItemNotDeleted = false;
        item.forEach(item =>{
          if(!item.isDeleted){
            isHasItemNotDeleted = true;
          }
        });
        return !isHasItemNotDeleted
      }

    }
  },
}