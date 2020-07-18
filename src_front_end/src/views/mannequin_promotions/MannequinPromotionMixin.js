import AccountReceivableCreateService from '@/services/AccountReceivableCreateService';
import { RequestStatuses } from "@/constants/RequestStatuses";
import MannequinPromotionService from '@/services/MannequinPromotionService';
import RequestService from '@/services/RequestService';
import { ApprovalFlowDetail } from "@/models/ApprovalFlowModel";
import {
  ImplementationStoresForRequestMannequinPromotionRequestItem,
  RequestMannequinPromotionDetailsRequestListItem,
  MannequinPromotionsRequestModel
} from "@/models/MannequinPromotionsRequestModel";
import {mapState, mapActions} from 'vuex';
import userRole from "@/helper/checkRoleUserLogin";
import formatDigitNumber from "@/helper/formatDigitNumber";
import isTablet from "@/helper/isTabletDevice";
import sortBySortNumber from "@/helper/sort";
import round from "lodash/round";
import cloneDeep from "lodash/cloneDeep";
import debounce from "lodash/debounce";
import isEmpty from "lodash/isEmpty";
import includes from "lodash/includes";
import isArray from "lodash/isArray";
import some from "lodash/some";
import RequestForPrint from "@/templates/request-detail";
import AutoFieldOfPaymentForTotal from "@/mixins/AutoFieldOfPaymentForTotal.js";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
import RouteNavigationGuardsForRequestDetail from "@/mixins/RouteNavigationGuardsForRequestDetail";
export default {
  mixins: [getStatusOfPageDetailRequest, RouteNavigationGuardsForRequestDetail, AutoFieldOfPaymentForTotal],
  data() {
    return {
      showPopupSupply: false,
      showPopupSearchProduct: false,
      selectedItemNumber: null,
      showPopupDelete:false,

      // Processing Dialog
      messageerror: "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。",
      messagenormal: "",
      showNormalDialog: false,
      showErrorDialog: false,
      showLockInEditing: false,
      showTimeoutEditing: false,
      showWarningLeavePage: false,
      timeoutForEditRequest: null, // setTimeout
      isLocked: false,
      // data of page
      item : new MannequinPromotionsRequestModel(),
      storeGNmOptions:[],
      modeForCreateRequest: '',
    };
  },
  computed: {
    //...mapState('AccountReceivableCreateModule', ['departmentList','applicationList','paymentMethodList','paymentPlaceList']),
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser,
      departmentList: state => state.DropdownModule["departmentList"],
      applicationList: state => state.DropdownModule["applicationList"],
      paymentMethodList: state => state.DropdownModule["paymentMethodList"],
      paymentPlaceList: state => state.DropdownModule["paymentPlaceList"],
    }),
    isFirstTimeApply() {
      return !this.item.requestModel.requestedAt || this.item.requestModel.requestedAt == "";
    },
    isRequestSendBack() {
      return this.item.requestModel.isSentBack == 1;
    },
    isShowSearchSupplierSearchButton() {
      return includes([2, 4, 6, 9], this.statusPage);
    },
    isShowImplementationInformationPlusIcon() {
      return includes([2, 4, 6, 9], this.statusPage);
    },
    isShowImplementationInformationCopyIcon() {
      return includes([2, 4, 6, 9], this.statusPage);
    },
    isShowImplementationInformationMinusIcon() {
      return includes([2, 4, 6, 9], this.statusPage) &&
        this.item.implementationStoresForRequestMannequinPromotionRequestList &&
        this.item.implementationStoresForRequestMannequinPromotionRequestList.length >= 2;
    },
    isCanReUse() {
      return ((this.statusPage == 3 || this.statusPage == 5) && this.item.requestModel.requestedAt && this.item.requestModel.requestedAt != "") ||
        this.statusPage == 7 || this.statusPage == 12 || this.statusPage == 14 || this.statusPage == 15;
    },
    isCanEdit() {
      if(this.isRequestSendBack) {
        return !isTablet() &&
            this.loginUser &&
            this.loginUser.roles &&
            (
                (this.loginUser.roles.includes('applyPersons') && this.item.requestModel.stepNumber == 1) ||
                (this.loginUser.roles.includes("firstApprovalPersons") && this.item.requestModel.stepNumber <= 2) ||
                (this.loginUser.roles.includes("secondApprovalPersons") && this.item.requestModel.stepNumber <= 3) ||
                (this.loginUser.roles.includes("thirdApprovalPersons") && this.item.requestModel.stepNumber <= 4)
            );
      } else {
        switch(this.requestStatus) {
          case "作成中":
            return true;
          case "確認待ち":
            return true;
          case "申請待ち":
            return this.isFirstTimeApply ||
                (!this.isFirstTimeApply && this.loginUser && this.loginUser.roles && this.loginUser.roles.includes('applyPersons'));
          case "承認待ち":
            return !isTablet() &&
                this.loginUser &&
                this.loginUser.roles &&
                (
                    (this.loginUser.roles.includes("firstApprovalPersons") && this.item.requestModel.stepNumber == 2) ||
                    (this.loginUser.roles.includes("secondApprovalPersons") && this.item.requestModel.stepNumber <= 3) ||
                    (this.loginUser.roles.includes("thirdApprovalPersons") && this.item.requestModel.stepNumber <= 4) ||
                    (this.loginUser.roles.includes("settlementApprovalPersons") && this.item.requestModel.stepNumber <= 5) ||
                    (this.loginUser.roles.includes("confirmSettlementPersons") && this.item.requestModel.stepNumber >= 4)
                );
          case "決済確認待ち":
            return this.loginUser && this.loginUser.roles && this.loginUser.roles.includes("confirmSettlementPersons");
          default:
            return false;
        }
      }
    },
    isCanPrint() {
      return !isTablet() && this.loginUser && this.loginUser.roles &&
        includes(this.loginUser.roles, "confirmSettlementPersons") &&
        ((this.statusPage == 7 && this.item.requestModel.stepNumber > 3) || this.statusPage == 12 || this.statusPage == 15);
    },
    tax10MannequinPromotionColumns() {
      let columns = [];
      if(includes([2, 4, 6, 7], this.statusPage) || (this.requestStatus == "承認待ち" && !isTablet())) {
        columns.push(this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      columns.push(...[
        //45
        this.getSelectColumnByKey("品目コード", "hinmokuCd", true),
        //46
        this.getInputColumnByKey("品目略称", "hinmokuRnm", true),
        //47
        this.getInputColumnByKey("荷姿", "nisugata", false),
        //48
        this.getNumericColumnByKey("入数", "irisu", 1, "initialIrisu"),
        //49
        this.getNumericColumnByKey("生産者<br>価格(円)", "mfYen", 0.01, "initialMfYen"),
        //50
        this.getNumericColumnByKey("標準原価<br>(営業)(円)", "hyojyunYen", 0.01, "initialHyojyunYen"),
        //51
        this.getNumericColumnByKey("数量<br>(ケース)", "numbersOfCases", 1, "initialNumbersOfCases"),
        //52
        this.getNumericColumnByKey("売上(生販)<br>(円)", "salesOfSeihan", 1, "initialSalesOfSeihan"
        ),
        //53
        this.getNumericColumnByKey("標準限界利益額<br>(営業)(円)", "standardMarginalProfit", 1, "initialStandardMarginalProfit"
        ),
        //54
        this.getNumericColumnByKey("未収金額<br>(円)", "accruedAmount",  1, "initialAccruedAmount"),
        //55
          this.getNumericColumnByKey("マネキン費用<br>(円)", "costsOfMannequin", 1, "initialCostsOfMannequin"
        ),
        //56
        this.getNumericColumnByKey("最終限界<br>利益額(円)", "finalMarginalProfit", 1, "initialFinalMarginalProfit"),
      ]);
      return columns;
    },
    mode() {
      return this.$route.params.mode || "view";
    },
    requestCd() {
      return this.$route.params.cd;
    },
    requestStatus() {
      let status = "作成中";
      if(this.mode == "reuse" || this.$route.path.includes("/create")) {
        status = "作成中";
      } else {
        status = this.item.requestModel.mstRequestStatusesResponse.name || "";
      }
      return status;
    },
    requestNumber(){
      return formatDigitNumber(this.item.requestModel.requestNumberResponse?.cd || "")
    },
    settlementNumber(){
      return this.item.requestModel.settlementNumber || ""
    },
    // disabled2() {
    //   if(this.modeForCreateRequest == 'confirmCreate' || this.modeForCreateRequest == 'view'){
    //     return true
    //   }else{
    //     return false
    //   }
    // },
    disabled() {
      // console.log(this.statusPage);
      // return this.statusPage == 2 || this.statusPage == 4 || this.statusPage == 6 || this.statusPage == 9;
      return !(this.mode == "reuse" || this.mode == "edit" || this.$route.path.includes("/create"));
    },
    disabledAfterAccountingCheck() {
      // return this.statusPage == 2 || this.statusPage == 4 || this.statusPage == 6 || this.statusPage == 9 || this.statusPage == 11;
      return !includes([2, 4, 6, 9, 11], this.statusPage);
    },
    isCanInputPaymentOn() {
      return this.loginUser &&
        this.loginUser.bumonCd == 4000 &&
        (
          (this.statusPage == 9 && this.item.requestModel.stepNumber > 3) ||
          (this.statusPage == 11 || this.statusPage == 13)
        );
    },
  },
  watch: {
    itemTotalForEightPercent(newValue) {
      this.item.requestModel.itemTotalForEightPercent = newValue;
      this.setInitialForPaymentTable('initialItemTotalForEightPercent',newValue);
    },
    itemTotalForTenPercent(newValue) {
      this.item.requestModel.itemTotalForTenPercent = newValue;
      this.setInitialForPaymentTable('initialItemTotalForTenPercent',newValue);
    },
    itemTotal(newValue) {
      this.item.requestModel.itemTotal = newValue;
      this.setInitialForPaymentTable('initialItemTotal',newValue);
    },
    consumptionTaxTotalForEightPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForEightPercent = newValue;
      this.setInitialForPaymentTable('initialConsumptionTaxTotalForEightPercent',newValue);
    },
    consumptionTaxTotalForTenPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForTenPercent = newValue;
      this.setInitialForPaymentTable('initialConsumptionTaxTotalForTenPercent',newValue);
    },
    consumptionTaxTotal(newValue) {
      this.item.requestModel.consumptionTaxTotal = newValue;
      this.setInitialForPaymentTable('initialConsumptionTaxTotal',newValue);
    },
    totalForEightPercent(newValue) {
      this.item.requestModel.totalForEightPercent = newValue;
      this.setInitialForPaymentTable('initialTotalForEightPercent',newValue);
    },
    totalForTenPercent(newValue) {
      this.item.requestModel.totalForTenPercent = newValue;
      this.setInitialForPaymentTable('initialTotalForTenPercent',newValue);
    },
    total(newValue) {
      this.item.requestModel.total = newValue;
      this.setInitialForPaymentTable('initialTotal',newValue);
    },
    "item.requestModel.approvalFlowDetailsRequest.bumonCd"(newValue, oldValue) {
      if(newValue && newValue != oldValue) {
        this.getListApplication(newValue)
        .then(() => {
          let isExistInList = some(this.applicationList, o => {
            return o.shainCd == this.item.requestModel.approvalFlowDetailsRequest.shainCd;
          });
          if(!isExistInList){
            this.item.requestModel.approvalFlowDetailsRequest.shainCd = null;
          }
        });
      }
    },
    showNormalDialog(newValue, oldValue) {
      if(oldValue && !newValue) {
        setTimeout(() => {
          if(this.showConfimSendBackModal){
            this.$router.push("/request/apply");
          } else {
            this.returnToList();
          }
        }, 600);
      }
    }
  },
  created() {
    if(!this.requestCd && !this.$route.path.includes("/create")) {
      this.returnToList();
    } else {
      this.fetchListData();
    }
  },
  methods: {
    ...mapActions("DropdownModule", [
      "getListPaymentPlace",
      'getListPaymentMethod',
      "getListDepartment",
      "getListApplication"
    ]),
    ...mapActions("AutoCalculateOnTable", ['calculateStandardMarginalProfitRatio','calculateSalesOfSeihan']),
    ...mapActions({lockScreen: 'AuthenticationModule/setLockScreen', unlockScreen:'AuthenticationModule/setUnlockScreen'}),
    fetchListData() {
      console.log("thaoht aaaa")
      this.getListDepartment();
      if(this.item.requestModel.approvalFlowDetailsRequest && this.item.requestModel.approvalFlowDetailsRequest.bumonCd){
        this.getListApplication(this.item.requestModel.approvalFlowDetailsRequest.bumonCd);
      }
      this.getListPaymentPlace();
      this.getListPaymentMethod();
    },
    checkUpdatedInfo(callback, type = undefined) {
      this.lockScreen();
      RequestService.checkRequestUpdated(this.item.requestModel.cd, this.item.requestModel.updatedAt)
      .then(response => {
        if(response.data && response.data.editShainName && response.data.startedEditAt && response.data.startedEditAt > 0) {
          this.item.requestModel.editShainName = response.data.editShainName;
          this.item.requestModel.startedEditAt = response.data.startedEditAt;
          this.showLockInEditing = true;
        } else if(response.data && response.data.isUpdated) {
          this.messageerror = "申請情報に更新がありました。<br/>" +
                  "ページをリロードし、再度確認して下さい";
          this.showErrorDialog = true;
        } else {
          if(typeof callback == "function") {
            if(type) {
              callback(type);
            } else {
              callback();
            }
          }
        }
        this.unlockScreen();
      }).catch(err =>{
        console.log(err)
        this.unlockScreen();
      })
    },
    getMinAndMaxValueByField(field) {
      switch(field) {
        case "subject":
          return {
            min: 1,
            max: 50
          }
        case "storeGNm":
          return {
            min: 1,
            max: 40
          }
        case "storeNameOfEvent":
          return {
            min: 1,
            max: 50
          }
        case "hinmokuCd":
          return {
            min: 1,
            max: 7
          };
        case "hinmokuRnm":
          return {
            min: 1,
            max: 30
          };
        case "irisu":
        case "billingAmount":
          return {
            min: 1,
            max: (1e12 - 1)
          };
        case "mfYen":
        case "hyojyunYen":
          return {
            min: -(0 - 0.01),
            max: (1e6 - 0.01)
          };
        case "finalMarginalProfit":
        case "numbers_of_cases":
          return {
            min: -(1e12 - 1),
            max: (1e12 - 1)
          };
        case "accruedAmount": {
          return {
            min: -(1e12 - 1),
            max: (1e12 - 1)
          };
        }
        case "nisugata":
          return {
            min: 1,
            max: 15
          };
        case "torihikiNm":
          return {
            min: 1,
            max: 80
          };
        case "remarks":
          return {
            min: 1,
            max: 1000
          }
        case "paymentOtherMethod":
          return {
            min: 1,
            max: 200
          }
        case "paymentDestination":
          return {
            min: 1,
            max: 40
          }
        case "bankName":
          return {
            min: 1,
            max: 16
          }
        case "bankAccountNumber":
          return {
            min: 1,
            max: 10
          }
        case "bankAccountName":
          return {
            min: 1,
            max: 40
          }
        case "itemTotalForEightPercent":
        case "itemTotalForTenPercent":
        case "consumptionTaxTotalForEightPercent":
        case "consumptionTaxTotalForTenPercent":
        case "itemTotal":
        case "consumptionTaxTotal":
        case "totalForEightPercent":
        case "totalForTenPercent":
        case "total":
          return {
            min: -(1e12 - 1),
            max: (1e12 - 1)
          }
        default:
          return {
            min: Number.MIN_SAFE_INTEGER || -9007199254740991,
            max: Number.MAX_SAFE_INTEGER || 9007199254740991
          };
      }
    },
    addItem(index, listName) {
      if(isEmpty(this.item[listName])){
        this.item[listName] = [];
      }
      let newItem = new RequestMannequinPromotionDetailsRequestListItem();
      let indexOfNewItem = index + 1;
      if (newItem) {
        this.item[listName].splice(indexOfNewItem, 0, newItem);
      }
    },
    addItemImplementationStore(index){
      if(!this.disabled){
        if(isEmpty(this.item.implementationStoresForRequestMannequinPromotionRequestList)){
          this.item.implementationStoresForRequestMannequinPromotionRequestList = []
        }
        let newItem = new ImplementationStoresForRequestMannequinPromotionRequestItem();
        newItem.cd = null;
        this.item.implementationStoresForRequestMannequinPromotionRequestList.splice((index+1), 0, newItem);
      }
    },
    copyItem(index, listName) {
      if(!this.disabled){
        if(index == -1 || index == this.item[listName].length) {
        return;
        }
        let newItem = cloneDeep(this.item[listName][index]);
        let indexOfNewItem = index + 1;
        newItem.cd = null;
        this.item[listName].splice(indexOfNewItem, 0, newItem);
      }
    },
    copyItemImplementationStore(index){
      this.item.implementationStoresForRequestMannequinPromotionRequestList.splice((index+1), 0, cloneDeep(this.item.implementationStoresForRequestMannequinPromotionRequestList[index]));
    },
    removeItem (index, listName) {
      if(index < 0) {
        return;
      }
      let deletedCd = this.item[listName][index].cd;
      if(deletedCd) {
        if(!this.item.requestMannequinPromotionDetailsRequestIsDeletedList) {
          this.item.requestMannequinPromotionDetailsRequestIsDeletedList = [];
        }
        this.item.requestMannequinPromotionDetailsRequestIsDeletedList.push(deletedCd);
      }
      this.item[listName].splice(index, 1);
    },
    removeItemImplementationStore(index){
      if(!this.disabled){
        let cd = this.item.implementationStoresForRequestMannequinPromotionRequestList[index].cd;
        if(isEmpty(this.item.implementationStoresForRequestMannequinPromotionRequestIsDeletedList)){
          this.item.implementationStoresForRequestMannequinPromotionRequestIsDeletedList = []
        }
        if(cd){
          this.item.implementationStoresForRequestMannequinPromotionRequestIsDeletedList.push(cd)
        }
        this.item.implementationStoresForRequestMannequinPromotionRequestList.splice(index, 1);
      }
    },
    setAttachFiles(files, replace = -1) {
      if(replace == -1) {
        if(!isArray(files)) {
          files = [files];
        }
        let existedList = this.item.requestModel?.fileModelList || [];
        this.item.requestModel.fileModelList = existedList.concat(files);
      } else {
        this.item.requestModel.fileModelList.splice(replace, 1, files);
      }
    },
    removeAttachFile(index) {
      this.item.requestModel.fileModelList.splice(index, 1);
    },
    reuse() {
      if(this.item.requestModel.stepNumber >= 2) {
        this.$router.push(`/mannequin-promotions/create/${this.requestCd}/reuse`);
      }
    },
    sumFieldForTable10Percent(field){
      if(field && !isEmpty(this.item.requestMannequinPromotionDetailsRequestList)){
        return this.item.requestMannequinPromotionDetailsRequestList.reduce((total,item)=>{
        let i = item[field] ? item[field] : 0 ;
        return isNaN((i + total)) ? 0 : (i + total)
      }, 0);
      } else {
        return 0;
      }
    },
    async autoCalculateTable(data, index, listName, field, initialField, precision = 0) {
      let isEditInitialField = this.mode == "create" || !this.item[listName][index].cd;
      this.item[listName][index][field] = isNaN(round(this.item[listName][index][field], precision)) ? undefined : round(this.item[listName][index][field], precision);
      if(/(mfYen|hyojyunYen|irisu|numbersOfCases)/.test(field)) {// StandardMarginalProfitRatio
        this.item[listName][index] = await this.calculateStandardMarginalProfitRatio({
          data,
          initialField: isEditInitialField,
          precision
        })
      }
      if(/(mfYen|irisu|numbersOfCases)/.test(field)) {// SalesOfSeihan
        this.item[listName][index] = await this.calculateSalesOfSeihan({
          data,
          initialField: isEditInitialField,
          precision
        });
      }
      if(isEditInitialField && !this.item[listName][index][initialField] && (field == "mfYen" || field == "irisu")) {
        this.item[listName][index][initialField] = this.item[listName][index][field];
      } else if(isEditInitialField) {
        this.item[listName][index][initialField] = this.item[listName][index][field];
      }
    },
    checkRequiredForFieldBasedOnStatusPage(field) {
      let rules = undefined;
      let messages = undefined;
      if(field) {
        switch (field) {
          case "torihikiNm": {// 7
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("torihikiNm").max
              };
              messages = {required: '取引先名を確認して下さい', max: '取引先名を確認して下さい'};
            }
            break;
          }
          case "billingAmount": {// 11
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("billingAmount").min,
                max_value: this.getMinAndMaxValueByField("billingAmount").max
              };
              messages = {required: '請求金額を確認して下さい', min_value: '請求金額を確認して下さい', max_value: '請求金額を確認して下さい'};
            }
            break;
          }
          case "billingOn": {// 12
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '請求日を確認して下さい'};
            }
            break;
          }
          case "approvalFlowDetailsRequest.bumonCd": {// 13
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '部門名を確認して下さい'};
            }
            break;
          }
          case "approvalFlowDetailsRequest.shainCd": {// 14
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '申請者名を確認して下さい'};
            }
            break;
          }
          case "requestModel.paymentPlace": {// 19
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '支払場所を確認して下さい'};
            }
            break;
          }
          case "requestModel.paymentMethod": {// 20
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '支払い方法を確認して下さい'};
            }
            break;
          }
          case "requestModel.paymentOtherMethod": {// 21
            if(includes([2, 4, 6, 9], this.statusPage) && this.item.requestModel.paymentMethod == "その他") {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("paymentOtherMethod").max
              };
              messages = {required: 'その他支払い方法を確認して下さい', max: 'その他支払い方法を確認して下さい'};
            }
            break;
          }
          case "requestModel.scheduledPaymentOn": {// 22
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '支払予定日を確認して下さい'};
            }
            break;
          }
          case "requestModel.paymentOn": {// 23
            if(((this.statusPage == 9 && this.item.requestModel.stepNumber > 3) ||
              (this.statusPage == 11 || this.statusPage == 13)) &&
              this.loginUser && this.loginUser.bumonCd == 4000
            ) {
              rules = {
                required: true
              };
              messages = {required: '支払日を確認して下さい'};
            }
            break;
          }
          case "requestModel.paymentDestination": {// 24
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("paymentDestination").max
              };
              messages = {required: '支払先を確認して下さい', max: '支払先を確認して下さい'};
            }
            break;
          }
          case "requestModel.bankName": {// 25
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("bankName").max
              };
              messages = {required: '銀行名を確認して下さい', max: '銀行名を確認して下さい'};
            }
            break;
          }
          case "requestModel.bankAccountNumber": {// 26
            if(includes([2, 4, 6, 9],this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("bankAccountNumber").max
              };
              messages = {required: '口座番号を確認して下さい', max: '口座番号を確認して下さい'};
            }
            break;
          }
          case "requestModel.bankAccountName": {// 27
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("bankAccountName").max
              };
              messages = {required: '口座名義を確認して下さい', max: '口座名義を確認して下さい'};
            }
            break;
          }
          case "requestModel.itemTotalForEightPercent": {// 35
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("itemTotalForEightPercent").min,
                max_value: this.getMinAndMaxValueByField("itemTotalForEightPercent").max
              };
              messages ={required: '8%対象品目計を確認して下さい', min_value: '8%対象品目計を確認して下さい', max_value: '8%対象品目計を確認して下さい'};
            }
            break;
          }
          case "requestModel.itemTotalForTenPercent": {// 36
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("itemTotalForTenPercent").min,
                max_value: this.getMinAndMaxValueByField("itemTotalForTenPercent").max
              };
              messages ={required: '10%対象品目計を確認して下さい', min_value: '10%対象品目計を確認して下さい', max_value: '10%対象品目計を確認して下さい'};
            }
            break;
          }
          case "requestModel.consumptionTaxTotalForEightPercent": {// 37
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("consumptionTaxTotalForEightPercent").min,
                max_value: this.getMinAndMaxValueByField("consumptionTaxTotalForEightPercent").max
              };
              messages ={required: '8%消費税計を確認して下さい', min_value: '8%消費税計を確認して下さい', max_value: '8%消費税計を確認して下さい'};
            }
            break;
          }
          case "requestModel.consumptionTaxTotalForTenPercent": {// 38
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("consumptionTaxTotalForTenPercent").min,
                max_value: this.getMinAndMaxValueByField("consumptionTaxTotalForTenPercent").max
              };
              messages ={required: '10%消費税計を確認して下さい', min_value: '10%消費税計を確認して下さい', max_value: '10%消費税計を確認して下さい'};
            }
            break;
          }
          case "requestModel.itemTotal": {// 39
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("itemTotal").min,
                max_value: this.getMinAndMaxValueByField("itemTotal").max
              };
              messages ={required: '品目合計を確認して下さい', min_value: '品目合計を確認して下さい', max_value: '品目合計を確認して下さい'};
            }
            break;
          }
          case "requestModel.consumptionTaxTotal": {// 40
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("consumptionTaxTotal").min,
                max_value: this.getMinAndMaxValueByField("consumptionTaxTotal").max
              };
              messages ={required: '消費税合計を確認して下さい', min_value: '消費税合計を確認して下さい', max_value: '消費税合計を確認して下さい'};
            }
            break;
          }
          case "requestModel.totalForEightPercent": {// 41
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("totalForEightPercent").min,
                max_value: this.getMinAndMaxValueByField("totalForEightPercent").max
              };
              messages ={required: '8%対象品目・消費税合計を確認して下さい', min_value: '8%対象品目・消費税合計を確認して下さい', max_value: '8%対象品目・消費税合計を確認して下さい'};
            }
            break;
          }
          case "requestModel.totalForTenPercent": {// 42
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("totalForTenPercent").min,
                max_value: this.getMinAndMaxValueByField("totalForTenPercent").max
              };
              messages ={required: '10%対象品目・消費税合計を確認して下さい', min_value: '10%対象品目・消費税合計を確認して下さい', max_value: '10%対象品目・消費税合計を確認して下さい'};
            }
            break;
          }
          case "requestModel.total": {// 43
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("total").min,
                max_value: this.getMinAndMaxValueByField("total").max
              };
              messages ={required: '8%対象品目計を確認して下さい', min_value: '8%対象品目計を確認して下さい', max_value: '8%対象品目計を確認して下さい'};
            }
            break;
          }
          case "detail.storeGNm": {// 46
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("storeGNm").max
              };
              messages = {required: '店舗グループ名を確認して下さい', max: '店舗グループ名を確認して下さい'};
            }
            break;
          }
          case "detail.shainNm": {// 47
            if(includes([2, 4, 6, 9, 11], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: '担当者名を確認して下さい'};
            }
            break;
          }
          case "detail.bumonNm": {// 48
            if(includes([2, 4, 6, 9, 11], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: '部門名を確認して下さい'};
            }
            break;
          }
          case "detail.hinmokuCd": // 50
          case "detail.hinmokuCd75": /* 75 */ { 
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("hinmokuCd").max
              };
              messages = {required: '品目コードを確認して下さい', max: '品目コードを確認して下さい'};
            }
            break;
          }
          case "detail.hinmokuRnm": // 51
          case "detail.hinmokuRnm76": /* 76 */{
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("hinmokuRnm").max
              };
              messages = {required: '品目略称を確認して下さい', max: '品目略称を確認して下さい'};
            }
            break;
          }
          case "detail.irisu": {// 53
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("irisu").min,
                max_value: this.getMinAndMaxValueByField("irisu").max
              };
              messages = {required: "入数を確認して下さい", min_value: "入数を確認して下さい", max_value: "入数を確認して下さい"};
            }
            break;
          }
          case "detail.mfYen": {// 54
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: 1,
                max_value: 1000000.00 - 0.01
              };
              messages ={required: "生産者価格を確認して下さい", min_value: "生産者価格を確認して下さい", max_value: "生産者価格を確認して下さい"};
            }
            break;
          }
          case "detail.hyojyunYen": {// 60
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: 1,
                max_value: 1000000.00 - 0.01
              };
              messages ={required: '標準原価(営業)を確認して下さい', min_value: "標準原価(営業)を確認して下さい", max_value: "標準原価(営業)を確認して下さい"};
            }
            break;
          }
          case "detail.accruedAmount": {// 62
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: (-1000000000000 + 1),
                max_value: 1000000000000 - 1
              };
              messages ={required: '未収金額を確認して下さい', min_value: "未収金額を確認して下さい", max_value: "未収金額を確認して下さい"};
            }
            break;
          }
          case "detail.finalMarginalProfit": {// 64
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: (-1000000000000 + 1),
                max_value: 1000000000000 - 1
              };
              messages ={required: '最終限界利益額を確認して下さい', min_value: "最終限界利益額を確認して下さい", max_value: "最終限界利益額を確認して下さい"};
            }
            break;
          }
          case "detail.nisugata": {// 77
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: 15
              };
              messages ={required: "荷姿を確認して下さい", max: "荷姿を確認して下さい"};
            }
            break;
          }
          case "detail.numbersOfCases": {
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: (-10000000000 + 1),
                max_value: 10000000000   - 1
              };
              messages ={required: "数量を確認して下さい", min_value: "数量を確認して下さい", max_value: "数量を確認して下さい"};
            }
            break;
          }
          case "detail.salesOfSeihan": {
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: (-10000000000 + 1),
                max_value: 10000000000   - 1
              };
              messages ={required: "売上(生販)を確認して下さい", min_value: "売上(生販)を確認して下さい", max_value: "売上(生販)を確認して下さい"};
            }
            break;
          }
          case "detail.standardMarginalProfit": {
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: (-10000000000 + 1),
                max_value: 10000000000   - 1
              };
              messages ={required: "標準限界利益額(営業)を確認して下さい", min_value: "標準限界利益額(営業)を確認して下さい", max_value: "標準限界利益額(営業)を確認して下さい"};
            }
            break;
          }
          case "detail.costsOfMannequin": {
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: 0,
                max_value: 10000000000 - 1
              };
              messages ={required: "マネキン費用を確認して下さい", min_value: "マネキン費用を確認して下さい", max_value: "マネキン費用を確認して下さい"};
            }
            break;
          }
          default:
            break;
        }
      }
      return { rules, messages };
    },
    getColumnByKey(label, field, getData) {
      return {
        label: label,
        field: field,
        getData: getData
      }
    },
    getInputColumnByKey(label, field, isReadOnly, onInput) {
      return {
        label: label,
        field: field,
        isInput: true,
        type: "text",
        isValidate: true,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).messages,
        disabled: this.disabled,
        class:this.checkClassRequire,
        isPlainText: true, // read only
        min: this.getMinAndMaxValueByField(field).min,
        max: this.getMinAndMaxValueByField(field).max,
        isReadOnly: isReadOnly,
        onInput: onInput
      }
    },
    getNumericColumnByKey(label, field, step, initialField) {
      return {
        label: label,
        field: field,
        isInput: true,
        type: "numeric",
        isValidate: true,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).messages,
        disabled: this.disabled,
        class:this.checkClassRequire,
        min: this.getMinAndMaxValueByField(field).min,
        max: this.getMinAndMaxValueByField(field).max,
        step: step ? step : 0.01,
        initialField: initialField,
        emtyValue: 0,
        onInput: this.autoCalculateTable
      }
    },
    getSelectColumnByKey(label, field, isOnSearch = false) {
      return {
        label: label,
        field: field,
        isSelect: true,
        disabled: this.disabled,
        class:this.checkClassRequire,
        isValidate: true,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).messages,
        options: this.getOptions,
        onInput: this.getValueFromSelect,
        onOpen: this.getOptionsForTable,
        isOnSearch: isOnSearch,
        onSearch: this.getOptionsForTable
      }
    },
    getButtonColumn(label, onClick) {
      return {
        isButton: true,
        isShow: true,
        labelButton: label,
        disabled: this.disabled,
        onClick: onClick
      }
    },
    searchProduct(data, index, listName) {
      this.selectedItemNumber = {rowIndex: index, listName: listName, itemNumber: data.itemNumber};
      this.showPopupSearchProduct = !this.showPopupSearchProduct;
    },
    getProduct(product) {
      let index = this.selectedItemNumber.rowIndex,
        listName = this.selectedItemNumber.listName;
      // 45
      this.item[listName][index].hinmokuCd = product.hinmokuCd;
      // 46
      this.item[listName][index].hinmokuRnm = product.hinmokuRnm;
      // 47
      this.item[listName][index].nisugata = product.nisugata;
      // 48
      this.item[listName][index].irisu = product.irisu;
      // 49
      this.item[listName][index].mfYen = product.mfYen;
      // 50
      this.item[listName][index].hyojyunYen = product.hyojyunYen;
      if(!this.item[listName][index].cd) {
        this.item[listName][index].initialIrisu = product.irisu;
        this.item[listName][index].initialMfYen = product.mfYen ? product.mfYen : this.item[listName][index].mfYen;
        this.item[listName][index].initialHyojyunYen = product.hyojyunYen;
      }
    },
    getSupplier(event) {
      this.item.requestModel.torihikiNm = event.torihiki1Nm + event.torihiki2Nm;
      this.item.requestModel.mstTorihikiCd = event.torihikiCd;
      this.showPopupSupply = false
    },
    searchStoreGnm: debounce(function (value) {
      if (value) {
        AccountReceivableCreateService.listStoreGroupName(value).then(res => {
          this.storeGNmOptions = res.data
          // console.log(op)
        }).catch(err => {
          console.log(err);
        })
      }
    }, 1000),
    changeDeparment(event){
      let itemfinded = this.departmentList?.find( elm => elm.bumonCd === event );
      this.item.requestModel.approvalFlowDetailsRequest.bumonNm = itemfinded ? itemfinded.bumonNm : "";
      this.item.requestModel.approvalFlowDetailsRequest.shainNm = "";
      this.item.requestModel.approvalFlowDetailsRequest.shainCd = "";
      this.getListApplication(event)
    },
    changeApplication(event){
      let itemfinded = this.applicationList?.find( elm => elm.shainCd === event );
      this.item.requestModel.approvalFlowDetailsRequest.shainNm = itemfinded ? itemfinded.shainNm : "";
    },
    changeStoreGnm(event){
      this.item.storeGCd = this.storeGNmOptions?.find( elm => elm.storeGNm === event ).storeGCd;
      setTimeout(()=>{this.storeGNmOptions = []},100)
    },
    handleErrors(errors) {
      if (errors) {
        return Object.values(errors).flat();
      }
      return [];
    },
    canInputDate(date){
      let today = new Date();
      today.setHours(0, 0, 0, 0);
      return date > today ;
    },
    checkEmpty(item){
      return isEmpty(item)
    },
    changeModePage(mode){
      // get status code from name
      let status = RequestStatuses.find(status => {
        return status.label.trim() == this.requestStatus;
      });
      if(status) {
        console.log(status)
        this.$router.push(`/mannequin-promotions/${status.value}/${this.requestCd}/${mode}`);
      }
    },
    fetchRequest(mode = "view") {
      this.lockScreen();
      MannequinPromotionService.detailMannequinPromotion(this.requestCd).then(response => {
        if(response.data) {
          this.item = response.data;
          userRole(null, this.item);
          // sort item in list
          if(this.item.implementationStoresForRequestMannequinPromotionRequestList) {
            this.item.implementationStoresForRequestMannequinPromotionRequestList = sortBySortNumber(this.item.implementationStoresForRequestMannequinPromotionRequestList);
          }
          if(this.item.requestMannequinPromotionDetailsRequestList) {
            this.item.requestMannequinPromotionDetailsRequestList = sortBySortNumber(this.item.requestMannequinPromotionDetailsRequestList);
          }
          if(!this.item.requestModel.approvalFlowDetailsRequest) {
            this.item.requestModel.approvalFlowDetailsRequest = new ApprovalFlowDetail();
          }
          if(mode == "edit") {
            if(this.loginUser &&
              this.item.requestModel.editShainCd &&
              this.item.requestModel.editShainCd != this.loginUser.sub
            ) {
              this.showLockInEditing = true;
              this.isLocked = true;
              if(this.$route.path.includes("/create")) {
                setTimeout(() => {
                  this.$router.go(-1);
                }, 2600);
              }
            } else {
              if(!this.$route.path.includes("/create")) {
                this.changeModePage(mode);
                this.isLocked = false;
              }
            }
          } else if(mode != this.mode){
            this.changeModePage(mode);
          }
        }
      })
      .catch(err => {
        console.log(err);
      })
      .then(() => {
        this.unlockScreen();
      });
    },
    editRequest() {
      this.checkUpdatedInfo(() => {
        this.fetchRequest("edit");
      })
    },
    backToView(){
      this.unlockRequest()
      .then(isUnlocked => {
        this.isLocked = true;
        if(isUnlocked) {
          this.fetchRequest("view");
        }
      });
    },
    unlockRequest() {
      if(!this.isLocked) {
        return RequestService.unlockRequest(this.requestCd)
          .then(response => {
            if(response.status == 200) {
              return true;
            } else {
              return false;
            }
          })
          .catch(err => {
            console.log(err);
            return false;
          })
          .then(isUnlocked => {
            return isUnlocked;
          });
      } else {
        return Promise.resolve(true);
      }
    },
    //actions manneiquin 
    async saveMannequinPromotions(isDraft){
      await this.setInitialForPaymentTable(true);
      let isValidated = await this.$refs.requestForm.validate();
      if(isValidated){
        this.item.requestMannequinPromotionDetailsRequestList = this.item.requestMannequinPromotionDetailsRequestList.map((item,index) => {
          item.sortNumber = index;
          return item
        });
        let files = this.item.requestModel.fileModelList;
        delete this.item.requestModel.fileModelList;
        this.item.requestModel.isTemp = isDraft;
        if(this.item.requestModel.cd){
          this.updateDraftequest(isDraft, files);
        }else{
          this.createMannequinProtions(isDraft, files);
        }
      }else{
        console.log(this.$refs)
        this.$refs.mainView.scrollTop = 0
      }
    },
    //save apply mannequin
    async updateMannequinPromotions() {
      let isValidated = await this.$refs.requestForm.validate();
      if(isValidated){
        this.item.requestMannequinPromotionDetailsRequestList = this.item.requestMannequinPromotionDetailsRequestList.map((item,index) => {
          item.sortNumber = index;
          return item
        });
        this.item.implementationStoresForRequestMannequinPromotionRequestList = this.item.implementationStoresForRequestMannequinPromotionRequestList.map((item,index) => {
          item.sortNumber = index;
          return item
        });
        let files = this.item.requestModel.fileModelList;
        delete this.item.requestModel.fileModelList;
        this.saveRequest(files);
      }else{
        console.log(this.$refs)
        this.$refs.mainView.scrollTop = 0
      }
    },
    //set initial filed for payent table before save
    setInitialForPaymentTable(elm){
      let t = elm ? 0 : undefined ;
      let modeForReuse = /(reuse|create|confirmCreate)/;
      console.log(modeForReuse.test(this.modeForCreateRequest))
      if(modeForReuse.test(this.modeForCreateRequest)){
        this.item.requestModel.itemTotalForEightPercent = t ;
        this.item.requestModel.consumptionTaxTotalForEightPercent = t ;
        this.item.requestModel.totalForEightPercent = t ;
        this.item.requestModel.initialTotalForEightPercent = t ;
        this.item.requestModel.initialItemTotalForEightPercent = t ;
        this.item.requestModel.initialConsumptionTaxTotalForEightPercent = t ;
      }
    },
    createMannequinProtions(isDraft,files){
      this.lockScreen();
      MannequinPromotionService.mannequinPromotions(files, this.item)
      .then(response => { 
        if(response.data){
          this.item = response.data;
          this.isSaveDraft = isDraft;
          this.showNormalDialog = true;
          // if(isDraft){
          //   setTimeout(()=>{
          //     this.$router.replace(`/mannequin-promotions/create/${res.data.requestModel.cd}/edit`);
          //   },2600)
          //   this.getDetailMannequinPromotion(res.data.requestModel.cd).then(value=>{
          //     if(value){this.item = value}
          //   })
          // }else{
          //   setTimeout(()=>{this.backToList()},2600)
          // }
        }
      }).catch(err =>{
        this.this.setInitialForPaymentTable(false);
        this.showErrorDialog = true;
        console.log(err)
      })
      .then(() => {
        this.unlockScreen();
      })
    },
    //uodate for save draft
    updateDraftequest(isDraft,files){
      this.lockScreen();
      MannequinPromotionService.mannequinPromotionsUpdate(files, this.item)
      .then(response => { 
        if(response.data){
          this.item = response.data;
          this.isSaveDraft = isDraft;
          this.showNormalDialog = true;
          // if(temp){
          //   this.showNormalDialog = true;
          //   setTimeout(()=>{this.backToList()},2600)
          // }else{
          //   this.showNormalDialog = true;
          //   this.item = res.data;
          //   this.unlockScreen();
          // }
        }
      }).catch(err =>{
        this.this.setInitialForPaymentTable(false);
        this.showErrorDialog = true
        console.log(err)
      })
      .then(() => {
        this.unlockScreen();
      });
    },
    //update
    saveRequest(files) {
      this.lockScreen();
      MannequinPromotionService.mannequinPromotionsForUpdate(files, this.item)
      .then(res => { 
        if(res.data){
          this.showNormalDialog = true;
        }
      }).catch(err =>{
        this.showErrorDialog = true
        console.log(err)
      })
      .then(() => {
        this.unlockScreen();
      })
    },
    getDetailMannequinPromotion(id){
      this.lockScreen();
      return MannequinPromotionService.detailMannequinPromotion(id)
      .then(res => {
        if(res.data){
          this.unlockScreen();
          let data = res.data;
          if(!isEmpty(data.requestMannequinPromotionDetailsRequestListItem) && data.length > 1){
            data.requestMannequinPromotionDetailsRequestListItem = sortBySortNumber(data.requestMannequinPromotionDetailsRequestListItem);
          }
          if(data.implementationStoresForRequestMannequinPromotionRequestList) {
            data.implementationStoresForRequestMannequinPromotionRequestList = sortBySortNumber(data.implementationStoresForRequestMannequinPromotionRequestList);
          }
          if(this.mode == "reuse") {
            data = this.setReuseForItemMannequin(data);
          }
          this.item = data;
        }
      }).catch(err => {
        this.showErrorDialog = true;
        setTimeout(() => {
          this.returnToList();
        }, 2600)
        console.log(err)
      })
      .then(() => {
        this.unlockScreen();
      })
    },
    deleteActionForMannequin(){
      if(this.indexOfimplementStoreMannequin !== null){
        this.removeItemImplementationStore(this.indexOfimplementStoreMannequin);
      }else{
        this.deleteMannequinPromotions();
      }
      this.showPopupDelete  =  false
    },  
    deleteMannequinPromotions(){
      let id = this.$route.params.cd;
      if(id){
        this.lockScreen();
        AccountReceivableCreateService.deleteRequest(id).then(res =>{
          if(res.data){
            this.showNormalDialog = true;
            // setTimeout(()=>{this.backToList()},2600)
            // this.unlockScreen();
          }
        }).catch(err =>{
          this.showErrorDialog = true;
          console.log(err);
        })
        .then(() => {
          this.unlockScreen();
        });
      }
    },
    setReuseForItemMannequin(data){
      let list = data;
      if(!isEmpty(list.implementationStoresForRequestMannequinPromotionRequestList)){
        list.implementationStoresForRequestMannequinPromotionRequestList = list.implementationStoresForRequestMannequinPromotionRequestList.map(item =>{
          item.cd = null;
          item.runEventOn = '';
          return item
        })
      }
      list.requestMannequinPromotionDetailsRequestList = list.requestMannequinPromotionDetailsRequestList.map(item =>{
        item.cd = null;
        return item
      })
      list.cd = null;
      list.requestModel.cd = null;
      list.requestModel.paymentOn = null;
      list.requestModel.scheduledPaymentOn = null;
      list.requestModel.billingOn = null;
      list.requestModel.settlementNumber = null;
      list.requestModel.requestedAt = null;
      if(!isEmpty( list.requestModel.requestNumberResponse)){
        list.requestModel.requestNumberResponse.cd = null;
        list.requestModel.requestNumberResponse.createdAt = null;
      }
      return list
    },
    print() {
      let template = RequestForPrint(this.item);
      this.$htmlToPaper(template);
    },
  },
};
