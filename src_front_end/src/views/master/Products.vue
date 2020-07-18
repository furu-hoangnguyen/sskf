<template>
  <section class="empbody">
    <div class="header">品目マスタ</div>
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
        <table @scroll="fixScrollTable" ref="contentProductMaster">
          <thead>
          <tr>
            <th v-for="(field, index) in fields" :key="index" :class="{ 'has-border' : index == 1 }"
                :style="{left:  parseInt(`${ index < 2 ? fixLeft : 0 }`)  + 'px'}" v-html="field.label">
              {{field.label}}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(product, index) in products" :key="index">
            <td v-for="(field, fieldIndex) in fields" :key="fieldIndex" :class="{ 'has-border' : fieldIndex == 1 }"
                @mouseover.self="handleHover($event,`${products[index].hinmokuCd + fieldIndex}`)"
                @mouseleave.self="handleHoverLeave(`${products[index].hinmokuCd + fieldIndex}`)"
                :id="`${products[index].hinmokuCd + fieldIndex}`" v-b-tooltip.hover.right.v-secondary="{title:innerTextToolTip, disabled: true}"
                :style="{left: parseInt(`${ fieldIndex < 2 ? fixLeft : 0 }`)   + 'px'}">
                <span v-if="field.displayAs === 'datetime'">
                    {{product[field.fieldName] | $_mixins_formatDate("YYYY/MM/DD HH:mm:ss") }}
                </span>
                <span v-else-if="field.displayAs === 'checkbox' && field.fieldName === 'hinmokuCdNk'">
                    <span v-if="product.hinmokuCdNk"></span>
                    <span v-else>
                      <i class='fas fa-check'></i>
                    </span>
                </span>
                <span v-else>
                    {{product[field.fieldName]}}
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
      class='mr-5'
      @change="onPageChanged($event)"
    >
    </b-pagination>
    </div>
    </div>
    
    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件
      </div>
      <b-row no-gutters>
        <b-col cols="6">
          <b-form-group label="品目略称" label-for="product_abbr_name" class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.hinmoku_rnm_for_search" type="text" id="product_abbr_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="品目ｶﾅ名" label-for="product_kana_name" class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.hinmoku_knm_for_search" type="text" id="product_kana_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="ブランド区分" label-for="brand_classification" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="brand_classification" v-model="filter.brand_kbn" :options="brandSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="販売形態区分" label-for="sales_form_classification" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="sales_form_classification" v-model="filter.han_keitai_kbn" :options="hanKeitaiSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="品目区分" label-for="product_classification" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="product_classification" v-model="filter.hinmoku_kbn" :options="hinmokuKbnSelectOptions" >

            </b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="事業コード" label-for="business_code" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="business_code" v-model="filter.jigyo_cd" :options="jigyoSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="規格" label-for="kikaku-search" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input type="number" min="0" step="1" v-model="filter.kikaku"  id="kikaku-search"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="荷姿" label-for="packing_type" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input type="text" id="packing_type" v-model="filter.nisugata_for_search"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="計算入数" label-for="irisu-search" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input type="number" id="irisu-search" v-model="filter.irisu" step="1"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="容量単位" label-for="capacity-search" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input type="text" id="capacity-search" v-model="filter.yoryo_tani"></b-form-input>
          </b-form-group>
        </b-col>

        <b-col cols="6">
          <b-form-group label="生産者価格" label-for="price-search" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input type="number" id="price-search" v-model="filter.mf_yen" step="0.01"></b-form-input>
          </b-form-group>
        </b-col>

        <b-col cols="6">
          <b-form-group label="標準原価(営業)" label-for="standard_cost" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input step="0.01" type="number" id="standard_cost" v-model="filter.hyojyun_yen"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="未収上限" label-for="receivable_limit" class="label_input searchEmployee searchproduct mt-3">
            <b-form-input step="0.01" type="number" id="receivable_limit"  v-model="filter.mishu_limit"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="商品カテゴリ" label-for="product_category" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="product_category" v-model="filter.category_hin_kbn" :options="categoryNameSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="商品サブカテゴリ" label-for="product_sub_category" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="product_sub_category" v-model="filter.category_hinsub_kbn" :options="subCategoryNameSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="商品シリーズ" label-for="product_series" class="label_input searchEmployee searchproduct mt-3">
            <b-form-select id="product_series" v-model="filter.category_series_kbn" :options="categorySeriesNameSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label=" " label-for="include_deleted" class="label_input searchEmployee searchEmployee_checkbox searchproduct mt-3">
            <b-form-checkbox size="lg" id="include_deleted" plain v-model="filter.includeDeleted">削除済みを含める</b-form-checkbox>
          </b-form-group>
        </b-col>
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
import FormatDate from "@/mixins/FormatDate";
import MasterService from "@/services/master/MasterService";
import ProductService from "@/services/ProductService";

