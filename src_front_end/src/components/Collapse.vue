<template>
  <b-container fluid no-gutters class="sskf_collapse">
    <b-row class="sskf_collapse_header p-1 " v-b-toggle="id">
      <b-col cols="auto" class="pl-0">
        <slot name="collapse-header"></slot>
        <i :class="['fas', show ? 'fa-angle-down' : 'fa-angle-right', 'pl-1']"></i>
      </b-col>
    </b-row>
    <b-collapse :id="id" class="sskf_collapse_body" visible v-model="show">
      <slot name="collapse-body"></slot>
    </b-collapse>
  </b-container>
</template>
<script>
export default {
  name: "Collapse",
  props: {
    id: {
      type: String,
      default: () => {
        return "sskf_" + Math.random().toString(36).substring(7);
      }
    },
    visible: {
      type: Boolean,
      default: true
    }
  },
  data(){
    return {
      show: true
    }
  }
}
</script>
<style lang="scss" scoped>
.sskf_collapse {
  .sskf_collapse_header {
    max-width: fit-content;
    line-height: 3rem;
    cursor: pointer;
    & > div {
      display: flex;
      flex-wrap: nowrap;
      align-items: center;
      font-weight: 400;
      font-size: 1rem;
    }
  }
}
</style>