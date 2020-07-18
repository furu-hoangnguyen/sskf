<template>
  <b-modal v-model="showpopup" hide-header centered hide-footer body-class="p-0" modal-class="supplier">
    <b-container fluid class="searchproductPopup searchAdminPopup searchRequestPopup searchSupply p-0 searchRequestPopupModify">
      <div class="bg_header px-5 pt-3">
        <div class="supplieName">取引先名検索</div>
        <b-row class="mt-3">
          <b-col cols="6">
            <b-form-group label="取引先略称" label-for="product_code" class="label_input searchEmployee searchproduct">
              <b-form-input type="text" v-model="dataSearch.torihikiRnmForSearch" id="product_code" @change="changeInput($event)"></b-form-input>
            </b-form-group>
          </b-col>
          <b-col cols="6">
            <b-form-group label="取引先略称カナ" label-for="product_classification" class="label_input searchEmployee searchproduct">
              <b-form-input type="text" v-model="dataSearch.torihikiRknmForSearch" id="product_classification" @change="changeInput($event)"></b-form-input>
            </b-form-group>
          </b-col>
        </b-row>

        <b-row class="mt-4">
          <b-col cols="8" align-self="end" class="searchSupplieBtn mb-3">
            <b-button-group>
              <b-button variant="none" @click="showpopup = false" class="btn-white">戻る</b-button>
              <b-button variant="none" @click="searchSupplier()" class="ml-3 btn-blue">検索する</b-button>
            </b-button-group>
          </b-col>

          <b-col cols="4" align-self="end">
            <div class="clearfix float-right">
              <div class="float-left mr-3">
                <p>表示件数</p>
                <b-form-select
                  v-model="dataSearch.limitNumber"
                  :options="litmit"
                  @change="goSelect($event)"
                  class="input_filter"
                  id="select_limit"
                >
                </b-form-select>
              </div>
            
              <div class="float-left">
                <p>表示順</p>
                <b-form-select
                  v-model="dataSearch.sortField"
                  :options="directions"
                  @change="goSelect($event)"
                  class="input_filter"
                  id="select_field_sort"
                >
                </b-form-select>
              </div>
            </div>
          </b-col>
        </b-row>
      </div>

      <div class="px-5 py-3 supplybody">
        <div class="w_body">
          <h6>取引先名称</h6>
          <div v-if="!dataSuplier || dataSuplier.content.length == 0">
            <hr class="m-0 hr" />
          </div>
        </div>
        
        <b-row class="search_record m-0 px-5 pb-3" ref="listSupplier">
          <b-table-simple v-if="dataSuplier">
            <b-tbody>
              <b-tr v-for="(item, index) in dataSuplier.content" 
                :key="`${item.torihikiCd + index}`" 
                @click="$emit('getSuplier', item)"
              >
                <b-td class="text_overflow"
                  :id="`${item.torihikiCd + index}`"
                  @mouseover.self="handleHover($event,`${item.torihikiCd + index}`)"
                  @mouseleave.self="handleHoverLeave(`${item.torihikiCd + index}`)"
                  v-b-tooltip.hover.top.v-secondary="{title:innerTextToolTip, disabled: true}"
                >
                  {{item["torihiki1Nm"]}} {{item["torihiki2Nm"]}}
                </b-td>
              </b-tr>
            </b-tbody>
          </b-table-simple>
        </b-row>

        <div class="hight_pagination">
          <div v-if="dataSuplier && dataSuplier.content.length > 0">
            <b-pagination
              v-model="pageCurrent"
              :total-rows="dataSuplier.totalElements"
              :per-page="dataSuplier.pageable.pageSize"
              :first-number="pageCurrent == 1"
              :prev-class="{'d-none' : pageCurrent == 1}"
              :last-number="pageCurrent == totalPages || totalPages == 0"
              :next-class="{'d-none' : pageCurrent == totalPages || totalPages == 0}"
              @change="handerNextPage($event)"
            >
            </b-pagination>
          </div>
        </div>
      </div>
    </b-container>
  </b-modal>
</template>

<script>
import SupplierService from "@/services/SupplierService";

