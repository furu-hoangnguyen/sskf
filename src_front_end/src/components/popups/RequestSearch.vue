<template>
  <b-modal v-model="showPopupSearch" centered hide-header hide-footer modal-class="search_request" content-class="popup_search_request">
    <b-container fluid class="searchproductPopup searchAdminPopup searchRequestPopup searchRequestPopupModify p-0">
      
        <div class="supplieName title_search_request_list">検索条件</div>
        <b-row no-gutters class="mt-3">
          <!--21-->
          <b-col cols="6">
            <b-form-group
              label="申請番号"
              label-for="apply_number"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
            >
              <b-form-input v-model="filterRequest.applyNumber" type="text" id="apply_number"></b-form-input>
            </b-form-group>
          </b-col>
          <!--22-->
          <b-col cols="6">
            <b-form-group
              label="決済番号"
              label-for="settlement_number"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list float-right"
            >
              <b-form-input v-model="filterRequest.settlementNumber" type="text" id="settlement_number"></b-form-input>
            </b-form-group>
          </b-col>
          <!--23-->
          <b-col cols="6">
            <b-form-group
              label="請求タイプ"
              label-for="request_type"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
            >
              <v-select
                id="request_type"
                class="main-vselect"
                v-model="filterRequest.requestType"
                :options="mstRequestTypes"
                :reduce="mstRequestType => mstRequestType.name"
                label="name"
              >
              <template #no-options="{}">
                検索結果はありません
              </template>
              </v-select>
            </b-form-group>
          </b-col>
          <!--24 display screen filter all-->
          <b-col cols="6" v-if="tabActive === 1">
            <b-form-group
              label="関係者"
              label-for="related_person"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive === 1}"
            >
              <b-form-input v-model="filterRequest.relatedPerson" type="text" id="related_person"></b-form-input>
            </b-form-group>
          </b-col>
          <!--25 diplay when screen filter in-process or all or send-back or reject-->
          <b-col cols="6" v-if="tabActive === 0 || tabActive === 1 || tabActive === 2 || tabActive === 3">
            <b-form-group
              label="確認者"
              label-for="confirm_person"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive !== 1 && (tabActive === 0 || tabActive === 2 || tabActive === 3)}"
            >
              <b-form-input v-model="filterRequest.confirmPerson" type="text" id="confirm_person"></b-form-input>
            </b-form-group>
          </b-col>
          <!--26 diplay when screen filter in-process or all or send-back or reject-->
          <b-col cols="6" v-if="tabActive === 0 || tabActive === 1 || tabActive === 2 || tabActive === 3">
            <b-form-group
              label="申請者"
              label-for="applicant"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive === 1}"
            >
              <b-form-input v-model="filterRequest.applicant" class="mainHeigh-2" type="text" id="applicant"></b-form-input>
            </b-form-group>
          </b-col>
          <!--27 diplay when screen filter in-process or all or send-back or reject-->
          <b-col cols="6" v-if="tabActive === 0 || tabActive === 1 || tabActive === 2 || tabActive === 3">
            <b-form-group
              label="承認者"
              label-for="approver"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive === 0 || tabActive === 2 || tabActive === 3}"
            >
              <b-form-input v-model="filterRequest.approver" type="text" id="approver"></b-form-input>
            </b-form-group>
          </b-col>
          <!--28 diplay when screen filter in-process or all-->
          <b-col cols="6" v-if="tabActive === 0 || tabActive === 1">
            <b-form-group
              label="ステータス"
              label-for="status"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive === 1}"
            >
              <v-select
                id="status"
                class="main-vselect"
                v-model="filterRequest.status"
                :options="mstRequestStatuses"
                :reduce="mstRequestStatuse => mstRequestStatuse.name"
                label="name"
              >
                <template #no-options="{}">
                  検索結果はありません
                </template>
              </v-select>
            </b-form-group>
          </b-col>
          <!--29-->
          <b-col cols="6">
            <b-form-group
              label="取引先名"
              label-for="supplier_name"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
              :class="{'float-right': tabActive !== 1 && tabActive !== 0 && tabActive !== 2 && tabActive !== 3 || tabActive == 0}"
            >
              <b-form-input v-model="filterRequest.supplierName" type="text" id="supplier_name"></b-form-input>
            </b-form-group>
          </b-col>
          <!--30-->
          <div class="d-flex">
            <b-form-group
              label="申請日"
              label-for="appliedAtMin_appliedAtMax"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list couple_search_request_list"
            >
              <date-picker
                v-model="filterRequest.appliedAtMin"
                value-type="YYYY/MM/DD HH:mm:ss"
                format="YYYY/MM/DD HH:mm:ss"
                :lang="lang"
              >
              </date-picker>
            </b-form-group>
            <span class="mx-2 span_tilde">~</span>
            <b-form-group class="label_input searchEmployee searchproduct couple_search_request_list">
              <date-picker
                v-model="filterRequest.appliedAtMax"
                value-type="YYYY/MM/DD HH:mm:ss"
                format="YYYY/MM/DD HH:mm:ss"
                :lang="lang"
                @input="addTimeAppliedAtMax"
              >
              </date-picker>
            </b-form-group>
          </div>

          <!--31-->
          <div class="d-flex">
            <b-form-group
              label="支払予定日"
              label-for="paymentScheduledDateMin_paymentScheduledDateMax"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
            >
              <date-picker v-model="filterRequest.paymentScheduledDateMin" value-type="YYYY/MM/DD" format="YYYY/MM/DD" :lang="lang"></date-picker>
            </b-form-group>
            <span class="mx-2 span_tilde">~</span>
            <b-form-group class="label_input searchEmployee searchproduct couple_search_request_list">
              <date-picker v-model="filterRequest.paymentScheduledDateMax" value-type="YYYY/MM/DD" format="YYYY/MM/DD" :lang="lang"></date-picker>
            </b-form-group>
          </div>

          <!--32-->
          <b-col cols="6">
            <b-form-group
              label="停滞日数"
              label-for="number_of_stagnancyDay"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
            >
              <b-form-input v-model="filterRequest.numberOfStagnancyDay" type="text" id="number_of_stagnancyDay"></b-form-input>
            </b-form-group>
          </b-col>

          <!--33-->
          <div class="d-flex">
            <b-form-group
              label="金額"
              label-for="amountMin_amountMax"
              class="label_input searchEmployee searchproduct label_search_request_list couple_search_request_list"
            >
              <b-form-input v-model="filterRequest.amountMin" type="text"></b-form-input>
            </b-form-group>
            <span class="mx-2 span_tilde">~</span>
            <b-form-group class="label_input searchEmployee searchproduct couple_search_request_list">
              <b-form-input v-model="filterRequest.amountMax" type="text"></b-form-input>
            </b-form-group>
          </div>
        </b-row>

        <div class="searchSupplieBtn p-0 btn_controller_search_request_list">
          <div>
            <b-button @click="onReset" variant="none" class="btn-white">リセット</b-button>
            <b-button @click="onSearch" variant="none" class="ml-3 btn-blue btn_search_request_list">検索</b-button>
          </div>
        </div>
     
    </b-container>
  </b-modal>
