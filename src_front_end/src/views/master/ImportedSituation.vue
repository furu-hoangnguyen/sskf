<template>
  <section class="empbody">
    <div class="header">マスタ取込状況</div>
    <div class="controller">
      <div class="filter-as-text">
        <p class="filter-title">検索条件:</p>
        <span>{{filterRequestAsText}}</span>
      </div>
      <div>
        <b-button variant="none" class="btn-white" @click="showPopupSearch = true">検索条件</b-button>
        <b-button variant="none" class="btn-white" @click="resetList">検索条件 <br>リセット</b-button>
      </div>
    </div>
    <div class="employee" >
      <div class="filter">
        <b-form-select
          v-model="request.sortField"
          :options="sorts"
          value-field="field"
          text-field="name"
          @change="goSelect">
        </b-form-select>

        <b-form-select
          v-model="request.limitNumber"
          :options="pageLimit"
          value-field="limit"
          text-field="text"
          @change="goSelect">
        </b-form-select>
      </div>

      <div class="main-table">
        <table ref="contentImportedSituationMaster">
          <thead>
            <tr>
              <th v-for="(field, index) in fields" :key="index">
                {{ field.label }}
              </th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="item in tableItems" :key="item.cd">
              <td v-for="(field, fieldIndex) in fields" :key="fieldIndex">
                <span v-if="field.displayAs === 'datetime'">
                  {{ item[field.fieldName] | $_mixins_formatDate("YYYY/MM/DD HH:mm:ss") }}
                </span>

                <span v-else-if="field.displayAs === 'number'">
                  {{ statusMap.find(s => parseInt(s.value) === item[field.fieldName]).text }}
                </span>

                <span v-else>
                  {{ optionsTableFilter.find( tb => tb.value === item[field.fieldName]).text }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div>
        <b-pagination
          align="right"
          v-model="currentPage"
          :total-rows="totalItemsPageable"
          :per-page="request.limitNumber"
          :first-number="currentPage == 1"
          :prev-class="{'d-none' : currentPage == 1}"
          :last-number="currentPage == totalPage || totalPage == 0"
          :next-class="{'d-none' : currentPage == totalPage || totalPage == 0}"
          class="mr-4"
          @change="onPageChanged($event)"
        >
        </b-pagination>
      </div>
    </div>
    
    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件
      </div>
      <b-row>
        <b-col cols="9">
          <b-form-group label="対象マスタ" label-for="filter_table" class="label_input searchEmployee mt-5">
            <b-form-select v-model="filterRequest.tableName" :options="optionsTableFilter" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="ステータス" label-for="filter_status" class="label_input searchEmployee mt-3">
            <b-form-select v-model="filterRequest.status" :options="statusMap"></b-form-select>
          </b-form-group>
        </b-col>

      </b-row>
      <b-row class="search_recordAdmin">

      </b-row>
      <b-row class="mt-4">
        <b-col cols="8" align-self="end">
          <b-button-group>
            <b-button variant="none" class="btn-filter btn-white" @click="resetSearchForm">リセット</b-button>
            <b-button variant="none" class="btn-filter btn-blue" @click="onSearch">検索する</b-button>
          </b-button-group>
        </b-col>
      </b-row>
    </AdminSearchDialog>
  </section>
</template>

<script>
import MasterService from "@/services/master/MasterService";
import FormatDate from "@/mixins/FormatDate";

export default {
  name: "ImportedSituation",
  mixins: [ FormatDate ],
  data() {
    return {
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterRequest: {
        tableName: "",
        status: ""
      },
      filterRequestAsText: "",
      request:{
        sortField: "importedAt==desc",
        pageNumber: 0,
        limitNumber: 20,
        keyword: ""
      },
      statusMap: [
        { value: "", text: "" },
        { value: "0", text: "取込待ち" },
        { value: "1", text: "取込中" },
        { value: "2", text: "取込済" },
        { value: "3", text: "エラー" }
      ],
      showPopupSearch: false,
      fields: [
        { fieldName: "importedTableName", label: "対象マスタ", displayAs: "text" },
        { fieldName: "importedAt", label: "取込完了日時", displayAs: "datetime" },
        { fieldName: "number", label: "ステータス", displayAs: "number" }
      ],
      tableItems: [],
      optionsTableFilter: [
        { value: "", text: "" },
        { value: "shain", text: "社員マスタ" },
        { value: "mst_hinmoku", text: "品目マスタ" },
        { value: "mst_store", text: "店舗マスタ" },
        { value: "mst_torihiki", text: "取引先マスタ"},
        { value: "mst_bumon", text: "部門マスタ" },
        { value: "mst_tanto", text: "担当マスタ" },
        { value: "mst_yakushoku", text: "役職マスタ" },
        { value: "mst_rel_yakushoku_shain", text: "役職社員中間マスタ" },
        { value: "mst_approvalflows", text: "承認フローマスタ" },
        { value: "mst_approvalflow_details", text: "承認フロー詳細マスタ" },
        { value: "mst_rel_approvalflows_systems", text: "システム名称承認フロー中間マスタ"},
        { value: "mst_systems", text: "システム名称マスタ"},
        { value: "shain_addresses", text: "社員メールアドレスマスタ"}
      ],
      sorts: [
        { field: "importedAt==desc", name: "取込完了日時(降順)" },
        { field: "importedAt==asc", name: "取込完了日時(昇順)" }
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
      ]
    };
  },
  methods: {
    getData() {
      this.onScrollTop();

      MasterService.getImportedSituationMasterStatus(this.request).then(res => {
        this.tableItems = res.data.content;
        this.totalItemsPageable = res.data.totalElements;
        this.totalPage = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      });
    },
    onPageChanged(event) {
      this.request.pageNumber = event - 1;
      this.getData();
    },
    resetSearchForm() {
      this.filterRequest.status = "";
      this.filterRequest.tableName = "";
      this.request.keyword = "";
    },
    resetList() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.limitNumber = 20;
      this.request.sortField = "importedAt==desc";
      this.filterRequestAsText = "";
      
      this.resetSearchForm();
      this.getData();
    },
    onSearch() {
      let filterItems = [];
      let filterText = [];
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.limitNumber = 20;
      this.request.sortField = "importedAt==desc";

      if (this.filterRequest.tableName) {
        filterItems.push("importedTableName==" + this.filterRequest.tableName);
        filterText.push("対象マスタ: " + this.optionsTableFilter.find( tb => tb.value == this.filterRequest.tableName).text);
      }
      if (this.filterRequest.status) {
        filterItems.push("number==" + this.filterRequest.status);
        filterText.push("ステータス: " + this.statusMap.find(s => parseInt(s.value) === parseInt(this.filterRequest.status)).text);
      }

      this.request.keyword = filterItems.join(";");
      this.filterRequestAsText = filterText.join(" / ");

      this.getData();
      this.showPopupSearch = false;
    },
    goSelect() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.getData();
    },
    onScrollTop() {
      if (this.$refs.contentImportedSituationMaster) {
        this.$refs.contentImportedSituationMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.getData();
  }
};
</script>

<style lang="scss" scoped>
.employee {
  max-width: 32.5rem;
  .filter {
    padding-right: 1.375rem;
  }
}

.main-table {
  table {
    th, td {
      min-width: 60px;
      max-width: 33%;
      &:first-child {
        max-width: 34%;
      }
    }
    tr td:last-child{
      width: 6.3rem;
    }
    // tr {
    //   th {
    //     min-width: 11rem !important;
    //     max-width: 11rem !important;

    //     &:nth-child(1) {
    //       min-width: 20rem !important;
    //       max-width: 20rem !important;
    //     }

    //     &:nth-child(3) {
    //       min-width: 8rem !important;
    //       max-width: 8rem !important;
    //     }
    //   }
    //   td {
    //     padding: 5px !important;
    //     min-width: 11rem !important;
    //     max-width: 11rem !important;

    //     &:nth-child(1) {
    //       min-width: 20rem !important;
    //       max-width: 20rem !important;
    //     }

    //     &:nth-child(3) {
    //       min-width: 8rem !important;
    //       max-width: 8rem !important;
    //     }
    //   }
    // }

    tr:last-child {
      td {
        border-bottom: 1px solid grey;
      }
    }
  }
}
</style>