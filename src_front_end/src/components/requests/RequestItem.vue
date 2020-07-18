<template>
  <router-link :to="getLink()">
    <div :class="['d-flex requestListItem',{'requestItem_tablet' : isTabletDevice}]">
      <div class="order-1 requestListItem_col requestListItem_col1">
        <!--10-->
        <p>【{{ item.mstRequestTypesResponse.name }}】</p>
        <p v-if="item.approvalFlowDetailsResponse && !isTabletDevice">
          <!--15-->
          {{ item.approvalFlowDetailsResponse.bumonNm }}
          <!--16-->
          <span class="applicant" v-if="item.approvalFlowDetailsResponse.shainNm">
            {{ item.approvalFlowDetailsResponse.shainNm }}
          </span>
        </p>
        <!--18-->
        <p v-if="item.requestedAt">
          申請日 : {{ item.requestedAt | $_mixins_formatDate("YYYY/MM/DD") }}
        </p>
      </div>

      <div class="order-2 requestListItem_col requestListItem_col2">
        <!--11-->
        <p :id="`torihikiNm${item.cd}`" :ref="`torihikiNm${item.cd}`" :class="{'text_overflow': true, 'd-none': isOverflowTorihikiNm}">
          {{ item.torihikiNm }}
        </p>
        <template v-if="isOverflowTorihikiNm">
          <p class="text_overflow" v-b-tooltip.hover.top="item.torihikiNm">
            {{ item.torihikiNm }}
          </p>
        </template>

        <!--13-->
        <template v-if="item.mstRequestTypesResponse.name === `展示会・協賛`">
          <p :id="`exhibition${item.cd}`" :ref="`exhibition${item.cd}`" v-if="item.requestExhibitionPromotionsResponse" 
            :class="{'text_overflow': true, 'd-none': isOverflowExhibition}">
            {{ item.requestExhibitionPromotionsResponse.subject }}
          </p>

          <template v-if="isOverflowExhibition">
            <p class="text_overflow" v-b-tooltip.hover.top="item.requestExhibitionPromotionsResponse.subject">
              {{ item.requestExhibitionPromotionsResponse.subject }}
            </p>
          </template>
        </template>

        <template v-else-if="item.mstRequestTypesResponse.name === `マネキン`">
          <p :id="`mannequin${item.cd}`" :ref="`mannequin${item.cd}`" v-if="item.requestMannequinPromotionsResponse"
            :class="{'text_overflow': true, 'd-none': isOverflowMannequin}">
            {{ item.requestMannequinPromotionsResponse.subject }}
          </p>

          <template v-if="isOverflowMannequin">
            <p class="text_overflow" v-b-tooltip.hover.top="item.requestMannequinPromotionsResponse.subject">
              {{ item.requestMannequinPromotionsResponse.subject }}
            </p>
          </template>
        </template>
        <template v-else>
          <!--12-->
          <p v-if="item.requestAccountsReceivablesResponse && item.requestAccountsReceivablesResponse.targetOn">
            対象年月: {{ item.requestAccountsReceivablesResponse.targetOn | $_mixins_formatDate("YYYY/MM") }}
          </p>
        </template>
        <template v-if="isTabletDevice">
          <p>
          <!--15-->
          {{ item.approvalFlowDetailsResponse.bumonNm }}
          <!--16-->
          <span class="applicant" v-if="item.approvalFlowDetailsResponse.shainNm">
            {{ item.approvalFlowDetailsResponse.shainNm }}
          </span>
          </p>
        </template>
      </div>

      <div class="order-3 requestListItem_col requestListItem_col3">
        <div :class="['clearfix d-flex flex-row justify-content-flex-end',{'flex-column': isTabletDevice}]">
          <div :class="['float-left',{'d-flex flex-row align-self-end w-100': isTabletDevice} ]">
            <p :class="{'ml-auto': isTabletDevice}">請求金額:</p>
              <!--14-->
            <p v-if="isTabletDevice">{{ $_mixins_formatCurrentcy(item.billingAmount) }}</p>
          </div>

          <div class="w-100 w_fix_3">
            <div class="clearfix d-flex flex-row justify-content-end">
              <div class="mr-auto ml-4" v-if="!isTabletDevice">
                <!--14-->
                <p>{{ $_mixins_formatCurrentcy(item.billingAmount) }}</p>
              </div>

              <div class="float-right">
                <!--19-->
                  <b-button v-if="item.isSentBack === 1" variant="none" class="btn-white btn_width btn_send_back">差し戻し</b-button>
                  <b-button v-else variant="none" class="btn-white btn_width">{{ item.mstRequestStatusesResponse.name }}</b-button>
              </div>
            </div>

            <!--17-->
            <div class="updated_status_at" v-if="item.updatedStatusAt">
              <span v-if="!isTabletDevice">ステータス</span>更新日時 : {{ item.updatedStatusAt | $_mixins_formatDate("YYYY/MM/DD hh:mm") }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script>
import FormatCurrencyMixin from "@/mixins/FormatCurrencyMixin";
import FormatDate from "@/mixins/FormatDate";
import { RequestTypes } from "@/constants/RequestTypes";
import { RequestStatuses } from "@/constants/RequestStatuses";
import isTablet from "@/helper/isTabletDevice";
export default {
  mixins:[FormatCurrencyMixin, FormatDate],
  data () {
    return {
      isOverflowTorihikiNm: false,
      isOverflowExhibition: false,
      isOverflowMannequin: false
    };
  },
  props: [ "item" ],
  computed: {
    requestType() {
      let typeItem = this.item.mstRequestTypesResponse.name;
      return RequestTypes.find(type => {
        return type.label.trim() == typeItem.trim();
      });
    },
    requestStatus() {
      let statusItem = this.item.mstRequestStatusesResponse.name;
      return RequestStatuses.find(status => {
        return status.label.trim() == statusItem.trim();
      });
    },
    isTabletDevice(){
      return isTablet();
    }
  },
  methods: {
    checkOverflow: function() {
      if (this.$refs["torihikiNm" + this.item.cd] && this.$refs["torihikiNm" + this.item.cd].clientWidth < this.$refs["torihikiNm" + this.item.cd].scrollWidth) {
        this.isOverflowTorihikiNm = true;
      }

      if (this.$refs["exhibition" + this.item.cd] && this.$refs["exhibition" + this.item.cd].clientWidth < this.$refs["exhibition" + this.item.cd].scrollWidth) {
        this.isOverflowExhibition = true;
      }

      if (this.$refs["mannequin" + this.item.cd] && this.$refs["mannequin" + this.item.cd].clientWidth < this.$refs["mannequin" + this.item.cd].scrollWidth) {
        this.isOverflowMannequin = true;
      }
    },
    getLink() {
      let mode = this.requestStatus.value == "create" ? "edit" : "view";
      return `/${this.requestType.value}/${this.requestStatus.value}/${this.item.cd}/${mode}`;
    }
  },
  mounted() {
    setTimeout(() => this.checkOverflow(), 200);
  }
};
</script>

<style lang="scss" scoped>
.requestListItem {
  border-top: 2px solid lightgrey;
  padding: 0 1rem;
  text-decoration: none;
  color: #000;
  .requestListItem_col {
    height: 7rem;
    display: flex;
    justify-content: center;
    flex-direction: column;
    overflow: hidden;
    p {
      margin-bottom: 0.3rem;
      font-size: 1rem;
      font-weight: 400;
    }
    .btn_width {
      width: 8rem;
      white-space: nowrap;
    }
    .btn_send_back {
      background-color: #DF6561;
      border-color: #DF6561;
      color: #fff;
    }
  }

  .requestListItem_col1 {
    width: 32%;
    p {
      &:nth-child(1) {
        color: #44a5cc;
      }
      &:nth-child(2),
      &:nth-child(3) {
        font-size: .75rem;
        padding-left: 0.7rem;
      }
      .applicant {
        float: right;
        margin-right: 2rem;
      }
    }
  }
  .requestListItem_col2 {
    width: 32%;

    p {
      font-weight: 700;
      margin-left: 1rem;
    }

    .text_overflow {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
  .requestListItem_col3 {
    width: 35%;
    white-space: nowrap;
    font-size: 1rem;
    text-align: right;
    p {
      font-weight: 700;
      margin-left: 0.75rem;
    }
    .updated_status_at {
      font-weight: 400;
      font-size: 1rem;
      text-align: right;
      white-space: nowrap;
      padding-top: 3px;
    }
    button {
      border-radius: 50px;
      font-weight: 700;
      float: right;
      cursor: default;
      height: 2rem;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .w_fix_3 {
      width: 17.7rem;

      p {
        margin: 0;
      }
    }
  }
}
.requestItem_tablet{
  padding: 1rem 2rem 0 2rem;
  .requestListItem_col1{
    width: 30%;
    line-height: 2.55;
    p {
      &:nth-child(1) {
        font-size: 1.56rem;
        font-weight: 700;
      }
      &:nth-child(2){
        font-size: 1rem;
        padding-left: 0.7rem;
      }
    }
  }
  .requestListItem_col2{
    width: 35%;
    line-height: 1.7;
    p {
      margin: 0;
      font-size: 1.2rem ;
      &:last-child{
        font-size: 1rem;
      }
    }
  }
  .requestListItem_col3{
    width: 35%;
    p {
      margin: 0;
      font-size: 1.2rem ;
      margin-bottom: 2px;
      white-space: nowrap;
      &:first-child{
        width: 5rem;
      }
      &:last-child{
        font-size: 1.5rem;
        line-height: 1;
        width: auto;
        max-width: calc(100% - 5rem);
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
    .resquestMoney{
      width: auto;
      max-width: 67%;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}
.request_item a:hover {
  text-decoration: none !important;
}

@media only screen and (min-width: 1440px) {
  .requestListItem {
    padding: 0 4rem;
  }
}

</style>