export default {
  name: "SearchSupplyDialog",
  data() {
    return {
      showpopup: this.value,
      litmit: [
        {value: 20, text: "20件"},
        {value: 50, text: "50件"}
      ],
      directions: [
        {value: "torihiki1Nm==ASC;torihiki2Nm==ASC", text: "取引先名称(昇順)"},
        {value: "torihiki1Nm==DESC;torihiki2Nm==DESC", text: "取引先名称(降順)"}
      ],
      businessPartner: [],
      dataSearch: {
        torihikiRknmForSearch: "",
        torihikiRnmForSearch: "",
        pageNumber: 1,
        limitNumber: 20,
        sortField: "torihiki1Nm==ASC;torihiki2Nm==ASC"
      },
      dataSuplier: null,
      pageCurrent: 1,
      totalPages: 0,
      textTooltip: " "
    };
  },
  props: {
    value: {
      type: Boolean
    }
  },
  methods: {
    handerNextPage(event){
      this.dataSearch.pageNumber = event;
      this.searchSupplier();
    },
    searchSupplier() {
      let request = {};
      let filterItems = [];

      if (this.dataSearch.torihikiRknmForSearch !== "" && this.dataSearch.torihikiRknmForSearch !== null) {
        this.dataSearch.torihikiRknmForSearch.split("　").join(" ").split(" ").forEach(key => {
          if(key) {
            filterItems.push("torihikiRknmForSearch=='%" + key + "%'");
          }
        });
      }

      if (this.dataSearch.torihikiRnmForSearch !== "" && this.dataSearch.torihikiRnmForSearch !== null) {
        this.dataSearch.torihikiRnmForSearch.split("　").join(" ").split(" ").forEach(key => {
          if(key) {
            filterItems.push("torihikiRnmForSearch=='%" + key + "%'");
          }
        });
      }

      request.pageNumber = this.dataSearch.pageNumber - 1;
      request.limitNumber = this.dataSearch.limitNumber;
      request.sortField = this.dataSearch.sortField;
      request.keyword = filterItems.join(";");
      
      this.onScrollTop();
      SupplierService.searchSuppliers(request).then(res => {
        this.dataSuplier = res.data;
        this.pageCurrent = res.data.number + 1;
        this.totalPages = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      })
    },
    changeInput(text) {
      this.dataSearch.pageNumber = 1;
    },
    goSelect(value) {
      this.dataSearch.pageNumber = 1;
      this.searchSupplier();
    },
    onScrollTop() {
      if (this.$refs.listSupplier) {
        this.$refs.listSupplier.scrollTop = 0;
      }
    },
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
          this.$root.$emit(' bv::enable::tooltip');
          this.$root.$emit('bv::show::tooltip', id);
        }
      }
    },
    innerTextToolTip(){
      return this.textTooltip;
    }
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
  p {
    margin: 0;
    font-size: 0.7rem;
    font-weight: 500;
  }

  .input_filter {
    font-size: 0.75rem;
    font-weight: 700;
    border: 1px solid grey;
    border-radius: 0;
    padding: 0 0.4rem;
    height: 1.5625rem;
  }

  .searchproductPopup {
    padding: 1vh 4vw;
  }

  .w_body {
    width: 37.5rem;
    margin: 0 auto;
  }

  .table {
    width: 37.5rem;
    margin: 0 auto;

    tr {
      cursor: pointer;

      td {
        border-top: 1px solid grey;
        font-size: 1rem;
        padding: 0.25rem 2.25rem;
      }
    }
  }
  .table tr:last-child td{
      border-bottom: 1px solid grey;
  }
  .bg_header {
    background-color: #E5E5E5;
  }

  .hr {
    border-top: 1px solid grey;
  }

  #select_limit {
    width: 5rem;
  }

  #select_field_sort {
    width: 8.9375rem;
  }

  td.text_overflow {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    display: block;
    min-width: 37.5rem;
    max-width: 37.5rem;
  }

</style>

<style lang="scss">

.searchSupply {
  .searchproduct {
    background-color: transparent !important;
  }

  .searchproduct label {
    padding-left: 0 !important;
    width: auto !important;
    padding-right: 0.25rem !important;
    font-size: 1rem !important;
  }

  .hight_pagination {
    height: 2rem;
  }
}
.supplier{
  .modal-dialog{
    max-width: 56.25rem;
  }
  .search_record{
    height: auto !important;
    max-height: 19rem;
  }
  .supplybody{
    height: 25.75rem;
  }
}
</style>