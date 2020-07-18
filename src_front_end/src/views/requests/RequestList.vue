<template>
  <div class="mt-4">
    <b-tabs
      content-class="searching-before-inputting_content"
      nav-class="searching-before-inputting_header"
      @input="getTabActive"
    >
      <div class="body_padding_inputting_content">
        <div class="controller_list">
          <div>
            <p>検索条件</p>
            <p v-if="!wrapperRequest.filterRequest.isEmptyString(displayConditionFilter)"
              class="font-weight-normal show_condition_filter">
              {{ displayConditionFilter }}
            </p>
          </div>
          <div>
            <b-button variant="none" class="btn-white" @click="() => (isSearch=!isSearch)">検索条件</b-button>
            <b-button variant="none" class="btn-white" @click="setDefaultConditionFilter">検索条件リセット</b-button>
          </div>
        </div>
      </div>

      <b-tab active title-item-class="w_tab">
        <template v-slot:title>
          進行中
          <br />
          ({{ totalElementsInProgress }})
        </template>

        <div v-if="tabActive === 0 && requestInfo.requestList.length > 0">
          <request-display-and-pagination
            v-bind:requestInfo="requestInfo"
            v-bind:is-update-list="isUpdateList"
            v-on:set-is-update-list="setIsUpdateList"
            v-on:handle-page="handlePage"
          >
          </request-display-and-pagination>
        </div>
      </b-tab>

      <b-tab title-item-class="w_tab">
        <template v-slot:title>
          すべて
          <br />
          ({{ totalElementsAll }})
        </template>

        <div v-if="tabActive === 1 && requestInfo.requestList.length > 0">
          <request-display-and-pagination
            v-bind:requestInfo="requestInfo"
            v-bind:is-update-list="isUpdateList"
            v-on:set-is-update-list="setIsUpdateList"
            v-on:handle-page="handlePage"
          >
          </request-display-and-pagination>
        </div>
      </b-tab>

      <b-tab title-item-class="w_tab">
        <template v-slot:title>
          差し戻し
          <br />
          ({{ totalElementsSendBack }})
        </template>

        <div v-if="tabActive === 2 && requestInfo.requestList.length > 0">
          <request-display-and-pagination
            v-bind:requestInfo="requestInfo"
            v-bind:is-update-list="isUpdateList"
            v-on:set-is-update-list="setIsUpdateList"
            v-on:handle-page="handlePage"
          >
          </request-display-and-pagination>
        </div>
      </b-tab>

      <b-tab title-item-class="w_tab">
        <template v-slot:title>
          却下
          <br />
          ({{ totalElementsReject }})
        </template>

        <div v-if="tabActive === 3 && requestInfo.requestList.length > 0">
          <request-display-and-pagination
            v-bind:requestInfo="requestInfo"
            v-bind:is-update-list="isUpdateList"
            v-on:set-is-update-list="setIsUpdateList"
            v-on:handle-page="handlePage"
          >
          </request-display-and-pagination>
        </div>
      </b-tab>

      <template v-slot:tabs-end>
        <li role="presentation" class="nav-item select_filter">
          <div class="filter">
            <b-form inline>
              <b-select
                v-model="selectedSort"
                @change="goSelectedSort($event)"
                :options="sorts"
                value-field="name"
                text-field="name"
                class="mr-3"
              ></b-select>
              <b-select
                v-model="selectedLimit"
                @change="goSelectedLimit($event)"
                :options="pageLimit"
                value-field="limit"
                text-field="text"
              ></b-select>
            </b-form>
          </div>
        </li>
      </template>
    </b-tabs>

    <request-search
      v-bind:is-search="isSearch"
      v-bind:tab-active="tabActive"
      v-bind:filter-request="wrapperRequest.filterRequest"
      v-on:set-is-search="setIsSearch"
      v-on:set-filter-request="setFilterRequest"
      v-on:set-filter-request-on-reset="setFilterRequestOnReset"
    >
    </request-search>
  </div>
</template>

<script>
import WrapperRequest from "@/models/WrapperRequest";
import FilterRequest from "@/models/FilterRequest";
import RequestService from "@/services/RequestService";

