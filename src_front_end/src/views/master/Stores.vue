<template>
  <section class="empbody">
    <div class="header">店舗マスタ</div>
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
        <table class="table b-table customScrollBar" @scroll="fixScrollTable" ref="contentStoreMaster">
          <thead>
            <tr>
              <th v-for="(field, index) in fields" :key="index" :class="{ 'has-border' : index == 2 }"
                  :style="{left:  parseInt(`${ index < 3 ? fixLeft : 0 }`)  + 'px'}">
                {{field.label}}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(store, index) in stores" :key="index">
              <td v-for="(field, fieldIndex) in fields" :key="fieldIndex"
                  @mouseover.self="fieldIndex < 2 ? handleHover($event,`${stores[index].storeCd + index + fieldIndex}`) : ''"
                  @mouseleave.self="fieldIndex < 2 ? handleHoverLeave(`${stores[index].storeCd + index + fieldIndex}`) : ''"
                  :class="{ 'has-border' : fieldIndex == 2 }"
                  :style="{left: parseInt(`${ fieldIndex < 3 ? fixLeft : 0 }`)   + 'px'}"
                  :id="`${stores[index].storeCd + index + fieldIndex}`"
                  v-b-tooltip.hover.top.v-secondary="{title:innerTextToolTip, disabled: true}"
              >
                <span v-if="field.displayAs === 'datetime'">
                      {{store[field.fieldName] | $_mixins_formatDate("YYYY/MM/DD HH:mm:ss") }}
                  </span>
                <span v-else>
                      {{store[field.fieldName]}}
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
          class='mr-4'
          @change="onPageChanged($event)"
        >
      </b-pagination>
      </div>
    </div>


    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件</div>
      <b-row no-gutters>
        <b-col cols="8">
          <b-form-group label="店舗コード" label-for="store_code" class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.store_cd" type="text" id="store_code"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="店舗グループコード" label-for="store_group_code" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.store_g_cd" type="text" id="store_group_code"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="店舗グループ名" label-for="store_group_name" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.store_g_nm_for_search" type="text" id="store_g_nm_for_search"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="8">
          <b-form-group label="店舗グループ担当者" label-for="store_group_charge" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.shain_nm" type="text" id="shain_nm"></b-form-input>
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
  name: "Stores",
  mixins:[FormatDate],
  data() {
    return {
      textTooltip:" ",
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterAsText: '',
      stores: [],
      filter: {
        store_cd: '',
        store_g_cd: '',
        store_g_nm_for_search: '',
        shain_nm: ''
      },
      request: {
        sortField: 'storeCd==asc',
        pageNumber: 0,
        limitNumber: 20
      },
      showPopupSearch: false,
      fixLeft: 0,
      fields: [ { fieldName: 'storeCd', label: '店舗コード', displayAs: 'text'},
        { fieldName: 'storeGCd', label: '店舗グループコード', displayAs: 'text'},
        { fieldName: 'storeGNm', label: '店舗グループ名', displayAs: 'text'},
        { fieldName: 'shainNm', label: '店舗グループ担当者', displayAs: 'text'},
        { fieldName: 'updateDateAw', label: '更新日時(AW)', displayAs: 'datetime'},
        { fieldName: 'updateDateMb', label: '更新日付(MB)', displayAs: 'datetime'}
      ],
      sorts: [
        { field: 'storeCd==asc', name: '店舗コード(昇順)' },
        { field: 'storeCd==desc', name: '店舗コード(降順)' },
        { field: 'storeGCd==asc', name: '店舗グループコード(昇順)' },
        { field: 'storeGCd==desc', name: '店舗グループコード(降順)' }
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
    handleHoverLeave(id) {
      if(id){
        this.$root.$emit('bv::hide::tooltip', id);
        this.$root.$emit('bv::disable::tooltip');
        this.textTooltip = " ";
      }
    },
    handleHover(event, id) {
      if(id){
        let elm1 = event.target;
        let text = elm1?.innerText ? elm1?.innerText : "";
        if ( elm1?.scrollWidth > elm1?.clientWidth) {
          this.textTooltip = text;
          this.$root.$emit('bv::enable::tooltip');
          this.$root.$emit('bv::show::tooltip', id);
        }
      }
    },
    innerTextToolTip(){
      return this.textTooltip;
    },
    getData(){
      this.onScrollTop();

      MasterService.getStores(this.request).then(res => {
        this.stores = res.data.content;
        this.totalItemsPageable = res.data.totalElements;
        this.totalPage = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      });
    },
    onSearch() {
      let filterItems = [];
      let filterText = [];
      this.currentPage = 1;
      this.request.pageNumber = 0;

      if (this.filter.store_cd) {
        filterItems.push("storeCd=='" + this.filter.store_cd + "%'");
        filterText.push('店舗コード: ' + this.filter.store_cd);
      }

      if (this.filter.store_g_cd) {
        filterItems.push("storeGCd=='" + this.filter.store_g_cd + "%'");
        filterText.push('店舗グループコード: ' + this.filter.store_g_cd);
      }

      if (this.filter.store_g_nm_for_search) {
        this.filter.store_g_nm_for_search.split('　').join(' ').split(' ').forEach(key => {
          if (key) {
            filterItems.push("storeGNmForSearch=='%" + key + "%'");
          }
        });
        filterText.push('店舗グループ名:' + this.filter.store_g_nm_for_search);
      }

      if (this.filter.shain_nm) {
        filterItems.push("mstTantoEntity.shainEntity.shainNm=='%" + this.filter.shain_nm + "%'");
        filterText.push('店舗グループ担当者: ' + this.filter.shain_nm);
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
      this.filter.store_cd = '';
      this.filter.store_g_cd = '';
      this.filter.store_g_nm_for_search = '';
      this.filter.shain_nm = '';

      this.request.keyword = '';
    },
    resetList() {
      this.filterAsText = '';
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.resetSearchForm();
      this.getData();
    },
    fixScrollTable(event) {
      this.fixLeft = event.target.scrollLeft;
    },
    goSelect() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.getData();
    },
    onScrollTop() {
      if (this.$refs.contentStoreMaster) {
        this.$refs.contentStoreMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.getData();
  }
};
</script>

<style lang='scss' scoped>
.employee {
  max-width: 54.625rem;
  padding-right: 0;
  .filter {
    padding-right: .6rem;
  }
  .main-table {
    table.b-table {
      display: block;
      td, th {
        font-size: 0.75rem;
        color: #000000;
        font-weight: 400;
        padding: 0.25rem;
        height: 2.25rem;
        min-width: 8.33rem;
        max-width: 16.67%;
      }
      tr:last-child {
        td {
          border-bottom: 1px solid grey;
        }
      }
      td{
        height: 1.4375rem;
      }
      th{
        height: 2.25rem;
      }
    }
  }
  .btn-filter {
    color: red;
  }
}
</style>