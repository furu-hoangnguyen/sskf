<template>
    <ValidationObserver ref="requestForm" v-slot="{errors, invalid, validated}">
        <section :class="['request account-receivable-apply', {validated: validated}, {invalid: invalid}]">
            <div class="request-header" >
                <div class="request-header-container">
                    <div class="title">
                        <h4>販売未収金申請</h4>
                        <div class="number">
                            <label class="application-num">申請番号: {{item.cd}}</label>
                            <label class="payment-num">決済番号: {{item.requestModel.settlementNumber}} </label>
                        </div>
                        <div v-if="isCanReUse" class="icon">
                            <i class="fas fa-recycle" @click="reuse"></i>
                        </div>
                    </div>
                    <ApproveRequestDialog
                            v-if="item.requestModel.mstApprovalFlows.cd"
                            :is-in-comment-modal="false"
                            :step-number="item.requestModel.stepNumber"
                            :approval-cd="item.requestModel.mstApprovalFlows && item.requestModel.mstApprovalFlows.cd"
                            :request-status="requestStatus"
                            :requested-at="item.requestModel.requestedAt"
                    ></ApproveRequestDialog>
                    <div class="request-header_status">
                        <div v-html="requestStatus"></div>
                    </div>
                </div>
            </div>
            <b-container fluid class="request-body customScrollBar">
        <b-row
                v-if="validated && handleErrors(errors).length > 0"
                class="errors-messages-container"
        >
          <b-col v-for="(error, index) in handleErrors(errors)" :key="index" sm="12">
            <b>エラー：{{ error }}</b>
          </b-col>
        </b-row>
        <!-- 販促金管理項目 -->
        <collapse>
          <template v-slot:collapse-header>販促金管理項目</template>
          <template v-slot:collapse-body>
            <b-row class="px-4 py-1">
              <b-col md="4" lg="4" class="pl-0">
                <DateSskf 
                  v-model="item.targetOn" 
                  valueType="YYYY-MM-DD" 
                  id="targetYear"
                  name="対象年月"
                  :rules="checkRequiredForFieldBasedOnStatusPage('targetOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('targetOn').messages"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM"/>
              </b-col>
              <b-col md="6" lg="6">
                <InputSskf 
                  id="customer-name" 
                  name="取引先名" 
                  v-model="item.requestModel.torihikiNm"
                  :plaintext="true"
                  :disabled-field="disabled" 
                  :rules="checkRequiredForFieldBasedOnStatusPage('torihikiNm').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('torihikiNm').messages"/>
              </b-col>
              <b-col lg="2" class="p-0" v-if="mode=='edit'">
                <b-button variant="none" class="btn_grey btn_getSupply" @click="showPopupSupply=true">取引先名検索</b-button>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="5" :lg="item.purpose == 'その他' ? 7 : 4 " class="pl-0">
                <div class="purpose-container">
                  <SelectSskf 
                    name="用途"
                    id="purpose"
                    v-model="item.purpose"
                    :isNormalSelect="true"
                    :options="useTypesList" 
                    :disabled-field="disabled"
                    :rules="checkRequiredForFieldBasedOnStatusPage('purpose').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('purpose').messages">
                  </SelectSskf>
                  <InputSskf
                    v-if="item.purpose == 'その他'"
                    id="other-purpose"
                    class="other-purpose"
                    v-model="item.purposeOfOthers"
                    :disabled-field="disabled" 
                    :rules="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').messages"/>
                </div>
              </b-col>
              <b-col md="5" lg="5">
                <SelectSskf 
                  name="内口銭/外口銭"
                  id="inner_outner"
                  v-model="item.commissionType"
                  :isNormalSelect="true"
                  :options="commissionTypeList"
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('commissionType').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('commissionType').messages">
                </SelectSskf>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="5" lg="4" class="pl-0">
                <InputNumbericSskf
                  separator="," 
                  name="請求金額(円)"
                  id="request_billing_amount"
                  v-model="item.requestModel.billingAmount"
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('billingAmount').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('billingAmount').messages"/>
              </b-col>
              <b-col md="5" lg="5">
                <DateSskf
                  id="billing_date"
                  v-model="item.requestModel.billingOn"
                  name="請求日"
                  valueType="YYYY-MM-DD"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM/DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('billingOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('billingOn').messages"/>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="5" lg="4" class="pl-0">
                <SelectSskf
                  v-if="item.requestModel.approvalFlowDetailsRequest"
                  name="部門名"
                  id="department"
                  v-model="item.requestModel.approvalFlowDetailsRequest.bumonCd"
                  :isNormalSelect="false"
                  :options="departmentList"
                  options-label="bumonNm"
                  options-value="bumonCd"
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.bumonCd').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.bumonCd').messages">
                </SelectSskf>
              </b-col>
              <b-col md="5" lg="5">
                  <SelectSskf
                    v-if="item.requestModel.approvalFlowDetailsRequest"
                    name="申請者名"
                    id="department"
                    v-model="item.requestModel.approvalFlowDetailsRequest.shainCd"
                    :isNormalSelect="false"
                    :reduce="text => text.value"
                    :options="applicationList"
                    options-label="shainNm"
                    options-value="shainCd"
                    :disabled-field="disabled"
                    :rules="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.shainCd').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.shainCd').messages">
                  </SelectSskf>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="12" lg="12" class="pl-0">
                <TextareaSskf
                  id="remarks"
                  name="備考"
                  v-model="item.remarks"
                  :row='3' 
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('remarks').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('remarks').messages"/>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <UploadFile
                :files="item.requestModel.fileModelList"
                :disabled="disabled"
                :request-status="requestStatus"
                @set-attach-file-for-request="setAttachFiles"
                @remove-file="removeAttachFile" />
            </b-row>
          </template>
        </collapse>
        <!-- 支払い情報 -->
        <collapse>
          <template v-slot:collapse-header>支払い情報</template>
          <template v-slot:collapse-body>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <SelectSskf
                  id="place_payment"
                  name="支払場所"
                  v-model="item.requestModel.paymentPlace"
                  :isNormalSelect="false"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentPlace').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentPlace').messages"
                  :options="paymentPlaceList"
                  options-label="bumonNm"
                  options-value="bumonNm"
                  :disabled-field="disabled"/>
              </b-col>
              <b-col md="4" lg="4">
                <SelectSskf
                  id="method_payment"
                  name="支払い方法"
                  v-model="item.requestModel.paymentMethod"
                  :isNormalSelect="true"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').messages"
                  :options="paymentMethodList"
                  :disabled-field="disabled"/>
              </b-col>
              <b-col v-if="item.requestModel.paymentMethod == 'その他'" md="4" lg="4" class="pl-0 other-payment-method">
                <InputSskf
                  class="fieldDependent"
                  name=""
                  id="method_payment_ther"
                  v-model="item.requestModel.paymentOtherMethod"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').messages"
                  :maxlength="200"
                  :disabled-field="disabled"/>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <DateSskf
                  id="payment_schedule_date"
                  name="支払予定日"
                  v-model="item.requestModel.scheduledPaymentOn"
                  valueType="YYYY-MM-DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').messages"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM/DD"/>
              </b-col>
              <b-col md="4" lg="4">
                <DateSskf
                  id="payment_date"
                  name="支払日"
                  v-model="item.requestModel.paymentOn"
                  valueType="YYYY-MM-DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').messages"
                  :disabled-field="disabled ||
                    !(statusPage == 9 && loginUser.bumonCd == 4000 && item.requestModel.stepNumber > 3) ||
                    !((statusPage == 11 || statusPage == 13) && loginUser.bumonCd == 4000)
                  "
                  formatDate="YYYY/MM/DD"/>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="7" lg="7" class="pl-0">
                <InputSskf
                  id="input-payment_paymentDestination"
                  name="支払先" 
                  v-model="item.requestModel.paymentDestination"
                  :disabled-field="disabled"
                  :maxlength="40"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').messages"/>
              </b-col>
              <b-col md="5" lg="5">
                <InputSskf
                  id="input-payment_bankName"
                  name="銀行名" 
                  v-model="item.requestModel.bankName"
                  :disabled-field="disabled"
                  :maxlength="16"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').messages"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <InputSskf
                  id="bankAccountNumber"
                  name="口座番号"
                  v-model="item.requestModel.bankAccountNumber"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountNumber').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountNumber').messages"
                  :maxlength="10"
                  :disabled-field="disabled" />
              </b-col>
              <b-col md="8" lg="8">
                <InputSskf 
                  id="bankAccountName"
                  name="口座名義"
                  v-model="item.requestModel.bankAccountName"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountName').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountName').messages"
                  :maxlength="40"
                  :disabled-field="disabled" />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="8" class="pl-0">
                <table class="table_payment table_payment_1">
                  <thead>
                    <tr>
                      <th>支払金額</th>
                      <th>8%対象品目(円)</th>
                      <th>10%対象品目(円)</th>
                      <th>合計金額(円)</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>品目</td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotalForEightPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotalForEightPercent != item.requestModel.initialItemTotalForEightPercent}]"
                          v-model="item.requestModel.itemTotalForEightPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForEightPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForEightPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotalForTenPercent != item.requestModel.initialItemTotalForTenPercent}]"
                          v-model="item.requestModel.itemTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotal != item.requestModel.initialItemTotal}]"
                          v-model="item.requestModel.itemTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                    </tr>
                    <tr>
                      <td>消費税</td>
                      <td>
                       <InputNumbericSskf
                          id="consumptionTaxTotalForEightPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotalForEightPercent != item.requestModel.initialConsumptionTaxTotalForEightPercent}]"
                          v-model="item.requestModel.consumptionTaxTotalForEightPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForEightPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForEightPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotalForTenPercent != item.requestModel.initialConsumptionTaxTotalForTenPercent}]"
                          v-model="item.requestModel.consumptionTaxTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotal != item.requestModel.initialConsumptionTaxTotal}]"
                          v-model="item.requestModel.consumptionTaxTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                    </tr>
                    <tr>
                      <td>合計金額</td>
                      <td>
                        <InputNumbericSskf
                          id="totalForEightPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.totalForEightPercent != item.requestModel.initialTotalForEightPercent}]"
                          v-model="item.requestModel.totalForEightPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForEightPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForEightPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="totalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.totalForTenPercent != item.requestModel.initialTotalForTenPercent}]"
                          v-model="item.requestModel.totalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="total"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.total != item.requestModel.initialTotal}]"
                          v-model="item.requestModel.total"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.total').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.total').messages"
                          separator=","
                          :disabled-field="disabled"/>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </b-col>
            </b-row>
          </template>
        </collapse>
        <!-- 販売未収条件 -->
        <collapse>
          <template v-slot:collapse-header>販売未収条件</template>
          <template v-slot:collapse-body>
            <data-table
              v-if="!disabled || !item.detailsForAccountsReceivablesRequestList || item.detailsForAccountsReceivablesRequestList.length > 0"
              key-list="detailsForAccountsReceivablesRequestList"
              :columns="salesUnpaidColumns"
              :data="item.detailsForAccountsReceivablesRequestList"
              :footer-data="sumAccruedAmount(item.detailsForAccountsReceivablesRequestList)"
              :disabled="disabled"
              :initial-max-item-number="1000"
              :is-locked="disabled"
              :request-status="requestStatus"
              @add-item="addItem"
              @copy-item="copyItem"
              @remove-item="removeItem"
            ></data-table>
            <p v-else>登録なし</p>
          </template>
        </collapse>
        <!-- 税8%販促費 -->
        <collapse>
          <template v-slot:collapse-header>税8%販促費</template>
          <template v-slot:collapse-body>
            <data-table
              v-if="!disabled || !item.detailsForPromotionalExpensesRequestTax8PercentList || item.detailsForPromotionalExpensesRequestTax8PercentList.length > 0"
              key-list="detailsForPromotionalExpensesRequestTax8PercentList"
              :columns="tax8PerctgSalesPromotionColumns"
              :data="item.detailsForPromotionalExpensesRequestTax8PercentList"
              :footer-data="sumAccruedAmount(item.detailsForPromotionalExpensesRequestTax8PercentList)"
              :disabled="disabled"
              :initial-max-item-number="2000"
              :is-locked="disabled"
              :request-status="requestStatus"
              @add-item="addItem"
              @copy-item="copyItem"
              @remove-item="removeItem"
            ></data-table>
            <p v-else>登録なし</p>
          </template>
        </collapse>
        <!-- 税10%販促費 -->
        <collapse>
          <template v-slot:collapse-header>税10%販促費</template>
          <template v-slot:collapse-body>
            <data-table
              v-if="!disabled || !item.detailsForPromotionalExpensesRequestTax10PercentList || item.detailsForPromotionalExpensesRequestTax10PercentList.length > 0"
              key-list="detailsForPromotionalExpensesRequestTax10PercentList"
              :columns="tax8PerctgSalesPromotionColumns"
              :data="item.detailsForPromotionalExpensesRequestTax10PercentList"
              :footer-data="sumAccruedAmount(item.detailsForPromotionalExpensesRequestTax10PercentList)"
              :disabled="disabled"
              :initial-max-item-number="3000"
              :is-locked="disabled"
              :request-status="requestStatus"
              @add-item="addItem"
              @copy-item="copyItem"
              @remove-item="removeItem"
            ></data-table>
            <p v-else>登録なし</p>
          </template>
        </collapse>
        <!-- 物流費 -->
        <collapse>
          <template v-slot:collapse-header>物流費</template>
          <template v-slot:collapse-body v-if="item.detailsForPromotionalExpensesRequestLogisticFeesList">
            <data-table
              v-if="!disabled || !item.detailsForPromotionalExpensesRequestLogisticFeesList || item.detailsForPromotionalExpensesRequestLogisticFeesList.length > 0"
              key-list="detailsForPromotionalExpensesRequestLogisticFeesList"
              :columns="tax8PerctgSalesPromotionColumns"
              :data="item.detailsForPromotionalExpensesRequestLogisticFeesList"
              :footer-data="sumAccruedAmount(item.detailsForPromotionalExpensesRequestLogisticFeesList)"
              :disabled="disabled"
              :initial-max-item-number="4000"
              :is-locked="disabled"
              :request-status="requestStatus"
              @add-item="addItem"
              @copy-item="copyItem"
              @remove-item="removeItem"
            ></data-table>
            <p v-else>登録なし</p>
          </template>
        </collapse>

        <FlowHistory></FlowHistory>

      </b-container>
    <div class="request-footer btnForRequest">
        <template v-if="mode != 'edit'">
            <b-button class="btn-white" variant="none" @click="returnToList"> 申請一覧に<br>戻る
            </b-button>
            <b-button v-if="isCanPrint" class="btn-white" variant="none" @click="print">
                印刷する
            </b-button>
        </template>
    </div>
    <CommentSlideDialog
      v-if="isShowPopupComment && selectedItemNumber"
      v-model="isShowPopupComment"
      :initComment="initComment"
      :mainItemId="commentRecordCd"
      :mainItemNumber="selectedItemNumber ? selectedItemNumber.itemNumber : null" 
      :request-status="requestStatus"
      :is-request-send-back="isRequestSendBack"
      :is-first-time-apply="isFirstTimeApply"
      :step-number="item.requestModel.stepNumber"
      :store-g-cd="selectedItemNumber.storeGCd"
      @refer-to-item="referToItem"
      @onAddedComment="onAddedComment"/>
  </section>
