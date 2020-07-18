<template>
  <ValidationObserver ref="accountReceivableForm" v-slot="invalid">
    <section :class="['request']">
      <slot name="request-header">
        <div class="request-header" >
          <div class="request-header-container">
            <div class="title">
              <h4>販売未収金申請</h4>
              <div class="number">
                <label class="application-num">申請番号: {{item.cd}}</label>
                <label class="payment-num">決済番号: {{item.requestModel.settlementNumber}} </label>
              </div>
              <div class="icon">
                <i class="fas fa-recycle"></i>
              </div>
            </div>
            <div class="request-header_status">
              <div v-html="requestStatus"></div>
            </div>
          </div>
        </div>
      </slot>
      <slot name="request-body">
        <b-container fluid class="request-body customScrollBar">
          <b-row v-if="errors.length > 0" class="errors-messages-container">
            <b-col
              v-for="(error, index) in errors" 
              :key="index"
              sm="12"
            >
              {{ error }}
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
                    name="対象年"
                    :changed="invalid.changed"
                    :rules="{ required: true }"
                    :messages="{required: '対象年月を確認して下さい'}"
                    :disabled-field="disabled"
                    formatDate="YYYY/MM"/>
                </b-col>
                <b-col md="6" lg="6">
                  <InputSskf 
                    id="customer-name" 
                    name="取引先名" 
                    v-model="item.requestModel.torihikiNm"
                    :plaintext="true"
                    :changed="invalid.changed"
                    :disabled-field="disabled" 
                    :rules="{ required: true, max: 80 }"
                    :messages="{required: '取引先名を確認して下さい', max: '取引先名を確認して下さい'}"/>
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
                      :changed="invalid.changed"
                      :isNormalSelect="true"
                      :options="useTypesList" 
                      :disabled-field="disabled"
                      :rules="{required: true}"
                      :messages="{required: '用途を確認して下さい'}">
                    </SelectSskf>
                    <InputSskf
                      v-if="item.purpose == 'その他'"
                      id="other-purpose"
                      class="other-purpose"
                      v-model="item.purposeOfOthers"
                      :changed="invalid.changed"
                      :disabled-field="disabled" 
                      :rules="{ required: true, max: 36 }"
                      :messages="{required: '用途 その他入力を確認して下さい', max: '用途 その他入力を確認して下さい'}"/>
                  </div>
                </b-col>
                <b-col md="5" lg="5">
                  <SelectSskf 
                    name="内口銭/外口銭"
                    id="inner_outner"
                    v-model="item.commissionType"
                    :changed="invalid.changed"
                    :rules="{required: true}"  
                    :isNormalSelect="true"
                    :options="commissionTypeList"
                    :disabled-field="disabled"
                    :messages="{required: '内口銭/外口銭を確認して下さい'}">
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
                    :rules="{required: true, min_value: 0}"
                    :disabled-field="disabled"
                    :min="0"
                    :max="999999999999"
                    :messages="{required: '請求金額を確認して下さい', min_value: '請求金額を確認して下さい'}"/>
                </b-col>
                <b-col md="5" lg="5">
                  <DateSskf
                    id="billing_date"
                    v-model="item.requestModel.billingOn"
                    name="請求日"
                    valueType="YYYY-MM-DD"
                    :changed="invalid.changed"
                    :rules="{ required: true }"
                    :disabled="disabled"
                    formatDate="YYYY/MM"
                    :messages="{required: '請求日を確認して下さい'}"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="5" lg="4" class="pl-0">
                  <SelectSskf
                    v-if="item.requestModel.approvalFlowDetailsRequest"
                    name="部門名"
                    id="department"
                    v-model="item.requestModel.approvalFlowDetailsRequest.bumonCd"
                    :changed="invalid.changed"
                    :rules="{required: true}"  
                    :isNormalSelect="false"
                    :options="departmentList"
                    options-label="bumonNm"
                    options-value="bumonCd"
                    :disabled-field="disabled"
                    :messages="{required: '請求日を確認して下さい'}">
                  </SelectSskf>
                </b-col>
                <b-col md="5" lg="5">
                    <SelectSskf
                      v-if="item.requestModel.approvalFlowDetailsRequest"
                      name="申請者名"
                      id="department"
                      v-model="item.requestModel.approvalFlowDetailsRequest.shainCd"
                      :changed="invalid.changed"
                      :rules="{required: true}"  
                      :isNormalSelect="false"
                      :reduce="text => text.value"
                      :options="applicationList"
                      options-label="shainNm"
                      options-value="shainCd"
                      :disabled-field="disabled"
                      :messages="{required: '申請者名を確認して下さい'}">
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
                    :rules="{ required: true }"
                    :messages="{required: '備考を確認して下さい'}"/>
                </b-col>
              </b-row>
              <!-- Files -->
              <b-row class="px-4 py-1 attachFile mt-3">
                <UploadFile
                :files="item.requestModel.fileModelList"
                :rules="{ max_value: 100*1024 }"
                :messages="{max_value: 'アップロードファイルを確認して下さい'}"
                @set-attach-file-for-request="setAttachFiles"
                @remove-file="removeAttachFile" />
              </b-row>
            </template>
          </collapse>
          <!-- 支払金額 -->
          <collapse>
            <template v-slot:collapse-header>支払金額</template>
            <template v-slot:collapse-body>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" lg="4" class="pl-0 pr-0">
                  <SelectSskf
                    id="place_payment"
                    name="支払場所"
                    v-model="item.requestModel.paymentPlace"
                    :changed="invalid.changed"
                    :isNormalSelect="false"
                    :rules="{required: true}"
                    :messages="{required: '支払場所を確認して下さい'}"
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
                    :changed="invalid.changed"
                    :isNormalSelect="true"
                    :rules="{required: true}"
                    :messages="{required: '支払い方法を確認して下さい'}"
                    :options="paymentMethodList"
                    :disabled-field="disabled"/>
                </b-col>
                <b-col v-if="item.requestModel.paymentMethod == 'その他'" md="4" lg="4" class="pl-0 other-payment-method">
                  <InputSskf
                    class="fieldDependent"
                    name=""
                    id="method_payment_ther"
                    v-model="item.requestModel.paymentOtherMethod"
                    :rules="{required: true}"
                    :messages="{required: 'その他支払い方法を確認して下さい'}"
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
                    :changed="invalid.changed"
                    :rules="{ required: true }"
                    :messages="{ required: '支払予定日を確認して下さい'}"
                    :disabled-field="disabled"
                    formatDate="YYYY/MM/DD"/>
                </b-col>
                <b-col md="4" lg="4">
                  <DateSskf
                    id="payment_date"
                    name="支払日"
                    v-model="item.requestModel.paymentOn"
                    valueType="YYYY-MM-DD"
                    :changed="invalid.changed"
                    :rules="{ required: true }"
                    :messages="{ required: '支払日を確認して下さい'}"
                    :disabled-field="disabled"
                    formatDate="YYYY/MM/DD"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="7" lg="7" class="pl-0">
                  <InputSskf
                    id="input-payment_paymentDestination"
                    name="支払先" 
                    v-model="item.requestModel.paymentDestination"
                    :changed="invalid.changed"
                    :disabled-field="disabled"
                    :maxlength="40"
                    :rules="{ required: true }"
                    :messages="{ required: '支払先を確認して下さい'}"/>
                </b-col>
                <b-col md="5" lg="5">
                  <InputSskf
                    id="input-payment_bankName"
                    name="銀行名" 
                    v-model="item.requestModel.bankName"
                    :disabled-field="disabled"
                    :maxlength="40"
                    :rules="{ required: true }"
                    :messages="{ required: '銀行名を確認して下さい'}"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" lg="4" class="pl-0 pr-0">
                  <InputSskf
                    id="bankAccountNumber"
                    name="口座番号"
                    v-model="item.requestModel.bankAccountNumber"
                    :rules="{required: true}"
                    :messages="{ required: '口座番号を確認して下さい'}"
                    :maxlength="10"
                    :disabled-field="disabled" />
                </b-col>
                <b-col md="8" lg="8">
                  <InputSskf 
                    id="bankAccountName"
                    name="口座名義"
                    v-model="item.requestModel.bankAccountName"
                    :messages="{ required: '口座名義を確認して下さい'}"
                    :rules="{required: true}"
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
                            class-field="fieldPayment"
                            v-model="item.requestModel.itemTotalForEightPercent"
                            :messages="{ required: '8%対象品目計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="itemTotalForTenPercent"
                            class-field="fieldPayment"
                            v-model="item.requestModel.itemTotalForTenPercent"
                            :messages="{ required: '10%対象品目計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="itemTotal"
                            class-field="fieldPayment"
                            v-model="item.requestModel.itemTotal"
                            :messages="{ required: '品目合計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                      </tr>
                      <tr>
                        <td>消費税</td>
                        <td>
                        <InputNumbericSskf
                            id="consumptionTaxTotalForEightPercent"
                            class-field="fieldPayment"
                            v-model="item.requestModel.consumptionTaxTotalForEightPercent"
                            :messages="{ required: '8%消費税計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="consumptionTaxTotalForTenPercent"
                            class-field="fieldPayment"
                            v-model="item.requestModel.consumptionTaxTotalForTenPercent"
                            :messages="{ required: '10%消費税計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="consumptionTaxTotal"
                            class-field="fieldPayment"
                            v-model="item.requestModel.consumptionTaxTotal"
                            :messages="{ required: '消費税合計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                      </tr>
                      <tr>
                        <td>合計金額</td>
                        <td>
                          <InputNumbericSskf
                            id="totalForEightPercent"
                            class-field="fieldPayment"
                            v-model="item.requestModel.totalForEightPercent"
                            :messages="{ required: '8%対象品目・消費税合計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="totalForTenPercent"
                            class-field="fieldPayment"
                            v-model="item.requestModel.totalForTenPercent"
                            :messages="{ required: '10%対象品目・消費税合計を確認して下さい'}"
                            :rules="{required: true}"
                            separator=","
                            :disabled-field="disabled"/>
                        </td>
                        <td>
                          <InputNumbericSskf
                            id="total"
                            class-field="fieldPayment"
                            v-model="item.requestModel.total"
                            :messages="{ required: '総合計を確認して下さい'}"
                            :rules="{required: true}"
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
                :is-locked="isLocked"
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
                :is-locked="isLocked"
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
                :columns="tax10PerctgSalesPromotionColumns"
                :data="item.detailsForPromotionalExpensesRequestTax10PercentList"
                :footer-data="sumAccruedAmount(item.detailsForPromotionalExpensesRequestTax10PercentList)"
                :disabled="disabled"
                :initial-max-item-number="3000"
                :is-locked="isLocked"
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
                :columns="logicPromotionFeeColumns"
                :data="item.detailsForPromotionalExpensesRequestLogisticFeesList"
                :footer-data="sumAccruedAmount(item.detailsForPromotionalExpensesRequestLogisticFeesList)"
                :disabled="disabled"
                :initial-max-item-number="4000"
                :is-locked="isLocked"
                :request-status="requestStatus"
                @add-item="addItem"
                @copy-item="copyItem"
                @remove-item="removeItem"
              ></data-table>
              <p v-else>登録なし</p>
            </template>
          </collapse>
        </b-container>
      </slot>
      
      <slot name="request-footer">
        <div class="request-footer btnForRequest">
          <template v-if="mode != 'edit'">
            <b-button class="btn-white" variant="none" @click="$router.push('/request/confirm')"> 確認リストに<br>戻る
            </b-button>
            <b-button class="btn-white" variant="none" @click="$router.replace(`/account-receivables/confirm-create/${requestCd}/edit`)">
              編集する
            </b-button>
            <b-button class="btn-blue" variant="none" @click="validateRequest">担当者確認を<br/>完了する
            </b-button>
          </template>
          <template v-else>
            <b-button class="btn-white" variant="none" @click="$router.replace(`/account-receivables/confirm-create/${requestCd}/view`)"> 
              戻る
            </b-button>
            <b-button class="btn-white" variant="none" @click="saveRequest">
              保存する
            </b-button>
          </template>
        </div>
      </slot>
      <CommentSlideDialog
        v-if="isShowPopupComment"
        v-model="isShowPopupComment" 
        :initComment="initComment"
        :mainItemId="commentRecordCd"
        :mainItemNumber="selectedItemNumber ? selectedItemNumber.itemNumber : null" 
        :store-g-cd="selectedItemNumber.storeGCd"
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
        v-model="isShowCommentModal"
        :request-comment="item.requestModel.comment"
        @cancel="closeConfirmCommentPopup"
        @submit="confirmRequest"
      ></CommentConfirmRequest>
      <NormalProcessingDialog v-if="showNormalDialog" title="確認しました。"></NormalProcessingDialog>
      <ErrorProcessingDialog v-model="showErrorDialog" title="エラーが発生しました。<br/>内容を確認し、再度送信をしてください。"></ErrorProcessingDialog>
      <LockInEditing v-model="showLockInEditing" :lockUser="item.requestModel.editShainName" :lockStartTime="item.requestModel.startedEditAt"></LockInEditing>
      <TimeoutEditing v-model="showTimeoutEditing"></TimeoutEditing>
    </section>
  </ValidationObserver>
