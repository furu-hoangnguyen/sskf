<template>
  <ValidationObserver ref="accountReceivableForm" v-slot="{errors,invalid,validated}">
    <section :class="['request account-receivable-confirm-create', {validated: validated}, {invalid: invalid}]">
      <div class="createAcount-header">
        <div class="createAcount-header_account">
          <div>販売未収金申請</div>
          <div>
            <label class="my-0">申請番号:</label>
            <label class="my-0">決済番号:</label>
          </div>
        </div>
        <div class="createAcount-header_status">
          <div class="createAcount-header_status_item">
            <p class="mb-0" v-html="requestStatus"></p>
        </div>
        </div>
      </div>
      <b-container fluid class="bv-example-row request-body fixScroll" ref="mainView">
        <b-row v-if="validated && handleErrors(errors).length > 0" class="errorsMessagesField">
          <b-col
            v-for="(error, index) in handleErrors(errors) "
            :key="index"
            sm="12">
            <b>エラー：{{ error }}</b>
          </b-col>
        </b-row>
        <section>
          <Collapse>
            <template slot="collapse-header">
              <span class="label_input_header">販促金管理項目</span>
            </template>
            <template slot="collapse-body">
              <b-row class="px-4 py-1">
                <b-col md="4" class="pl-0 pr-0">
                  <DateSskf v-model="item.targetOn" valueType="YYYY-MM-DD" id="targetDate"
                    :rules="checkRequiredForFieldBasedOnStatusPage('targetOn').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('targetOn').messages"
                    name="対象年月"
                    :disabled-field="disabled"
                    :disabled-date="disabledInputDate"
                    formatDate="YYYY/MM"/>
                </b-col>
                <b-col md="6">
                  <InputSskf 
                    id="input-horizontal"
                    name="取引先名"
                    :disabled-field="disabled"
                    :plaintext="true"
                    :rules="checkRequiredForFieldBasedOnStatusPage('torihikiNm').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('torihikiNm').messages"
                    :maxlength="getMinAndMaxValueByField('torihikiNm').max"
                    v-model="item.requestModel.torihikiNm"/>
                </b-col>
                <b-col md="2" class="p-0">
                  <b-button variant="none" class="btn_grey btn_getSupply" @click="showPopupSupply=true">取引先名検索
                  </b-button>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <SelectSskf
                    name="用途"
                    id="use_other_input"
                    :changed="invalid.changed"
                    v-model="item.purpose"
                    :rules="checkRequiredForFieldBasedOnStatusPage('purpose').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('purpose').messages"
                    :isNormalSelect="true"
                    :options="useTypesList"
                    :disabled-field="disabled">
                  </SelectSskf>
                </b-col>
                <b-col md="4" class="pl-0" v-if="item.purpose == 'その他'">
                  <InputSskf
                    id="input-horizontal"
                    name=""
                    :rules="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('purposeOfOthers').messages"
                    :maxlength="getMinAndMaxValueByField('purposeOfOthers').max"
                    class="fieldDependent"
                    :disabled-field="disabled"
                    v-model="item.purposeOfOthers"/>
                </b-col>
                <b-col md="4">
                  <SelectSskf
                    name="内口銭/外口銭"
                    id="use_other_input"
                    :changed="invalid.changed"
                    v-model="item.commissionType"
                    :rules="checkRequiredForFieldBasedOnStatusPage('commissionType').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('commissionType').messages"
                    :isNormalSelect="true"
                    :options="commissionTypeList"
                    :disabled-field="disabled"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <InputNumbericSskf
                    separator=","
                    name="請求金額(円)"
                    id="billing_amount" 
                    :disabled-field="disabled"
                    :rules="checkRequiredForFieldBasedOnStatusPage('billingAmount').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('billingAmount').messages"
                    :min="getMinAndMaxValueByField('billingAmount').min"
                    :max="getMinAndMaxValueByField('billingAmount').max"
                    v-model="item.requestModel.billingAmount"/>
                </b-col>
                <b-col md="4">
                  <DateSskf
                    v-model="item.requestModel.billingOn" 
                    valueType="YYYY-MM-DD"
                    id="billing_date"
                    :changed="invalid.changed"
                    :rules="checkRequiredForFieldBasedOnStatusPage('billingOn').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('billingOn').messages"
                    name="請求日"
                    :disabled-field="disabled"
                    :disabled-date="disabledInputDate"
                    formatDate="YYYY/MM/DD"/>
                </b-col>
              </b-row>

              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <SelectSskf
                    name="部門名"
                    id="use_other_input"
                    :rules="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.bumonCd').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.bumonCd').messages"
                    v-model="item.requestModel.approvalFlowDetailsRequest.bumonCd"
                    :isNormalSelect="false"
                    optionsLabel="bumonNm"
                    optionsValue="bumonCd"
                    :options="departmentList" :disabled-field="disabled"/>
                </b-col>
                <b-col md="4">
                  <SelectSskf
                    name="申請者名"
                    id="field2"
                    :changed="invalid.changed"
                    :isNormalSelect="false"
                    optionsLabel="shainNm"
                    optionsValue="shainCd"
                    :rules="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.shainCd').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('approvalFlowDetailsRequest.shainCd').messages"
                    v-model="item.requestModel.approvalFlowDetailsRequest.shainCd"
                    :options="applicationList" :disabled-field="disabled"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="12" class="pl-0">
                  <TextareaSskf name="備考"
                    id="remarks"
                    v-model="item.remarks" :row='3'
                    :maxlength="getMinAndMaxValueByField('remarks').max"
                    :rules="checkRequiredForFieldBasedOnStatusPage('remarks').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('remarks').messages"
                    :disabled-field="disabled"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <UploadFile
                  :files="item.requestModel.fileModelList"
                  :disabled="disabled"
                  request-status="作成中"
                  @set-attach-file-for-request="setAttachFiles"
                  @remove-file="removeAttachFile" />
              </b-row>
            </template>
          </Collapse>
        </section>
        <section>
          <Collapse>
            <template slot="collapse-header">
              <span class="label_input_header">支払情報</span>
            </template>
            <template slot="collapse-body">
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <SelectSskf
                    name="支払場所"
                    id="payment_place"
                    :changed="invalid.changed"
                    :isNormalSelect="false"
                    optionsLabel="bumonNm"
                    optionsValue="bumonNm"
                    v-model="item.requestModel.paymentPlace"
                    :options="paymentPlaceList"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentPlace').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentPlace').messages"
                    :disabled-field="disabled"/>
                </b-col>
                <b-col md="4" class="pr-0">
                  <SelectSskf
                    name="支払い方法"
                    id="payment_method"
                    :changed="invalid.changed"
                    :isNormalSelect="true"
                    v-model="item.requestModel.paymentMethod"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentMethod').messages"
                    :options="paymentMethodList"
                    :disabled-field="disabled"/>
                </b-col>
                <b-col md="4" class="pl-0">
                  <InputSskf 
                    v-if="item.requestModel.paymentMethod == 'その他'"
                    id="input-horizontal" name=""
                    :maxlength="getMinAndMaxValueByField('paymentOtherMethod').max"
                    class="fieldDependent" :disabled-field="disabled"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOtherMethod').messages"
                    v-model="item.requestModel.paymentOtherMethod"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <DateSskf 
                    v-model="item.requestModel.scheduledPaymentOn" 
                    valueType="YYYY-MM-DD"
                    id="payment_scheduled_date"
                    :changed="invalid.changed"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.scheduledPaymentOn').messages"
                    name="支払予定日"
                    :disabled-field="disabled"
                    formatDate="YYYY/MM/DD"/>
                </b-col>
                <b-col md="4" class="pr-0">
                  <DateSskf 
                    v-model="item.requestModel.paymentOn"
                    valueType="YYYY-MM-DD"
                    id="payment_date"
                    :changed="invalid.changed"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentOn').messages"
                    name="支払日"
                    :disabled-field="!isCanInputPaymentOn"
                    formatDate="YYYY/MM/DD"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="8" class="pl-0 pr-0">
                  <InputSskf
                    id="input-payment_destination" 
                    name="支払先" 
                    :maxlength="getMinAndMaxValueByField('paymentDestination').max"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.paymentDestination').messages"
                    v-model="item.requestModel.paymentDestination"/>
                </b-col>
                <b-col md="4">
                  <InputSskf
                    id="input-account_number"
                    name="銀行名" 
                    :maxlength="getMinAndMaxValueByField('bankName').max"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankName').messages"
                    v-model="item.requestModel.bankName"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="4" class="pl-0 pr-0">
                  <InputSskf
                    id="input-bankAccountNumber"
                    name="口座番号"
                    :maxlength="getMinAndMaxValueByField('bankAccountNumber').max"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountNumber').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountNumber').messages"
                    v-model="item.requestModel.bankAccountNumber"/>
                </b-col>
                <b-col md="8">
                  <InputSskf
                    id="bankAccountName"
                    name="口座名義"
                    :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountName').rules"
                    :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.bankAccountName').messages"
                    :maxlength="getMinAndMaxValueByField('bankAccountName').max"
                    v-model="item.requestModel.bankAccountName"/>
                </b-col>
              </b-row>
              <b-row class="px-4 py-1 mt-3">
                <b-col md="10" class="pl-0 ">
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
                          :class-field="[
                            'fieldPayment', 
                            {'change-value': item.requestModel.itemTotalForEightPercent != item.requestModel.initialItemTotalForEightPercent}
                          ]"
                          v-model="item.requestModel.itemTotalForEightPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForEightPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForEightPercent').messages"
                          separator=","/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotalForTenPercent != item.requestModel.initialItemTotalForTenPercent}]"
                          v-model="item.requestModel.itemTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotalForTenPercent').messages"
                          separator="," />
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.itemTotal != item.requestModel.initialItemTotal}]"
                          v-model="item.requestModel.itemTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.itemTotal').messages"
                          separator="," />
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
                          separator="," />
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotalForTenPercent != item.requestModel.initialConsumptionTaxTotalForTenPercent}]"
                          v-model="item.requestModel.consumptionTaxTotalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotalForTenPercent').messages"
                          separator="," />
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotal"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.consumptionTaxTotal != item.requestModel.initialConsumptionTaxTotal}]"
                          v-model="item.requestModel.consumptionTaxTotal"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.consumptionTaxTotal').messages"
                          separator="," />
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
                          separator="," />
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="totalForTenPercent"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.totalForTenPercent != item.requestModel.initialTotalForTenPercent}]"
                          v-model="item.requestModel.totalForTenPercent"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.totalForTenPercent').messages"
                          separator="," />
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="total"
                          :class-field="['fieldPayment', {'change-value': item.requestModel.total != item.requestModel.initialTotal}]"
                          v-model="item.requestModel.total"
                          :rules="checkRequiredForFieldBasedOnStatusPage('requestModel.total').rules"
                          :messages="checkRequiredForFieldBasedOnStatusPage('requestModel.total').messages"
                          separator="," />
                      </td>
                    </tr>
                  </tbody>
                </table>
                </b-col>
              </b-row>
            </template>
          </Collapse>
        </section>
        <!--        table-1-->
        <section>
          <Collapse>
            <template slot="collapse-header">
              <span class="label_input_header">販売未収条件</span>
            </template>
            <template slot="collapse-body">
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
          </Collapse>
        </section>
        <!--        table-2-->
        <section>
          <Collapse>
            <template slot="collapse-header"><span class="label_input_header">税8%販促費</span></template>
            <template slot="collapse-body">
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
          </Collapse>
        </section>
        <!--        table-3-->
        <section>
          <Collapse>
            <template slot="collapse-header"><span class="label_input_header">税10%販促費</span></template>
            <template slot="collapse-body">
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
          </Collapse>
        </section>
        <!--        table-4-->
        <section>
          <Collapse>
            <template slot="collapse-header"><span class="label_input_header">物流費</span></template>
            <template slot="collapse-body">
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
          </Collapse>
        </section>
      </b-container>
      <div class="btnForRequest">
        <div v-if="modeForCreateRequest == 'create'">
          <b-button
            v-if="statusPage == 2"
            variant="none" class="btn-white"
            @click="showWarningLeavePage = true">
            作成リストに<br>戻る
          </b-button>
          <b-button v-if="mode !== 'confirmCreate'" variant="none"
                    @click="saveAccountReceivable(true)"
                    class="btn-white">下書き<br>保存する
          </b-button>
          <b-button
            v-if="statusPage == 2"
            variant="none"
            @click="showPopupDelete = true"
            class="btn-white">破棄する
          </b-button>
          <b-button
            v-if="statusPage == 2"
            variant="none" class="btn-blue"
            @click="modeForCreateRequest = 'confirmCreate'">
            入力内容を確認する
          </b-button>
        </div>
        <div v-else>
          <b-button 
            v-if="isCanEdit" variant="none" class="btn-white" @click="modeForCreateRequest = 'create'">編集する
          </b-button>
          <b-button v-if="statusPage == 1" variant="none" class="btn-blue" @click="saveAccountReceivable(false)">作成を完了する
          </b-button>
        </div>
      </div>
      <SearchProductDialog v-if="showPopupSearchProduct" v-model="showPopupSearchProduct" @getProduct="getProduct">
      </SearchProductDialog>
      <DeleteDialog v-if="showPopupDelete" v-model="showPopupDelete" title="申請情報を破棄しました" @delete="deleteAccountReceivable">
      </DeleteDialog>
      <SearchSupplyDialog v-if="showPopupSupply" v-model="showPopupSupply" @getSuplier="getSupplier"/>
      <CommentSlideDialog
        v-if="isShowPopupComment"
        v-model="isShowPopupComment" 
        :initComment="initComment"
        :mainItemId="commentRecordCd"
        :mainItemNumber="selectedItemNumber ? selectedItemNumber.itemNumber : null" 
        request-status="作成中"
        :store-g-cd="selectedItemNumber.storeGCd"
        @refer-to-item="referToItem"
        @onAddedComment="onAddedComment"/>
      <LockInEditing v-model="showLockInEditing" :lockUser="item.requestModel.editShainName" :lockStartTime="item.requestModel.startedEditAt"></LockInEditing>
      <TimeoutEditing v-model="showTimeoutEditing"></TimeoutEditing>
      <NormalProcessingDialog v-model="showNormalDialog" :title="messageNormal"></NormalProcessingDialog>
      <ErrorProcessingDialog v-model="showErrorDialog" :title="messageerror"></ErrorProcessingDialog>
      <LeavePageWarning v-model="showWarningLeavePage" @leave-page="returnToList"></LeavePageWarning>
    </section>
  </ValidationObserver>

