<template>
  <b-modal v-model="showpopup" @hide="hide($event)" modal-class="adminSearch" centered hide-header hide-footer>
    <b-container fluid class="bv-example-row searchproductPopup searchAdminPopup">
      <slot/>
    </b-container>
  </b-modal>
</template>
<script>
  export default {
    name: "AdminSearchDialog",
    data() {
      return {
        showpopup: this.value
      };
    },
    props: {
      value: {
        type: Boolean
      }
    },
    methods: {
      hide(event) {
      }
    },
    watch: {
      value() {
        this.showpopup = this.value;
      },
      showpopup() {
        this.$emit("input", this.showpopup);
      }
    }
  };
</script>
<style lang="scss" scoped>
  p {
    margin: 0;
    font-size: 0.7rem;
    font-weight: 500;
  }

  .input_filter {
    width: 100%;
    font-size: 0.7rem;
    font-weight: 700;
    border: 1px solid grey;
    border-radius: 0;
    padding: 0 0.4rem;
  }

  .searchproductPopup {
    padding: 1vh 4vw;
  }

  .searchproductPopup .supplieName {
    text-decoration: none;
  }
  /deep/ .adminSearch{
    .modal-dialog{
      max-width: 56.25rem;;
    }
  }
</style>
