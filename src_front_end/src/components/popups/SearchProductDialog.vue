<template>
  <b-modal v-model="showpopup" centered modal-class="productDialog" body-class="p-0" hide-header hide-footer body>
    <b-container fluid class="bv-example-row p-0 searchproductPopup">
      <div class="productDialog_header">
        <div class="supplieName">商品コード検索</div>
      <div class="mt-3 d-flex flex-row">
          <b-form-group label="品目コード" label-for="product_code" class="label_input">
            <b-form-input type="text" id="product_code" v-model="dataSearch.hinmokuCd"></b-form-input>
          </b-form-group>
          <b-form-group label="品目区分" label-for="product_classification" class="label_input label_input1">
            <b-select v-model="dataSearch.hinmokuKbn" id="business_code"
                      :options="listProductsClassification"
                      text-field="labelItem"
                      value-field="code"
            ></b-select>
          </b-form-group>
          <b-form-group label="品目カナ名" label-for="product_kananame" class="label_input">
            <b-form-input type="text" id="product_kananame" v-model="dataSearch.hinmokuKnmForSearch"></b-form-input>
          </b-form-group>
      </div>
      <div class="mt-3 d-flex flex-row">
          <b-form-group label="品目略称" label-for="product_abbrname" class="label_input ">
            <b-form-input type="text" id="product_abbrname" v-model="dataSearch.hinmokuRnmForSearch"></b-form-input>
          </b-form-group>
          <b-form-group label="事業コード" label-for="business_code " class="label_input label_input1">
            <b-select v-model="dataSearch.jigyoCd" id="business_code"
                      :options="listBusinessCode"
                      text-field="labelItem"
                      value-field="code"
            ></b-select>

          </b-form-group>
          <b-form-group label="荷姿" label-for="packing_type" class="label_input">
            <b-form-input type="text" id="packing_type" v-model="dataSearch.nisugataForSearch"></b-form-input>
          </b-form-group>
      </div>
      <div class="mt-auto flex-row d-flex h-100">
          <b-button-group>
            <b-button variant="none" @click="showpopup = false" class="btn-white">戻る</b-button>
            <b-button variant="none" @click="searchProduct" class="ml-3 btn-blue">検索する</b-button>
          </b-button-group>
        <div class="ml-auto align-self-end productSort1">
          <p>表示件数</p>
          <b-form-select id="product_classification" v-model="dataSearch.limitNumber" :options="litmit" @change="handleChangeSortField"></b-form-select>
        </div>
        <div class="align-self-end productSort2">
          <p>表示順</p>
          <b-form-select id="product_kananame" v-model="dataSearch.sortField" :options="directions" @change="handleChangeSortField"></b-form-select>
        </div>
      </div>
      </div>
      <b-row class="search_record mt-1 productSearchTableData" >
        <b-table-simple v-if="dataProduct" class="customScrollBar">
          <b-thead>
            <b-tr>
              <b-th v-for="(item,index) in fieldsSearchProduct" :key="index">
                {{item}}
              </b-th>
            </b-tr>
          </b-thead>
          <b-tbody>
            <b-tr v-for="(item,index) in dataProduct.content" :key="index" @click="()=>{$emit('getProduct', item); showpopup = false}">
              <b-td>{{item.hinmokuCd}}</b-td>
              <b-td>{{item.hinmokuRnm}}</b-td>
              <b-td>{{item.hinmokuKbn}}</b-td>
              <b-td>{{item.nisugata}}</b-td>
            </b-tr>
          </b-tbody>
        </b-table-simple>
      </b-row>
    </b-container>
    <div  v-if="dataProduct">
      <b-pagination
              v-model="dataSearch.pageNumber"
              :total-rows="dataProduct.totalElements"
              :per-page="dataProduct.size "
              :first-number="dataSearch.pageNumber == 1"
              :prev-class="{'d-none' : dataSearch.pageNumber == 1}"
              :last-number="dataSearch.pageNumber == dataProduct.totalPages || this.dataProduct.totalPages == 0"
              :next-class="{'d-none' : dataSearch.pageNumber == this.dataProduct.totalPages || this.dataProduct.totalPages == 0}"
              class="mr-4"
              @input="handelNextPage"
      >
        <template v-slot:first-text>
          <i class='fas fa-angle-double-left pagiClass'></i>
        </template>
        <template v-slot:prev-text>
          <i class='fas fa-angle-left pagiClass'></i>
        </template>
        <template v-slot:next-text>
          <i class='fas fa-angle-right pagiClass'></i>
        </template>
        <template v-slot:last-text>
          <i class='fas fa-angle-double-right pagiClass'></i>
        </template>
      </b-pagination>
    </div>
  </b-modal>