</template>

<script>
  import convertOptionsVueSelect from '@/helper/convertOptionsVueSelect';
  import AccountReceivableCreateService from '@/services/AccountReceivableCreateService'
  import ProductService from '@/services/ProductService';
  import checkEmpty from "@/mixins/checkEmpty"
  import isEmpty from "lodash/isEmpty";
  import forEach from "lodash/forEach";
  import RoundNumber from "@/mixins/RoundNumber"
  import TokenService from '@/services/TokenService';
  import _ from 'lodash';
  import AccountReceivablesMixin from "./AccountReceivablesMixin";
  import {mapState, mapActions, mapMutations, mapGetters} from 'vuex';
  export default {
    name: "Create",
    mixins: [checkEmpty,RoundNumber,AccountReceivablesMixin],
    data() {
      return {
        isSaveDraft: false,
        showPopupDelete: false,
        modeForCreateRequest:'create',
        // errors: [],
        commentRecordCd: null,
        controlShowSelect: {manageItem: 0, promotion8: 0, promotion10: 0, logisticsCost: 0},
        //supplier
        showPopupSupply: false,
        isShowPopupComment: false,
        selectedItemNumber: null,
        initComment: "",
        useType: null,
        modelNotReuseAccountReceivable: ['targetOn', 'scheduledPaymentOn', 'paymentOn'],
        //product
        currentProductItem: null,
        showPopupSearchProduct: false,

        messageerror:'入力時にエラーが発生しました。<br>※詳細は、ページ上部のエラー表示をご確認ください。',
        messageNormal:"申請を下書き保存しました。"
      };
    },
    computed: {
      // requestStatus() {
      //   let status = this.item.requestModel.mstRequestStatusesResponse.name || "作成中";
      //   return status;
      // },
      stepNumber(){
        if(this.item.requestModel.stepNumber){
          return this.item.requestModel.stepNumber;
        }else{
          return 1;
        }
      },
      disabled(){
        return this.modeForCreateRequest == 'confirmCreate'
      },
      mode(){
        return this.$route.params.mode ? this.$route.params.mode : "create";
        // if(this.$route.params.cd){
        //     switch (this.$route.params.mode) {
        //       case 'reuse':
        //         return 'reuse';
        //       case 'edit':
        //         return 'edit';
        //     }
        // }else{
        //   return 'create'
        // }
        // return ''
      },
    },
    created() {
      switch (this.mode) {
        case 'reuse':
          this.getDetailAccountReceivableReuse({cd:this.$route.params.cd, mode: ''})
          // .then(value =>{
            
          // })
          break;
        case 'edit':
          this.isSaveDraft = false;
          this.fetchRequest("edit");
          break;
        default:
          console.log("go to")
          if (this.$route.query.supplier) {
            this.item.requestModel.torihikiNm = this.$route.query.supplier;
            this.item.requestModel.mstTorihikiCd = this.$route.query.supplierCd;
          }
          break;
      }
    },
    methods: {
      ...mapActions('AccountReceivableCreateModule', ['createAccountReceivables', "addItemListManageItem", "addPromotions8Percents",
        "addPromotions10Percents", "addPromotionsCostLogistics",
        "deleteItemAccountReceivablesDetailRequestList", "addItemComment", "addItemAccountReceivablesProduct", "setValueDenpyoNet", 'final_take_unit_price', 'accrued_amount', 'sales_amount', 'final_marginal_profit', 'final_marginal_profit_ratio'
        ,"setChargeNameForRequestTable", "setDepartmentNameForRequestTable"
        , "setModeAccountReceivableCreate", "resetDataAccountReceivable", "showSlideNormalProcess"
      ]),
      ...mapActions({lockScreen: 'AuthenticationModule/setLockScreen', unlockScreen: 'AuthenticationModule/setUnlockScreen'}),
      ...mapMutations('AccountReceivableCreateModule', [
        'SET_ATTACH_FILE', 'ACCRUED_AMOUNT', 'SET_SUPPLIER_NAME_FOR_TABLE','RESET_STATEMENT1', 
        'SET_SHOW_ERROR_PROCESS_DIALOG', 'SET_SHOW_NORMAL_PROCESS_DIALOG','RESET_STATEMENT','SET_STORREGNM'
      ]),

      // getButtonColumn(label, onClick) {
      //   return {
      //     isButton: true,
      //     isShow: true,
      //     labelButton: label,
      //     disabled: this.checkDisabled,
      //     onClick: onClick
      //   }
      // },
      //handle button controll
        searchProduct(data, index, listName) {
        this.selectedItemNumber = {rowIndex: index, listName: listName, itemNumber: data.itemNumber};
        this.showPopupSearchProduct = !this.showPopupSearchProduct;
      },
      returnToList(){
        if(!this.isSaveDraft) {
          this.$router.push("/request/create");
        } else if(this.isSaveDraft && this.mode != "edit"){
          this.isSaveDraft = false;
          this.$router.push(`/account-receivables/create/${this.item.requestModel.cd}/edit`);
          setTimeout(() => {
            this.fetchRequest("edit");
          }, 50)
        } else {
          this.isSaveDraft = false;
          this.fetchRequest("edit");
        }
      },
      async checkValidateAccountReceivableForm(callback, value) {
        let isValid = await this.$refs.accountReceivableForm.validate();
        if (isValid) {
          this[callback](value);
        } else {
          let err = await this.$refs.accountReceivableForm.errors;
        }
      },
      /* getDetailAccountReceivable(param) {
        this.lockScreen();
        return AccountReceivableCreateService.getDetailAccountReceivableByRequestId(param).then(res => {
          if (res.data) {
            this.item = res
          }
        }).catch(err => {
          this.returnToList();
          console.log(err);
        })
        .then(data => {
          this.unlockScreen()
          return data;
        });
      }, */
      getDetailAccountReceivableReuse(param) {
        return AccountReceivableCreateService.getDetailAccountReceivableByRequestId(param)
        .then((response) => {
          let data = response.data;
          if(!data || !data.cd) {
            // this.item = new
            return
          }
          forEach(this.modelNotReuseAccountReceivable, key => {
            data[key] = "";
          });
          data.cd = null;
          data.requestModel.cd = null;
          data.requestModel.requestedAt = null;
          data.requestModel.settlementNumber = null;
          forEach(["detailsForAccountsReceivablesRequestList",
            "detailsForPromotionalExpensesRequestTax8PercentList",
            "detailsForPromotionalExpensesRequestTax10PercentList",
            "detailsForPromotionalExpensesRequestLogisticFeesList"
          ], listName => {
            if(data[listName] && !isEmpty(data[listName])){
              forEach(data[listName], item => {
                item.cd = null;
                item.isChecked = 0;
                item.commentDetailsRequests = [];
                item.hasComment = null;
                item.accountReceivableDetailCd = null;
              })
            }
          });
          // return data;
          this.item = data;
        })
      },
      async saveAccountReceivable(isDraft){
        let isValidated = await this.$refs.accountReceivableForm.validate();
        if(isValidated){
          forEach(["detailsForAccountsReceivablesRequestList",
            "detailsForPromotionalExpensesRequestTax8PercentList",
            "detailsForPromotionalExpensesRequestTax10PercentList",
            "detailsForPromotionalExpensesRequestLogisticFeesList"
          ], listName => {
            if(this.item[listName] && !isEmpty(this.item[listName])){
              forEach(this.item[listName], (item, index) => {
                item.sortNumber = index;
              })
            }
          });
          // if(!isEmpty(this.item.detailsForAccountsReceivablesRequestList)){
          //     this.item.detailsForAccountsReceivablesRequestList = this.item.detailsForAccountsReceivablesRequestList.map((item,index) => {
          //     item.sortNumber = index;
          //     return item
          //   });
          // }
          // if(!isEmpty(this.item.detailsForPromotionalExpensesRequestLogisticFeesList)){
          //     this.item.detailsForPromotionalExpensesRequestLogisticFeesList = this.item.detailsForPromotionalExpensesRequestLogisticFeesList.map((item,index) => {
          //     item.sortNumber = index;
          //     return item
          //   });
          // }
          // if(!isEmpty(this.item.detailsForPromotionalExpensesRequestTax10PercentList)){
          //     this.item.detailsForPromotionalExpensesRequestTax10PercentList = this.item.detailsForPromotionalExpensesRequestTax10PercentList.map((item,index) => {
          //     item.sortNumber = index;
          //     return item
          //   });
          // }
          // if(!isEmpty(this.item.detailsForPromotionalExpensesRequestTax8PercentList)){
          //     this.item.detailsForPromotionalExpensesRequestTax8PercentList = this.item.detailsForPromotionalExpensesRequestTax8PercentList.map((item,index) => {
          //     item.sortNumber = index;
          //     return item
          //   });
          // }
          // this.setInitialForTable();
          let files = this.item.requestModel.fileModelList;
          delete this.item.requestModel.fileModelList;
          this.item.requestModel.isTemp = isDraft;
          if(this.item.requestModel.cd){
            this.updateAccountReceivable(files,isDraft);
          }else{
            this.createAccountReceivable(files,isDraft);
          }
        }else{
          console.log(this.$refs)
          this.$refs.mainView.scrollTop = 0
        }
      },
      createAccountReceivable(files, isDraft) {
        this.lockScreen();
        // let modeCreate = this.$route.params.mode == 'edit' ? 'edit' : '' ;
        AccountReceivableCreateService.accountReceivables(files,this.item)
        .then(response => {
          if(response.data){
            this.item = response.data;
            this.isSaveDraft = isDraft;
            this.messageNormal = "申請を下書き保存しました。";
            this.showNormalDialog = true;
            // setTimeout(()=>{
            //   if (isDraft) {
                
            //       this.getDetailAccountReceivable({cd:res.data.requestModel.cd, mode:modeCreate}).then(value =>{
            //     if(value){
                  
            //     } else {
            //       this.returnToList();
            //     }
            // },2700);
            this.unlockScreen();
            }
        }).catch(err => {
          this.unlockScreen();
          this.showErrorDialog = true;
          console.log(err)
        });
      },
      updateAccountReceivable(files, isDraft) {
        this.lockScreen();
        AccountReceivableCreateService.accountReceivablesUpdate(files, this.item)
        .then(response => {
          if (response.data) {
            this.item = response.data;
            this.isSaveDraft = isDraft;
            this.messageNormal = "申請を下書き保存しました。";
            this.showNormalDialog = true;
            // setTimeout(()=>{
            //   if (!isDraft) {
            //     this.returnToList();
            //   }
            // },2700)
          }
        }).catch(err => {
          this.showErrorDialog = true;
        })
        .then(() => {
          this.unlockScreen();
        });
      },
      deleteAccountReceivable() {
      if (this.item.requestModel.cd && this.mode == 'edit') {
        this.messageNormal = "申請情報を破棄しました。"
        this.showPopupDelete = false;
        this.lockScreen()
        AccountReceivableCreateService.deleteRequest(this.item.requestModel.cd).then(res => {
          if (res.data) {
            this.unlockScreen();
            this.showNormalDialog = true ;
            setTimeout(()=>{
              this.returnToList();
            },2700)

          }
        }).catch(err => {
          this.unlockScreen();
          this.showErrorDialog = true;
        })
      }
  },
    }
  }
</script>

<style lang="scss" scoped>
/*   .main-vselect {
    background-color: white;
    width: 100%;
  }

  .commentBtn {
    background-color: white;
  }
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
          &.change-value {
            background: #FCE655;
          }
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
} */
@import "@/assets/scss/pages/_request-detail.scss";
</style>
