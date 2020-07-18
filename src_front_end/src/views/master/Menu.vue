<template>
  <div class="admin">
    <h3>マスタメニュー</h3>
    <div class="menu">
      <b-button variant="none" @click="$router.push('/master/employee')">社員マスタ</b-button>
      <b-button variant="none" @click="$router.push('/master/product')">品目マスタ</b-button>
      <b-button variant="none" @click="$router.push('/master/store')">店舗マスタ</b-button>
    </div>
    <div class="menu">
      <b-button variant="none" @click="$router.push('/master/supplier')">取引先マスタ</b-button>
      <b-button variant="none" @click="$router.push('/master/approvalflow')">承認フローマスタ</b-button>
      <b-button variant="none" @click="$router.push('/master/charge')">担当マスタ</b-button>
    </div>
    <div class="menu">
      <b-button variant="none" @click="$router.push('/master/imported-situation')">マスタ取込状況</b-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Menu",
    data() {
      return {};
    }
  };
</script>

<style lang="scss" scoped>
  .admin {
    padding-top: 2.5rem;
    padding-left: 9.375rem;
    h3 {
      margin-bottom: 5.25rem;
      font-size: 1.5rem;
      color: #000000;
      line-height: 2.5rem;
    }
    .menu {
      margin-bottom: 3.75rem;
      /* margin: 10vh 0;
      &:nth-child(1) {
        margin-top: 10vh;
        background-color: aqua;
      } */
      button {
        height: 3.125rem;
        border: 1px solid grey;
        padding: 0;
        min-width: 12.5rem;
        white-space: nowrap;
        &:not(:nth-child(1)) {
          margin-left: 20vh;
        }
      }
    }
  }
</style>