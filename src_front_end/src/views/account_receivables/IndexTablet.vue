<template>
  <ValidationObserver ref="requestForm" v-slot="{errors, invalid, validated}">
    <section :class="[
                      'request account-receivable-apply', 
                      {validated: validated}, 
                      {invalid: invalid},
                      {requestTablet : isTabletDevice}]">
      <div class="request-header" >
        <div class="request-header-container">
          <div class="title">
            <h4>販売未収金申請</h4>
            <div class="number">
              <label class="application-num" v-if="item.requestModel.requestNumberResponse && item.requestModel.requestNumberResponse.cd">
                申請番号: {{ formatDigitNumber(item.requestModel.requestNumberResponse.cd) }}
              </label>
              <label class="application-num" v-else>申請番号:</label>
              <label class="payment-num">決済番号: {{item.requestModel.settlementNumber}} </label>
            </div>
            <div v-if="isCanReUse" class="icon">
              <i class="fas fa-recycle" @click="reuse"></i>
            </div>
          </div>
          <ApproveRequestDialog
            v-if="item.requestModel.mstApprovalFlows && item.requestModel.mstApprovalFlows.cd"
            :is-in-comment-modal="false"
            :step-number="item.requestModel.stepNumber"
            :approval-cd="item.requestModel.mstApprovalFlows && item.requestModel.mstApprovalFlows.cd"
            :request-status="requestStatus"
            :requested-at="item.requestModel.requestedAt"
            :approving-step="approvingStep"
            @set-approving-step="setApprovingStep"
          ></ApproveRequestDialog>
          <div :class="['request-header_status', {'send-back': isRequestSendBack}]">
            <div>
              <span>{{isRequestSendBack ? "差し戻し" : requestStatus}}</span>
              <span v-show="mode == 'edit'"><br/>（編集中）</span>
            </div>
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
              <div class="tablet_lg">
                <DateSskf 
                  v-model="item.targetOn" 
                  valueType="YYYY-MM-DD" 
                  id="targetYear"
                  name="対象年月"
                  :rules="checkRequiredForFieldBasedOnStatusPage('targetOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('targetOn').messages"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM"/>
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="w-100 tablet_xxl">
                <InputSskf 
                  id="customer-name" 
                  name="取引先名" 
                  v-model="item.requestModel.torihikiNm"
                  :plaintext="true"
                  :disabled-field="disabled" 
                  :rules="checkRequiredForFieldBasedOnStatusPage('torihikiNm').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('torihikiNm').messages"/>
              </div>
              <b-col lg="2" class="p-0" v-if="mode=='edit'">
                <b-button variant="none" class="btn_grey btn_getSupply" @click="showPopupSupply=true">取引先名検索</b-button>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
                <div class="purpose-container tablet_lg">
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
                </div>
                <div class="tablet_xl fieldDependent ">
                  <InputSskf
                  v-if="item.purpose == 'その他'"
                  id="other-purpose"
                  class="other-purpose"
                  v-model="item.purposeOfOthers"
                  :disabled-field="disabled" 
                  :rules="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').messages"/>
                </div> 
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_xxl"> 
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
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
                <InputNumbericSskf
                  separator="," 
                  name="請求金額(円)"
                  id="request_billing_amount"
                  v-model="item.requestModel.billingAmount"
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('billingAmount').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('billingAmount').messages"/>
              </div>
              <div class="tablet_lg tablet_marginLeft">
                <DateSskf
                  id="billing_date"
                  v-model="item.requestModel.billingOn"
                  name="請求日"
                  valueType="YYYY-MM-DD"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM/DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('billingOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('billingOn').messages"/>
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
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
              </div>
              <div class="tablet_lg tablet_marginLeft">
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
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_xxl">
                <TextareaSskf
                  id="remarks"
                  name="備考"
                  v-model="item.remarks"
                  :row="3" 
                  :disabled-field="disabled"
                  :rules="checkRequiredForFieldBasedOnStatusPage('remarks').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('remarks').messages"/>
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_xxl">
                <UploadFile
                :files="item.requestModel.fileModelList"
                :disabled="disabled"
                :request-status="requestStatus"
                @set-attach-file-for-request="setAttachFiles"
                @remove-file="removeAttachFile" />
              </div>
            </b-row>
          </template>
        </collapse>
        <!-- 支払い情報 -->
        <collapse>
          <template v-slot:collapse-header>支払い情報</template>
          <template v-slot:collapse-body>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
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
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
                <SelectSskf
                  id="method_payment"
                  name="支払い方法"
                  v-model="item.requestModel.paymentMethod"
                  :isNormalSelect="true"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').messages"
                  :options="paymentMethodList"
                  :disabled-field="disabled"/>
              </div>
              <div v-if="item.requestModel.paymentMethod == 'その他'" class="tablet_xl">
                <InputSskf
                  class="fieldDependent"
                  name=""
                  id="method_payment_ther"
                  v-model="item.requestModel.paymentOtherMethod"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').messages"
                  :maxlength="200"
                  :disabled-field="disabled"/>
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
                <DateSskf
                  id="payment_schedule_date"
                  name="支払予定日"
                  v-model="item.requestModel.scheduledPaymentOn"
                  valueType="YYYY-MM-DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').messages"
                  :disabled-field="disabled"
                  formatDate="YYYY/MM/DD"/>
              </div>
              <div class="tablet_lg tablet_marginLeft">
                <DateSskf
                  id="payment_date"
                  name="支払日"
                  v-model="item.requestModel.paymentOn"
                  valueType="YYYY-MM-DD"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').messages"
                  :disabled-field="disabled || !isCanInputPaymentOn"
                  formatDate="YYYY/MM/DD"/>
              </div>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <div class="tablet_lg">
                <InputSskf
                  id="input-payment_paymentDestination"
                  name="支払先" 
                  v-model="item.requestModel.paymentDestination"
                  :disabled-field="disabled"
                  :maxlength="40"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').messages"/>
              </div>
              <div class="tablet_lg tablet_marginLeft">
                <InputSskf
                  id="input-payment_bankName"
                  name="銀行名" 
                  v-model="item.requestModel.bankName"
                  :disabled-field="disabled"
                  :maxlength="16"
                  :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').rules"
                  :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').messages"
                />
              </div>
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotalForTenPercent != item.requestModel.initialItemTotalForTenPercent}]"
                          v-model="item.requestModel.itemTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').messages"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotal != item.requestModel.initialItemTotal}]"
                          v-model="item.requestModel.itemTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').messages"
                          separator=","
                          :disabled-field="true"/>
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotalForTenPercent != item.requestModel.initialConsumptionTaxTotalForTenPercent}]"
                          v-model="item.requestModel.consumptionTaxTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').messages"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotal != item.requestModel.initialConsumptionTaxTotal}]"
                          v-model="item.requestModel.consumptionTaxTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').messages"
                          separator=","
                          :disabled-field="true"/>
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="totalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.totalForTenPercent != item.requestModel.initialTotalForTenPercent}]"
                          v-model="item.requestModel.totalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').messages"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="total"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.total != item.requestModel.initialTotal}]"
                          v-model="item.requestModel.total"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.total').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.total').messages"
                          separator=","
                          :disabled-field="true"/>
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
        <template v-if="requestStatus.includes('却下')">
          <b-button class="btn-white" variant="none" @click="$router.push('/request/list')"> 
            申請一覧に<br/>戻る
          </b-button>
        </template>
        <template v-else-if="mode != 'edit'">
          <b-button class="btn-white" variant="none" @click="returnToList"> 承認リストに<br>戻る
          </b-button>
          <b-button v-if="isCanEdit"  class="btn-white" variant="none" @click="editRequest">
            編集する
          </b-button>
          <b-button v-if="isCanPrint" class="btn-white" variant="none" @click="print">
            印刷する
          </b-button>
          <b-button v-if="isApproveUserLogged"
            class="btn-blue" variant="none" @click="showCommentModal('sendback')">
            差し戻す
          </b-button>
          <b-button
            v-if="isApplyPersonLogged &&
              isSendBackAndDisplayPC &&
              (statusPage == 7 || statusPage == 8)"
            class="btn-blue" 
            variant="none" 
            @click="showCommentModal('confirm-send-back')">
            差し戻しを確認する
          </b-button>
          <b-button
            v-if="isApproveUserLogged"
            class="btn-blue" variant="none" 
            @click="showCommentModal('reject')">
            却下する
          </b-button>
          <b-button 
            v-if="isApproveUserLogged"
            class="btn-blue" variant="none" @click="showCommentModal('approve')">
            承認する
          </b-button>
        </template>
        <template v-else>
          <b-button class="btn-white" variant="none" @click="exitEditing"> 
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
        v-model="isShowCommentModalForApprove"
        :request-comment="commentForApprove"
        :is-approve="true"
        :request-cd="item.requestModel.cd"
        :request-bumon-cd="item.requestModel.approvalFlowDetailsRequest.bumonCd"
        :request-bumon-name="item.requestModel.approvalFlowDetailsRequest.bumonNm"
        :is-first-time="item.requestModel.requestedAt == null"
        title="右記承認フローで申請します。"
        btn-submit-label="承認する"
        :step-number="item.requestModel.stepNumber"
        :saved-approval-flow="item.requestModel.mstApprovalFlows"
        :approving-step="approvingStep"
        @cancel="closeConfirmCommentPopup"
        @submit="approve"
      ></CommentConfirmRequest>
      <CommentConfirmRequest
        v-model="isShowCommentModalForSendBack"
        :request-comment="commentForSendBack"
        :is-send-back="true"
        :request-cd="item.requestModel.cd"
        :request-bumon-cd="item.requestModel.approvalFlowDetailsRequest.bumonCd"
        :request-bumon-name="item.requestModel.approvalFlowDetailsRequest.bumonNm"
        :is-first-time="item.requestModel.requestedAt == null"
        title="下記に差し戻します。(クリックして変更可能です)"
        btn-submit-label="差し戻す"
        :step-number="item.requestModel.stepNumber"
        :saved-approval-flow="item.requestModel.mstApprovalFlows"
        @cancel="closeConfirmCommentPopup"
        @submit="sendBack"
      ></CommentConfirmRequest>
      <CommentConfirmRequest
        v-model="isShowCommentModalForReject"
        :request-comment="commentForReject"
        title="申請を却下します。"
        btn-submit-label="却下する"
        @cancel="closeConfirmCommentPopup"
        @submit="reject"
      ></CommentConfirmRequest>
      <ConfirmSendBackDialog v-model="showConfimSendBackModal" @submit="confirmSendBack"></ConfirmSendBackDialog>
      <NormalProcessingDialog v-model="showNormalDialog" :title="normalTitleProcess"></NormalProcessingDialog>
      <ErrorProcessingDialog v-model="showErrorDialog" :title="messageerror"></ErrorProcessingDialog>
      <LockInEditing v-model="showLockInEditing" :lockUser="item.requestModel.editShainName" :lockStartTime="item.requestModel.startedEditAt"></LockInEditing>
      <TimeoutEditing v-model="showTimeoutEditing"></TimeoutEditing>
      <LeavePageWarning v-model="showWarningLeavePage" @leave-page="backToView"></LeavePageWarning>
    </section>
  </ValidationObserver>