</template>

<script>
import RequestService from "@/services/RequestService";
import FilterRequest from "@/models/FilterRequest";

export default {
  data() {
    return {
      mstRequestTypes: [],
      mstRequestStatuses: [],
      lang: {
        formatLocale: {
          months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
          // MMM
          monthsShort:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
          // dddd
          weekdays: ["日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"],
          // ddd
          weekdaysShort: ["日", "月", "火", "水", "木", "金", "土"],
          // dd
          weekdaysMin: ["日", "月", "火", "水", "木", "金", "土"],
          // first day of week
          firstDayOfWeek: 0,
          // first week contains January 1st.
          firstWeekContainsDate: 1,
          // format "a", "A"
        }
      }
    }
  },
  props: [ "isSearch", "tabActive", "filterRequest" ],
  computed: {
    showPopupSearch: {
      get: function () {
        return this.isSearch;
      },
      set: function (newValue) {
        this.$emit("set-is-search", newValue);
      }
    }
  },
  methods: {
    onReset() {
      this.$emit("set-filter-request-on-reset", new FilterRequest());
    },
    onSearch() {
      this.$emit("set-filter-request", this.filterRequest);
    },
    getFilterSelect() {
      RequestService.getFilterSelect()
        .then(res => {
          if (res.data) {
            this.mstRequestTypes = res.data.mstRequestTypesResponses;
            this.mstRequestStatuses = res.data.mstRequestStatusesResponses;

            this.mstRequestStatuses.push({
              "cd": 0,
              "name": "差し戻し" ///Send back
            });
          }
        })
        .catch(err => {
          console.log("getRequestRearch: " + err);
        });
    },
    addTimeAppliedAtMax() {
      this.filterRequest.appliedAtMax = this.filterRequest.appliedAtMax.replace("00:00:00", "23:59:59");
    }
  },
  created() {
    this.getFilterSelect();
  }
}
</script>

<style lang="scss">

.search_request {
  .modal-dialog {
    max-width: 56.25rem;
    max-height: 33.75rem;
  }

  .popup_search_request {
    .modal-body {
      padding: 1.875rem 6.25rem;
    }

    .title_search_request_list {
      margin-left: -1.25rem;
    }
  }
}

.searchproductPopup {
  .controller {
    button {
      padding: 1rem 0;
      text-align: center;
      width: 8rem;
    }
  }

  .searchSupplieBtn {
    .btn_search_request_list {
      border: 1px solid #44a5cc;
    }
  }

  .btn_controller_search_request_list {
    margin-top: 1.625rem;
  }

  .label_search_request_list label {
    font-size: 1rem !important;
    width: 5rem !important;
    min-width: 5rem !important;
    padding: 0 !important;
    margin-right: 0.625rem !important;
    margin-top: 0.625rem !important;
  }

  .couple_search_request_list > div {
    width: 12.5rem !important;
    min-width: 12.5rem !important;
    margin-top: 0.625rem !important;
  }

  .d-flex {
    .span_tilde {
      margin-top: 0.625rem !important;
      height: 1.875rem !important;
      padding: 3px 0;
    }
  }
}


</style>