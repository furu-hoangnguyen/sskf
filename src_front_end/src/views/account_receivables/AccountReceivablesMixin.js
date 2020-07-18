import isTablet from "@/helper/isTabletDevice";
import userRole from "@/helper/checkRoleUserLogin";
import { mapState, mapActions } from "vuex";
import RequestForPrint from "@/templates/request-detail";
import TypeOfInput from "@/constants/TypeOfInput";
import { RequestStatuses } from "@/constants/RequestStatuses";
import RequestService from '@/services/RequestService';
import AccountReceivableCreateService from '@/services/AccountReceivableCreateService';
import ProductService from '@/services/ProductService';
import CommentService from "@/services/CommentService";
import {
  DetailsForAccountsReceivablesRequest,
  DetailsForPromotionalExpensesRequestLogisticFees,
  DetailsForPromotionalExpensesRequestTax8Percent,
  DetailsForPromotionalExpensesRequestTax10Percent,
  AccountReceivablesRequest
} from "@/models/AccountReceivablesRequestModel";
import { ApprovalFlowDetail } from "@/models/ApprovalFlowModel";
import cloneDeep from "lodash/cloneDeep";
import some from "lodash/some";
import includes from "lodash/includes";
import isArray from "lodash/isArray";
import forEach from "lodash/forEach";
import round from "lodash/round";
import sortBySortNumber from "@/helper/sort";
import formatDigitNumber from "@/helper/formatDigitNumber";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
import AutoFieldOfPaymentForTotal from "@/mixins/AutoFieldOfPaymentForTotal";
import RouteNavigationGuardsForRequestDetail from "@/mixins/RouteNavigationGuardsForRequestDetail";
export default {
  mixins: [getStatusOfPageDetailRequest, AutoFieldOfPaymentForTotal, RouteNavigationGuardsForRequestDetail],
  data() {
    return {
      messageerror: "エラーが発生しました。<br/>内容を確認し、再度送信をしてください。",
      item: new AccountReceivablesRequest(),
      // table
      optionsForTable: {},
      //Show modal
      selectedItemNumber: null,
      isShowPopupComment: false,
      initComment: "",
      commentRecordCd: null,
      showPopupSupply: false,
      supplier: null,
      showPopupSearchProduct: false,

      // Processing Dialog
      showNormalDialog: false,
      showErrorDialog: false,
      showLockInEditing: false,
      showTimeoutEditing: false,
      showWarningLeavePage: false,
      timeoutForEditRequest: null, // setTimeout

      // next to new route
      nextPath: undefined,
      referSelectItemNumber: null,
      isLocked: false
    }
  },
  computed: {
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser,
      useTypesList: state => state.DropdownModule["useTypesList"],
      commissionTypeList: state => state.DropdownModule["commissionTypeList"],
      departmentList: state => state.DropdownModule["departmentList"],
      applicationList: state => state.DropdownModule["applicationList"],
      paymentMethodList: state => state.DropdownModule["paymentMethodList"],
      paymentPlaceList: state => state.DropdownModule["paymentPlaceList"],
      listClassification: state => state.DropdownModule["listClassification"]
    }),
    requestCd() {
      return this.$route.params.cd;
    },
    requestStatus() {
      let status = "作成中";
      console.log("aaaa")
      if(this.mode == "reuse" || this.$route.path.includes("/create")) {
        status = "作成中";
      } else {
        status = this.item.requestModel.mstRequestStatusesResponse.name || "";
      }
      return status;
    },
    mode() {
      return this.$route.params.mode || "view";
    },
    requestNumber(){
      return formatDigitNumber(this.item.requestModel.requestNumberResponse?.cd || "")
    },
    disabled() {
      return !includes([2, 4, 6, 9], this.statusPage);
    },
    disabledAfterAccountingCheck() {
      return !includes([2, 4, 6, 9, 11], this.statusPage);
    },
    salesUnpaidColumns() {
      let columns = [
        this.getColumnByKey("明細番号", "itemNumber")
      ];
      if(!includes([1, 2, 10], this.statusPage)) {
        columns.push(this.getCheckDiffPriceColumn())
      }
      if(this.statusPage == 3) {
        // show confirm icon
        columns.push(this.getConfirmColumn());
      }
      if(this.statusPage == 5) {
        // Show charge icon
        columns.push(this.getConfirmUserColumn());
      }
      columns.push(...[
        this.getCommentColumn(),
        this.getSelectColumnByKey("店舗グループ名", "storeGNm", true),
        this.getSelectColumnByKey("担当者名", "shainNm", true),
        this.getSelectColumnByKey( "部門名", "bumonNm", true),
        this.getSelectColumnByKey("項目名", "typeOfItem", false)
      ]);
      if(includes([2, 4, 6, 7], this.statusPage) || (this.requestStatus == "承認待ち" && !isTablet())) {
        columns.push(this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      columns.push(...[
        this.getSelectColumnByKey("品目コード", "hinmokuCd", true),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", true),
        this.getInputColumnByKey("容量", "yoryo", false),
        this.getNumericColumnByKey("入数", "irisu", 1, "initialIrisu"),
        this.getNumericColumnByKey("生産者<br>価格(円)", "mfYen", 0.01, "initialMfYen"),
        this.getNumericColumnByKey("口銭<br>(%)", "commission", 0.01, "initialCommission"),
        this.getNumericColumnByKey("c/s引条件<br>(円)", "csdiscount", 0.01, "initialCSDiscount"),
        this.getNumericColumnByKey("伝票NET<br>(円)", "denpyoNet", 0.01, "initialDenpyoNet"),
        this.getNumericColumnByKey("未収単価<br>(円)", "accruedUnitPrice", 0.01, "initialAccruedUnitPrice"),
        this.getNumericColumnByKey("最終手取<br>単価(円)", "finalTakeUnitPrice", 0.01, "initialFinalTakeUnitPrice"),
        this.getNumericColumnByKey("標準原価<br>(営業)(円)", "hyojyunYen", 0.01, "initialHyojyunYen"),
        this.getNumericColumnByKey("販売本数", "quantityOfSoldItems", 1, "initialQuantityOfSoldItems"),
        this.getNumericColumnByKey("未収金額<br>(円)", "accruedAmount", 1, "initialAccruedAmount", "detailsForAccountsReceivablesRequestList"),
        this.getNumericColumnByKey("売上金額<br>(円)", "salesAmount", 1, "initialSalesAmount"),
        this.getNumericColumnByKey("最終限界<br>利益額(円)", "finalMarginalProfit", 1, "initialFinalMarginalProfit"),
        this.getNumericColumnByKey("最終限界<br>利益率(%)", "finalMarginalProfitRatio", 0.01, "initialFinalMarginalProfitRatio")
      ])
      return columns;
    },
    tax8PerctgSalesPromotionColumns() {
      let columns = [this.getColumnByKey("明細番号", "itemNumber", null)];
      if(this.statusPage == 3) {
        // show confirm icon
        columns.push(this.getConfirmColumn());
      }
      if(this.statusPage == 5) {
        // Show charge icon
        columns.push(this.getConfirmUserColumn());
      }
      columns.push(...[
        this.getCommentColumn(),
        this.getSelectColumnByKey("店舗グループ名", "storeGNm", true),
        this.getSelectColumnByKey("担当者名", "shainNm", true),
        this.getSelectColumnByKey( "部門名", "bumonNm", false),
        this.getSelectColumnByKey("販促費目", "typeOfPromotionalExpenses", false),
        this.getSelectColumnByKey("入力タイプ", "typeOfInput", true),
        this.getSelectColumnByKey("区分", "classification", true),
        this.getSelectColumnByKey("ブランド区分	", "brandClassification", false),
        this.getSelectColumnByKey("カテゴリ", "categoryName", false)
      ]);
      if(includes([2, 4, 6, 7], this.statusPage) || (this.requestStatus == "承認待ち" && !isTablet())) {
        columns.push(this.getButtonColumn("商品検索", (data, index, listName) => {
          this.searchProduct(data, index, listName);
        }));
      }
      columns.push(...[
        this.getSelectColumnByKey("品目コード", "hinmokuCd", true),
        this.getInputColumnByKey("品目略称", "hinmokuRnm", true),
        this.getInputColumnByKey("荷姿", "nisugata", false),
        this.getNumericColumnByKey("未収金額<br>(円)", "accruedAmount", 1, "initialAccruedAmount")
      ]);
      return columns;
    },
    isShowSearchSupplierSearchButton() {
      return includes([2, 4, 6, 9], this.statusPage);
    },
    isCanPrint() {
      return !isTablet() && this.loginUser && this.loginUser.roles &&
        includes(this.loginUser.roles, "confirmSettlementPersons") &&
        ((this.statusPage == 7 && this.item.requestModel.stepNumber > 3) || this.statusPage == 12 || this.statusPage == 15);
    },
    isCanReUse() {
      return ((this.statusPage == 3 || this.statusPage == 5) && this.item.requestModel.requestedAt && this.item.requestModel.requestedAt != "") ||
        this.statusPage == 7 || this.statusPage == 12 || this.statusPage == 14 || this.statusPage == 15;
    },
    isRequestSendBack() {
      return this.item.requestModel.isSentBack == 1;
    },
    isCanInputPaymentOn() {
      return this.loginUser &&
        this.loginUser.bumonCd == 4000 &&
        (
          (this.statusPage == 9 && this.item.requestModel.stepNumber > 3) ||
          (this.statusPage == 11 || this.statusPage == 13)
        );
    },
    isFirstTimeApply() {
      return !this.item.requestModel.requestedAt || this.item.requestModel.requestedAt == "";
    },
    isCanEdit() {
      let isCanShowOnCurrentPage = includes([1, 3, 5, 7, 12], this.statusPage);
      if(this.isRequestSendBack) {
        return !isTablet() &&
            isCanShowOnCurrentPage &&
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
            return isCanShowOnCurrentPage;
          case "確認待ち":
            return isCanShowOnCurrentPage;
          case "申請待ち":
            return isCanShowOnCurrentPage && this.isFirstTimeApply ||
                (!this.isFirstTimeApply && this.loginUser && this.loginUser.roles && this.loginUser.roles.includes('applyPersons'));
          case "承認待ち":
            return isCanShowOnCurrentPage && !isTablet() &&
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
            return isCanShowOnCurrentPage && this.loginUser && this.loginUser.roles && this.loginUser.roles.includes("confirmSettlementPersons");
          default:
            return isCanShowOnCurrentPage;
        }
      }
    }
  },
  watch: {
    mode(newValue, oldValue) {
      if(newValue != oldValue) {
        if(newValue == "edit") {
          this.timeoutForEditRequest = setTimeout(() => {
            this.showTimeoutEditing = true;
          }, 60 * 60 * 1000);
        } else if(this.timeoutForEditRequest){
          clearTimeout(this.timeoutForEditRequest);
        }
      }
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
    "item.commissionType"(newValue, oldValue) {
      if(newValue != oldValue) {
        if(this.item.detailsForAccountsReceivablesRequestList) {
          forEach(this.item.detailsForAccountsReceivablesRequestList, async item => {
            let isEditInitialField = this.mode == "create" || !item.cd;
            item = await this.calculateDenpyoNet({
              item,
              commissionType: this.item.commissionType, 
              initialField: isEditInitialField
            });
          });
        }
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
      console.log("log here")
      this.fetchListData();
    }
  },
  methods: {
    ...mapActions("DropdownModule", [
      "getUseTypes",
      "getCommissionTypes",
      "getListPaymentPlace",
      'getListPaymentMethod',
      "getListChargeName",
      "getListDepartment",
      "listDepartmentForItem",
      "getListApplication",
      "getListStoreGroup",
      "getListPromotionExpenses",
      "getListInputType",
      "getListItem",
      "getListClassifications",
      "getListBrandClassification",
      "getListCategory"
    ]),
    ...mapActions("AutoCalculateOnTable", [
      "calculateDenpyoNet",
      "calculateFinalTakeUnitPrice",
      "calculateAccruedAmount",
      "calculateSalesAmount",
      "calculateFinalMarginalProfit",
      "calculateFinalMarginalProfitRatio"
    ]),
    fetchRequest(mode = "view") {
      AccountReceivableCreateService.getDetailAccountReceivableByRequestId({
        cd: this.requestCd,
        mode: mode || this.mode
      }).then(response => {
        if(response.data) {
          this.item = response.data;
          userRole(null, this.item);
          this.sortItemInTable();
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
      });
    },
    fetchListData() {
      this.getUseTypes();
      this.getCommissionTypes();
      this.getListDepartment();
      if(this.item.requestModel.approvalFlowDetailsRequest && this.item.requestModel.approvalFlowDetailsRequest.bumonCd){
        this.getListApplication(this.item.requestModel.approvalFlowDetailsRequest.bumonCd);
      }
      this.getListPaymentPlace();
      this.getListPaymentMethod();
      this.getListItem();
    },
    sortItemInTable() {
      // sort item in list
      if(this.item.detailsForAccountsReceivablesRequestList) {
        this.item.detailsForAccountsReceivablesRequestList = sortBySortNumber(this.item.detailsForAccountsReceivablesRequestList);
      }
      if(this.item.detailsForPromotionalExpensesRequestTax8PercentList) {
        this.item.detailsForPromotionalExpensesRequestTax8PercentList = sortBySortNumber(this.item.detailsForPromotionalExpensesRequestTax8PercentList);
      }
      if(this.item.detailsForPromotionalExpensesRequestTax10PercentList) {
        this.item.detailsForPromotionalExpensesRequestTax10PercentList = sortBySortNumber(this.item.detailsForPromotionalExpensesRequestTax10PercentList);
      }
      if(this.item.detailsForPromotionalExpensesRequestLogisticFeesList) {
        this.item.detailsForPromotionalExpensesRequestLogisticFeesList = sortBySortNumber(this.item.detailsForPromotionalExpensesRequestLogisticFeesList);
      }
    },
    changeModePage(mode){
      // get status code from name
      let status = RequestStatuses.find(status => {
        return status.label.trim() == this.requestStatus;
      });
      if(status) {
        this.$router.push(`/account-receivables/${status.value}/${this.requestCd}/${mode}`);
      }
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
    reuse() {
      if(this.item.requestModel.stepNumber >= 2) {
        this.$router.push(`/account-receivables/create/${this.requestCd}/reuse`);
      }
    },
    disabledInputDate(date) {
      let today = new Date();
        today.setHours(0, 0, 0, 0);
        return date > today ;
    },
    getMinAndMaxValueByField(field, list = null) {
      switch(field) {
        case "storeGNm":
          return {
            min: 1,
            max: 40
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
        case "yoryo":
          return {
            min: 1,
            max: 8
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
        case "commission":
        case "csdiscount":
        case "accruedUnitPrice":
          return {
            min: -(0 - 0.01),
            max: (1e10 - 0.01)
          };
        case "denpyoNet":
        case "finalTakeUnitPrice":
        case "finalMarginalProfitRatio":
          return {
            min: -(1e10 - 0.01),
            max: (1e10 - 0.01)
          };
        case "quantityOfSoldItems":
        case "salesAmount":
        case "finalMarginalProfit":
          return {
            min: -(1e12 - 1),
            max: (1e12 - 1)
          };
        case "accruedAmount": {
          if(list == "detailsForAccountsReceivablesRequestList") {
            return {
              min: -(1e12 - 1),
              max: (1e12 - 1)
            };
          } else {
            return {
              min: 1,
              max: (1e12 - 1)
            };
          }
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
        case "purposeOfOthers": {
          return {
            min: 1,
            max: 36
          }
        }
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
    checkRequiredForFieldBasedOnStatusPage(field, listName = null) {
      let rules = undefined;
      let messages = undefined;
      if(field) {
        switch (field) {
          case "targetOn": { // 6
            if(includes([2, 4, 6, 9, 11], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '対象年月を確認して下さい'};
            }
            break;
          }
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
          case "purpose": {// 8
            if(includes([2, 4, 6, 9, 11],this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '用途を確認して下さい'};
            }
            break;
          }
          case "purposeOfOthers": {// 9
            if(includes([2, 4, 6, 9, 11], this.statusPage) && this.item.purpose == 'その他') {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("purposeOfOthers").max //36
              };
              messages = {required: '用途 その他入力を確認して下さい', max: '用途 その他入力を確認して下さい'};
            }
            break;
          }
          case "commissionType": {// 10
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '内口銭/外口銭を確認して下さい'};
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
          case "remarks": {// 15
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("remarks").max
              };
              messages = {required: '備考を確認して下さい'};
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
              messages ={required: "総合計を確認して下さい", min_value: "総合計を確認して下さい", max_value: "総合計を確認して下さい"};
            }
            break;
          }
          case "detail.storeGNm": {// 46
            if(includes([2, 4, 6, 9, 11], this.statusPage)) {
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
          case "detail.typeOfItem": {// 49
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages = {required: '項目名を確認して下さい'};
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
          case "detail.yoryo": {// 52
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("yoryo").max
              };
              messages ={required: '容量を確認して下さい', max: "容量を確認して下さい"};
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
                min_value: this.getMinAndMaxValueByField("mfYen").min,
                max_value: this.getMinAndMaxValueByField("mfYen").max
              };
              messages ={required: "生産者価格を確認して下さい", min_value: "生産者価格を確認して下さい", max_value: "生産者価格を確認して下さい"};
            }
            break;
          }
          case "detail.commission": {// 55
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                min_value: this.getMinAndMaxValueByField("commission").min,
                max_value: this.getMinAndMaxValueByField("commission").max
              };
              messages = {required: '口銭を確認して下さい', min_value: "口銭を確認して下さい", max_value: "口銭を確認して下さい"};
            }
            break;
          }
          case "detail.csdiscount": {// 56
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                min_value: this.getMinAndMaxValueByField("csdiscount").min,
                max_value: this.getMinAndMaxValueByField("csdiscount").max
              };
              messages = {required: 'c/s引条件を確認して下さい', min_value: "c/s引条件を確認して下さい", max_value: "c/s引条件を確認して下さい"};
            }
            break;
          }
          case "detail.denpyoNet": {// 57
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("denpyoNet").min,
                max_value: this.getMinAndMaxValueByField("denpyoNet").max
              };
              messages = {required: "伝票NETを確認して下さい", min_value: "伝票NETを確認して下さい", max_value: "口銭を確伝票NETを確認して下さい認して下さい"};
            }
            break;
          }
          case "detail.accruedUnitPrice": {// 58
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("accruedUnitPrice").min,
                max_value: this.getMinAndMaxValueByField("accruedUnitPrice").max
              };
              messages ={required: '未収単価を確認して下さい', min_value: "未収単価を確認して下さい", max_value: "未収単価を確認して下さい"};
            }
            break;
          }
          case "detail.finalTakeUnitPrice": {// 59
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("finalTakeUnitPrice").min,
                max_value: this.getMinAndMaxValueByField("finalTakeUnitPrice").max
              };
              messages ={required: '最終手取単価を確認して下さい', min_value: "最終手取単価を確認して下さい", max_value: "最終手取単価を確認して下さい"};
            }
            break;
          }
          case "detail.hyojyunYen": {// 60
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("hyojyunYen").min,
                max_value: this.getMinAndMaxValueByField("hyojyunYen").max
              };
              messages ={required: '標準原価(営業)を確認して下さい', min_value: "標準原価(営業)を確認して下さい", max_value: "標準原価(営業)を確認して下さい"};
            }
            break;
          }
          case "detail.quantityOfSoldItems": {// 61
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("quantityOfSoldItems").min,
                max_value: this.getMinAndMaxValueByField("quantityOfSoldItems").max
              };
              messages ={required: '販売本数を確認して下さい', min_value: "販売本数を確認して下さい", max_value: "販売本数を確認して下さい"};
            }
            break;
          }
          case "detail.accruedAmount": {// 62
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("accruedAmount", listName).min,
                max_value: this.getMinAndMaxValueByField("accruedAmount", listName).max
              };
              messages ={required: '未収金額を確認して下さい', min_value: "未収金額を確認して下さい", max_value: "未収金額を確認して下さい"};
            }
            break;
          }
          case "detail.salesAmount": {// 63
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("salesAmount").min,
                max_value: this.getMinAndMaxValueByField("salesAmount").max
              };
              messages ={required: '売上金額を確認して下さい', min_value: "売上金額を確認して下さい", max_value: "売上金額を確認して下さい"};
            }
            break;
          }
          case "detail.finalMarginalProfit": {// 64
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("finalMarginalProfit").min,
                max_value: this.getMinAndMaxValueByField("finalMarginalProfit").max
              };
              messages ={required: '最終限界利益額を確認して下さい', min_value: "最終限界利益額を確認して下さい", max_value: "最終限界利益額を確認して下さい"};
            }
            break;
          }
          case "detail.finalMarginalProfitRatio": {// 65
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                min_value: this.getMinAndMaxValueByField("finalMarginalProfitRatio").min,
                max_value: this.getMinAndMaxValueByField("finalMarginalProfitRatio").max
              };
              messages ={required: '最終限界利益率を確認して下さい', min_value: "最終限界利益率を確認して下さい", max_value: "最終限界利益率を確認して下さい"};
            }
            break;
          }
          case "detail.typeOfPromotionalExpenses": {// 70
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: '販促費目を確認して下さい'};
            }
            break;
          }
          case "detail.typeOfInput": {// 71
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: '入力タイプを確認して下さい'};
            }
            break;
          }
          case "detail.classification": {// 72
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: '区分を確認して下さい'};
            }
            break;
          }
          case "detail.brandClassification": {// 73
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: 'ブランド区分を確認して下さい'};
            }
            break;
          }
          case "detail.categoryName": {// 74
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true
              };
              messages ={required: 'カテゴリを確認して下さい'};
            }
            break;
          }
          case "detail.nisugata": {// 77
            if(includes([2, 4, 6, 9], this.statusPage)) {
              rules = {
                required: true,
                max: this.getMinAndMaxValueByField("nisugata").max
              };
              messages ={required: "荷姿を確認して下さい", max: "荷姿を確認して下さい"};
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
        isValidate: this.checkTypeOfInputOnDetailForRequired,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).messages,
        disabled: this.checkDisabled,
        class:this.checkClassRequire,
        isPlainText: true, // read only
        min: this.getMinAndMaxValueByField(field).min,
        max: this.getMinAndMaxValueByField(field).max,
        isReadOnly: isReadOnly,
        onInput: onInput
      }
    },
    getNumericColumnByKey(label, field, step, initialField, listName = null) {
      return {
        label: label,
        field: field,
        isInput: true,
        type: "numeric",
        isValidate: this.checkTypeOfInputOnDetailForRequired,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`, listName).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`, listName).messages,
        disabled: this.disabled,
        class:this.checkClassRequire,
        min: this.getMinAndMaxValueByField(field, listName).min,
        max: this.getMinAndMaxValueByField(field, listName).max,
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
        disabled: this.checkDisabled,
        class:this.checkClassRequire,
        isValidate: this.checkTypeOfInputOnDetailForRequired,
        rules: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).rules,
        messages: this.checkRequiredForFieldBasedOnStatusPage(`detail.${field}`).messages,
        options: this.getOptions,
        onInput: this.getValueFromSelect,
        onOpen: this.getOptionsForTable,
        isOnSearch: isOnSearch,
        onSearch: this.getOptionsForTable
      }
    },
    getCommentColumn() {
      return {
        label: "コメント",
        isComment: true,
        status: data => {
          let isEnteredComment = data.commentDetailsRequests && data.commentDetailsRequests.length > 0;
          let isBelongToUser = this.loginUser && this.loginUser.sub == data.shainCd;
          if(this.requestStatus == "申請待ち") {
            return (isEnteredComment && data.isConfirmUser) || !data.isConfirmUser;
          } else if(this.requestStatus == "確認待ち") {
            if(!data.hasDiff) {
              return true;
            } else {
              return isBelongToUser && isEnteredComment && data.hasDiff;
            }
          } else {
            return true;
          }
        },
        onClick: (data, index, listName) => {
          this.selectedItemNumber = {rowIndex: index, listName: listName, itemNumber: data.itemNumber, storeGCd:data.storeGCd};
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
          return data.mishuLimit < data.accruedUnitPrice;
        }
      }
    },
    getConfirmColumn() {
      return {
        label: "確認",
        isConfirm: true,
        isShow: this.statusPage == 3,
        status: data => {
          return data.isChecked ? "confirmed" : this.loginUser && data.shainCd == this.loginUser.sub ? "not-confirm" : ""
        },
        onClick: (data, listName, index, columns) => {
          this.item[listName][index].isChecked = data.isChecked == 1 ? 0 : 1;
          if(listName == "detailsForAccountsReceivablesRequestList") {
            this.item[listName][index].hasDiff = data.isChecked == 1 ? data.mishuLimit < data.accruedUnitPrice : undefined;
          }
        }
      }
    },
    getButtonColumn(label, onClick) {
      return {
        isButton: true,
        isShow: includes([2, 4, 6, 7], this.statusPage) || (this.requestStatus == "承認待ち" && !isTablet()),
        labelButton: label,
        disabled: this.checkDisabled ,
        onClick: onClick
      }
    },
    getConfirmUserColumn() {
      return {
        label: "担当者",
        isConfirmUser: true,
        onClick: (data, listName, index) => {
          if(listName == "" || index < 0) {
            return;
          }
          data.isConfirmUser = !data.isConfirmUser;
          let list = this.item[listName] || [];
          this.$set(list, index, data);
          this.$set(this.item, listName, list);
          if(data.isConfirmUser) {
            this.salesChargeEditItemRequests.push({
              accountReceivableDetailCd: data.accountReceivableDetailCd,
              commentDetailsRequest: data.commentDetailsRequest?.length > 0 ? data.commentDetailsRequest[0] : "",
              shainCdItem: data.shainCd
            });
          } else {
            this.salesChargeEditItemRequests = this.salesChargeEditItemRequests.filter(filterItem => {
              return filterItem.accountReceivableDetailCd != data.accountReceivableDetailCd;
            })
          }
        }
      }
    },
    sumAccruedAmount(list) {
      if(!list || !isArray(list)) {
        return 0;
      }
      return list.reduce(( a, currentItem ) => {
        let accruedAmountA = a,
          accruedAmountB = !currentItem.isDeleted && currentItem.accruedAmount ? currentItem.accruedAmount : 0;
        return parseInt(accruedAmountA) + parseInt(accruedAmountB);
      }, 0);
    },
    checkClassRequire(data, field, listName, index){
      if(this.disabled){
        return ""
      }else{
        switch (field) {
          case "hinmokuCd":
          case "storeGNm":
            return 'autoCompleteClass'
          default:
            return ""
        }
      }
      
    },
    async autoCalculateTable(data, index, listName, field, initialField, precision = 0) {
      let isEditInitialField = this.mode == "create" || !this.item[listName][index].cd;
      this.item[listName][index][field] = isNaN(round(this.item[listName][index][field], precision)) ? undefined : round(this.item[listName][index][field], precision);
      if(/(mfYen|csdiscount|irisu|commission)/.test(field)) {// denpyoNet
        this.item[listName][index] = await this.calculateDenpyoNet({
          item: data,
          commissionType: this.item.commissionType, 
          initialField: isEditInitialField,
          precision
        });
      }
      if(/(mfYen|accruedUnitPrice)/.test(field)) {// finalTakeUnitPrice
        this.item[listName][index] = await this.calculateFinalTakeUnitPrice({
          data,
          initialField: isEditInitialField,
          precision
        });
      }
      if(/(accruedUnitPrice|quantityOfSoldItems)/.test(field)) {// accruedAmount
        this.item[listName][index] = await this.calculateAccruedAmount({data, initialField: isEditInitialField});
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems)/.test(field)) { // salesAmount
        this.item[listName][index] = await this.calculateSalesAmount({data, initialField: isEditInitialField})
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems|hyojyunYen|quantityOfSoldItems)/.test(field)) {// finalMarginalProfit
        this.item[listName][index] = await this.calculateFinalMarginalProfit({data, initialField: isEditInitialField});
      }
      if(/(mfYen|accruedUnitPrice|quantityOfSoldItems|hyojyunYen)/.test(field)) {// finalMarginalProfitRatio
        this.item[listName][index] = await this.calculateFinalMarginalProfitRatio({data, initialField: isEditInitialField});
      }
      if(isEditInitialField && !this.item[listName][index][initialField] && (field == "mfYen" || field == "irisu")) {
        this.item[listName][index][initialField] = this.item[listName][index][field];
      } else if(isEditInitialField) {
        this.item[listName][index][initialField] = this.item[listName][index][field];
      }
    },
    getOptions (index, field, listName) {
      return this.optionsForTable[listName] &&
        this.optionsForTable[listName][index] && 
        this.optionsForTable[listName][index][field]
        ? this.optionsForTable[listName][index][field]
        : [];
    },
    getOptionsForTable(search, loading, index, field, listName) {
      if(!search) {
        switch(field) {
          case "bumonNm": {
            search = this.item[listName][index].shainCd;
            break;
          }
          case "categoryName": {
            search = this.item[listName][index].typeOfInput;
            break
          }
          case "storeGNm":
          case "shainNm":
          case "hinmokuCd": {
            break;
          }
          default: {
            search = true;
            break;
          }
        }
      }
      let promise = null;
      if(search && search != "") {
        if(loading) {
          
          // loading(true);
        }
        switch (field) {
          case "storeGNm": {
            promise = this.getListStoreGroup(search);
            break;
          }
          case "shainNm": {
            promise = this.getListChargeName(search);
            break;
          }
          case "bumonNm": {
            promise = this.listDepartmentForItem(search);
            break;
          }
          case "hinmokuCd": {
            let param = {keyword: `hinmokuCd=='${search}%'`}
            promise = ProductService.listProducts(param);
            break;
          }
          case "typeOfPromotionalExpenses": {
            promise = this.getListPromotionExpenses();
            break;
          }
          case "typeOfItem": {
            promise = this.getListItem();
            break;
          }
          case "typeOfInput": {
            promise = this.getListInputType();
            break;
          }
          case "classification": {
            promise = this.getListClassifications();
            break;
          }
          case "brandClassification": {
            promise = this.getListBrandClassification();
            break;
          }
          case "categoryName": {
            promise = this.getListCategory(search);
            break;
          }
        }
        if(promise) {
          promise
          .then((response) => {
            let rtnOptions = response;
            if(field == "hinmokuCd") {
              rtnOptions = response.data.content.reduce((list, item) => {
                let option = [{text: item.hinmokuCd, value: item}]
                return [...list, ...option]
              }, []);
            }
            return rtnOptions;
          })
          .catch(err => {
            console.log(err);
            return [];
          })
          .then(rtnOptions => {
            if(!this.optionsForTable[listName]) {
              this.$set(this.optionsForTable, listName, {});
            }
            if(!this.optionsForTable[listName][index]) {
              this.$set(this.optionsForTable[listName], index, {});
            }
            this.$set(this.optionsForTable[listName][index], field, rtnOptions || []);
            if(loading) {
              setTimeout(() => {
                // loading(false);
              }, 350);
            }
          });
        }
      }
    },
    getValueFromSelect(data, index, field, listName, selectedValue) {
      let value = selectedValue.value;
      if(!value) {
        return;
      }
      switch (field) {
        case "storeGNm": {
          this.item[listName][index]["storeGCd"] = value.storeGCd;
          this.item[listName][index]["storeGNm"] = value.storeGNm;
          // set for charge name
          this.item[listName][index]["shainNm"] = value.shainNm;
          this.item[listName][index]["shainCd"] = value.shainCd;
          this.item[listName][index]["bumonCd"] = "";
          this.item[listName][index]["bumonNm"] = "";
          break;
        }
        case "shainNm": {
          this.item[listName][index]["shainCd"] = value.shainCd;
          this.item[listName][index]["shainNm"] = value.shainNm;
          this.item[listName][index]["bumonCd"] = "";
          this.item[listName][index]["bumonNm"] = "";
          break;
        }
        case "bumonNm": {
          this.item[listName][index]["bumonCd"] = value.bumonCd;
          this.item[listName][index]["bumonNm"] = value.bumonNm;
          break;
        }
        case "hinmokuCd": {
          this.selectedItemNumber = {
            rowIndex: index,
            listName
          };
          this.getProduct(value);
          break;
        }
        case "typeOfInput": {
          this.item[listName][index]["typeOfInput"] = value;
          if(listName != "detailsForAccountsReceivablesRequestList"){
            if(this.item[listName][index]["typeOfInput"] != TypeOfInput.productCode){
              this.item[listName][index]["hinmokuCd"] = "";
              this.item[listName][index]["hinmokuRnm"] = "";
              this.item[listName][index]["nisugata"] = "";
            } else {
              this.item[listName][index]["classification"] = "";
              this.item[listName][index]["brandClassification"] = "";
              this.item[listName][index]["categoryName"] = "";
            }
          }
          break;
        }
        default: {
          this.item[listName][index][field] = value;
          break;
        }
      }
    },
    checkDisabled(data, field, listName) {
      if(includes(["storeGNm", "shainNm", "bumonNm"], field)) {
        return this.disabledAfterAccountingCheck;
      } if(this.disabled) {
        return true;
      } else {
        switch(field) {
          case "classification":
          case "brandClassification":
          case "categoryName":
            return (listName != "detailsForAccountsReceivablesRequestList" && (!data.typeOfInput || data.typeOfInput == TypeOfInput.productCode));
          case "hinmokuCd":
          case "hinmokuRnm":
          case "nisugata":
            return (listName != "detailsForAccountsReceivablesRequestList" && (!data.typeOfInput || data.typeOfInput != TypeOfInput.productCode))
          case "buttonProduct":
            return listName != "detailsForAccountsReceivablesRequestList" && (!data.typeOfInput || data.typeOfInput != TypeOfInput.productCode);
          default:
            return this.disabled;
        }
      }
    },
    checkTypeOfInputOnDetailForRequired(data, field, listName) {
      switch(field) {
        case "storeGNm":
        case "shainNm":
        case "bumonNm":
        case "typeOfItem":
        case "yoryo":
        case "irisu":
        case "mfYen":
        case "denpyoNet":
        case "accruedUnitPrice":
        case "finalTakeUnitPrice":
        case "hyojyunYen":
        case "quantityOfSoldItems":
        case "accruedAmount":
        case "salesAmount":
        case "finalMarginalProfit":
        case "finalMarginalProfitRatio":
        case "typeOfPromotionalExpenses":
        case "typeOfInput":
          return true;
        case "commission": 
        case "csdiscount":
          return false;
        case "classification":
        case "brandClassification":
        case "categoryName":
          return data.typeOfInput != TypeOfInput.productCode;
        case "hinmokuCd":
        case "hinmokuRnm":
          return (listName == "detailsForAccountsReceivablesRequestList") ||
            (listName != "detailsForAccountsReceivablesRequestList" && data.typeOfInput == TypeOfInput.productCode);
        case "nisugata":
          return data.typeOfInput == TypeOfInput.productCode;
      }
    },
    checkUpdatedInfo(callback, type = undefined) {
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
      })
    },
    editRequest() {
      this.checkUpdatedInfo(() => {
        this.fetchRequest("edit");
      })
    },
    saveRequest() {
      this.$refs.requestForm.validate()
      .then(isValidated => {
        if(!isValidated) {
          // scroll to top
          let requestBody = this.$refs.requestForm.$el.getElementsByClassName("request-body")[0];
          requestBody.scrollTop = 0;
        } else {
          if(this.item.detailsForAccountsReceivablesRequestList) {
            forEach(this.item.detailsForAccountsReceivablesRequestList, (item, index) => {
              item.sortNumber = index;
            });
          }
          if(this.item.detailsForPromotionalExpensesRequestTax8PercentList) {
            forEach(this.item.detailsForPromotionalExpensesRequestTax8PercentList, (item, index) => {
              item.sortNumber = index;
            });
          }
          if(this.item.detailsForPromotionalExpensesRequestTax10PercentList) {
            forEach(this.item.detailsForPromotionalExpensesRequestTax10PercentList, (item, index) => {
              item.sortNumber = index;
            });
          }
          if(this.item.detailsForPromotionalExpensesRequestLogisticFeesList) {
            forEach(this.item.detailsForPromotionalExpensesRequestLogisticFeesList, (item, index) => {
              item.sortNumber = index;
            });
          }
          AccountReceivableCreateService.updateAccountReceivableConfirmRequest(this.item.requestModel.fileModelList, this.item)
          .then(response => {
            if(response){
              this.returnToList();
            }
          })
          .catch(err => {
            console.log(err);
          });
        }
      });
    },
    print() {
      let template = RequestForPrint(this.item);
      this.$htmlToPaper(template);
    },
    // action table
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
        if(this.requestStatus == "申請待ち" || this.requestStatus == "承認待ち") {
          newItem.isChecked = 1;
        }
        if(!this.item[listName]) {
          this.item[listName] = [];
        }
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
      let deletedCd = this.item[listName][index].accountReceivableDetailCd;
      if(deletedCd) {
        if(!this.item.accountReceivableDetailIsDeleted) {
          this.item.accountReceivableDetailIsDeleted = [];
        }
        this.item.accountReceivableDetailIsDeleted.push(deletedCd);
      }
      this.item[listName].splice(index, 1);
    },
    // upload file
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
    // data for Modal
    onAddedComment(newComment) {
      let commentDetailsRequest = [];
      let selectedIndex = this.selectedItemNumber.rowIndex,
        listName = this.selectedItemNumber.listName;
      let forAddCommentIntoSalesChargeEditItemRequests = [];
      let storeGNm = this.item[listName][selectedIndex].storeGNm;
      let chargeName = this.item[listName][selectedIndex].shainNm;
      if (newComment.isSameStoreName || newComment.isSameShain || newComment.isSameShainExclamation) {
        forEach(this.item[listName], (item, index) => {
          let isAdded = false;
          if (newComment.isSameStoreName && item.storeGNm == storeGNm) {
            isAdded = true;
          } else if (newComment.isSameShain && item.shainNm == chargeName) {
            isAdded = true;
          } else if(newComment.isSameShainExclamation && item.shainNm == chargeName && item.mishuLimit < item.accruedUnitPrice){
            isAdded = true;
          }
          if(isAdded) {
            if(!this.item[listName][index].commentDetailsRequests) {
              this.item[listName][index].commentDetailsRequests = [];
            }
            this.item[listName][index].commentDetailsRequests = [{
              comment: newComment.comment,
              isDeputy: 0
            }];
            forAddCommentIntoSalesChargeEditItemRequests.push({
              accountReceivableDetailCd: this.item[listName][index].accountReceivableDetailCd,
              comment: newComment.comment
            });
            if(this.item[listName][index].accountReceivableDetailCd && this.item[listName][index].cd) {
              commentDetailsRequest.push({
                comment: newComment.comment,
                isDeputy: 0,
                requestAccountsReceivableDetailsCd: this.item[listName][index].accountReceivableDetailCd,
                isChecked: this.requestStatus == "申請待ち"
                  ? 0
                  : this.requestStatus == "確認待ち"
                    ? (this.item[listName][index].isChecked  ? 1 : 0)
                    : null
              });
            }
            this.item[listName][index].hasComment = this.item[listName][index]?.commentDetailsRequests.length > 0;
          }
        });
      } else {
        this.item[listName][selectedIndex].commentDetailsRequests = [{
          comment: newComment.comment,
          isDeputy: 0
        }];
        forAddCommentIntoSalesChargeEditItemRequests.push({
          accountReceivableDetailCd: this.item[listName][selectedIndex].accountReceivableDetailCd,
          comment: newComment.comment
        });
        if(this.item[listName][selectedIndex].accountReceivableDetailCd && this.item[listName][selectedIndex].cd) {
          commentDetailsRequest.push({
            comment: newComment.comment,
            isDeputy: 0,
            requestAccountsReceivableDetailsCd: this.item[listName][selectedIndex].accountReceivableDetailCd,
            isChecked: this.requestStatus == "申請待ち"
                  ? 0
                  : this.requestStatus == "確認待ち"
                    ? (this.item[listName][selectedIndex].isChecked  ? 1 : 0)
                    : null
          });
        }
        this.item[listName][selectedIndex].hasComment = this.item[listName][selectedIndex]?.commentDetailsRequests.length > 0;
      }
      if(forAddCommentIntoSalesChargeEditItemRequests.length > 0 && this.isApply) {
        this.addCommentIntoSalesChargeEditItemRequests(forAddCommentIntoSalesChargeEditItemRequests);
      }
      // Call api
      if(this.requestStatus != "作成中" && this.requestCd && commentDetailsRequest.length > 0) {
        CommentService.saveCommentForItems(this.requestCd, commentDetailsRequest)
        .then(response => {
          if(response.status == 200) {
            this.item.requestModel.updatedAt = response.data.updatedAt;
          }
        })
        .catch(err => {
          this.messageerror = "コメント入力時にエラーが発生しました。";
          this.showErrorDialog = true;
        });
      }
    },
    referToItem(itemNumber) {
      if(itemNumber) {
        // Scroll to item
        let itemNumberElement = document.getElementById(itemNumber);
        let itemNumberElementParent = itemNumberElement.closest("table").parentNode;
        let requestBody = this.$refs.requestForm.$el.getElementsByClassName("request-body")[0];
        requestBody.scrollTop = itemNumberElementParent.offsetTop - 100;
        if(this.referSelectItemNumber) {
          let beforeReferenEle = document.getElementById(this.referSelectItemNumber);
          beforeReferenEle.classList.remove("refering");
        }
        this.referSelectItemNumber = itemNumber;
        itemNumberElement.classList.add("refering");
      } else {
        if(this.referSelectItemNumber) {
          let beforeReferenEle = document.getElementById(this.referSelectItemNumber);
          beforeReferenEle.classList.remove("refering");
        }
        this.referSelectItemNumber = itemNumber;
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
        //54
        this.item[listName][index].mfYen = product.mfYen;
        // to compare with accruedUnitPrice -> for guideline icon
        this.item[listName][index].mishuLimit = product.mishuLimit;
      } else {
        //77
        this.item[listName][index].nisugata = product.nisugata;
      }
      if(!this.item[listName][index].cd) {
        // for new item
        if (listName == 'detailsForAccountsReceivablesRequestList') {
          //53
          this.item[listName][index].initialIrisu = product.irisu;
          //54
          this.item[listName][index].initialMfYen = product.mfYen ? product.mfYen : this.item[listName][index].mfYen;
        }
      }
      this.selectedItemNumber = null;
      this.showPopupSearchProduct = false;
    },
    getSupplier(event) {
      this.showPopupSupply = false;
      let torihikiNm = event.torihiki1Nm + event.torihiki2Nm;
      let torihikiCd = event.torihikiCd;
      this.item.requestModel.torihikiNm = torihikiNm;
      this.item.requestModel.mstTorihikiCd = torihikiCd;
      this.supplier = event;
    },
    // errors
    handleErrors(errors) {
      if(errors) {
        let formatErrors = new Set(Object.values(errors).flat());
        return Array.from(formatErrors);
      }
      return [];
    },
    // unlock request
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
  }
}