</template>
<script>
import isTablet from "@/helper/isTabletDevice";
import RequestService from "@/services/RequestService";
import AccountReceivablesMixin from "./AccountReceivablesMixin";
import formatDigitNumber from "@/helper/formatDigitNumber";

export default {
  name: "IndexTablet",
  mixins: [AccountReceivablesMixin],
  data() {
    return {
      approvingStep: 0,
      normalTitleProcess: "承認しました。",
      isShowCommentModalForApprove: false,
      isShowCommentModalForSendBack: false,
      isShowCommentModalForReject: false,
      commentForApprove: "",
      commentForSendBack: "",
      commentForReject: "",
      showConfimSendBackModal: false
    };
  },
  computed:{
    isSendBackAndDisplayPC() {
      return this.item.requestModel.isSentBack == 1 && !isTablet();
    },
    isApproveUserLogged() {
      return this.loginUser &&
        this.loginUser.roles &&
        (
          (this.item.requestModel.stepNumber == 2 &&
            (
              this.loginUser.roles.includes("firstApprovalPersons") ||
              this.loginUser.roles.includes("secondApprovalPersons") ||
              this.loginUser.roles.includes("thirdApprovalPersons") ||
              this.loginUser.roles.includes("settlementApprovalPersons")
            )
          ) ||
          (this.item.requestModel.stepNumber == 3 &&
            (
              this.loginUser.roles.includes("secondApprovalPersons") ||
              this.loginUser.roles.includes("thirdApprovalPersons") ||
              this.loginUser.roles.includes("settlementApprovalPersons")
            )
          ) ||
          (this.item.requestModel.stepNumber == 4 && 
            (
              this.loginUser.roles.includes("thirdApprovalPersons") ||
              this.loginUser.roles.includes("settlementApprovalPersons")
            )
          ) ||
          (this.item.requestModel.stepNumber == 5 &&
            this.loginUser.roles.includes("settlementApprovalPersons")
          )
        );
    },
    isApplyPersonLogged() {
       return this.loginUser &&
        this.loginUser.roles &&
        this.loginUser.roles.includes("applyPersons");
        
    },
    isTabletDevice(){
      return isTablet();
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
      this.$router.push("/request/approve");
    },
    closeConfirmCommentPopup(comment) {
      if(this.isShowCommentModalForApprove) {
        this.commentForApprove = comment;
        this.isShowCommentModalForApprove = false;
      } else if(this.isShowCommentModalForSendBack) {
        this.commentForSendBack = comment;
        this.isShowCommentModalForSendBack = false;
      } else if(this.isShowCommentModalForReject) {
        this.commentForReject = comment;
        this.isShowCommentModalForReject = false;
      }
    },
    showCommentModal(type){
      this.checkUpdatedInfo(type => {
        switch (type) {
          case "approve": {
            this.isShowCommentModalForApprove = true;
            break;
          }
          case "sendback": {
            this.isShowCommentModalForSendBack = true;
            break;
          }
          case "confirm-send-back": {
            this.showConfimSendBackModal = true;
            break;
          }
          case "reject": {
            this.isShowCommentModalForReject = true;
            break;
          }
          default:
            break;
        }
      }, type);
    },
    setApprovingStep(step) {
      this.approvingStep = step;
    },
    sendBack(comment, step) {
      let requestModel = {
        comment: comment,
        requestCd: parseInt(this.requestCd),
        targetStep: step
      };
      RequestService.sendBack(requestModel)
      .then(response => {
        this.normalTitleProcess = "差し戻しをしました。";
        this.showNormalDialog = true;
      })
      .catch(err => {
        console.log(err);
        this.messageerror = "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。";
        this.showErrorDialog = true;
      })
    },
    confirmSendBack() {
      if(!this.requestCd){
        return;
      }
      RequestService.confirmSendBack(parseInt(this.requestCd))
      .then(response => {
        this.normalTitleProcess = "差し戻しを確認しました。";
        this.showNormalDialog = true;
      })
      .catch(err => {
        console.log(err);
        this.messageerror = "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。";
        this.showErrorDialog = true;
      })
    },
    reject(comment) {
      let requestModel = {
        comment: comment,
        requestCd: parseInt(this.requestCd),
        approvingStep: this.approvingStep
      };
      RequestService.reject(requestModel)
      .then(response => {
        this.normalTitleProcess = "却下しました。";
        this.showNormalDialog = true;
      })
      .catch(err => {
        console.log(err);
        this.messageerror = "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。";
        this.showErrorDialog = true;
      })
    },
    approve(comment) {
      let requestModel = {
        approvingStep: this.approvingStep,
        comment: comment,
        requestCd: parseInt(this.requestCd)
      };
      RequestService.approvalRequest(requestModel)
      .then(response => {
        this.normalTitleProcess = "承認しました。";
        this.showNormalDialog = true;
      })
      .catch(err => {
        console.log(err);
        this.messageerror = "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。";
        this.showErrorDialog = true;
      })
    },
    reuse() {
      if(this.item.requestModel.stepNumber >= 2) {
        this.$router.push(`/account-receivables/create/${this.requestCd}/reuse`);
      }
    },
    exitEditing() {
      if(!isTablet()){
        this.showWarningLeavePage = true;
      } else {
        this.backToView();
      }
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