<template>
  <div class='mt-4'>
    <section>
      <b-tabs
        content-class="searching-before-inputting_content scrollbar"
        nav-class="searching-before-inputting_header"
      >
        <b-tab >
          <template v-slot:title>
            作成中
            <br />
            ({{ requestInfo.totalElements }})
          </template>

          <collapse class="px-4">
            <template slot="collapse-header">
              <span class="pl-5">新規作成</span>
            </template>
            <template slot="collapse-body">
              <b-container fluid class="searchAdminPopup searchRequestPopup">
                <div class='body_padding_inputting_content_create searchRequestPopupModify'>
                  <b-row>
                    <b-col cols="3">
                      <h5>請求タイプ</h5>
                      <v-select
                        v-model="typeName"
                        class="table_requestList main-vselect"
                        :options='searchType'
                        :reduce='searchType => searchType.name'
                        label='name'
                      >
                        <template #no-options="{}">
                          検索結果はありません
                        </template>
                      </v-select>
                    </b-col>
                    <b-col cols="3" align-self="end">
                      <b-button
                        variant="secondary"
                        @click="showPopupSupply = !showPopupSupply"
                        class="searchSupplyName float-right btn_display_supplier"
                        :disabled='typeName === null'
                      >
                        取引先名検索
                      </b-button>
                    </b-col>
                    <b-col cols="4">
                      <h5>取引先名</h5>
                      <b-input readonly
                        :value="suppliersObject" class="text_overflow"
                        :id="hashCode(suppliersObject)"
                        @mouseover.self="handleHoverInput($event,`${hashCode(suppliersObject)}`)"
                        @mouseleave.self="handleHoverLeave(`${hashCode(suppliersObject)}`)"
                        v-b-tooltip.hover.top.v-secondary="{title:innerTextToolTip, disabled: true}"
                      >
                      </b-input>
                    </b-col>
                  </b-row>
                </div>
                <b-row :class="{'supplier_table_create tablerequestlist customScrollBar': true, 'mt-4': requestListReuse && requestListReuse.length > 0}"
                  @scroll="handleScroll"
                  ref="requestListReuseContent"
                >
                  <b-table-simple v-if="requestListReuse && requestListReuse.length > 0" class='m-0'>
                    <b-tbody>
                      <b-tr v-for="(item, index) in requestListReuse" :key="index">
                        <b-td v-if='item.mstRequestTypesResponse.name === `販売未収金` && item.requestAccountsReceivablesResponse' class="w_td_1">
                          {{$moment(item['requestAccountsReceivablesResponse']['targetOn']).format('YYYY年MM月分')}}
                        </b-td>
                        <b-td v-else-if='item.mstRequestTypesResponse.name === `展示会・協賛` && item.requestExhibitionPromotionsResponse'
                          class="w_td_1 text_overflow max_width_1"
                          :id="hashCode(item.requestExhibitionPromotionsResponse.subject)"
                          @mouseover.self="handleHover($event,`${hashCode(item.requestExhibitionPromotionsResponse.subject)}`)"
                          @mouseleave.self="handleHoverLeave(`${hashCode(item.requestExhibitionPromotionsResponse.subject)}`)"
                          v-b-tooltip.hover.top.v-secondary="{title:innerTextToolTip, disabled: true}"
                        >
                          {{ item.requestExhibitionPromotionsResponse.subject }}
                        </b-td>
                        <b-td v-else-if='item.mstRequestTypesResponse.name === `マネキン` && item.requestMannequinPromotionsResponse'
                          class="w_td_1 text_overflow max_width_1"
                          :id="hashCode(item.requestMannequinPromotionsResponse.subject)"
                          @mouseover.self="handleHover($event,`${hashCode(item.requestMannequinPromotionsResponse.subject)}`)"
                          @mouseleave.self="handleHoverLeave(`${hashCode(item.requestMannequinPromotionsResponse.subject)}`)"
                          v-b-tooltip.hover.top.v-secondary="{title:innerTextToolTip, disabled: true}"
                        >
                          {{ item.requestMannequinPromotionsResponse.subject }}
                        </b-td>

                        <b-td class="w_td_2 text-left pr-0">合計金額 :</b-td>
                        <b-td class="w_td_2 text-left px-0">{{$_mixins_formatCurrentcy(item['billingAmount'])}}</b-td>
                        <b-td class="pl-2">
                          <b-button variant="none" :to=" pageReuseRedirect +item.cd + '/reuse' ">
                            <i class='fas fa-recycle reuseIcon'></i>
                          </b-button>
                        </b-td>
                      </b-tr>
                    </b-tbody>
                  </b-table-simple>
                </b-row>
                <b-row class='body_padding_inputting_content_create'>
                  <b-col cols="12">
                    <b-button variant="none" @click="pageOfSearchType()" class="create_new mt-3">新規作成する</b-button>
                  </b-col>
                </b-row>
              </b-container>
            </template>
          </collapse>

          <div class='mt-2'>
            <request-display-and-pagination
              v-bind:requestInfo="requestInfo"
              v-bind:is-update-list="isUpdateList"
              v-on:set-is-update-list="setIsUpdateList"
              v-on:handle-page="handlePage"
            >
            </request-display-and-pagination>
          </div>
        </b-tab>
      </b-tabs>
      <SearchSupplyDialog v-if="showPopupSupply" v-model="showPopupSupply" @getSuplier="getSupplier($event)"/>
    </section>
  </div>
