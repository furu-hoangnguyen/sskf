<template>
  <request-display-one-tab
    v-bind:titleRequest="titleRequest"
    v-bind:typeRequest="typeRequest"
  >
  </request-display-one-tab>
</template>

<script>
import TokenService from "@/services/TokenService";

export default {
  name: "ConfirmSettlement",
  data() {
    return {
      titleRequest: "決済確認中",
      typeRequest: 11
    }
  },
  beforeRouteEnter (to, from, next) {
    if (TokenService.isUserAccountingCharge()) {
      next();
    } else {
      next("/");
    }
  }
}
</script>

<style lang='scss' scoped>
</style>