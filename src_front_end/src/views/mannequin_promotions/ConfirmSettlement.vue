<template>
  <ValidationObserver ref="requestForm" v-slot="{ errors, invalid, validated }">
    <section
            :class="[
        'request',
        { validated: validated },
        { invalid: invalid },
      ]"
    >
      <div class="request-header">
        <div class="request-header-container">
          <div class="title">
            <h4>販売未収金申請</h4>
            <div class="number">
              <label class="application-num">申請番号:{{requestNumber}}</label>
              <label class="payment-num">決済番号:{{settlementNumber}}</label>
            </div>
            <div class="icon">
              <i class="fas fa-recycle" @click="reuse"></i>
            </div>
            <div class="ml-auto">
              <ApproveRequestDialog
                v-if="item.requestModel.mstApprovalFlows && item.requestModel.mstApprovalFlows.cd"
                :is-in-comment-modal="false"
                :step-number="item.requestModel.stepNumber"
                :approval-cd="item.requestModel.mstApprovalFlows && item.requestModel.mstApprovalFlows.cd"
                :request-status="requestStatus"
                :requested-at="item.requestModel.requestedAt"
              ></ApproveRequestDialog>
            </div>
          </div>
          <div :class="['request-header_status', {'send-back': isRequestSendBack}]">
            <div>
              <span>{{isRequestSendBack ? "差し戻し" : requestStatus}}</span>
              <span v-show="mode == 'edit'"><br/>（編集中）</span>
            </div>
          </div>
        </div>
      </div>
      <b-container fluid class="request-body customScrollBar" ref="mainView">
        <b-row
                v-if="validated && handleErrors(errors).length > 0"
                class="errorsMessagesField"
        >
          <b-col v-for="(error, index) in handleErrors(errors)" :key="index" sm="12">
            <b>エラー：{{ error }}</b>
          </b-col>
        </b-row>
        <collapse>
          <template v-slot:collapse-header>販促金管理項目</template>
          <template v-slot:collapse-body>
            <b-row class="px-4 py-1">
              <b-col md="12" lg="12" class="pl-0">
                <b-row no-gutters class="p-0 m-0 flex-row">
                  <InputSskf
                          v-model="item.subject"
                          id="customer-name"
                          class="subject_mannequin"
                          name="表題"
                          :max="50"
                          :rules="{ required: true }"
                          :disabledField="disabled"
                          :messages="{ required: '表題を確認して下さい' }"
                  />
                  <b-button
                          variant="none"
                          :disabled="disabled"
                          class="btn_grey btn_getSupply ml-4"
                          @click="showPopupSupply = true"
                  >取引先名検索
                  </b-button>
                </b-row>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="6" lg="6" class="pl-0">
                <InputSskf
                        v-model="item.requestModel.torihikiNm"
                        name="取引先名"
                        id="supplier_name"
                        :disabled-field="true"
                        :rules="{ required: true }"
                        :maxlength="80"
                        :messages="{required: '取引先名を確認して下さい',                       
                  }"
                />
              </b-col>
              <b-col md="6" lg="6">
                <SelectSskf 
                      :isNormalSelect="false"
                      name="店舗グループ名" id="storegnm_input"
                      :rules="{required: true}" v-model="item.storeGNm"
                      :disabledField="disabled"
                      optionsLabel="storeGNm"
                      optionsValue="storeGNm"
                      :messages="{required: '店舗グループ名を確認して下さい'}"
                      @search="(value)=>{searchStoreGnm(value)}"
                      @change="changeStoreGnm"
                      :options="storeGNmOptions" />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="12" class="pl-0 mannequinTable">
                <table>
                  <thead>
                  <tr>
                    <th>実施店舗</th>
                    <th>実施日</th>
                    <th class="mannequinTableActions" align="left">
                      <button v-if="checkEmpty(item.implementationStoresForRequestMannequinPromotionRequestList) &&
                        isShowImplementationInformationPlusIcon"
                        @click="addItemImplementationStore(0)">
                        <i class="fas fa-plus-circle"></i>
                      </button>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for=" (elm,index) in item.implementationStoresForRequestMannequinPromotionRequestList" :key="index">
                    <td>
                      <InputSskf
                        v-model="elm.storeNameOfEvent"
                        name=""
                        :id="`${'storeNameOfEvent' + index}`"
                        :rules="{ required: true }"
                        :maxlength="50"
                        :disabledField="disabled"
                        class="fieldDependent"
                        :messages="{required: '実施店舗を確認して下さい'}"                   
                />
                    </td>
                    <td>
                      <DateSskf
                        id="runEventOn"
                        name=""
                        v-model="elm.runEventOn"
                        valueType="YYYY-MM-DD"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        class="fieldDependent"
                        :messages="{ required: '実施日を確認して下さい'}"
                        formatDate="YYYY/MM/DD"/>
                    </td>
                    <td align="left" class="mannequinTableActions">
                      <button v-if="isShowImplementationInformationPlusIcon" @click="addItemImplementationStore(index)">
                        <i class="fas fa-plus-circle"></i>
                      </button>
                      <button v-if="isShowImplementationInformationCopyIcon" @click="copyItemImplementationStore(index)">
                        <i class="fas fa-copy"></i>
                      </button>
                      <button v-if="isShowImplementationInformationMinusIcon" @click="removeItemImplementationStore(index)">
                        <i class="fas fa-minus-circle"></i>
                      </button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="12" lg="12" class="pl-0">
                <TextareaSskf
                        id="remarks"
                        name="備考"
                        v-model="item.contentOfImplementationStores"
                        :row="6"
                        placeholder="..."
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :messages="{ required: '内容を確認して下さい' }"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <!--              12-->
              <b-col md="4" lg="4" class="pl-0">
                <InputNumbericSskf
                        v-model="item.requestModel.billingAmount"
                        id="input-billingAmount"
                        name="請求金額(円)"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :max="999999999999"
                        :min="1"
                        :messages="{ required: '請求金額を確認して下さい' }"
                />
              </b-col>
              <!--              13-->
              <b-col md="4" lg="4">
                <DateSskf
                        v-model="item.requestModel.billingOn"
                        id="input-account_number"
                        name="請求日"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :disabled-date="canInputDate"
                        :messages="{ required: '請求日を確認して下さい' }"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <!--              14-->
              <b-col md="4" lg="4" class="pl-0">
                <SelectSskf
                        v-model="item.requestModel.approvalFlowDetailsRequest.bumonCd"
                        id="input-department-name"
                        name="部門名"
                        :isNormalSelect="false"
                        optionsLabel="bumonNm"
                        optionsValue="bumonCd"
                        :options="departmentList"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        @change="changeDeparment"
                        :messages="{ required: '部門名を確認して下さい' }"
                />
              </b-col>
              <!--              15-->
              <b-col md="4" lg="4">
                <SelectSskf
                        v-model="item.requestModel.approvalFlowDetailsRequest.shainCd"
                        id="input-application-name"
                        name="申請者名"
                        :isNormalSelect="false"
                        optionsLabel="shainNm"
                        optionsValue="shainCd"
                        :options="applicationList"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        @change="changeApplication"
                        :messages="{ required: '申請者名を確認して下さい' }"
                        
                />
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
        <Collapse>
          <template slot="collapse-header">
            <span class="label_input_header">支払情報</span>
          </template>
          <template slot="collapse-body">
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <SelectSskf
                        v-model="item.requestModel.paymentPlace"
                        :isNormalSelect="false"
                        name="支払場所"
                        id="payment_place"
                        :changed="invalid.changed"
                        optionsLabel="bumonNm"
                        optionsValue="bumonNm"
                        :rules="{ required: false }"
                        :messages="{ required: '支払場所を確認して下さい' }"
                        :options="paymentPlaceList"
                        :disabledField="disabled"
                />
              </b-col>
              <b-col md="4" lg="4" class="pr-0">
                <SelectSskf
                        v-model="item.requestModel.paymentMethod"
                        name="支払い方法"
                        id="billing_date"
                        :changed="invalid.changed"
                        :isNormalSelect="true"
                        :rules="{ required: false }"
                        :disabledField="disabled"
                        :messages="{ required: '支払い方法を確認して下さい' }"
                        :options="paymentMethodList"
                />
              </b-col>
              <b-col md="4" lg="4" class="pl-0" v-if="item.requestModel.paymentMethod == 'その他'">
                <InputSskf
                        v-model="item.requestModel.paymentOtherMethod"
                        id="input-horizontal"
                        name
                        :maxlength="200"
                        class="fieldDependent"
                        :disabledField="false"
                        :rules="{ required: true }"
                        :messages="{ required: 'その他支払い方法を確認して下さい' }"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <DateSskf
                        v-model="item.requestModel.scheduledPaymentOn"
                        valueType="YYYY-MM-DD"
                        id="payment_scheduled_date"
                        :changed="invalid.changed"
                        :rules="{ required: true }"
                        :messages="{ required: '支払予定日を確認して下さい' }"
                        name="支払予定日"
                        :disabledField="disabled"
                        formatDate="YYYY/MM/DD"
                />
              </b-col>
              <b-col md="4" lg="4" class="pr-0">
                <DateSskf
                        v-model="item.requestModel.paymentOn"
                        id="payment_date"
                        :changed="invalid.changed"
                        :rules="{ required: isCanInputPaymentOn }"
                        :messages="{ required: '支払日を確認して下さい' }"
                        name="支払日"
                        :disabledField="!isCanInputPaymentOn"
                        formatDate="YYYY/MM/DD"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="8" lg="8" class="pl-0 pr-0">
                <InputSskf
                        v-model="item.requestModel.paymentDestination"
                        id="input-payment_destination"
                        name="支払先"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :maxlength="40"
                        :messages="{ required: '支払先を確認して下さい' }"
                />
              </b-col>
              <b-col md="4" lg="4">
                <InputSskf
                        v-model="item.requestModel.bankName"
                        id="input-account_number"
                        name="銀行名"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :maxlength="16"
                        :messages="{ required: '銀行名を確認して下さい' }"
                />
              </b-col>
            </b-row>
            <b-row class="px-4 py-1 mt-3">
              <b-col md="4" lg="4" class="pl-0 pr-0">
                <InputSskf
                        v-model="item.requestModel.bankAccountNumber"
                        id="input-bankAccountNumber"
                        :disabledField="disabled"
                        name="口座番号"
                        :rules="{ required: true }"
                        :maxlength="10"
                        :messages="{ required: '口座番号を確認して下さい' }"
                />
              </b-col>
              <b-col md="8" lg="8">
                <InputSskf
                        v-model="item.requestModel.bankAccountName"
                        id="bankAccountName"
                        name="口座名義"
                        :rules="{ required: true }"
                        :disabledField="disabled"
                        :maxlength="40"
                        :messages="{ required: '口座名義を確認して下さい' }"
                />
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotalForTenPercent"
                          class-field="fieldPayment"
                          v-model="item.requestModel.itemTotalForTenPercent"
                          :messages="{ required: '10%対象品目計を確認して下さい',validNumber:'10%対象品目計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="itemTotal"
                          class-field="fieldPayment"
                          v-model="item.requestModel.itemTotal"
                          :messages="{ required: '品目合計を確認して下さい',validNumber:'品目合計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotalForTenPercent"
                          class-field="fieldPayment"
                          v-model="item.requestModel.consumptionTaxTotalForTenPercent"
                          :messages="{ required: '10%消費税計を確認して下さい',validNumber:'10%消費税計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="consumptionTaxTotal"
                          class-field="fieldPayment"
                          v-model="item.requestModel.consumptionTaxTotal"
                          :messages="{ required: '消費税合計を確認して下さい',validNumber:'消費税合計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
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
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="totalForTenPercent"
                          class-field="fieldPayment"
                          v-model="item.requestModel.totalForTenPercent"
                          :messages="{ required: '10%対象品目・消費税合計を確認して下さい',validNumber:'10%対象品目・消費税合計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                      <td>
                        <InputNumbericSskf
                          id="total"
                          class-field="fieldPayment"
                          v-model="item.requestModel.total"
                          :messages="{ required: '総合計を確認して下さい',validNumber:'総合計を確認して下さい'}"
                          :rules="{required: true,validNumber:{ minNumber:-999999999999, maxNumber:999999999999}}"
                          separator=","
                          :disabled-field="true"/>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </b-col>
            </b-row>
          </template>
        </Collapse>
        <collapse>
          <template v-slot:collapse-header>税10%販促費</template>
          <template v-slot:collapse-body>
            <data-table
                    v-if="!item.requestMannequinPromotionDetailsRequestList ||item.requestMannequinPromotionDetailsRequestList.length > 0 || $route.path.includes('apply')"
                    key-list="requestMannequinPromotionDetailsRequestList"
                    :columns="tax10MannequinPromotionColumns"
                    :data="item.requestMannequinPromotionDetailsRequestList"
                    :disabled="false"
                    :initial-max-item-number="3000"
                    :request-status="requestStatus"
                    @add-item="addItem"
                    @copy-item="copyItem"
                    @remove-item="removeItem"
            >
              <td slot="tfoot" colspan="5" align='right'>小計:</td>
              <!-- //57  -->
              <td slot="tfoot"><InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('mfYen')}`"
                        id="input-sumFieldForTable10Percent"
                        class="fieldDependent manneAutofield"
                        name=""
                /></td>
              <td slot="tfoot" colspan="2">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('hyojyunYen')}`"
                        id="input-hyojyunYen"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('salesOfSeihan')}`"
                        id="input-salesOfSeihan"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('standardMarginalProfit')}`"
                        id="input-standardMarginalProfit"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('accruedAmount')}`"
                        id="input-accruedAmount"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('costsOfMannequin')}`"
                        id="input-costsOfMannequin"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('finalMarginalProfit')}`"
                        id="input-finalMarginalProfit"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
            </data-table>
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
                class="btn-blue" variant="none" @click="showCommentModal()">
                決済確認する
            </b-button>
        </template>
        <template v-else>
            <b-button class="btn-white" variant="none" @click="showWarningLeavePage = true">
                戻る
            </b-button>
            <b-button class="btn-white" variant="none" @click="updateMannequinPromotions">
                保存する
            </b-button>
        </template>
    </div>
    </section>
    <CommentConfirmRequest
      v-model="isShowCommentModalForSettlement"
      :request-comment="commentForSettlement"
      title="決済確認します。"
      btn-submit-label="決済確認する"
      :step-number="item.requestModel.stepNumber"
      @cancel="closeConfirmCommentPopup"
      @submit="settlement"></CommentConfirmRequest>
    <SearchSupplyDialog v-if="showPopupSupply" v-model="showPopupSupply" @getSuplier="getSupplier"/>
    <SearchProductDialog v-if="showPopupSearchProduct"  v-model="showPopupSearchProduct"  @getProduct="getProduct" />
    <LeavePageWarning v-model="showWarningLeavePage" @leave-page="returnToList"></LeavePageWarning>
    <NormalProcessingDialog v-model="showNormalDialog" title="決済確認しました。"/>
    <ErrorProcessingDialog v-model="showErrorDialog" :title="messageerror"></ErrorProcessingDialog>
    <LockInEditing v-model="showLockInEditing" :lockUser="item.requestModel.editShainName" :lockStartTime="item.requestModel.startedEditAt"></LockInEditing>
      <TimeoutEditing v-model="showTimeoutEditing"></TimeoutEditing>
  </ValidationObserver>
</template>

<script>
import {mapActions} from 'vuex';
import TokenService from '@/services/TokenService'
import MannequinPromotionMixin from './MannequinPromotionMixin';
import RequestService from '@/services/RequestService';
import isTablet from "@/helper/isTabletDevice";
  export default {
    name: "Approve",
    mixins: [MannequinPromotionMixin],
    data() {
      return {
        isShowCommentModalForSettlement: false,
        commentForSettlement: ""
      }
    },
    computed:{
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
      ...mapActions("AutoCalculateOnTable", ['calculateStandardMarginalProfitRatio','calculateSalesOfSeihan']),
      returnToListInprogress() {
        this.$router.push("/request/list");
      },
      returnToList() {
          this.$router.push("/request/confirm-settlement");
      },
      showCommentModal() {
        this.checkUpdatedInfo(() => {
          this.isShowCommentModalForSettlement = true;
        });
      },
      closeConfirmCommentPopup(comment) {
        this.isShowCommentModalForSettlement = false;
        this.commentForSettlement = comment;
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
    }
  };
</script>

<style lang="scss" scoped>
@import "@/assets/scss/pages/_request-detail.scss";
</style>