export default {
  name: "Products",
  mixins:[FormatDate],
  data() {
    return {
      textTooltip:" ",
      categoryNameSelectOptions: [],
      subCategoryNameSelectOptions: [],
      categorySeriesNameSelectOptions: [],
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterAsText: '',
      products: [],
      filter: {
        hinmoku_rnm_for_search: '',
        hinmoku_knm_for_search: '',
        brand_kbn: '',
        han_keitai_kbn: '',
        hinmoku_kbn: '',
        jigyo_cd: '',
        kikaku: null,
        nisugata_for_search: '',
        irisu: null,
        yoryo_tani: '',
        mf_yen: null,
        hyojyun_yen: null,
        mishu_limit: null,
        category_hin_kbn: '',
        category_hinsub_kbn: '',
        category_series_kbn: '',
        includeDeleted: false
      },
      request: {
        sortField: 'hinmokuCd==asc',
        pageNumber: 0,
        limitNumber: 20
      },
      showPopupSearch: false,
      fixLeft: 0,
      fields: [ { fieldName: 'hinmokuCd', label: '品目コード', displayAs: 'text'},
        { fieldName: 'hinmokuRnm', label: '品目略称', displayAs: 'text'},
        { fieldName: 'hinmokuKnm', label: '品目カナ名', displayAs: 'text'},
        { fieldName: 'brandKbn', label: 'ﾌﾞﾗﾝﾄﾞ<br />区分', displayAs: 'text'},
        { fieldName: 'hanKeitaiKbn', label: '販売形態<br />区分', displayAs: 'text'},
        { fieldName: 'hinmokuKbn', label: '品目区分', displayAs: 'text'},
        { fieldName: 'jigyoCd', label:'事業コード', displayAs: 'text'},
        { fieldName: 'kikaku', label: '規格', displayAs: 'text'},
        { fieldName: 'nisugata', label: '荷姿', displayAs: 'text'},
        { fieldName: 'irisu', label: '計算入数', displayAs: 'text'},
        { fieldName: 'yoryoTani', label: '容量単位', displayAs: 'text'},
        { fieldName: 'mfYen', label: '生産者価格', displayAs: 'text'},
        { fieldName: 'hyojyunYen', label: '標準原価<br />(営業)', displayAs: 'text'},
        { fieldName: 'mishuLimit', label: '未収上限', displayAs: 'text'},
        { fieldName: 'categoryHinKbn', label: '商品カテゴリ', displayAs: 'text'},
        { fieldName: 'categoryHinsubKbn', label: '商品サブカテゴリ', displayAs: 'text'},
        { fieldName: 'categorySeriesKbn', label: '商品シリーズ', displayAs: 'text'},
        { fieldName: 'hinmokuCdNk', label: '削除<br />ステータス', displayAs: 'checkbox'},
        { fieldName: 'batchUpdateDateHinmoku', label: 'バッチ更新日時<br />(品目マスタ)', displayAs: 'datetime'},
        { fieldName: 'updateDateAw', label: '更新日時(AW)', displayAs: 'datetime'},
        { fieldName: 'updateDateMb', label: '更新日付(MB)', displayAs: 'datetime'}
      ],
      brandSelectOptions: [{ value: '', text: '' }, { value: '1', text: 'NB' }, { value: '2', text: 'PB' }, { value: '3', text: 'OEM' }, { value: '4', text: 'その他' }],
      hanKeitaiSelectOptions: [{ value: '', text: '' }, { value: '0', text: '家庭用' }, { value: '1', text: '業務用' }],
      hinmokuKbnSelectOptions: [{ value: '', text: '' }, { value: '10', text: '製品' }, { value: '11', text: '商品' }, { value: '12', text: '原料' }, { value: '13', text: '包材' }, { value: '14', text: '半製品' }, { value: '15', text: 'セット品' },
                              { value: '19', text: '輸入品' }, { value: '20', text: '運賃' }, { value: '21', text: '訂正' }, { value: '22', text: '消費税' }, { value: '23', text: '値引' }, { value: '24', text: '値増' }, { value: '25', text: '販促費' }, { value: '26', text: '物流費' }],
      jigyoSelectOptions: [{ value: '', text: '' }, { value: '1000', text: '調味料事業' }, { value: '6000', text: '飲料事業' }, { value: '8000', text: 'アグリ事業' }, { value: '9999', text: 'その他' }],
      sorts: [
        {
          field: 'hinmokuCd==asc',
          name: '品目コード(昇順)'
        },
        {
          field: 'hinmokuCd==desc',
          name: '品目コード(降順)'
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
        let elm1 = event.target
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
    },
    getData(){
      this.onScrollTop();
      
      ProductService.listProducts(this.request).then(res => {
        this.products = res.data.content;
        this.products.forEach(product => {
          product.brandKbn = this.brandSelectOptions.find( item => item.value == product.brandKbn).text;
          product.hanKeitaiKbn = this.hanKeitaiSelectOptions.find( item => item.value == product.hanKeitaiKbn).text;
          product.hinmokuKbn = this.hinmokuKbnSelectOptions.find( item => item.value == product.hinmokuKbn).text;
          product.jigyoCd = this.jigyoSelectOptions.find( item => item.value == product.jigyoCd).text;
        });
        this.totalItemsPageable = res.data.totalElements;
        this.totalPage = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      });
    },
    getFilterSelectOptions() {
      this.categoryNameSelectOptions = [{ value: '', text: '' }];
      this.subCategoryNameSelectOptions = [{ value: '', text: '' }];
      this.categorySeriesNameSelectOptions = [{ value: '', text: '' }];

      MasterService.getEmployeeFilterSelectOptions().then(res => {
        res.data.forEach(category => {
          if (category.categoryType == 0) {
            this.categoryNameSelectOptions.push({ value: category.name, text: category.name });
          } else if (category.categoryType == 1) {
            this.subCategoryNameSelectOptions.push({ value: category.name, text: category.name });
          } else if (category.categoryType == 2) {
            this.categorySeriesNameSelectOptions.push({ value: category.name, text: category.name });
          }
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
      this.request.sortField = "hinmokuCd==asc";
      this.request.limitNumber = 20;

      if (this.filter.hinmoku_rnm_for_search) {
        this.filter.hinmoku_rnm_for_search.split('　').join(' ').split(' ').forEach(key => {
          if (key) {
            filterItems.push("hinmokuRnmForSearch=='%" + key + "%'");
          }
        });
        filterText.push('品目略称:' + this.filter.hinmoku_rnm_for_search);
      }
      if (this.filter.hinmoku_knm_for_search) {
        this.filter.hinmoku_knm_for_search.split('　').join(' ').split(' ').forEach(key => {
          if (key) {
            filterItems.push("hinmokuKnmForSearch=='%" + key + "%'");
          }
        });
        filterText.push('品目ｶﾅ名:' + this.filter.hinmoku_knm_for_search);
      }
      if (this.filter.brand_kbn) {
        filterItems.push("brandKbn=='" + this.filter.brand_kbn + "'");
        filterText.push('ブランド区分:' + this.brandSelectOptions.find( item => item.value == this.filter.brand_kbn).text);
      }
      if (this.filter.han_keitai_kbn) {
        filterItems.push("hanKeitaiKbn=='" + this.filter.han_keitai_kbn + "'");
        filterText.push('販売形態区分:' + this.hanKeitaiSelectOptions.find( item => item.value == this.filter.han_keitai_kbn).text);
      }
      if (this.filter.hinmoku_kbn) {
        filterItems.push("hinmokuKbn=='" + this.filter.hinmoku_kbn + "'");
        filterText.push('品目区分: ' + this.hinmokuKbnSelectOptions.find( item => item.value == this.filter.hinmoku_kbn).text);
      }
      if (this.filter.jigyo_cd) {
        filterItems.push("jigyoCd=='" + this.filter.jigyo_cd + "'");
        filterText.push('事業コード: ' + this.jigyoSelectOptions.find( item => item.value == this.filter.jigyo_cd).text);
      }
      if (this.filter.kikaku) {
        filterItems.push("kikaku=='" + this.filter.kikaku + "%'");
        filterText.push('規格: ' + this.filter.kikaku);
      }
      if (this.filter.nisugata_for_search) {
        filterItems.push("nisugataForSearch=='%" + this.filter.nisugata_for_search + "%'");
        filterText.push('荷姿: ' + this.filter.nisugata_for_search);
      }
      if (this.filter.irisu) {
        filterItems.push("irisu=='" + this.filter.irisu + "%'");
        filterText.push('計算入数: ' + this.filter.irisu);
      }
      if (this.filter.yoryo_tani) {
        filterItems.push("yoryoTani=='" + this.filter.yoryo_tani + "%'");
        filterText.push('容量単位: ' + this.filter.yoryo_tani);
      }
      if (this.filter.mf_yen) {
        filterItems.push("mfYen=='" + this.filter.mf_yen + "'");
        filterText.push('生産者価格: ' + this.filter.mf_yen);
      }
      if (this.filter.hyojyun_yen) {
        filterItems.push("hyojyunYen=='" + this.filter.hyojyun_yen + "'");
        filterText.push('標準原価(営業): ' + this.filter.hyojyun_yen);
      }
      if (this.filter.mishu_limit) {
        filterItems.push("mishuLimit=='" + this.filter.mishu_limit + "'");
        filterText.push('未収上限: ' + this.filter.mishu_limit);
      }
      if (this.filter.category_hin_kbn) {
        filterItems.push("categoryHinKbn=='" + this.filter.category_hin_kbn + "'");
        filterText.push('商品カテゴリ: ' + this.categoryNameSelectOptions.find( item => item.value == this.filter.category_hin_kbn).text);
      }
      if (this.filter.category_hinsub_kbn) {
        filterItems.push("categoryHinsubKbn=='" + this.filter.category_hinsub_kbn + "'");
        filterText.push('商品サブカテゴリ: ' + this.subCategoryNameSelectOptions.find( item => item.value == this.filter.category_hinsub_kbn).text);
      }
      if (this.filter.category_series_kbn) {
        filterItems.push("categorySeriesKbn=='" + this.filter.category_series_kbn + "'");
        filterText.push('商品シリーズ: ' + this.categorySeriesNameSelectOptions.find( item => item.value == this.filter.category_series_kbn).text);
      }

      if (this.filter.includeDeleted) {
        filterText.push('削除済みを含める');
      } else {
        filterItems.push("(hinmokuCdNk!='';hinmokuCdNk!=null)");
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
      this.filter.hinmoku_rnm_for_search = '';
      this.filter.hinmoku_knm_for_search = '';
      this.filter.brand_kbn = '';
      this.filter.han_keitai_kbn = '';
      this.filter.hinmoku_kbn = '';
      this.filter.jigyo_cd = '';
      this.filter.kikaku = null;
      this.filter.nisugata_for_search = '';
      this.filter.irisu = null;
      this.filter.yoryo_tani = '';
      this.filter.mf_yen = null;
      this.filter.hyojyun_yen = null;
      this.filter.mishu_limit = null;
      this.filter.category_hin_kbn = '';
      this.filter.category_hinsub_kbn = '';
      this.filter.category_series_kbn = '';
      this.filter.includeDeleted = false;
      this.request.keyword = "hinmokuCdNk!='';hinmokuCdNk!=null";
    },
    resetList() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.sortField = "hinmokuCd==asc";
      this.request.limitNumber = 20;
      this.resetSearchForm();
      this.filterAsText = "";
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
      if (this.$refs.contentProductMaster) {
        this.$refs.contentProductMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.getFilterSelectOptions();
    this.request.keyword = "hinmokuCdNk!='';hinmokuCdNk!=null";
    this.getData();
  }
};
</script>

<style lang='scss' scoped>
.filter-as-text {
  width: 65%;
}
.btn-filter {
  padding: 0.7rem 2rem;
  width: 10rem;
  height: 4rem;
  margin: 0 1vw;
}

.employee {
  .filter {
    height: auto;
    padding-right: 0.7rem;
    
    select {
      border-radius: 0;
      border-color: #03030399;
    }
  }
}

/* .controller {
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
 */
.main-table {
  table {
    tr {
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