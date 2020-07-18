<template>
  <ValidationObserver ref="requestForm" v-slot="{ errors, invalid, validated }" class="mannequin_promotions">
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
              <!-- <label class="application-num">申請番号:{{requestNumber}}</label>
              <label class="payment-num">決済番号:{{settlementNumber}}</label> -->
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
      <b-container fluid class="request-mannequin customScrollBar" ref="mainView">
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
                          :disabledField="disabledAfterAccountingCheck"
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
                    <th v-if="checkEmpty(item.implementationStoresForRequestMannequinPromotionRequestList)" class="mannequinTableActions" align="left">
                      <button @click="addItemImplementationStore(0)">
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
                        :rules="{ required: true, max: 50 }"
                        :maxlength="50"
                        :disabledField="disabled"
                        class="fieldDependent"
                        :messages="{required: '実施店舗を確認して下さい', max: '実施店舗を確認して下さい'}"                   
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
                      <button @click="addItemImplementationStore(index)">
                        <i class="fas fa-plus-circle"></i>
                      </button>
                      <button @click="copyItemImplementationStore(index)">
                        <i class="fas fa-copy"></i>
                      </button>
                      <button v-if="item.implementationStoresForRequestMannequinPromotionRequestList.length > 1" @click="deleteImplementStoreManneuqin(index)">
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
        <collapse>
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
        </collapse>
        <collapse>
          <template v-slot:collapse-header>税10%販促費</template>
          <template v-slot:collapse-body>
            <data-table
                    v-if="!item.requestMannequinPromotionDetailsRequestList ||item.requestMannequinPromotionDetailsRequestList.length > 0 || $route.path.includes('/create')"
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
              <td slot="tfoot" colspan="5" class="text-right">小計:</td>
              <!-- //57  -->
              <td slot="tfoot" class="footerColor"><InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('mfYen')}`"
                        id="input-sumFieldForTable10Percent"
                        class="fieldDependent manneAutofield"
                        name=""
                /></td>
              <td slot="tfoot" class="footerColor">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('hyojyunYen')}`"
                        id="input-hyojyunYen"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td></td>
              <td slot="tfoot" class="footerColor">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('salesOfSeihan')}`"
                        id="input-salesOfSeihan"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot"  class="footerColor">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('standardMarginalProfit')}`"
                        id="input-standardMarginalProfit"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot"  class="footerColor">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('accruedAmount')}`"
                        id="input-accruedAmount"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot"  class="footerColor">
                <InputNumbericSskf
                        :value="`${sumFieldForTable10Percent('costsOfMannequin')}`"
                        id="input-costsOfMannequin"
                        class="fieldDependent manneAutofield"
                        name=""
                />
              </td>
              <td slot="tfoot"  class="footerColor">
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
      </b-container>
      <div class="request-footer btnForRequest">
          <template v-if="modeForCreateRequest !== 'confirmCreate'">
            <b-button variant="none" class="btn-white" @click="showWarningLeavePage = true">作成リストに<br>戻る </b-button>
            <b-button  variant="none" class="btn-white" @click="saveMannequinPromotions(true)"> 下書き <br>保存する </b-button>
            <b-button  variant="none" class="btn-white" @click="showPopupDelete = true">破棄する</b-button>
            <b-button  variant="none" class="btn-blue" @click="modeForCreateRequest = 'confirmCreate' " >入力内容を <br> 確認する</b-button>
        </template>
        <template v-else>
            <b-button  variant="none" class="btn-white p-3" @click="modeForCreateRequest = $route.params.mode ">編集する</b-button>
            <b-button  variant="none" class="btn-blue p-3" @click="saveMannequinPromotions(false)">作成を完了する</b-button>
        </template>
      </div>
    </section>
    <SearchSupplyDialog v-if="showPopupSupply" v-model="showPopupSupply" @getSuplier="getSupplier"/>
    <SearchProductDialog v-if="showPopupSearchProduct"  v-model="showPopupSearchProduct"  @getProduct="getProduct" />
    <LeavePageWarning v-model="showWarningLeavePage" @leave-page="returnToList"></LeavePageWarning>
    <NormalProcessingDialog v-model="showNormalDialog" title="申請書の作成を完了しました。 "/>
    <ErrorProcessingDialog v-model="showErrorDialog" title="エラーが発生しました。<br/>内容を確認し、再度送信をしてください。"></ErrorProcessingDialog>
    <DeleteDialog v-if="showPopupDelete" :title="messageDelete" v-model="showPopupDelete" @delete="deleteActionForMannequin">
      </DeleteDialog>
  </ValidationObserver>
</template>

<script>
import {mapActions} from 'vuex';
import TokenService from '@/services/TokenService';
import MannequinPromotionMixin from './MannequinPromotionMixin';
import isEmpty from "lodash/isEmpty";
import sortBy from "lodash/sortBy";
  export default {
    name: "Create",
    mixins: [MannequinPromotionMixin],
    data() {
      return {
        isSaveDraft: false,
        messageDelete:'実施情報を削除しますか？',
        indexOfimplementStoreMannequin: null,
      }
    },
    watch:{
      showPopupDelete(newValue){
        if(!newValue){
          this.indexOfimplementStoreMannequin = null;
        }
      }
    },
    methods:{
      deleteImplementStoreManneuqin(cd){
        this.indexOfimplementStoreMannequin = cd;
        this.messageDelete = "実施情報を削除しますか？"
        this.showPopupDelete = true;
      },
      returnToList(){
        if(!this.isSaveDraft) {
          this.$router.push("/request/create");
        } else if(this.isSaveDraft && this.mode != "edit"){
          this.isSaveDraft = false;
          this.$router.push(`/mannequin-promotions/create/${this.item.requestModel.cd}/edit`);
        } else {
          this.isSaveDraft = false;
          this.fetchRequest("edit");
        }
      },
    },
    created() {
      this.modeForCreateRequest = 'create';
      switch (this.$route.params.mode) {
        case 'reuse':
          this.modeForCreateRequest = 'reuse';
          this.getDetailMannequinPromotion(this.$route.params.cd)
          // .then(value =>{
          //     let list = value ;
          //     if(!isEmpty(list) && list.length > 1){
          //     list = sortBy(list, item => {
          //     return item.sortNumber;
          //   })}
          // })
          break;
        case 'edit':
          this.isSaveDraft = false;
          this.modeForCreateRequest = 'edit';
          this.fetchRequest("edit")
          break;
        default:
          
      console.log("thaoht 1")
          if (this.$route.query.supplier) {
            this.item.requestModel.torihikiNm = this.$route.query.supplier;
            this.item.requestModel.mstTorihikiCd = this.$route.query.supplierCd;
          }
          break;
      }
      if(this.modeForCreateRequest !== 'edit'){
        let user = TokenService.getUserLogin()
        this.item.requestModel.approvalFlowDetailsRequest.bumonNm = user.bumonNm;
        this.item.requestModel.approvalFlowDetailsRequest.bumonCd = user.bumonCd;
        this.item.requestModel.approvalFlowDetailsRequest.shainNm = user.shainNm;
        this.item.requestModel.approvalFlowDetailsRequest.shainCd = user.sub;
      }
      
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

          div.icon {
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
          color: #44a5cc;
          border: 2px solid #44a5cc;
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

    .request-body {
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

        .form-control-plaintext {
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
        padding: 0.3rem 1rem;
        margin-left: 2rem;
        width: auto;
        height: auto;
      }
    }

    .change-value {
      background: #fce655;
    }
    .table th, .table td{
      padding: 0;
    }
    
  }
</style>9