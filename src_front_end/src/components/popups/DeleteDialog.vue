<template>
  <b-modal v-model="showpopup" size="lg" centered hide-header hide-footer body-class="p-0">
    <b-container fluid class="p-0">
      <b-row class="deleteHeader px-5 pt-3" align-v="center">
       <p> {{title}}</p>
      </b-row>
      <b-row class="deleteBtn px-5">
        <b-button variant="none" class="btn-white" @click="showpopup = false">戻る</b-button>
        <b-button variant="none" @click="$emit('delete')" class="btn-blue">削除する</b-button>
      </b-row>
    </b-container>
  </b-modal>
</template>
<script>
  import SupplierService from "@/services/SupplierService";
  import modelApi from "@/services/ModelApi";
  export default {
    name: "DeleteDialog",
    data() {
      return {
        showpopup: this.value,
      };
    },
    props: {
      value: {
        type: Boolean
      },
      title: {
        type: String,
        default: "申請情報を破棄しますか？"
      }
    },
    methods: {

    },
    watch: {
      value() {
        this.showpopup = this.value;
      },
      showpopup() {
        this.$emit("input", this.showpopup);
      }
    },
    computed: {

    },
  };
</script>
<style lang="scss" scoped>
.deleteHeader{
  height: 25rem;
  text-align: center;
  p{
    font-size: 1.3rem;
    font-weight: 500;
    margin-left: 20%;
  }
}
.deleteBtn{
  padding: 1rem 0;
  float: right;
  button{
    padding: 1rem 0;
    width: 10rem;
    margin-right: 3rem;
  }
}
</style>