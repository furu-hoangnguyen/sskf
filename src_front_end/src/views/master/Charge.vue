<template>
  <section class="empbody">
    <div class="header">担当マスタ</div>
    <div class="controller">
      <div class="filter-as-text">
        <p>検索条件:</p>
        <span>{{filterAsText}}</span>
      </div>
      <div>
        <b-button variant="none" class="btn-white" @click="showPopupSearch = true">検索条件</b-button>
        <b-button variant="none" class="btn-white" @click="resetList">検索条件<br>リセット</b-button>
      </div>
    </div>
    <div class="employee">
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
        <table ref="contentChargeMaster">
          <thead>
          <tr>
            <th v-for="(field, index) in fields" :key="index">
              {{field.label}}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(charge, index) in charges" :key="index">
            <td v-for="(field, fieldIndex) in fields" :key="fieldIndex">
              <span v-if="field.displayAs === 'datetime'">
                    {{charge[field.fieldName] | $_mixins_formatDate("YYYY/MM/DD HH:mm:ss") }}
              </span>
              <span v-else-if="field.displayAs === 'checkbox'">
                  <span v-if="charge[field.fieldName] == '0'">
                    <i class="fas fa-check"></i>
                  </span>
              </span>
              <span v-else>
                    {{charge[field.fieldName]}}
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
      <b-row no-gutters>
        <b-col cols="8">
          <b-form-group label="販売担当者コード" label-for="input_tanto_cd" class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.tanto_cd" type="text" id="input_tanto_cd"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="社員名" label-for="shain_name" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.shain_nm" type="text" id="shain_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="部門名" label-for="bumon_name" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select
              v-model="filter.bumon_nm"
              :options="departmentSelectOptions"
              value-field="text"
              text-field="text"
              id="bumon_name"
            >
            </b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="担当者ステータス" label-for="tanto_status" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select 
              v-model="filter.tanto_status"
              id="tanto_status"
              :options="tantoStatusList"
            >
            </b-form-select>
          </b-form-group>
        </b-col>

        <b-row class="search_recordAdmin">

        </b-row>

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
  name: "Charge",
  mixins:[FormatDate],
  data() {
    return {
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterAsText: "",
      departmentSelectOptions: [],
      charges: [],
      filter: {
        tanto_cd: "",
        shain_nm: "",
        bumon_nm: "",
        tanto_status: ""
      },
      request: {
        sortField: "tantoCd==asc",
        pageNumber: 0,
        limitNumber: 20
      },
      showPopupSearch: false,
      fixLeft: 0,
      fields: [ { fieldName: "tantoCd", label: "販売担当者コード", displayAs: "text"},
        { fieldName: "shainNm", label: "社員名", displayAs: "text"},
        { fieldName: "bumonNm", label: "部門名", displayAs: "text"},
        { fieldName: "tantoStatus", label: "担当者ステータス", displayAs: "checkbox"},
        { fieldName: "batchUpdateDate", label: "バッチ更新日時", displayAs: "datetime"}
      ],
      sorts: [
        { field: "tantoCd==asc", name: "販売担当者コード(昇順)" },
        { field: "tantoCd==desc", name: "販売担当者コード(降順)" }
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
      tantoStatusList: [
        { value: '0', text: '有効' },
        { value: '9', text: '無効' }
      ] 
    };
  },
  methods: {
    getData(){
      console.log(this.request.keyword);
      this.onScrollTop();

      MasterService.getTantos(this.request).then(res => {
        this.charges = res.data.content;
        this.totalItemsPageable = res.data.totalElements;
        this.totalPage = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      });
    },
    getFilterSelectOptions() {
      this.departmentSelectOptions = [{ value: "", text: "" }];
      MasterService.getDepartmentSelectOptions().then(res => {
        res.data.forEach(department => {
          this.departmentSelectOptions.push({ value: department.bumonCd, text: department.bumonNm });
        });
      }).catch(err => {
        console.log(err);
      });
    },
    onSearch() {
      let filterItems = [];
      let filterText = [];
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.limitNumber = 20;
      this.request.sortField = "tantoCd==asc";

      if (this.filter.tanto_cd) {
        filterItems.push("tantoCd=='" + this.filter.tanto_cd + "%'");
        filterText.push("販売担当者コード: " + this.filter.tanto_cd);
      }

      if (this.filter.shain_nm) {
        this.filter.shain_nm.split('　').join(' ').split(' ').forEach(key => {
          if (key) {
            filterItems.push("shainEntity.shainNm=='%" + key + "%'");
          }
        });
        filterText.push("社員名: " + this.filter.shain_nm);
      }

      if (this.filter.bumon_nm) {
        filterItems.push("mstBumonEntity.bumonNm=='" + this.filter.bumon_nm + "'");
        filterText.push("部門名: " + this.filter.bumon_nm);
      }

      if (this.filter.tanto_status) {
        filterItems.push("tantoStatus=='" + this.filter.tanto_status + "'");
        filterText.push("担当者ステータス: " + this.tantoStatusList.find(item => item.value === this.filter.tanto_status).text);
      }

      this.request.keyword = filterItems.join(";");
      this.filterAsText = filterText.join(" / ");
      this.getData();
      this.showPopupSearch = false;
    },
    onPageChanged(event) {
      this.request.pageNumber = event - 1;
      this.getData();
    },
    resetSearchForm() {
      this.filter.tanto_cd = "";
      this.filter.bumon_nm = "";
      this.filter.tanto_status = "";
      this.filter.shain_nm = "";

      this.request.keyword = "";
    },
    resetList() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.limitNumber = 20;
      this.request.sortField = "tantoCd==asc";
      this.filterAsText = "";

      this.resetSearchForm();
      this.getData();
    },
    goSelect() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.getData();
    },
    onScrollTop() {
      if (this.$refs.contentChargeMaster) {
        this.$refs.contentChargeMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.getFilterSelectOptions();
    this.getData();
  }
};
</script>

<style lang="scss" scoped>
.employee {
  max-width: 48.5rem;
  /* .filter {
    height: auto;
    padding-right: 0;
    select {
      width: auto;
      border-radius: 0;
      border-color: #03030399;

      &:nth-child(2) {
        width: 5rem;
      }
    }
  } */
  .filter{
    padding-right: .6rem;
  }
  .main-table {
    table {
      display: block;
      td, th {
        font-size: 0.75rem;
        color: #000000;
        font-weight: 400;
        padding: 0.5rem;
        height: 2.25rem;
        min-width: 8.33rem;
        max-width: 16.67%;
      }
      tr:last-child {
        td {
          border-bottom: 1px solid grey;
        }
      }
    }
  }
}

</style>