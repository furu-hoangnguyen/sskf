<template>
  <request-display-one-tab
    v-bind:titleRequest="titleRequest"
    v-bind:typeRequest="typeRequest"
  >
  </request-display-one-tab>
</template>

<script>
export default {
  name: "ConfirmSettlement",
  data() {
    return {
      titleRequest: "確認中",
      typeRequest: 8
    }
  }
}
</script>

<style lang='scss' scoped>
</style>