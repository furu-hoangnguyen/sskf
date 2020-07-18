<template>
    <ValidationObserver ref="requestForm" v-slot="{errors, invalid, validated}">
        <section :class="['request account-receivable-apply', {validated: validated}, {invalid: invalid}]">
            <div class="request-header" >
                <div class="request-header-container">
                    <div class="title">
                        <h4>販売未収金申請</h4>
                        <div class="number">
                          <label class="application-num">
                            申請番号: {{ requestNumber}}
                          </label>
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
              <b-col md="5" lg="5">
                <InputSskf 
                  id="customer-name" 
                  name="取引先名" 
                  v-model="item.requestModel.torihikiNm"
                  :plaintext="true"
                  :disabled-field="disabled" 
                  :rules="checkRequiredForFieldBasedOnStatusPage('torihikiNm').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('torihikiNm').messages"/>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col :md="item.purpose == 'その他' ? 7 : 4 " :lg="item.purpose == 'その他' ? 7 : 4 " class="pl-0">
                <div class="purpose-container">
                  <SelectSskf 
                    name="用途"
                    id="purpose"
                    class="purpose"
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
                    id="applicant"
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
                  :disabled-field="!isCanInputPaymentOn"
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
            <b-button class="btn-white" variant="none" @click="returnToList"> 決済確認<br>リストに戻る
            </b-button>
            <b-button v-if="isCanEdit"  class="btn-white" variant="none" @click="editRequest">
                編集する
            </b-button>
            <b-button v-if="isCanPrint" class="btn-white" variant="none" @click="print">
                印刷する
            </b-button>
            <b-button v-if="isCanReturnToInProgressList" class="btn-white" variant="none" @click="returnToListInprogress">
                申請一覧<br>に戻る
            </b-button>
            <b-button 
                v-if="isConfirmSettlementUserLogged && item.requestModel.paymentOn && item.requestModel.paymentOn != ''"
                class="btn-blue" variant="none" @click="showCommentModal('settlement')">
                決済確認する
            </b-button>
        </template>
        <template v-else>
            <b-button class="btn-white" variant="none" @click="showWarningLeavePage = true">
                戻る
            </b-button>
            <b-button class="btn-white" variant="none" @click="saveRequest">
                保存する
            </b-button>
        </template>
    </div>
            <CommentSlideDialog
              v-if="isShowPopupComment"
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
            <SearchSupplyDialog
                    v-if="showPopupSupply"
                    v-model="showPopupSupply"
                    @getSuplier="getSupplier"/>
            <SearchProductDialog
                v-if="showPopupSearchProduct"
                v-model="showPopupSearchProduct"
                @getProduct="getProduct" />
            <CommentConfirmRequest
                v-model="isShowCommentModalForSettlement"
                :request-comment="commentForSettlement"
                title="決済確認します。"
                btn-submit-label="決済確認する"
                :step-number="item.requestModel.stepNumber"
                @cancel="closeConfirmCommentPopup"
                @submit="settlement"
            ></CommentConfirmRequest>

            <NormalProcessingDialog v-model="showNormalDialog" title="決済確認しました。"></NormalProcessingDialog>
            <ErrorProcessingDialog v-model="showErrorDialog" :title="messageerror"></ErrorProcessingDialog>
            <LockInEditing v-model="showLockInEditing" :lockUser="item.requestModel.editShainName" :lockStartTime="item.requestModel.startedEditAt"></LockInEditing>
            <TimeoutEditing v-model="showTimeoutEditing"></TimeoutEditing>
            <LeavePageWarning v-model="showWarningLeavePage" @leave-page="backToView"></LeavePageWarning>
        </section>
    </ValidationObserver>
</template>
<script>
import RequestService from '@/services/RequestService';
import AccountReceivablesMixin from "./AccountReceivablesMixin";
import formatDigitNumber from "@/helper/formatDigitNumber";

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
    isCanReturnToInProgressList() {
      return this.statusPage == 15 ||
        this.statusPage == 14 ||
        (this.statusPage == 12 &&
          this.loginUser &&
          this.loginUser.roles && 
          !this.loginUser.roles.includes('confirmSettlementPersons')
        );
    },
    isConfirmSettlementUserLogged() {
      return this.loginUser &&
        this.loginUser.roles &&
        this.loginUser.roles.includes('confirmSettlementPersons');
    },
    disabled() {
      return true; // overwrite in mixin
    },
  },
  created() {
    if(!this.requestCd) {
      this.returnToList();
    } else {
      this.fetchRequest();
    }
  },
  methods:{
    returnToListInprogress() {
        this.$router.push("/request/list");
    },
    returnToList() {
        this.$router.push("/request/confirm-settlement");
    },
        
    closeConfirmCommentPopup(comment) {
        this.commentForSettlement = comment;
        this.isShowCommentModalForSettlement = false;
    },
    showCommentModal(type){
        this.checkUpdatedInfo(() => {
            this.isShowCommentModalForSettlement = true;
        });
    },
    settlement(comment) {
        let requestModel = {
            comment: comment,
            requestCd: parseInt(this.requestCd)
        };
        RequestService.settlementRequest(requestModel)
            .then(response => {
                this.showNormalDialog = true;
            })
            .catch(err => {
                console.log(err);
                this.messageerror = "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。";
                this.showErrorDialog = true;
            })
    },
    formatDigitNumber(num) {
      return formatDigitNumber(num);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "@/assets/scss/pages/_request-detail.scss";
</style>