</ValidationObserver>
</template>
<script>
import RequestService from '@/services/RequestService';
import AccountReceivablesMixin from "./AccountReceivablesMixin";
export default {
  name: "AccountReceivablesConfirmSettlement",
  mixins: [AccountReceivablesMixin],
  data() {
    return {
      isShowCommentModalForSettlement: false,
      commentForSettlement: ""
    };
  },
  computed: {
    disabled() {
      return true;
    }
  },
  created() {
    if(!this.requestCd) {
      this.returnToList();
    } else {
      this.fetchRequest();
    }
  },
    methods:{
        returnToList() {
            this.$router.push("/request/list");
        },
    }
};
</script>
<style lang="scss" scoped>
    .request {
        height: 100%;
        .request-header {
            width: 100%;
            height: 4.1875rem;
            z-index: 1;
            background-color: rgba(197, 194, 194, 0.397);
            border-bottom: 1px solid #00000099;
            .request-header-container {
                position: relative;
                display: flex;
                flex-wrap: nowrap;
                padding-left: 1.5625rem;
                align-items: center;
                height: 100%;
                .title {
                    flex-grow: 1;
                    display: inline-flex;
                    align-items: center;
                    h4 {
                        font-size: 1.25rem;
                        font-weight: 700;
                    }
                    div.number {
                        margin-left: 1.8rem;
                        .application-num,
                        .payment-num {
                            text-align: left;
                            font-size: 0.75rem;
                            color: rgb(33, 37, 41);
                            display: block;
                        }
                    }
                    div.icon{
                        margin-left: 1rem;
                        i {
                            font-size: 1.25rem;
                        }
                    }
                }
                .request-header_status {
                    margin-right: 1rem;
                    margin-top: 1rem;
                    align-self: flex-start;
                    width: 5rem;
                    height: 5rem;
                    max-width: 5rem;
                    max-height: 5rem;
                    font-size: 1rem;
                    font-weight: 700;
                    text-align: center;
                    background: #fff;
                    color: #44A5CC;
                    border: 2px solid #44A5CC;
                    border-radius: 50%;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 9;
                    & > div {
                        white-space: normal;
                    }
                }
            }
        }
        .request-body{
            padding-left: 1.5625rem;
            padding-bottom: 3rem;
            overflow: auto;
            height: 100%;
            max-height: calc(100% - 10rem);
            .errors-messages-container {
                color: #df6561;
            }
            .purpose-container {
                display: flex;
                width: 100%;
                max-width: 100%;
                justify-content: space-between;
                & > * {
                    flex: 1 0 50%;
                    max-width: 100%;
                    &.other-purpose {
                        /deep/ .form-group.label_input {
                            height: 100%;
                            & > div {
                                width: 100%;
                                input {
                                    height: 100%;
                                }
                            }
                        }
                    }
                }
            }
            .other-payment-method {
                margin-left: -15px;
            }
            .table_payment {
                td {
                    padding: 0;
                    /deep/ input {
                        padding: 0 0.5rem;
                    }
                }
            }
            /deep/ .label_input {
                .mx-datepicker .mx-input-wrapper {
                    position: unset;
                }
                .vs__selected-options {
                    display: flex;
                    flex-wrap: nowrap;
                }
                input:disabled {
                    color: #030303 !important;
                    cursor: not-allowed;
                }
                .form-control-plaintext{
                    padding: 0.375rem 0.75rem;
                    background: #fff;
                    outline: none;
                }
            }
            /deep/ .table-container .table > tbody > tr > td[data-field="hinmokuRnm"] {
                min-width: 12.5rem !important;
            }
        }
        .request-footer.btnForRequest {
            display: flex;
            .btn {
                padding: .3rem 1rem;
                margin-left: 2rem;
                width: auto;
                height: auto;
            }
        }
        .change-value {
            background: #FCE655;
        }
    }
</style>