</template>
<script>
import cloneDeep from "lodash/cloneDeep";
import { mapState, mapActions } from "vuex";
import AccountReceivableCreateService from '@/services/AccountReceivableCreateService';
import CommissionType from "@/constants/CommissionType";
import {
  DetailsForAccountsReceivablesRequest,
  DetailsForPromotionalExpensesRequestLogisticFees,
  DetailsForPromotionalExpensesRequestTax8Percent,
  DetailsForPromotionalExpensesRequestTax10Percent,
  AccountReceivablesRequest
} from "@/models/AccountReceivablesRequestModel";
import getMaxItemList from "@/helper/getMaxItemList";
export default {
  name: "ConfirmCreate",
  data() {
    return {
      item: new AccountReceivablesRequest(),
      isShowPopupComment: false,
      showPopupSupply: false,
      showPopupSearchProduct: false,
      initComment: "",
      commentRecordCd: null,
      selectedItemNumber: null,
      supplier: null,
      errors: [],
      isLocked: false,
      isShowCommentModal: false,
      timeoutForEditRequest: null,
      showNormalDialog: false,
      showErrorDialog: false,
      showLockInEditing: false,
      showTimeoutEditing: false,
      optionsForTable: {}
    };
  },
  computed:{
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser,
      useTypesList: state => state.AccountReceivableCreateModule["useTypesList"],
      commissionTypeList: state => state.AccountReceivableCreateModule["commissionTypeList"],
      departmentList: state => state.AccountReceivableCreateModule["departmentList"],
      applicationList: state => state.AccountReceivableCreateModule["applicationList"],
      paymentMethodList: state => state.AccountReceivableCreateModule["paymentMethodList"],
      paymentPlaceList: state => state.AccountReceivableCreateModule["paymentPlaceList"],
      listChargeName: state => state.AccountReceivableCreateModule["listChargeName"],
      listItem: state => state.AccountReceivableCreateModule["listItem"],
      listClassification: state => state.AccountReceivableCreateModule["listClassification"]
    }),
    requestCd() {
      return this.$route.params.cd;
    },
    requestStatus() {
       let status = this.item.requestModel.mstRequestStatusesResponse.name || '';
       if(this.$route.params.mode == "edit") {
        status += "<br/>（編集中）";
       }
       return status;
    },
    mode() {
      return this.$route.params.mode;
    },
    disabled() {
      return this.$route.params.mode != "edit";
    },
    salesUnpaidColumns() {
      let columns = [
        this.getColumnByKey("明細番号", "itemNumber"),
        this.getCheckDiffPriceColumn(),
        this.getConfirmColumn(),
        this.getCommentColumn(),
        this.getInputColumnByKey("店舗グループ名", "storeGNm", "text", true, { required: true, max: 40 }, {
            required: '店舗グループ名を確認して下さい',
            max: '店舗グループ名を確認して下さい'
          }, 1, 40, false
        ),
        this.getSelectColumnByKey("担当者名", "shainCd", this.getOptions, true, { required: true }, {
            required: '担当者名を確認して下さい'
          }, true, this.getOptionsForTable
        ),
        this.getSelectColumnByKey( "部門名", "bumonCd", this.getOptions, true, { required: true },
          {
            required: "部門名を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("項目名", "typeOfItem", this.listItem, true, { required: true }, {
            required: "項目名を確認して下さい"
          }, false
        ),
        this.getSelectColumnByKey("品目コード", "hinmokuCd", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", "text", true, { required: true, max: 30 }, {
            required: "品目略称を確認して下さい",
            max: "品目略称を確認して下さい"
          }, 1, 30, true
        ),
        this.getInputColumnByKey("容量", "yoryo", "text", true, { required: true, max: 8 },
          {
            required: "容量を確認して下さい",
            max: "容量を確認して下さい"
          }, 1, 8, false
        ),
        this.getNumericColumnByKey("入数", "irisu", true, { required: true, max_value: 9999, min_value: 1 },
          {
            required: "入数を確認して下さい",
            min_value: "入数を確認して下さい",
            max_value: "入数を確認して下さい"
          }, 0, 9999, 1, "initialIrisu"
        ),
        this.getNumericColumnByKey("生産者価格(円)", "mfYen", true, { required: true, max_value: 99999999.99},
          {
            required: "生産者価格を確認して下さい",
            max_value: "生産者価格を確認して下さい"
          }, 0.00, 99999999.99, 0.01, "initialMfYen"
        ),
        this.getNumericColumnByKey("口銭(%)", "commission", true, { required: true },
          {
            required: "荷姿を確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialCommission"
        ),
        this.getNumericColumnByKey("c/s引条件(円)", "csdiscount", true, { required: true },
          {
            required: "c/s引条件を確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialCSDiscount",
        ),
        this.getNumericColumnByKey("伝票NET(円)", "denpyoNet", true, { required: true },
          {
            required: "伝票NETを確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialDenpyoNet"
        ),
        this.getNumericColumnByKey("未収単価(円)", "accruedAmount", true, { required: true },
          {
            required: "未収単価を確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialAccruedAmount"
        ),
        this.getNumericColumnByKey("最終手取単価(円)", "finalTakeUnitPrice", true, { required: true },
          {
            required: "最終手取単価を確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialFinalTakeUnitPrice"
        ),
        this.getNumericColumnByKey("標準原価(営業)(円)", "hyojyunYen", true, { required: true },
          {
            required: "標準原価(営業)を確認して下さい"
          }, 0.00, 99999999.99, 0.01, "initialHyojyunYen"
        ),
        this.getNumericColumnByKey("販売本数", "quantityOfSoldItems", true, { required: true, min_value: 1 },
          {
            required: "販売本数を確認して下さい",
            min_value: "販売本数を確認して下さい"
          }, 0, 999999999999, 1, "initialQuantityOfSoldItems"
        ),
        this.getNumericColumnByKey("未収金額(円)", "accruedAmount", true, { required: true },
          {
            required: "未収金額を確認して下さい"
          }, 0, 999999999999, 1, "initialAccruedAmount"
        ),
        this.getNumericColumnByKey("売上金額(円)", "salesAmount", true, { required: true },
          {
            required: "売上金額を確認して下さい"
          }, 0, 999999999999, 1, "initialSalesAmount"
        ),
        this.getNumericColumnByKey("最終限界利益額(円)", "finalMarginalProfit", true, { required: true },
          {
            required: "最終限界利益額を確認して下さい"
          }, 0, 99999999999, 1, "initialFinalMarginalProfit",
        ),
        this.getNumericColumnByKey("最終限界利益率(%)", "finalMarginalProfitRatio", true, { required: true },
          {
            required: "最終限界利益額を確認して下さい"
          }, 0.00, 999999999999.99, 0.01, "initialFinalMarginalProfitRatio"
        )
      ];
      if(this.mode == "edit") {
        columns.splice(8, 0, this.getButtonColumn("商品検索", (data, index, listName) => {
            this.searchProduct(data, index, listName);
          }
        ));
      }
      return columns;
    },
    tax8PerctgSalesPromotionColumns() {
      let columns = [
        this.getColumnByKey("明細番号", "itemNumber", null),
        this.getCheckDiffPriceColumn(),
        this.getConfirmColumn(),
        this.getCommentColumn(),
        this.getInputColumnByKey("店舗グループ名", "storeGNm", "text", true, { required: true }, {
            required: '店舗グループ名を確認して下さい',
            max: '店舗グループ名を確認して下さい'
          }, 1, 40, false, function(datum, event) {}
        ),
        this.getSelectColumnByKey("担当者名", "shainCd", this.getOptions, true, { required: true }, {
            required: '担当者名を確認して下さい'
          }, true, this.getOptionsForTable
        ),
        this.getSelectColumnByKey( "部門名", "bumonCd", this.getOptions, true, { required: true },
          {
            required: "部門名を確認して下さい"
          }, true, this.getOptionsForTable
        ),
        this.getSelectColumnByKey("販促費目", "typeOfPromotionalExpenses", this.listItem, true, { required: true }, {
            required: "販促費目を確認して下さい"
          }, false
        ),
        this.getSelectColumnByKey("入力タイプ", "typeOfInput", this.getOptions, true, { required: true }, {
            required: "入力タイプを確認して下さい"
          }, true, this.getOptionsForTable
        ),
        this.getSelectColumnByKey("区分", "classification", this.listClassification, true, { required: true }, {
            required: "区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("ブランド区分	", "brandClassification", this.listClassification, true, { required: true }, {
            required: "ブランド区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("カテゴリ", "categoryName", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("品目コード", "hinmokuCd", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", "text", true, { required: true }, {
            required: "品目略称を確認して下さい",
            max: "品目略称を確認して下さい"
          }, 1, 30, true
        ),
        this.getInputColumnByKey("荷姿", "nisugata", "text", true, { required: true }, {
            required: "荷姿を確認して下さい",
            max: "荷姿を確認して下さい"
          }, 1, 15, false
        ),
        this.getNumericColumnByKey("未収金額(円)", "accruedAmount", true, { required: true },
          {
            required: "未収金額を確認して下さい"
          }, 0, 999999999999, 1, "initialAccruedAmount"
        )
      ];
      if(this.mode == "edit") {
        columns.splice(12, 0, this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      return columns;
    },
    tax10PerctgSalesPromotionColumns() {
      let columns = [
        this.getColumnByKey("明細番号", "itemNumber", null),
        this.getCheckDiffPriceColumn(),
        this.getConfirmColumn(),
        this.getCommentColumn(),
        this.getInputColumnByKey("店舗グループ名", "storeGNm", "text", true, { required: true }, {
            required: '店舗グループ名を確認して下さい',
            max: '店舗グループ名を確認して下さい'
          }, 1, 40, false, function(datum, event) {}
        ),
        this.getSelectColumnByKey("担当者名", "shainCd", this.getOptions, true, { required: true }, {
            required: '担当者名を確認して下さい'
          }, true
        ),
        this.getSelectColumnByKey( "部門名", "bumonCd", this.getOptions, true, { required: true },
          {
            required: "部門名を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("販促費目", "typeOfPromotionalExpenses", this.listItem, true, { required: true }, {
            required: "販促費目を確認して下さい"
          }, false
        ),
        this.getSelectColumnByKey("入力タイプ", "typeOfInput", this.getOptions, true, { required: true }, {
            required: "入力タイプを確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("区分", "classification", this.listClassification, true, { required: true }, {
            required: "区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("ブランド区分	", "brandClassification", this.listClassification, true, { required: true }, {
            required: "ブランド区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("カテゴリ", "categoryName", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("品目コード", "hinmokuCd", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", "text", true, { required: true }, {
            required: "品目略称を確認して下さい",
            max: "品目略称を確認して下さい"
          }, 1, 30, true
        ),
        this.getInputColumnByKey("荷姿", "nisugata", "text", true, { required: true }, {
            required: "荷姿を確認して下さい",
            max: "荷姿を確認して下さい"
          }, 1, 15, false
        ),
        this.getNumericColumnByKey("未収金額(円)", "accruedAmount", true, { required: true },
          {
            required: "未収金額を確認して下さい"
          }, 0, 999999999999, 1, "initialAccruedAmount"
        )
      ];
      if(this.mode == "edit") {
        columns.splice(12, 0, this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      return columns;
    },
    logicPromotionFeeColumns() {
      let columns = [
        this.getColumnByKey("明細番号", "itemNumber", null),
        this.getCheckDiffPriceColumn(),
        this.getConfirmColumn(),
        this.getCommentColumn(),
        this.getInputColumnByKey("店舗グループ名", "storeGNm", "text", true, { required: true }, {
            required: '店舗グループ名を確認して下さい',
            max: '店舗グループ名を確認して下さい'
          }, 1, 40, false, function(datum, event) {}
        ),
        this.getSelectColumnByKey("担当者名", "shainCd", this.getOptions, true, { required: true }, {
            required: '担当者名を確認して下さい'
          }, true
        ),
        this.getSelectColumnByKey( "部門名", "bumonCd", this.getOptions, true, { required: true },
          {
            required: "部門名を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("販促費目", "typeOfPromotionalExpenses", this.listItem, true, { required: true }, {
            required: "販促費目を確認して下さい"
          }, false
        ),
        this.getSelectColumnByKey("入力タイプ", "typeOfInput", this.getOptions, true, { required: true }, {
            required: "入力タイプを確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("区分", "classification", this.listClassification, true, { required: true }, {
            required: "区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("ブランド区分	", "brandClassification", this.listClassification, true, { required: true }, {
            required: "ブランド区分を確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("カテゴリ", "categoryName", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getSelectColumnByKey("品目コード", "hinmokuCd", this.getOptions, true,{ required: true }, {
            required: "品目コードを確認して下さい"
          }, true
        ),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", "text", true, { required: true }, {
            required: "品目略称を確認して下さい",
            max: "品目略称を確認して下さい"
          }, 1, 30, true
        ),
        this.getInputColumnByKey("荷姿", "nisugata", "text", true, { required: true }, {
            required: "荷姿を確認して下さい",
            max: "荷姿を確認して下さい"
          }, 1, 15, false
        ),
        this.getNumericColumnByKey("未収金額(円)", "accruedAmount", true, { required: true },
          {
            required: "未収金額を確認して下さい"
          }, 0, 999999999999, 1, "initialAccruedAmount"
        )
      ];
      if(this.mode == "edit") {
        columns.splice(12, 0, this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      return columns;
    },
    itemTotalForEightPercent() {
      return this.sumAccruedAmount(this.item.detailsForAccountsReceivablesRequestList) + this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax8PercentList);
    },
    itemTotalForTenPercent() {
      return this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax10PercentList) + this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestLogisticFeesList);
    },
    itemTotal() {
      return this.itemTotalForEightPercent + this.itemTotalForTenPercent;
    },
    consumptionTaxTotalForEightPercent() {
      return Math.round(this.item.requestModel.itemTotalForEightPercent * 1.08);
    },
    consumptionTaxTotalForTenPercent() {
      return Math.round(this.item.requestModel.itemTotalForTenPercent * 1.10);
    },
    consumptionTaxTotal() {
      return this.item.requestModel.consumptionTaxTotalForEightPercent + this.item.requestModel.consumptionTaxTotalForTenPercent;
    },
    totalForEightPercent() {
      return this.item.requestModel.itemTotalForEightPercent + this.item.requestModel.consumptionTaxTotalForEightPercent;
    },
    totalForTenPercent() {
      return this.item.requestModel.itemTotalForTenPercent + this.item.requestModel.consumptionTaxTotalForTenPercent;
    },
    total() {
      return this.item.requestModel.itemTotal + this.item.requestModel.consumptionTaxTotal;
    }
  },
  watch: {
    mode(newValue) {
      this.fetchRequest();
    },
    itemTotalForEightPercent(newValue) {
      this.item.requestModel.itemTotalForEightPercent = newValue;
    },
    itemTotalForTenPercent(newValue) {
      this.item.requestModel.itemTotalForTenPercent = newValue;
    },
    itemTotal(newValue) {
      this.item.requestModel.itemTotal = newValue;
    },
    consumptionTaxTotalForEightPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForEightPercent = newValue;
    },
    consumptionTaxTotalForTenPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForTenPercent = newValue;
    },
    consumptionTaxTotal(newValue) {
      this.item.requestModel.consumptionTaxTotal = newValue;
    },
    totalForEightPercent(newValue) {
      this.item.requestModel.totalForEightPercent = newValue;
    },
    totalForTenPercent(newValue) {
      this.item.requestModel.totalForTenPercent = newValue;
    },
    total(newValue) {
      this.item.requestModel.total = newValue;
    },
    "item.commissionType"(newValue, oldValue) {
      if(newValue != oldValue) {
        if(this.item.detailsForAccountsReceivablesRequestList) {
          this.item.detailsForAccountsReceivablesRequestList.forEach(async item => {
            item = await this.calculateDenpyoNet({item, commissionType: this.item.commissionType});
          });
        }
      }
    }
  },
  created() {
    this.item = {"cd":146,"targetOn":"2020-05-01","purpose":"展示会出席","purposeOfOthers":"","commissionType":"内口銭","remarks":"hjkl","requestModel":{"cd":194,"mstTorihikiCd":"","torihikiNm":"静岡塩業（株）静岡本社","isSentBack":null,"billingAmount":23423324,"billingOn":"2020-05-30","paymentPlace":"東京支店  家庭用営業課","paymentMethod":"小切手(持参)","paymentOtherMethod":"","scheduledPaymentOn":"2020-05-01","paymentOn":"2020-05-31","paymentDestination":"assssssssss","bankName":"ádasdsad","bankAccountNumber":"","bankAccountName":"","itemTotalForEightPercent":1,"itemTotalForTenPercent":1,"consumptionTaxTotalForEightPercent":2,"consumptionTaxTotalForTenPercent":2,"itemTotal":12,"consumptionTaxTotal":3,"totalForEightPercent":3,"totalForTenPercent":3,"total":21,"isTemp":null,"shainCd":null,"bumonNm":null,"approvalFlowDetailsRequest":{"bumonCd":"BM A","shainCd":"NgocCD"},"fileModelList":[{"requestAttachmentCd":46,"fileOriginalName":"3456.jpg","awsS3FilePath":"request_198/3456.jpg","fileSize":null}],"filesIsDeleted":null,"mstRequestStatusesResponse":{"cd":9,"name":"確認待ち"}},"accountReceivableDetailIsDeleted":null,"detailsForAccountsReceivablesRequestList":[{"accountReceivableDetailCd":172,"itemNumber":"1001","storeGNm":"a","shainCd":"thao","shainNm":"A","bumonCd":"1100","bumonNm":"営業本部","sortNumber":0,"isDeleted":0,"commentDetailsRequests":null,"cd":86,"typeOfItem":"EDLP","hinmokuCd":"A2","hinmokuRnm":"hoang b","yoryo":"nullA2","irisu":1,"initialIrisu":1,"mfYen":12.00,"initialMfYen":12.00,"commission":0.00,"initialCommission":0.00,"initialCSDiscount":0.00,"denpyoNet":12.00,"initialDenpyoNet":0.00,"accruedUnitPrice":0.00,"initialAccruedUnitPrice":0.00,"finalTakeUnitPrice":0.00,"initialFinalTakeUnitPrice":0.00,"hyojyunYen":0.00,"initialHyojyunYen":0.00,"quantityOfSoldItems":0,"initialQuantityOfSoldItems":0,"accruedAmount":0,"initialAccruedAmount":0,"salesAmount":0,"initialSalesAmount":0,"finalMarginalProfit":0,"initialFinalMarginalProfit":0,"finalMarginalProfitRatio":0.00,"initialFinalMarginalProfitRatio":0.00,"csdiscount":0.00}],"detailsForPromotionalExpensesRequestTax8PercentList":[{"accountReceivableDetailCd":173,"itemNumber":"2001","storeGNm":"a","shainCd":"thao","shainNm":"thao","bumonCd":"1100","bumonNm":"営業本部","sortNumber":0,"isDeleted":0,"commentDetailsRequests":null,"cd":65,"typeOfInput":"商品カテゴリ","typeOfPromotionalExpenses":"マネキン代","classification":"家庭用","brandClassification":"PB","categoryName":"仕入品","hinmokuCd":"111215","hinmokuRnm":"成城石井胡麻づくしﾄﾞﾚ30M","nisugata":"30ml/50*6","accruedAmount":0,"initialAccruedAmount":0}],"detailsForPromotionalExpensesRequestTax10PercentList":[],"detailsForPromotionalExpensesRequestLogisticFeesList":[]};
    // if(!this.requestCd) {
    //   this.$router.push("/request/confirm");
    // } else {
    //   this.getUseTypes();
    //   this.getCommissionTypes();
    //   this.getListDepartment();
    //   this.getListPaymentPlace();
    //   this.getListPaymentMethod();
    //   this.getListItem();
    //   this.fetchRequest();
    // }
  },
  methods:{
    ...mapActions('AccountReceivableCreateModule', ["getUseTypes", "getCommissionTypes", 'getListDepartment', 'getListApplication', 'getListPaymentPlace',
      'getListPaymentMethod', 'createAccountReceivables', "deleteAttachFile", "addItemListManageItem", "addPromotions8Percents",
      "addPromotions10Percents", "addPromotionsCostLogistics", "getListItem", "setAttachFileForRequest",
      "deleteItemAccountReceivablesDetailRequestList", "addItemComment", "addItemAccountReceivablesProduct", "setValueDenpyoNet", 'final_take_unit_price', 'accrued_amount', 'sales_amount', 'final_marginal_profit', 'final_marginal_profit_ratio'
      , "getListPromotionExpenses", "getListInputType", "getListClassifications", "getListBrandClassification", "getListCategory", "getListChargeName", "setChargeNameForRequestTable", "setDepartmentNameForRequestTable"
      , "getDetailAccountReceivable", "setModeAccountReceivableCreate"
    ]),
    ...mapActions("AutoCalculateOnTable", [
      "calculateDenpyoNet",
      "calculateFinalTakeUnitPrice",
      "calculateAccruedAmount",
      "calculateSalesAmount",
      "calculateFinalMarginalProfit",
      "calculateFinalMarginalProfitRatio"
    ]),
    fetchRequest() {
      AccountReceivableCreateService.getDetailAccountReceivableByRequestId({cd: this.requestCd, mode: this.mode || 'view'})
        .then(response => {
          if(response.data) {
            this.isLocked = false;
            this.item = response.data;
            if(this.mode == "edit") {
              if(this.item.requestModel.editShainCd && this.item.requestModel.editShainCd != this.loginUser.sub) {
                this.showLockInEditing = true;
                this.isLocked = true;
              } else {
                this.timeoutForEditRequest = setTimeout(() => {
                  this.showTimeoutEditing = true;
                }, 60 * 60 * 1000);
              }
            }
          }
        })
        .catch(error => {
          console.log(error);
          this.isLocked = false;
        });
    },
    getColumnByKey(label, field, getData) {
      return {
        label: label,
        field: field,
        getData: getData
      }
    },
    getInputColumnByKey(label, field, type, isValidate, validateRule, messages, min, max, isReadOnly, onInput) {
      return {
        label: label,
        field: field,
        isInput: true,
        type: type ? type : "text",
        isValidate: isValidate,
        validateRule: validateRule,
        messages: messages,
        disabled: this.disabled,
        isPlainText: true, // read only
        min: min,
        max: max,
        isReadOnly: isReadOnly,
        onInput: onInput
      }
    },
    getNumericColumnByKey(label, field, isValidate, validateRule, messages, min, max, step, initialField, emtyValue = 0) {
      return {
        label: label,
        field: field,
        isInput: true,
        type: "numeric",
        isValidate: isValidate,
        validateRule: validateRule,
        messages: messages,
        disabled: this.disabled,
        min: min,
        max: max,
        step: step ? step : 0.01,
        initialField: initialField,
        emtyValue: 0,
        onInput: this.autoCalculateTable
      }
    },
    getSelectColumnByKey(label, field, options, isValidate, validateRule, messages, isOnSearch = false, onSearch = undefined) {
      return {
        label: label,
        field: field,
        isSelect: true,
        disabled: this.disabled,
        isValidate: isValidate,
        validateRule: validateRule,
        messages: messages,
        options: options,
        onInput: (index, field, keyList) => {
          
        },
        onOpen: () => {
        },
        isOnSearch: isOnSearch,
        onSearch: onSearch
      }
    },
    getCommentColumn() {
      return {
        label: "コメント",
        isComment: true,
        status: data => {
          return data.hasComment || (data.commentDetailsRequests && data.commentDetailsRequests.length > 0); 
        },
        onClick: (data, index, listName) => {
          this.selectedItemNumber = {rowIndex: index, listName: listName, itemNumber: data.itemNumber};
          this.isShowPopupComment = true;
          if(this.item[listName][index].commentDetailsRequests){
            this.initComment = this.item[listName][index].commentDetailsRequests[0].comment;
          }
          this.commentRecordCd = this.item[listName][index]?.accountReceivableDetailCd;
        }
      }
    },
    getCheckDiffPriceColumn() {
      return {
        label: "",
        isCheckDiffPrice: true,
        status: (data, columns) => {
          let isDiff = false;
          for(let i = 0; i < columns.length; i ++) {
            let col = columns[i];
            if(col.type == "numeric" && data[col.field] != data[col.initialField]) {
              isDiff = true;
              break;
            }
          }
          return isDiff; 
        }
      }
    },
    getConfirmColumn() {
      return {
        label: "確認",
        isConfirm: true,
        status: data => {
          return data.isChecked ? "confirmed" : this.loginUser && data.shainCd == this.loginUser.sub ? "not-confirm" : ""
        },
        onClick: (data, listName) => {
          data.isChecked = data.isChecked == 1 ? 0 : 1;
          let list = this.item[listName] || [];
          let indexItem = list.findIndex(item => {
            return item.cd == data.cd;
          });
          this.$set(list, indexItem, data);
          this.$set(this.item, listName, list);
        }
      }
    },
    getButtonColumn(label, onClick) {
      return {
        isButton: true,
        isShow: this.mode == "edit",
        label: label,
        disabled: this.disabled,
        onClick: onClick
      }
    },
    async autoCalculateTable(data, index, listName, field) {
      if(/(mfYen|csdiscount|irisu|commission)/.test(field)) {// denpyoNet
        this.item[listName][index] = await this.calculateDenpyoNet({item: data, commissionType: this.item.commissionType});
      }
      if(/(mfYen|accruedUnitPrice)/.test(field)) {// finalTakeUnitPrice
        this.item[listName][index] = await this.calculateFinalTakeUnitPrice(data);
      }
      if(/(accruedUnitPrice|quantityOfSoldItems)/.test(field)) {// accruedAmount
        this.item[listName][index] = await this.calculateAccruedAmount(data);
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems|)/.test(field)) { // salesAmount
        this.ite = await this.calculateSalesAmount(data)
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems|hyojyunYen|quantityOfSoldItems|)/.test(field)) {// finalMarginalProfit
        this.item[listName][index] = await this.calculateFinalMarginalProfit(data);
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems|hyojyunYen|)/.test(field)) {// finalMarginalProfitRatio
        this.item[listName][index] = await this.calculateFinalMarginalProfitRatio(data);
      }
    },
    getOptions (index, field, listName) {
      return this.optionsForTable[listName] &&
        this.optionsForTable[listName][index] && 
        this.optionsForTable[listName][index][field]
        ? this.optionsForTable[listName][index][field]
        : [];
    },
    async getOptionsForTable(search, loading, index, field, listName) {
      let rtnOptions = [];
      if(search && search != "") {
        loading(true);
        switch (field) {
          case "storeGNm": {
            rtnOptions = [];
            break;
          }
          case "shainCd": {
            // let response = await this.getListChargeName(search);
            // rtnOptions = response.reduce((list, item) => {
            //   let option = [{label: item.shainNm, value: item}]
            //   return [...list, ...option]
            // }, []);
            // rtnOptions = [
            //   {label:}
            // ]
            break;
          }
          case "bumonCd": {
            rtnOptions = [];
            break;
          }
          case "hinmokuCd": {
            rtnOptions = [];
            break;
          }
          case "typeOfInput": {
            rtnOptions = [];
            break;
          }
          case "categoryName": {
            rtnOptions = [];
            break;
          }
        }
        if(!this.optionsForTable[listName]) {
          this.$set(this.optionsForTable, listName, {});
        }
        if(!this.optionsForTable[listName][index]) {
          this.$set(this.optionsForTable[listName], index, {});
        }
        this.$set(this.optionsForTable[listName][index], field, rtnOptions || []);
        setTimeout(() => {
          loading(false);
        }, 350);
      }
    },
    getDependencyOptionsForTable(index, field, listName) {

    },
    addItem(index, listName, maxItemInitital) {
      let newItem = null;
      let indexOfNewItem = index + 1;
      switch (listName) {
        case "detailsForAccountsReceivablesRequestList":
          newItem = new DetailsForAccountsReceivablesRequest();
          break;
        case "detailsForPromotionalExpensesRequestTax8PercentList":
          newItem = new DetailsForPromotionalExpensesRequestTax8Percent();
          break;
        case "detailsForPromotionalExpensesRequestTax10PercentList":
          newItem = new DetailsForPromotionalExpensesRequestTax10Percent();
          break;
        case "detailsForPromotionalExpensesRequestLogisticFeesList":
          newItem = new DetailsForPromotionalExpensesRequestLogisticFees();
          break;
      }
      if(newItem) {
        let itemNumber = maxItemInitital + 1;
        newItem.itemNumber = itemNumber;
        this.item[listName].splice(indexOfNewItem, 0, newItem);
      }
    },
    copyItem(index, listName, maxItemInitital) {
      if(index == -1 || index == this.item[listName].length) {
        return;
      }
      let newItem = cloneDeep(this.item[listName][index]);
      let indexOfNewItem = index + 1;
      newItem.itemNumber = maxItemInitital + 1;
      newItem.cd = null;
      newItem.accountReceivableDetailCd = null;
      this.item[listName].splice(indexOfNewItem, 0, newItem);
    },
    removeItem (index, listName) {
      if(index == -1 || index == this.item[listName].length) {
        return;
      }
      this.item[listName].splice(index, 1);
    },
    calculateMaxItemNumber(maxItemInitital, listName) {
      let maxItemNumber = maxItemInitital;
      if(this.item[listName]?.length > 0) {
        maxItemNumber = getMaxItemList(this.item[listName], 'itemNumber');
      }
      return maxItemNumber + 1;
    },
    setAttachFiles(files) {
      if(!Array.isArray(files)) {
        files = [files];
      }
      let existedList = this.item.requestModel?.fileModelList || [];
      this.item.requestModel.fileModelList = existedList.concat(files);
    },
    removeAttachFile(index) {
      this.item.requestModel.fileModelList.splice(index, 1);
    },
    onAddedComment(newComment) {
      let index = this.selectedItemNumber.rowIndex,
        listName = this.selectedItemNumber.listName;

      let storeGNm = this.item[listName][index].storeGNm;
      let chargeName = this.item[listName][index].shainNm;
      if (newComment.isSameStoreName || newComment.isSameShain || newComment.isSameShainExclamation) {
        this.item[listName].forEach((item, index) => {
          if (item.storeGNm == storeGNm && newComment.isSameStoreName && this.item[listName][index].commentDetailsRequests) {
            this.item[listName][index].commentDetailsRequests[0].comment = newComment.comment;
          } else {
            if (item.shainNm == chargeName && newComment.isSameShain && this.item[listName][index].commentDetailsRequests) {
              this.item[listName][index].commentDetailsRequests[0].comment = newComment.comment;
            } else {
              this.item[listName][index].commentDetailsRequests = [{
                comment: newComment.comment,
                isDeputy: 0
              }];
            }
          }
        })
      } else {
        if (this.item[listName][index].commentDetailsRequests) {
          this.item[listName][index].commentDetailsRequests[0].comment = newComment.comment;
        } else {
          this.item[listName][index].commentDetailsRequests = [{
            comment: newComment.comment,
            isDeputy: 0
          }];
        }
      }
      this.item[listName][index].hasComment = this.item[listName][index]?.commentDetailsRequests.length > 0;
      this.selectedItemNumber = null;
    },
    sumAccruedAmount(list) {
      if(!list || !Array.isArray(list)) {
        return 0;
      }
      return list.reduce(( a, currentItem ) => {
        let accruedAmountA = a,
          accruedAmountB = currentItem.accruedAmount || 0;
        return accruedAmountA + accruedAmountB
      }, 0);
    },
    validateRequest() {
      // check data validate
      // get all row which are belong to login user
      let columnsToCheck = [];
      let setColumnFields = new Set();
      columnsToCheck = columnsToCheck.concat(this.salesUnpaidColumns.filter(column => {
        if(!setColumnFields.has(column.initialField) && column.initialField) {
          setColumnFields.add(column.initialField);
          return column;
        }
      }));
      columnsToCheck = columnsToCheck.concat(this.tax8PerctgSalesPromotionColumns.filter(column => {
        if(!setColumnFields.has(column.initialField) && column.initialField) {
          setColumnFields.add(column.initialField);
          return column;
        }
      }));
      columnsToCheck = columnsToCheck.concat(this.tax10PerctgSalesPromotionColumns.filter(column => {
        if(!setColumnFields.has(column.initialField) && column.initialField) {
          setColumnFields.add(column.initialField);
          return column;
        }
      }));
      columnsToCheck = columnsToCheck.concat(this.logicPromotionFeeColumns.filter(column => {
        if(!setColumnFields.has(column.initialField) && column.initialField) {
          setColumnFields.add(column.initialField);
          return column;
        }
      }));
      let listRowsNeedChecked = [],
        listRowNeedCheckDiffHasComment = [];
      if(this.item.detailsForAccountsReceivablesRequestList) {
        listRowsNeedChecked= listRowsNeedChecked.concat(this.item.detailsForAccountsReceivablesRequestList.filter(data => {
          return this.loginUser && data.shainCd == this.loginUser.sub;
        }));
      }
      if(this.item.detailsForPromotionalExpensesRequestTax8PercentList) {
        listRowsNeedChecked= listRowsNeedChecked.concat(this.item.detailsForPromotionalExpensesRequestTax8PercentList.filter(data => {
          return this.loginUser && data.shainCd == this.loginUser.sub;
        }));
      }
      if(this.item.detailsForPromotionalExpensesRequestTax10PercentList) {
        listRowsNeedChecked= listRowsNeedChecked.concat(this.item.detailsForPromotionalExpensesRequestTax10PercentList.filter(data => {
          return this.loginUser && data.shainCd == this.loginUser.sub;
        }));
      }
      if(this.item.detailsForPromotionalExpensesRequestLogisticFeesList) {
        listRowsNeedChecked= listRowsNeedChecked.concat(this.item.detailsForPromotionalExpensesRequestLogisticFeesList.filter(data => {
          return this.loginUser && data.shainCd == this.loginUser.sub;
        }));
      }
      let isCheckedAllRows = listRowsNeedChecked.every(data => data.isChecked);

      listRowNeedCheckDiffHasComment = listRowsNeedChecked.filter(data => {
        let isChangeInitital = columnsToCheck.some(column => {
          return data[column.field] != data[column.initialField];
        });
        return isChangeInitital && !data.hasComment;
      });
      let isFillCommentForAllChangeInitialValue = listRowNeedCheckDiffHasComment.length > 0;
      if(!isCheckedAllRows || isFillCommentForAllChangeInitialValue) {
        if(listRowNeedCheckDiffHasComment.length > 0) {
          // set error to comment
          let listNames = [
            "detailsForAccountsReceivablesRequestList",
            "detailsForPromotionalExpensesRequestTax8PercentList",
            "detailsForPromotionalExpensesRequestTax10PercentList",
            "detailsForPromotionalExpensesRequestLogisticFeesList"
          ];
          listRowNeedCheckDiffHasComment.forEach(data => {
            // find data in list
            let dataIndex = -1;
            for(let i = 0; i< listNames.length; i++) {
              let listName = listNames[i];
              let list = this.item[listName];
              dataIndex = list.findIndex(item => {
                return item.cd == data.cd;
              });
              if(dataIndex > -1) {
                data.hasDiff = true;
                this.$set(list, dataIndex, data);
                this.$set(this.item, listName, list);
                break;
              }
            }
          })
        }
      } else {
        this.isShowCommentModal = true;
      }
    },
    closeConfirmCommentPopup(comment) {
      this.item.requestModel.comment = comment;
      this.isShowCommentModal = false;
    },
    confirmRequest(comment) {
      if(!comment || comment == "") {
        return;
      }
      this.item.requestModel.comment = comment;
      AccountReceivableCreateService.saveAccountReceivableConfirmRequest(null, this.item)
      .then(response => {
        this.showNormalDialog = true;
        setTimeout(() => {
          this.showNormalDialog = false;
          setTimeout(() => {
            this.$router.push("/request/confirm");
          }, 600);
        }, 2000);
      })
      .catch(err => {
        console.log(err);
        this.showErrorDialog = true;
      });
    },
    async saveRequest() {
      this.errors = [];
      let isValidated = await this.$refs.accountReceivableForm.validate();
      if(isValidated) {
        AccountReceivableCreateService.updateAccountReceivableConfirmRequest(this.item.requestModel.fileModelList, this.item)
        .then(response => {
          this.$router.replace(`/account-receivables/confirm-create/${this.requestCd}/view`);
        })
        .catch(err => {
          console.log(err);
        });
      } else {
        setTimeout(() => {
          let errors = [];
          if(typeof this.$refs.accountReceivableForm.errors == "object") {
            this.errors = Object.values(this.$refs.accountReceivableForm.errors).flat();
          }
        }, 200);
      }
    },
    searchProduct(data, index, listName) {
      this.selectedItemNumber = {rowIndex: index, listName: listName, itemNumber: data.itemNumber};
      this.showPopupSearchProduct = !this.showPopupSearchProduct;
    },
    getProduct(product) {
      let index = this.selectedItemNumber.rowIndex,
        listName = this.selectedItemNumber.listName;

      this.item[listName][index].hinmokuCd = product.hinmokuCd;
      //51
      this.item[listName][index].hinmokuRnm = product.hinmokuRnm;
      //control field 2 type table (have 2 type table)
      if (listName == 'detailsForAccountsReceivablesRequestList') {
        //52
        this.item[listName][index].yoryo = product.kikaku + product.yoryoTani;
        //53
        this.item[listName][index].irisu = product.irisu;
        this.item[listName][index].initialIrisu = product.irisu;
        //54
        this.item[listName][index].mfYen = product.mfYen;
        this.item[listName][index].initialMfYen = product.mfYen;
      } else {
        //77
        this.item[listName][index].nisugata = product.nisugata;
      }
      this.selectedItemNumber = null;
    },
    getSupplier(event) {
      this.showPopupSupply = false;
      let torihikiNm = event.torihiki1Nm + event.torihiki2Nm;
      let torihikiCd = event.torihikiCd;
      this.item.requestModel.torihikiNm = torihikiNm;
      this.item.requestModel.mstTorihikiCd = torihikiCd;
      this.supplier = event;
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
          white-space: nowrap;
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
}
</style>