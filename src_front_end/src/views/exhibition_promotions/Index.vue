<template>
  <router-view />
</template>

<script>
import isTablet from "@/helper/isTabletDevice";
  export default {
    name: "Index",
    beforeRouteEnter (to, from, next){
      if(isTablet() && to.path.indexOf('tablet') == -1){
        let path = to.path.replace('exhibition-promotions','exhibition-promotions/tablet');
        next(path)
      }else{
        next()
      }
    },
  }
</script>

<style scoped>

</style>