</template>
<script>
  import ProductService from "@/services/ProductService";
  export default {
    name: "SearchProductDialog",
    data() {
      return {
        showpopup: this.value,
        litmit: [
          {value: 20, text: '20'},
          {value: 50, text: '50'}

        ],
        directions: [
          {value: 'hinmokuCd==ASC', text: '品目コード(昇順)'},
          {value: 'hinmokuCd==DESC', text: '品目コード(降順)'},
          {value: 'hinmokuRnm==ASC', text: '品目略称(昇順)'},
          {value: 'hinmokuRnm==DESC', text: '品目略称(降順)'}
        ],
        listBusinessCode: [],
        listProductsClassification:[],
        dataSearch: {
          hinmokuCd:'',
          hinmokuKbn:'',
          hinmokuKnmForSearch:'',
          hinmokuRnmForSearch:'',
          jigyoCd:'',
          nisugataForSearch:'',
          pageNumber: 1,
          limitNumber: 20,
          sortField: 'hinmokuCd==ASC'
        },
        hasSort: false,
        dataProduct: null,
        fieldsSearchProduct:['品目コード','品目略称','品目区分','荷姿']
      };
    },
    props: {
      value: {
        type: Boolean
      }
    },
    methods: {
      handleChangeSortField(){
        this.hasSort = true;
      },
      getBusinessCode(){
        ProductService.listBusinessCode()
              .then(res => {
                this.listBusinessCode = res.data;
                this.dataSearch.hinmokuKbn = res.data[0].code
              })
              .catch(err => {
                console.log(err);
              });
      },
      getProductClassification(){
        ProductService.listProductsClassification()
            .then(res => {
              this.listProductsClassification = res.data
              this.dataSearch.jigyoCd = res.data[0].code
            })
            .catch(err => {
              console.log(err);
            });
      },
      searchProduct() {
        let filterItems = [];
        if (this.dataSearch.hinmokuCd) {
          filterItems.push("hinmokuCd=='" + this.dataSearch.hinmokuCd + "%'");
        }

        if (this.dataSearch.hinmokuKnmForSearch) {
          this.dataSearch.hinmokuKnmForSearch.split('　').join(' ').split(' ').forEach(key => {
            if (key) {
              filterItems.push("hinmokuKnmForSearch=='%" + key + "%'");
            }
          });
        }

        if (this.dataSearch.hinmokuRnmForSearch) {
          this.dataSearch.hinmokuRnmForSearch.split('　').join(' ').split(' ').forEach(key => {
            if (key) {
              filterItems.push("hinmokuRnmForSearch=='%" + key + "%'");
            }
          });
        }

        if (this.dataSearch.nisugataForSearch) {
          filterItems.push("nisugataForSearch=='%" + this.dataSearch.nisugataForSearch + "%'");
        }

        if (this.dataSearch.hinmokuKbn) {
          filterItems.push("hinmokuKbn=='" + this.dataSearch.hinmokuKbn + "'");
        }

        if (this.dataSearch.jigyoCd) {
          filterItems.push("jigyoCd=='" + this.dataSearch.jigyoCd + "'");
        }


        var param = {
          pageNumber: this.hasSort ? 0 : (this.dataSearch.pageNumber - 1),
          limitNumber: this.dataSearch.limitNumber,
          sortField: this.dataSearch.sortField,
          keyword : filterItems.join(";")
        };
        ProductService.listProducts(param).then(res => {
          this.dataProduct = res.data;
          this.dataProduct.content.forEach(product => {
            product.hinmokuKbn = this.listProductsClassification.find(cl => cl.code == product.hinmokuKbn).labelItem;
          });
          this.hasSort = false;
        }).catch(err => {
          console.log(err);
        })
      },
      handelNextPage(){
        this.searchProduct();
      },
    },
    computed:{

    },
    watch: {
      value() {
        this.showpopup = this.value;
      },
      showpopup() {
        this.$emit("input", this.showpopup);
      }
    },
    created() {
      this.getBusinessCode();
      this.getProductClassification();
    }
  };
</script>
<style lang="scss" scoped>
  p {
    margin: 0;
    font-size: 0.7rem;
    font-weight: 500;
  }

  .input_filter {
    width: 100%;
    font-size: 0.7rem;
    font-weight: 700;
    border: 1px solid grey;
    border-radius: 0;
    padding: 0 0.4rem;
  }

  .searchproductPopup {
    button{
      padding: 1rem 0;
      text-align: center;
      width: 7.3125rem;
      height: 3.125rem;
      margin-top: 1.5rem;
      line-height: 1;
      &:first-child{
        margin-left: 1rem;
      }
    }
  }
 
/deep/.productDialog{
  .modal-dialog{
    max-width: 56.25rem;
  }
  .search_record{
    max-height: 20.75rem;
    max-width: 51rem;
    margin: 0 auto;
    padding-top: 2rem;
    padding-left: 2rem;
    overflow: hidden;
    table{
      min-height: 20.75rem;
      max-height: 20.75rem;
      max-width: 100%;
      min-width: 100%;
      padding: 0 1.6rem 0 1.6rem;
      overflow-y: auto;
      display: block;
    }
    .table td {
      border-top: 1px solid grey;
      font-size: 1rem;
    }
    table tr{
      cursor: pointer;
    }
    table th, table td{
      padding: 0;
      font-weight: 500;
      width: 7rem;
      min-height: 1.5rem;
      font-size: 1rem;
      vertical-align: middle;
      line-height: 1;
      height: 1.5rem;
      padding: 0 .5rem;
      &:first-child{
        max-width: 7rem;
        min-width: 7rem;
        padding-left: 0;
      }
      &:nth-child(2){
        max-width: 23rem;
        min-width: 23rem;
      }
    }
    table th{
      border: none;
    }
  }
  .productDialog_header{
    height: 13.75rem;
    padding: 1rem 1rem 0 3.75rem;
    background-color: #E5E5E5;
    display: flex;
    justify-content: center;
    flex-direction: column;
    .productSort1 select{
      width: 5rem;
      height:1.5625rem;
      border-radius: 0;
      border: 1px solid black;
      padding-top: 0;
      padding-bottom: 0;
      font-size: .75rem;
    }
    .productSort2 select{
      width: 8.9375rem;
      height: 1.5625rem;
      border-radius: 0;
      border: 1px solid black;
      margin-left: 1rem;
      padding-top: 0; 
      padding-bottom: 0;
      font-size: .75rem;
    }
  }
  
}
</style>