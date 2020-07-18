<template>
  <request-display-one-tab
    v-bind:titleRequest="titleRequest"
    v-bind:typeRequest="typeRequest"
  >
  </request-display-one-tab>
</template>

<script>
export default {
  name: "Apply",
  data() {
    return {
      titleRequest: "申請中",
      typeRequest: 9
    }
  }
}
</script>

<style lang='scss' scoped>
</style>