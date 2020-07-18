<template>
  <div :class="['main_request_display_body body_padding_inputting_content customScrollBar',{'requestDisplayTablet': isTabletDevice}]" ref="contentRequestItems">
    <b-container fluid :class="['m-0 ',{'pt-4': !isTabletDevice }]">
      <div v-for="(item, index) in requestList" v-bind:key="item.cd" class="request_item">
        <request-item
          v-if="!updateList"
          v-bind:item="item"
          :class="{'firstItem': index == 0 && requestList.length > 1 && isTabletDevice}"
        >
        </request-item>
      </div>
    </b-container>
  </div>
</template>

<script>
import RequestService from "@/services/RequestService";
import isTablet from "@/helper/isTabletDevice";
export default {
  name: "RequestDisplay",
  data() {
    return {};
  },
  props: ["requestList", "isUpdateList"],
  methods: {
    setIsUpdateList(value) {
      this.$emit("set-is-update-list", value);
    }
  },
  computed: {
    updateList() {
      return this.isUpdateList;
    },
    isTabletDevice(){
      return isTablet()
    }
  },
  watch: {
    updateList(newValue) {
      if (this.$refs.contentRequestItems) {
        this.$refs.contentRequestItems.scrollTop = 0;
      }
    },
    isUpdateList(newValue, oldValue) {
      if(newValue != oldValue) {
        this.setIsUpdateList(false);
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.setIsUpdateList(false);
    })
  }
};
</script>

<style lang="scss">
.main_request_display_body {
  height: 50vh;
  overflow-y: auto;
  align-self: flex-end;
  .request_item:last-of-type {
    .requestListItem {
      border-bottom: 2px solid lightgrey;
    }
  }
}
  // @media screen and (max-width: 1367px) {
  //   .main_request_display_body{
  //     max-height: calc(100% - 200px);
  //   }
  // }
.main_request_display_body{
    max-height: calc(100% - 8rem);
    height: auto;
  }
.requestDisplayTablet{
  padding: 0 !important;
}    
.firstItem .requestListItem{
  border-top: none !important;
}
</style>