export default {
  name: "RequestList",
  data() {
    return {
      tabActive: 0,
      totalElementsInProgress: 0,
      totalElementsAll: 0,
      totalElementsSendBack: 0,
      totalElementsReject: 0,
      requestInfo: {
        pageCurrent: 1,
        pageSize: 20,
        totalElements: 0,
        totalPages: 0,
        requestList: []
      },
      isUpdateList: false,
      sorts: [
        {
          name: "申請日時(古い順)",
          field: "requestedAt",
          orderBy: "asc"
        },
        {
          name: "申請日時(新しい順)",
          field: "requestedAt",
          orderBy: "desc"
        },
        {
          name: "更新日時(古い順)",
          field: "updatedStatusAt",
          orderBy: "asc"
        },
        {
          name: "更新日時(新しい順)",
          field: "updatedStatusAt",
          orderBy: "desc"
        },
        {
          name: "金額(昇順)",
          field: "billingAmount",
          orderBy: "asc"
        },
        {
          name: "金額(降順)",
          field: "billingAmount",
          orderBy: "desc"
        }
      ],
      pageLimit: [
        {
          text: "20件",
          limit: 20
        },
        {
          text: "50件",
          limit: 50
        }
      ],
      selectedSort: "申請日時(古い順)",
      selectedLimit: 20,
      wrapperRequest: new WrapperRequest(),
      displayConditionFilter: "",
      isSearch: false
    };
  },
  methods: {
    getTabActive(index) {
      this.tabActive = index;
      this.wrapperRequest = new WrapperRequest(index + 3, 0, "requestedAt", "asc", 20);
      this.wrapperRequest.filterRequest = new FilterRequest();
      this.selectedSort = "申請日時(古い順)";
      this.selectedLimit = 20;
      this.displayConditionFilter = "";

      this.getRequestList(this.wrapperRequest);
    },
    goSelectedSort(name) {
      this.sorts.forEach(sort => {
        if (sort.name === name) {
          this.wrapperRequest.field = sort.field;
          this.wrapperRequest.orderBy = sort.orderBy;
          this.wrapperRequest.page = 0;

          this.getRequestList(this.wrapperRequest);
        }
      });
    },
    goSelectedLimit(limit) {
      this.wrapperRequest.limit = limit;
      this.wrapperRequest.page = 0;

      this.getRequestList(this.wrapperRequest);
    },
    setTotalElementsForTabActive(total) {
      switch (this.tabActive) {
        case 1:
          this.totalElementsAll = total;
          break;
        case 2:
          this.totalElementsSendBack = total;
          break;
        case 3:
          this.totalElementsReject = total;
          break;
        default:
          this.totalElementsInProgress = total;
      }
    },
    getRequestList(wrapperRequest) {
      RequestService.getRequestList(wrapperRequest)
        .then(res => {
          this.requestInfo.requestList = res.data.content;
          this.requestInfo.totalElements = res.data.totalElements;
          this.requestInfo.pageSize = res.data.pageable.pageSize;
          this.requestInfo.pageCurrent = res.data.number + 1;
          this.requestInfo.totalPages = res.data.totalPages;
          this.setTotalElementsForTabActive(res.data.totalElements);
          this.isUpdateList = true;
        })
        .catch(err => {
          console.log("getRequestList: " + err);
        });
    },
    handlePage(event) {
      this.wrapperRequest.page = event - 1;
      this.getRequestList(this.wrapperRequest);
    },
    setIsSearch(newValue) {
      this.isSearch = newValue;
    },
    setFilterRequest(newFilterRequest){
      this.wrapperRequest.page = 0;
      this.wrapperRequest.field = "requestedAt";
      this.wrapperRequest.orderBy = "asc";
      this.wrapperRequest.limit = 20;
      this.wrapperRequest.filterRequest = newFilterRequest;
      this.displayConditionFilter = newFilterRequest.toString();

      this.selectedSort = "申請日時(古い順)";
      this.selectedLimit = 20;

      this.getRequestList(this.wrapperRequest);
      this.isSearch = !this.isSearch;
    },
    setFilterRequestOnReset(newFilterRequest) {
      this.wrapperRequest.filterRequest = newFilterRequest;
    },
    setDefaultConditionFilter() {
      this.wrapperRequest.page = 0;
      this.wrapperRequest.field = "requestedAt";
      this.wrapperRequest.orderBy = "asc";
      this.wrapperRequest.limit = 20;
      this.wrapperRequest.filterRequest = new FilterRequest();
      this.displayConditionFilter = "";

      this.selectedSort = "申請日時(古い順)";
      this.selectedLimit = 20;

      this.getRequestList(this.wrapperRequest);
    },
    countRequestListDefault() {
      let promise = new RequestService.countRequestListDefault()
      .then(res => {
        this.totalElementsAll = res.data[0];
        this.totalElementsSendBack = res.data[1];
        this.totalElementsReject = res.data[2];
      })
      .catch(err => {
        console.log("countRequestListDefault: " + err);
      })
    },
    setIsUpdateList(value) {
      this.isUpdateList = value;
    }
  },
  created() {
    this.countRequestListDefault();
  }
};
</script>

<style lang="scss" scoped>
</style>