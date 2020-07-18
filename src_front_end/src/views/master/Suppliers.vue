<template>
  <section class="empbody">
    <div class="header">取引先マスタ</div>
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
      <div class="main-table tableAdmin-supplier">
        <table @scroll="fixScrollTable" ref="contentSupplierMaster">
          <thead>
          <tr>
            <th v-for="(field, index) in fields" :key="index" :class="{ 'has-border relative-pos' : index == 2 }"
                :style="{left:  parseInt(`${ index < 3 ? fixLeft : 0 }`)  + 'px'}" v-html="field.label">
              {{field.label}}
            </th>
          </tr>
          </thead>
          <tbody>
            <tr v-for="(supplier, index) in suppliers" :key="index">
              <td v-for="(field, fieldIndex) in fields" :key="fieldIndex"
                  @mouseover.self="handleHover($event,`${suppliers[index].torihikiCd + fieldIndex}`)"
                  @mouseleave.self="handleHoverLeave(`${suppliers[index].torihikiCd + fieldIndex}`)"
                  :id="`${suppliers[index].torihikiCd + fieldIndex}`" v-b-tooltip.hover.right.v-secondary="{title:innerTextToolTip, disabled: true}"
                  :style="{left: parseInt(`${ fieldIndex < 3 ? fixLeft : 0 }`) + 'px'}" :class="{ 'has-border relative-pos' : fieldIndex == 2 }">

                <span v-if="field.displayAs === 'datetime'">
                      {{supplier[field.fieldName] | $_mixins_formatDate("YYYY/MM/DD HH:mm:ss") }}
                </span>
                <span v-else-if="field.displayAs === 'checkbox'">
                    <span v-if="supplier.torihikiCdNk"></span>
                    <span v-else>
                      <i class="fas fa-check"></i>
                    </span>
                </span>
                <span v-else>
                      {{supplier[field.fieldName]}}
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
      class="mr-5"
      @change="onPageChanged($event)"
    >
    </b-pagination>
      </div>
    </div>

    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件
      </div>
      <b-row no-gutters>
        <b-col cols="9">
          <b-form-group label="取引先コード" label-for="supplier_code"
                        class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.torihiki_cd" type="text" id="supplier_code"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="取引先略称" label-for="supplier_abbr_name"
                        class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.torihiki_rnm_for_search" type="text" id="supplier_abbr_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="取引先略称カナ" label-for="supplier_abbr_kana_name"
                        class="label_input searchEmployee searchproduct mt-3">
            <b-form-input v-model="filter.torihiki_rknm_for_search" type="text" id="supplier_abbr_kana_name"></b-form-input>
          </b-form-group>
        </b-col>

        <b-col cols="9">
          <b-form-group label=" " label-for="include_deleted"
                        class="label_input searchEmployee searchEmployee_checkbox searchproduct mt-3">
            <b-form-checkbox size="lg" id="include_deleted" v-model="filter.includeDeleted" plain>削除済みを含める</b-form-checkbox>
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
  name: "Suppliers",
  mixins:[FormatDate],
  data() {
    return {
      textTooltip:" ",
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterAsText: "",
      suppliers: [],
      filter: {
        torihiki_cd: "",
        torihiki_rnm_for_search: "",
        torihiki_rknm_for_search: "",
        includeDeleted: false
      },
      request: {
        sortField: "torihikiCd==asc",
        pageNumber: 0,
        limitNumber: 20
      },
      showPopupSearch: false,
      fixLeft: 0,
      fields: [ { fieldName: "torihikiCd", label: "取引先コード", displayAs: "text"},
        { fieldName: "torihiki1Nm", label: "取引先名1", displayAs: "text"},
        { fieldName: "torihiki2Nm", label: "取引先名2", displayAs: "text"},
        { fieldName: "torihikiRnm", label: "取引先略称", displayAs: "text"},
        { fieldName: "torihikiRknm", label: "取引先略称カナ", displayAs: "text"},
        { fieldName: "seikyuFlg", label: "請求先 <br />フラグ", displayAs: "text"},
        { fieldName: "choaiKoriFlg", label: "帳合先 <br />フラグ", displayAs: "text"},
        { fieldName: "torihikiStatus", label: "取引先 <br />ステータス", displayAs: "text"},
        { fieldName: "torihikiCdNk", label: "削除 <br />ステータス", displayAs: "checkbox"},
        { fieldName: "batchUpdateDate", label: "バッチ更新日時", displayAs: "datetime"}
      ],
      sorts: [
        { field: "torihikiCd==asc", name: "取引先コード(昇順)" },
        { field: "torihikiCd==desc", name: "取引先コード(降順)" },
        { field: "torihikiRknm==asc", name: "取引先略称カナ(昇順)" },
        { field: "torihikiRknm==desc", name: "取引先略称カナ(降順)" }
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
        this.$root.$emit("bv::hide::tooltip", id);
        this.$root.$emit("bv::disable::tooltip");
        this.textTooltip = " ";
      }
    },
    handleHover(event, id) {
      if(id){
        let elm1 = event.target
        let text = elm1?.innerText ? elm1?.innerText : "";
        if ( elm1?.scrollWidth > elm1?.clientWidth) {
          this.textTooltip = text;
          this.$root.$emit(" bv::enable::tooltip");
          this.$root.$emit("bv::show::tooltip", id);
        }
      }
    },
    innerTextToolTip(){
      return this.textTooltip
    },
    getData(){
      this.onScrollTop();

      MasterService.getSupliers(this.request).then(res => {
        this.suppliers = res.data.content;
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
      this.request.limitNumber = 20;

      if (this.filter.torihiki_cd) {
        filterItems.push("torihikiCd=='" + this.filter.torihiki_cd + "%'");
        filterText.push("取引先コード: " + this.filter.torihiki_cd);
      }

      if (this.filter.torihiki_rnm_for_search) {
        this.filter.torihiki_rnm_for_search.split(" ").join(" ").split(" ").forEach(key => {
          if (key) {
            filterItems.push("torihikiRnmForSearch=='%" + key + "%'");
          }
        });
        filterText.push("取引先略称: " + this.filter.torihiki_rnm_for_search);
      }

      if (this.filter.torihiki_rknm_for_search) {
        this.filter.torihiki_rknm_for_search.split("　").join(" ").split(" ").forEach(key => {
          if (key) {
            filterItems.push("torihikiRknmForSearch=='%" + key + "%'");
          }
        });
        filterText.push("取引先略称カナ:" + this.filter.torihiki_rknm_for_search);
      }

      if (this.filter.includeDeleted) {
        filterText.push("削除済みを含める");
      } else {
        filterItems.push("(torihikiCdNk!='';torihikiCdNk!=null)");
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
      this.filter.torihiki_cd = "";
      this.filter.torihiki_rnm_for_search = "";
      this.filter.torihiki_rknm_for_search = "";
      this.filter.includeDeleted = false;

      this.request.keyword = "torihikiCdNk!='';torihikiCdNk!=null";
    },
    resetList() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.filterAsText = "";
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
      if (this.$refs.contentSupplierMaster) {
        this.$refs.contentSupplierMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.request.keyword = "torihikiCdNk!='';torihikiCdNk!=null";
    this.getData();
  }
};

</script>

<style lang="scss" scoped>

.btn-filter {
  padding: 0.7rem 2rem;
  width: 10rem;
  height: 4rem;
  margin: 0 1vw;
}

.filter-as-text {
  width: 65%;
}

.employee {
  .filter {
    height: auto;
    padding-right: 0.7rem;
    
    select {
      width: auto;
      border-radius: 0;
      border-color: #03030399;

      &:nth-child(2) {
        width: 5rem;
      }
    }
  }
}

.controller {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 1.4rem;
  padding: 1rem 0rem 0.5rem 2rem;
  height: auto;

  button {
    padding: 0.7rem 2rem;
    width: 10rem;
    height: 4rem;
    margin: 0;
    margin-right: 1.875rem;

    &:nth-child(2) {
      margin-right: 0.9375rem;
    }
  }
}

.main-table {
  table {
    tr {
      th {
        min-width: 10.5rem;
      }

      td {
        padding: 5px !important;
      }
    }

    tr:last-child {
      td {
        border-bottom: 1px solid grey;
      }
    }
  }
}
</style>