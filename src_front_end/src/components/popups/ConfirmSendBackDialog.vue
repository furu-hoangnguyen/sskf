<template>
  <b-modal v-model="isVisible" size="lg" centered hide-header hide-footer body-class="p-0">
    <b-container fluid class="p-0">
      <b-row class="confirm-sendback-header px-5 pt-3" align-v="center">
       <p> {{title}}</p>
      </b-row>
      <b-row class="btn-confirm px-5">
        <b-button variant="none" class="btn-white" @click="isVisible = false">戻る</b-button>
        <b-button variant="none" @click="$emit('submit')" class="btn-blue">確認する</b-button>
      </b-row>
    </b-container>
  </b-modal>
</template>
<script>
export default {
  name: "ConfirmSendBackDialog",
  data() {
    return {
      isVisible: false,
    };
  },
  model: {
    prop: "value",
    event: "change"
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: "差し戻しを確認し、再申請を行います。"
    }
  },
  watch: {
    value(newValue, oldValue) {
      if(newValue != oldValue) {
        this.isVisible = this.value;
      }
    },
    isVisible(newValue) {
      if(!newValue) {
        this.updateModel(false);
      }
    }
  },
  methods: {
    updateModel(val) {
      if (val !== this.value) {
        this.$emit('change', val)
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.confirm-sendback-header{
  height: 25rem;
  text-align: center;
  p{
    font-size: 1.3rem;
    font-weight: 500;
    margin-left: 20%;
  }
}
.btn-confirm{
  padding: 1rem 0;
  float: right;
  button{
    padding: 1rem 0;
    width: 10rem;
    margin-right: 3rem;
  }
}
</style>