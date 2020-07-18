<template>
  <request-display-one-tab
    v-bind:titleRequest="titleRequest"
    v-bind:typeRequest="typeRequest"
  >
  </request-display-one-tab>
</template>

<script>
export default {
  name: "Approve",
  data() {
    return {
      titleRequest: "承認中",
      typeRequest: 10
    }
  }
}
</script>

<style lang='scss' scoped>
</style>