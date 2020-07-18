<template>
  <router-view />
</template>

<script>
import isTablet from "@/helper/isTabletDevice";
  export default {
    name: "Index.vue",
    beforeRouteEnter (to, from, next){
      console.log(to)
      if(isTablet() && to.path.indexOf('tablet') == -1){
        let path = to.path.replace('account-receivables','account-receivables/tablet');
        next(path)
      } else{
        next();
      }
    },
  }
</script>

<style scoped>

</style>