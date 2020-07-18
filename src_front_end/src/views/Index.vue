<template>
  <b-container fluid>
    <b-row class="wrapper_home">
      <b-col offset="1" cols="11">
        <p class="size_item ml_1">－お知らせ－</p>
      </b-col>

      <b-container fluid v-if="!isRequestEmpty()">
        <b-col offset="1" cols="11" v-if="!isTabletDevice && countRequest.create > 0">
          <p class="size_item mb-1">
            作成中の請求が<router-link to="/request/create">{{countRequest.create}}件</router-link>あります。
          </p>
        </b-col>
        <b-col offset="1" cols="11" v-if="!isTabletDevice && countRequest.waitingConfirm > 0">
          <p class="size_item mb-1">
            確認待ちの請求が<router-link to="/request/confirm">{{countRequest.waitingConfirm}}件</router-link>あります。
          </p>
        </b-col>
        <b-col offset="1" cols="11" v-if="!isTabletDevice && countRequest.waitingApply > 0">
          <p class="size_item mb-1">
            申請待ちの請求が<router-link to="/request/apply">{{countRequest.waitingApply}}件</router-link>あります。
          </p>
        </b-col>
        <b-col offset="1" cols="11" v-if="countRequest.waitingApprove > 0">
          <p class="size_item mb-1">
            承認待ちの請求が<router-link to="/request/approve">{{countRequest.waitingApprove}}件</router-link>あります。
          </p>
        </b-col>
        <b-col offset="1" cols="11" v-if="!isTabletDevice && countRequest.waitingConfirmSettlement > 0 && isUserAccountingCharge">
          <p class="size_item">
            決済確認待ちの請求が<router-link to="/request/confirm-settlement">{{countRequest.waitingConfirmSettlement}}件</router-link>あります。
          </p>
        </b-col>
      </b-container>
      <b-container fluid v-else>
        <b-col offset="1" cols="11">
          <p class="size_item">
            お知らせはありません。
          </p>
        </b-col>
      </b-container>
    </b-row>
  </b-container>
</template>

<script>
import { mapState } from "vuex";
import isTablet from "@/helper/isTabletDevice";
import TokenService from "@/services/TokenService";

export default {
  name: "Index",
  data() {
    return {
      url: "login"
    };
  },
  computed: {
    ...mapState("CountRequestModule",[
      "countRequest"
    ]),
    isTabletDevice() {
      return isTablet();
    },
    isUserAccountingCharge() {
      return TokenService.isUserAccountingCharge();
    }
  },
  methods: {
    isRequestEmpty() {
      if (this.isUserAccountingCharge) {
        if (this.countRequest.create === 0 && this.countRequest.waitingConfirm === 0
          && this.countRequest.waitingApply === 0 && this.countRequest.waitingApprove === 0
          && this.countRequest.waitingConfirmSettlement === 0) {
          return true;
        }
      } else {
        if (this.countRequest.create === 0 && this.countRequest.waitingConfirm === 0
          && this.countRequest.waitingApply === 0 && this.countRequest.waitingApprove === 0) {
          return true;
        }
      }

      return false;
    }
  },

};
</script>

<style lang="scss" scoped>
.wrapper_home {
  margin-top: 6.333333%;

  .size_item {
    font-size: 1.25rem;

    a {
      color: #f00;
      text-decoration: underline;
    }
  }

  .ml_1 {
    margin-left: -20px;
  }
}
</style>