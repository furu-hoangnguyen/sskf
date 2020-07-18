<template>
  <div v-if="requestInfo.requestList.length > 0">
    <request-display
      v-bind:request-list="requestInfo.requestList"
      v-bind:is-update-list="isUpdateList"
      v-on:set-is-update-list="setIsUpdateList"
    >
    </request-display>

    <b-pagination
      align="right"
      v-model="requestInfo.pageCurrent"
      :total-rows="requestInfo.totalElements"
      :per-page="requestInfo.pageSize"
      class="pt-3 pr-5"
      @change="handlePage($event)"
      :first-number="requestInfo.pageCurrent == 1"
      :prev-class="{'d-none': requestInfo.pageCurrent == 1}"
      :last-number="requestInfo.pageCurrent == requestInfo.totalPages"
      :next-class="{'d-none' : requestInfo.pageCurrent == requestInfo.totalPages}"
    ></b-pagination>
  </div>
</template>

<script>
export default {
  name: "RequestDisplayAndPagination",
  props: [ "requestInfo", "isUpdateList" ],
  data() {
    return {};
  },
  methods: {
    setIsUpdateList(value) {
      this.$emit('set-is-update-list', value);
    },
    handlePage(event) {
      this.$emit('handle-page', event);
    }
  }
}
</script>