</template>

<script>
import FormatCurrencyMixin from "@/mixins/FormatCurrencyMixin.js";
import WrapperRequest from '@/models/WrapperRequest';
import RequestService from '@/services/RequestService';
import SupplierService from "@/services/SupplierService";
import hashCode from "@/helper/getHashFromString";
import router from "@/router";

export default {
  name: "ResquestListCreate",
  mixins:[ FormatCurrencyMixin ],
  data() {
    return {
      textTooltip: " ",
      showPopupSupply: false,
      searchType: [],
      typeName: null,
      suppliers: null,
      wrapperRequest: new WrapperRequest(7, 0, 'requestedAt', 'asc', 20),
      requestInfo: {
        pageCurrent: 1,
        pageSize: 20,
        totalElements: 0,
        totalPages: 0,
        requestList: []
      },
      isUpdateList: false,
      requestListReuse: []
    };
  },
  methods: {
    getSupplier(event){
      this.showPopupSupply = false;
      this.suppliers = event;
      var now = this.$moment(new Date()).subtract(1,'years').format('YYYY-MM-DD');
      var keyword = "mstRequestTypesEntity.name=='" + this.typeName + "'" +
          ";mstTorihikiCd=='" + event.torihikiCd + "'" +
          ";billingOn>='" + now + "'" +
          ";requestedAt!=null" +
          ";mstRequestStatusesEntity.name!='却下'";
      
      if (this.typeName === '販売未収金') { // Accounts receivables
        keyword += ";requestAccountsReceivablesEntities.cd!=null";
      } else if (this.typeName === '展示会・協賛') { // Exhibition
        keyword += ";requestExhibitionPromotionsEntities.cd!=null";
      } else if (this.typeName === 'マネキン') { // Mannequin
        keyword += ";requestMannequinPromotionsEntities.cd!=null";
      }
      
      var queryString = "keyword=" + keyword + "&sortField=" + "requestedAt==DESC";

      this.onScrollTop();
      SupplierService.getRequestListReuse(queryString).then(res => {
        this.requestListReuse = res.data;
      }).catch(err => {
        console.log(err);
      })
    },
    getRequestType(){
      if(!this.searchType.length){
        RequestService.getTypeSearchBeforeInput()
          .then(res => {
            this.searchType = res.data;
            this.typeName = res.data[0].name
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    handleScroll(event){
      var fixBootm = event.target.scrollHeight - event.target.scrollTop - event.target.clientHeight;
    },
    getRequestList(wrapperRequest) {
      RequestService.getRequestList(wrapperRequest)
        .then(res => {
          this.requestInfo.requestList = res.data.content;
          this.requestInfo.totalElements = res.data.totalElements;
          this.requestInfo.pageSize = res.data.pageable.pageSize;
          this.requestInfo.pageCurrent = res.data.number + 1;
          this.requestInfo.totalPages = res.data.totalPages;
          this.isUpdateList = true;
        })
        .catch(err => {
          console.log('getRequestList-Create: ' + err);
        });
    },
    setIsUpdateList(value) {
      this.isUpdateList = value;
    },
    handlePage(event) {
      this.wrapperRequest.page = event - 1;
      this.getRequestList(this.wrapperRequest);
    },
    goReuseAccountReceivable(cd){
      return '/account-receivables/create/'+ cd + '/?mode=reuse'
    },
    onScrollTop() {
      if (this.$refs.requestListReuseContent) {
        this.$refs.requestListReuseContent.scrollTop = 0;
      }
    },
    hashCode(s) {
      return hashCode(s);
    },
    handleHoverLeave(id) {
      if(id !== "0" && id){
        this.$root.$emit('bv::hide::tooltip', id);
        this.$root.$emit('bv::disable::tooltip');
        this.textTooltip = " ";
      }
    },
    handleHoverInput(event, id) {
      if(id !== "0" && id){
        let elm1 = event.target;
        let text = elm1?._value ? elm1?._value : "";
        if ( elm1?.scrollWidth > elm1?.clientWidth) {
          this.textTooltip = text;
          this.$root.$emit(' bv::enable::tooltip');
          this.$root.$emit('bv::show::tooltip', id);
        }
      }
    },
    handleHover(event, id) {
      if(id !== "0" && id){
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
    },
    
    pageOfSearchType() {
      switch (this.typeName) {
        case "販売未収金":
          this.definePath("/account-receivables/create", this.suppliersObject);
          break;
        case "展示会・協賛":
          this.definePath("/exhibition-promotions/create", this.suppliersObject);
          break;
        case "マネキン":
          this.definePath("/mannequin-promotions/create", this.suppliersObject);
          break;
        default:
          router.push("/");
      }
    },
    definePath(path, q) {
      if (q === '' || q === null || q === undefined) {
        router.push({
          path: path
        });
      } else {
        router.push({
          path: path, query: { supplier: q, supplierCd: this.suppliers.torihikiCd}
        });
      }
    }
  },
  computed: {
    suppliersObject() {
      if (this.suppliers) {
        return this.suppliers.torihiki1Nm + this.suppliers.torihiki2Nm ;
      }

      return '';
    },
    pageReuseRedirect(){
      switch (this.typeName) {
        case "販売未収金":
          return "/account-receivables/create/";
        case "展示会・協賛":
          return "/exhibition-promotions/create/" ;
        case "マネキン":
          return "/mannequin-promotions/create/" ;
        default:
          return '';
      }
    },
  },
  created() {
    this.getRequestType();
    this.getRequestList(this.wrapperRequest);
  }
};
</script>

<style lang="scss" scoped>
.table th, .table td {
  border-top-width: 0;
  border-bottom: 1px solid grey;
  font-size: 1rem;
  padding: .5rem;
  font-weight: 400;
}

.supplier_table_create {
  margin: 0 18% 0 15%;
  max-height: 13rem;
  overflow-y: scroll;
  padding-right: 1rem;
  width: 43.75rem;

  .table {
    .w_td_1 {
      width: 28.25rem;
    }

    .w_td_2 {
      width: 4.75rem;
      white-space: nowrap;
    }
  }
}
.table_requestList {
  width: 12.5rem;
  .vs__dropdown-toggle{
    min-height: 2.4rem;
  }
}
.reuseIcon{
  font-size: 1.5rem;
  cursor: pointer;
}

.scrollbar {
  max-height: calc(100vh - 10rem);
  overflow: hidden;
}

.searching-before-inputting_content {
  .searchRequestPopup {
    input {
      border-radius: 0;
      border-width: 0;
      width: 18.75rem;
    }

    .text_overflow {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .max_width_1 {
      max-width: 28.25rem;
    }

    .btn_display_supplier {
      height: 1.875rem;
      padding: 0 0.625rem;
    }
  }

  .body_padding_inputting_content_create {
    padding-left: 4rem;
  }
}

</style>