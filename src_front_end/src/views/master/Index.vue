<template>
  <router-view />
</template>

<script>
  import TokenService from "@/services/TokenService";
  export default {
    name: "Index",
    beforeRouteEnter (to, from, next){
      const isForAdmin =  to.matched.some(record => record.meta.onlyForAdmin);
      if(isForAdmin){
        let user =  TokenService.getUserLogin();
        if(user && user.bumonCd == "1100"){
          next()
        }else if(from.path !== '/'){
          next('/')
        }
      }
    },
    beforeRouteUpdate(to, from, next){
      const isForAdmin =  to.matched.some(record => record.meta.onlyForAdmin);
      if(isForAdmin){
        let user =  TokenService.getUserLogin();
        if(user && user?.bumonCd == "1100"){
          next()
        }else {
          next('/')
        }
      }
    }
  }
</script>

<style scoped>

</style>