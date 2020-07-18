<template>
  <div :class="['approval-flow', {'in-modal': isInCommentModal, approvalTablet: isTablet() }]" :id="'approval-flow' + (isInCommentModal ? '-in-modal' : '')">
    <div class="flow">
      <template v-for="(item, keyName) in nonDeputyApprovalPerson">
        <div :key="`approveDialog_item-${keyName}`"
          :id="keyName + (isInCommentModal ? '-in-modal' : '')"
          :class="[
            'approveDialog_item',
            {active: isShowApproverDropdown == keyName},
            {'send-back-selected': isSelectedStep(keyName)}
          ]">
          <div class="header-flow" @click.stop="!isTablet() && onClickFlow(keyName)">
            <div class="w-100">
              {{keyName | formatStepName}} 
              <i 
                v-if="!isTablet()"
                class="fas fa-angle-down float-right"
                
              ></i>
            </div>
            <div class="w-100 user">
              <i v-if="isCurrentStep(keyName)" class="fas fa-file-alt documnetIcon "></i>
              <i
                v-if="isLoginUserInApproval(keyName)"
                :class="['fas', isApprovingStep(keyName) ? 'fa-user-alt' : 'fa-user']"
                @click.stop="setApprovingStep(keyName)"
              ></i>
            </div>
          </div>
          <template v-if="isShowForTablet && isTablet()">
            <div class="tablet-action">
              <b-button @click="showTooltip((keyName + (isInCommentModal ? '-in-modal' : '')), $event)">担当者<br/>確認</b-button>
            </div>
          </template>
          <transition v-if="!isTablet()" name="translate-fade-down">
            <div
              v-show="isShowApproverDropdown == keyName"
              class="approve-flow-person"
                @click.stop
                ref="dropdown-container"
              >
              <div v-if="statusPage <= 11" key="for-edit" class="approve-flow-person-container">
                <div class="row-1 header1">
                  <span class="btn"></span>
                  <span class="radio"></span>
                  <span class="bumon">現担当者の部門名</span>
                  <span class="shain">現担当者名</span>
                  <span class="action"></span>
                </div>
                <div class="row-2 person-list customScrollBar non-deputy">
                  <div v-for="(person, index) in item" :key="`non-deputy-${person.shainCd}-${index}`">
                    <span class="btn"></span>
                    <span class="radio">
                      <b-form-radio 
                      v-model="nonDeputyPersonSelected" 
                      :name="keyName + '-non-deputy'" 
                      :value="person"
                    ></b-form-radio>
                    </span>
                    <span class="bumon"> {{person.bumonNm}} </span>
                    <span class="shain"> {{person.shainNm}} </span>
                    <span class="action"></span>
                  </div>
                </div>

                <div class="row-3 header1 action-change-current-charge">
                  <span class="btn">
                    <b-button
                      v-if="showReplaceCharge(keyName)"
                      :disabled="!(deputyApprovalPerson[keyName] &&
                        deputyApprovalPerson[keyName].length > 0)"
                      @click="changePersonPosition(keyName)"
                    >担当交代アイコン</b-button>
                  </span>
                  <span class="radio">
                    <i v-if="showReplaceCharge(keyName)" class="fas fa-exchange-alt"></i>
                  </span>
                  <span class="bumon">{{ deputyApprovalPerson[keyName].length > 0 ? "代理人の部門名" : ""}}</span>
                  <span class="shain">{{ deputyApprovalPerson[keyName].length > 0 ? "代理人名" : ""}}</span>
                  <span class="action"></span>
                </div>

                <div v-if="deputyApprovalPerson[keyName]" class="row-4 person-list customScrollBar deputy">
                  <div
                    v-for="(person, index2) in deputyApprovalPerson[keyName]"
                    :key="`deputy-${person.shainCd}-${index2}`">
                    <span class="btn"></span>
                    <span class="radio">
                      <b-form-radio 
                      v-model="deputyPersonSelected" 
                      :name="keyName + '-deputy'" 
                      :value="person"
                    ></b-form-radio>
                    </span>
                    <span class="bumon"> {{person.bumonNm}} </span>
                    <span class="shain"> {{person.shainNm}} </span>
                    <span class="action">
                      <i v-if="showAddOrDeleteDeputy(keyName)" class="fas fa-minus-circle" @click="removeOutDeputyList(keyName, person)"></i>
                    </span>
                  </div>
                </div>

                <div class="row-5 header1 search" v-if="showAddOrDeleteDeputy(keyName)">
                  <span class="btn"></span>
                  <span class="radio"></span>
                  <span class="search-title">代理人検索</span>
                </div>

                <div class="row-6 header1 search" v-if="showAddOrDeleteDeputy(keyName)">
                  <span class="btn"></span>
                  <span class="radio"></span>
                  <span class="bumon">部門名</span>
                  <span class="shain">氏名</span>
                  <span class="action"></span>
                </div>

                <div class="row-7 header1 search" v-if="showAddOrDeleteDeputy(keyName)">
                  <span class="btn"></span>
                  <span class="radio"></span>
                  <span class="search bumon">
                    <v-select
                      class="main-vselect"
                      v-model="searchBumon"
                      :options="departmentList"
                      :clearable="false"
                      label="bumonNm"
                      value="bumonCd"
                      @open="searchDepartments"
                      @search="(search, loading) => {searchDepartments(search, loading)}"
                    >
                      <template #no-options="{}">
                        検索結果はありません
                      </template>
                    </v-select>
                  </span>
                  <span class="search shain">
                    <v-select
                      class="main-vselect"
                      v-model="searchShain"
                      :options="shainList"
                      :clearable="false"
                      label="shainNm"
                      value="shainCd"
                      @open="searchShainList"
                      @search="(search, loading) => {searchShainList(search, loading)}"
                    ></v-select>
                  </span>
                  <span class="action">
                    <i v-if="showAddOrDeleteDeputy(keyName)" class="fas fa-plus-circle" @click="addToDeputyList(keyName)"></i>
                  </span>
                </div>
              </div>
              <div v-else key="for-read" class="approve-flow-person-container read-only">
                <div class="row-1 header1">
                  <span class="bumon">現担当者の部門名</span>
                  <span class="shain">現担当者名</span>
                </div>
                <div class="row-2 person-list customScrollBar non-deputy">
                  <div v-for="(person, index) in item" :key="`non-deputy-${person.shainCd}-${index}`">
                    <span class="bumon"> {{person.bumonNm}} </span>
                    <span class="shain"> {{person.shainNm}} </span>
                  </div>
                </div>
                <div class="row-3 header1 action-change-current-charge">
                  <span class="bumon">{{ deputyApprovalPerson[keyName].length > 0 ? "代理人の部門名" : ""}}</span>
                  <span class="shain">{{ deputyApprovalPerson[keyName].length > 0 ? "代理人名" : ""}}</span>
                </div>

                <div v-if="deputyApprovalPerson[keyName]" class="row-4 person-list customScrollBar deputy">
                  <div
                    v-for="(person, index2) in deputyApprovalPerson[keyName]"
                    :key="`deputy-${person.shainCd}-${index2}`">
                    <span class="bumon"> {{person.bumonNm}} </span>
                    <span class="shain"> {{person.shainNm}} </span>
                  </div>
                </div>
              </div>
            </div>
          </transition>
        </div>
        <div
          :key="`approveDialog_next-${keyName}`"
          v-if="keyName != 'settlementApprovalPersons'"
          class="approveDialog_next"
          ><i class="fas fa-long-arrow-alt-right"></i>
        </div>
        <b-tooltip
          :key="`approveDialog_tooltip-${keyName}`"
          v-if="isHasData(keyName)"
          :class="keyName"
          :target="keyName + (isInCommentModal ? '-in-modal' : '')"
          :triggers="!isShowForTablet || !isTablet()? 'hover' : 'click'"
          placement="bottom"
          custom-class="approval-flow-tooltip"
          :container="'approval-flow' + (isInCommentModal ? '-in-modal' : '')"
        >
          <div class="container-tooltip">
          <ul class="customScrollBar">
              <li v-for="(person, index) in item" :key="index">
                <div>
                <span>{{person.bumonNm}}</span>
                <span>{{person.shainNm}}</span>
                </div>
              </li>
          </ul>
          </div>
        </b-tooltip>
      </template>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
import isTablet from "@/helper/isTabletDevice";
import userRole from "@/helper/checkRoleUserLogin";
import filter from "lodash/filter";
import some from "lodash/some";
import cloneDeep from "lodash/cloneDeep";
import isEmpty from "lodash/isEmpty";
import forEach from "lodash/forEach";
import RequestService from '@/services/RequestService';
import ApprovalFlowDetailsService from '@/services/ApprovalFlowDetailsService';
import MstBumonService from '@/services/MstBumonService';
import ShainService from '@/services/ShainService';
import {ApprovalFlowDetail, ApprovalFlowModel} from "@/models/ApprovalFlowModel";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
export default {
  name: 'ApproveRequestFlow',
  props: {
    isInCommentModal: {
      type: Boolean,
      default: true
    },
    requestStatus: {
      type: String,
      default: ""
    },
    requestedAt: {
      type: String,
      default: undefined
    },
    stepNumber: {
      type: [String, Number],
      default: 0
    },
    approvalCd: {
      type: [String, Number],
      default: 0
    },
    approvedFlow: {
      type: Object,
      default: () => {}
    },
    initialForSendBack: {
      type: [String, Number],
      default: 0
    },
    isShowForTablet: {
      type: Boolean,
      default: false
    },
    approvingStep: {
      type: Number,
      default: 1
    }
  },
  mixins: [getStatusOfPageDetailRequest],
  data() {
    return {
      isTablet: isTablet,
      isShowApproverDropdown: undefined,
      approvalPersons: new ApprovalFlowModel(),
      nonDeputyPersonSelected: undefined,
      deputyPersonSelected: undefined,
      searchBumon: undefined,
      searchShain: undefined,
      departmentList: [],
      shainList: [],
      selectedStep: undefined
    }
  },
  computed:{
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser
    }),
    requestCd() {
      return this.$route.params.cd;
    },
    nonDeputyApprovalPerson() {
      let _nonDeputyApprovalPerson = new ApprovalFlowModel();
      if(this.approvalPersons) {  
        forEach(this.approvalPersons, (value, key) => {
          _nonDeputyApprovalPerson[key] = filter(value, o => {
            return !o.isDeputy;
          });
        });
      }
      return _nonDeputyApprovalPerson;
    },
    deputyApprovalPerson() {
      let _deputyApprovalPerson = new ApprovalFlowModel();
      if(this.approvalPersons) {  
        forEach(this.approvalPersons, (value, key) => {
          _deputyApprovalPerson[key] = filter(value, o => {
            return o.isDeputy;
          });
        });
      }
      return _deputyApprovalPerson;
    }
  },
  watch: {
    approvedFlow: {
      handler(newValue) {
        if(this.isInCommentModal) {
          this.approvalPersons = isEmpty(this.approvedFlow) ? new ApprovalFlowModel() : this.approvedFlow;
        }
      }, deep : true
    },
    initialForSendBack(newValue) {
      if(this.isInCommentModal && newValue != 0) {
        this.selectedStep = newValue;
      }
    }
  },
  filters: {
    formatStepName(value) {
      switch (value) {
        case "applyPersons": {
          return "申請";
        }
        case "firstApprovalPersons": {
          return "第1承認";
        }
        case "secondApprovalPersons": {
          return "第2承認";
        }
        case "thirdApprovalPersons": {
          return "第3承認";
        }
        case "settlementApprovalPersons": {
          return "決裁";
        }
        default:
          return "";
      }
    }
  },
  created() {
    if(!this.isInCommentModal) {
      this.getSavedApprovedPersons();
    } else {
      this.approvalPersons = isEmpty(this.approvedFlow) ? new ApprovalFlowModel() : this.approvedFlow;
      this.selectedStep = this.initialForSendBack;
    }
  },
  mounted() {
    this.$nextTick(() => {
      if(!this.isInCommentModal) {
        if(typeof document === "object") {
          this.isShowApproverDropdown != ""
            ? document.body.addEventListener('click', this.closeApproverDropdown)
            : document.body.removeEventListener('click', this.closeApproverDropdown);
        }
      } else if(this.isShowForTablet && isTablet()) {
        // close tooltip
        if(typeof document === "object") {
          this.isShowApproverDropdown != ""
            ? document.body.addEventListener('click', this.closeTooltip)
            : document.body.removeEventListener('click', this.closeTooltip);
        }
      }
    })
  },
  methods: {
    getSavedApprovedPersons() {
      if(this.$route.params.cd) {
        RequestService.getSavedApprovalPersons(this.requestCd)
        .then(response => {
          this.approvalPersons = response.data;
          userRole(this.approvalPersons);
          let keyApprovalFirst = undefined;
          forEach(this.approvalPersons, (value, key) => {
            let stepByKey = this.converKeyToStepNumber(key);
            if(!keyApprovalFirst && this.isLoginUserInApproval(key) && stepByKey >= this.stepNumber) {
              keyApprovalFirst = key;
            }
          });
          this.setApprovingStep(keyApprovalFirst);
        })
        .catch(err => {
        console.log(err);
        });
      }
    },
    showReplaceCharge(key) {
      return key != 'settlementApprovalPersons' &&
        this.approvalCd &&
        [3, 5, 7].includes(this.statusPage) &&
        this.loginUser &&
        this.loginUser.roles &&
        this.loginUser.roles.length > 0 &&
        ( // Action permissions
          this.loginUser.roles.includes("applyPersons") ||
          this.loginUser.roles.includes("firstApprovalPersons") ||
          this.loginUser.roles.includes("secondApprovalPersons") ||
          this.loginUser.roles.includes("thirdApprovalPersons")
        ) &&
        (
          (
            this.requestStatus == "確認待ち" && this.requestedAt && this.requestedAt != ""
          ) ||
          (
            this.requestStatus == "申請待ち" && this.requestedAt && this.requestedAt != ""
          ) || this.requestStatus == "承認待ち"
        );
    },
    showAddOrDeleteDeputy(key){
      let isShow = key != 'settlementApprovalPersons' &&
        this.approvalCd &&
        [3, 5, 7].includes(this.statusPage);
        
      let isConfirmAndSendBack = this.requestStatus == "確認待ち" && this.requestedAt && this.requestedAt != "",
        isApplyAndSendBack = this.requestStatus == "申請待ち" && this.requestedAt && this.requestedAt != "",
        isApprove = this.requestStatus == "承認待ち";
      if(key == "applyPersons") {
        return isShow && (isConfirmAndSendBack || isApplyAndSendBack || isApprove);
      } else if(key == "firstApprovalPersons" || key == "secondApprovalPersons" || key == "thirdApprovalPersons"){
        return isShow && this.loginUser &&
          this.loginUser.roles &&
          this.loginUser.roles.length > 0 &&
          this.loginUser.roles.includes(key) &&
          (isConfirmAndSendBack || isApplyAndSendBack || isApprove);
      }
      return false;

    },
    isHasData(key) {
      return this.isShowApproverDropdown != key &&
        this.nonDeputyApprovalPerson[key] &&
        this.nonDeputyApprovalPerson[key].length > 0;
    },
    converKeyToStepNumber(key) {
      switch (key) {
        case "applyPersons": {
          return 1;
        }
        case "firstApprovalPersons": {
          return 2;
        }
        case "secondApprovalPersons": {
          return 3;
        }
        case "thirdApprovalPersons": {
          return 4;
        }
        case "settlementApprovalPersons": {
          return 5;
        }
        default:
          return 0;
      }
    },
    isSelectedStep(key) {
      let step = this.converKeyToStepNumber(key);
      return this.selectedStep == step;
    },
    onClickFlow(key) {
      if(!this.isInCommentModal) {
        this.showApproverDropdown(key);
      } else if(this.initialForSendBack) {
        this.selectToSendBack(key);
      }
    },
    setApprovingStep(key) {
      if(this.requestStatus != "却下") {
        let step = this.converKeyToStepNumber(key);
        if(!this.isInCommentModal && step >= this.stepNumber) {
          this.$emit("set-approving-step", step);
        }
      }
    },
    showApproverDropdown(key) {
      this.nonDeputyPersonSelected = undefined;
      this.deputyPersonSelected = undefined;
      this.departmentList = [];
      this.shainList = [];
      this.searchBumon = undefined;
      this.searchShain = undefined;
      if(this.isShowApproverDropdown != key) {
        this.isShowApproverDropdown = key;
      } else {
        this.isShowApproverDropdown = "";
      }
    },
    closeApproverDropdown ($event) {
      if (!$event || !this.$el.contains($event.target)) {
        if (this.isShowApproverDropdown != "") {
          this.showApproverDropdown(this.isShowApproverDropdown);
        }
      }
    },
    selectToSendBack(key) {
      if(this.isValidForSendBack(key)){
        let step = this.converKeyToStepNumber(key);
        this.selectedStep = step;
        if(this.selectedStep < this.stepNumber) {
          this.$emit("select-step-to-send-back", this.selectedStep);
        }
      }
    },
    isCurrentStep(key) {
      let step = this.converKeyToStepNumber(key);
      return this.stepNumber == step;
    },
    isValidForSendBack(key) {
      let step = this.converKeyToStepNumber(key);
      return this.stepNumber > step;
    },
    isLoginUserInApproval(key){
      return some(this.approvalPersons[key], o => {
        return this.loginUser && o.shainCd == this.loginUser.sub;
      });
    },
    isApprovingStep(key) {
      let stepByKey = this.converKeyToStepNumber(key);
      return stepByKey == this.approvingStep && stepByKey >= this.stepNumber;
    },
    changePersonPosition(key) {
      if(this.nonDeputyPersonSelected &&
        this.nonDeputyPersonSelected.bumonCd &&
        this.nonDeputyPersonSelected.shainCd &&
        this.deputyPersonSelected &&
        this.deputyPersonSelected.bumonCd &&
        this.deputyPersonSelected.shainCd
      ) {
        ApprovalFlowDetailsService.replaceChargePersons({
          approvalFlowDetailMain: this.nonDeputyPersonSelected.cd,
          approvalFlowDetailIsDeputy: this.deputyPersonSelected.cd
        })
        .then(response => {
          // clear selected
          this.nonDeputyPersonSelected = undefined;
          this.deputyPersonSelected = undefined;
          this.getSavedApprovedPersons();
        })
        .catch(err => {
          console.log(err);
        });
      }
    },
    searchDepartments(search, loading) {
      if(loading && typeof loading == "function") {
        loading(true);
      }
      let step = this.converKeyToStepNumber(this.isShowApproverDropdown);
      this.departmentList = [];
      MstBumonService.listDepartmentByStepNumber(step, this.approvalCd)
      .then(response => {
        this.departmentList = response.data || [];
      })
      .catch(err => {
        console.log(err);
        this.departmentList = [];
      })
      .then(() => {
        if(loading && typeof loading == "function") {
          loading(false);
        }
      });
    },
    searchShainList(search, loading) {
      if(loading && typeof loading == "function") {
        loading(true);
      }
      this.shainList = [];
      if(this.searchBumon && this.searchBumon.bumonCd) {
        let step = this.converKeyToStepNumber(this.isShowApproverDropdown);
        ShainService.listShiansForApproval(step, this.searchBumon.bumonCd, this.requestCd)
        .then(response => {
          this.shainList = response.data;
        })
        .catch(err => {
          console.log(err);
          this.shainList = [];
        })
        .then(() => {
          if(loading && typeof loading == "function") {
            loading(false);
          }
        });
      }
    },
    addToDeputyList(key) {
      if(this.searchBumon &&
        this.searchBumon.bumonCd &&
        this.searchBumon.bumonNm &&
        this.searchShain &&
        this.searchShain.shainCd &&
        this.searchShain.shainNm
      ){
        let newDeputyPerson = new ApprovalFlowDetail();
        newDeputyPerson.bumonCd = this.searchBumon.bumonCd,
        newDeputyPerson.bumonNm = this.searchBumon.bumonNm,
        newDeputyPerson.shainCd = this.searchShain.shainCd,
        newDeputyPerson.shainNm = this.searchShain.shainNm,
        newDeputyPerson.stepNumber = this.converKeyToStepNumber(key);
        ApprovalFlowDetailsService.insertDeputyPerson(this.requestCd, newDeputyPerson)
        .then(response => {
          this.searchBumon = undefined;
          this.searchShain = undefined;
          this.getSavedApprovedPersons();
        })
        .catch(err => {
          console.log(err);
        });
      }
    },
    removeOutDeputyList(key, person) {
      if(person.cd) {
        ApprovalFlowDetailsService.deleteChargeIsDeputy(person.cd)
        .then(response => {
          if(this.deputyPersonSelected &&
            this.deputyPersonSelected.bumonCd == person.bumonCd &&
            this.deputyPersonSelected.shainCd == person.shainCd
          ) {
            this.deputyPersonSelected = undefined;
          }
          this.getSavedApprovedPersons();
        })
        .catch(err => {
          console.log(err);
        })
      }
    },
    showTooltip(id, event) {
      if(this.isShowForTablet && isTablet()) {
        if(event && event.target) {
          event.target.style.cursor = "wait";
        }
        this.$root.$emit("bv::hide::tooltip");
        setTimeout(() => {
          if(event && event.target) {
            event.target.style.cursor = "default";
          }
          if(id != this.selectedFlow) {
            this.$root.$emit("bv::show::tooltip", id);
            this.selectedFlow = id;
          } else {
            this.selectedFlow = "";
          }
        }, 200);
      }
    },
    closeTooltip($event) {
      this.$root.$emit("bv::hide::tooltip");
    }
  },
  destroyed () {
    document.body.removeEventListener('click', this.closeApproverDropdown);
    document.body.removeEventListener('click', this.closeTooltip);
  },
}
</script>
<style lang="scss" scoped>
.approval-flow {
  width: 100%;
  max-width: 37.1875rem;
  height: 100%;
  .flow {
    display: flex;
    min-height: 100%;
    max-height: 100%;
    padding: 0px 1rem;
    padding-top: 10px;
    & > div {
      flex: auto;
      color: #707070;
      &.approveDialog_next {
        background: transparent;
        max-width: 5%;
        min-width: 5%;
        align-self: center;
        display: flex;
        justify-content: center;
        font-size: 1rem;
        font-weight: 700;
        color: #000000;
        min-height: 3.4375rem;
        align-self: baseline;
        & > i {
          align-self: center;
        }
      }
      &:not(.approveDialog_next) {
        width: calc(80%/5);
        max-width: 4.6875rem;
        min-width: 4.6875rem;
        font-size: 0.75rem;
        display: flex;
        flex-flow: row wrap;
        position: relative;
        border-top-right-radius: 4px;
        & > .header-flow {
          width: 100%;
          background: #fff;
          min-height: 3.4375rem;
          & > div.w-100 {
            justify-content: flex-end;
            i {
              font-size: 1.375rem;
              font-weight: 700;
            }
            &.user {
              display: flex;
              i {
                font-size: 1.6875rem;
                &.fa-user {
                  margin-right: 2px;
                }
                &.fa-file-alt {
                  margin-bottom: 5px;
                  font-size: 1.25rem;
                  &:last-child{
                    margin-right: 40%;
                  }
                }
                &.fa-user-alt{
                  color: #44A5CC;
                }
              }
            }
            &:first-child {
              padding: 3px;
              padding-bottom: 0;
              justify-content: space-between;
            }
          }
        }
        & > .tablet-action {
          margin-top: 10px;
          height: 3.125rem;
          max-height: 3.125rem;
          width: 100%;
          & > button {
            font-size: 1.125rem;
            height: 3.125rem;
            width: 100%;
          }
        }
        & > .approve-flow-person {
          position: absolute;
          top: 99%;
          right: -2px;
          min-width: 20rem;
          max-width: 24rem;
          z-index: 99;
          background: #44A5CC;
          color: #fff;
          padding: 10px 5px;
          border-radius: 4px;
          border-top-right-radius: 0;
          border-top-left-radius: 0;
          border: 2px solid #000;
          /* min-height: 12rem; */
          max-height: 70vh;
          display: flex;
          flex-wrap: nowrap;
          & > .approve-flow-person-container {
            width: 100%;
            max-width: 100%;
            & > div {
              flex-grow: 1;
              width: 100%;
              display: flex;
              flex-wrap: nowrap;
              &.person-list {
                flex-wrap: wrap;
                max-height: 7rem;
                overflow-y: auto;
                overflow-x: hidden;
              }
              & > div {
                flex-grow: 1;
                width: 100%;
                display: flex;
                flex-wrap: nowrap;
                margin: 2px;
              }
              span {
                vertical-align: middle;
                text-align: center;
                padding: 3px;
                margin: 0 2px;
                align-self: center;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100%;
                &.btn {
                  min-width: 3.5rem;
                  max-width: 3.5rem;
                  padding: 0;
                  & > button {
                    font-size: 0.75rem;
                    background: #fff;
                    border-radius: 5px;
                    color: #000;
                  }
                }
                &.radio {
                  min-width: 1rem;
                  max-width: 1rem;
                  padding: 0;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                  i.fas.fa-exchange-alt {
                    transform: rotate(90deg);
                    font-size: 1.4rem;
                  }
                  /deep/ .custom-control.custom-radio {
                    .custom-control-label::before {
                      border-color: #000;
                    }
                    .custom-control-input:checked ~ .custom-control-label::before {
                      color: #fff;
                      border-color: #000;
                      background-color: #fff;
                    }
                    .custom-control-input:checked ~ .custom-control-label::after {
                      background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='-4 -4 8 8'%3e%3ccircle r='3' fill='%23000'/%3e%3c/svg%3e");
                    }
                  }
                }
                &.action {
                  padding: 0;
                  min-width: 1rem;
                  max-width: 1rem;
                  i {
                    cursor: pointer;
                    font-size: 1rem;
                    border: 1px solid #000;
                    border-radius: 50%;
                    background: #000;
                  }
                }
                &.deputy-header {
                  vertical-align: bottom;
                }
                &.search-title {
                  text-align: left;
                  border-bottom: 1px solid #fff !important;
                  padding: 0;
                  padding-top: 10px;
                  flex-grow: 1;
                  justify-content: flex-start;
                }
                &.search {
                  padding: 0;
                  min-width: 8.5rem;
                  max-width: 8.5rem;
                  &.shain {
                    min-width: 5.5rem;
                    max-width: 5.5rem;
                  }
                  /deep/ .main-vselect {
                    max-height: 1.5rem;
                    .vs__dropdown-toggle {
                      padding: 0;
                      min-height: 1.5rem;
                      max-height: 1.5rem;
                      .vs__selected-options {
                        max-height: 1.5rem;
                        .vs__selected {
                          max-height: 1.5rem;
                        }
                        input {
                          margin: 0;
                          padding: 0;
                        }
                      }
                      .vs__actions {
                        max-height: 1.5rem;
                      }
                    }
                    .vs__dropdown-menu {
                      z-index: 9999;
                      max-height: 8rem;
                      position: static;
                      & > li {
                        min-width: 100%;
                        max-width: 100%;
                        & > * {
                          min-width: 100%;
                          max-width: 100%;
                        }
                      }
                    }
                  }
                }
                &.bumon {
                  min-width: 8.5rem;
                  max-width: 8.5rem;
                  align-self: flex-end;
                }
                &.shain {
                  min-width: 5.5rem;
                  max-width: 5.5rem;
                  align-self: flex-end;
                }
              }
              &:not(.header1) {
                span:not(.btn):not(.radio):not(.action) {
                  background: #E5E5E5;
                  color: #000000;
                }
              }
            }
            &.read-only {
              .bumon {
                max-width: 60%;
                width: 60%;
              }
              .shain {
                max-width: 40%;
                width: 40%;
              }
            }
          }
        }
        &.active {
          background: #44A5CC !important;
          color: #fff;
          & > .header-flow {
            width: 100%;
            background: inherit;
            margin-bottom: -2px;
            z-index: 9001;
            & > div.w-100 {
              &.user {
                padding-right: 3px;
              }
              i.fa-user,
              i.fa-user-alt {
                color: #707070;
              }
            }
          }
        }
        &.send-back-selected {
          border: 1px solid #DF6561;
          i.fa-user,
          i.fa-user-alt {
            margin-bottom: -4px;
            margin-right: 4px;
          }
        }
      }
    }
  }
  .container-tooltip {
    background: transparent;
    background: #44A5CC;
    border-radius: 6px;
    padding: 3px;
    & > ul{
      max-height: 11rem;
      overflow-y: auto;
      overflow-x: hidden;
      list-style: none;
      padding: 10px 0px;
      margin-bottom: 8px;
      & > li {
        padding: 0;
        min-width: 11rem;
        & > div {
          padding: 0.5rem;
          margin:0 2px;
          border-bottom: 1px solid #fff;
          & > span {
            display: block;
            color: #fff;
            font-size: 0.75rem;
            font-weight: 400;
            width: 100%;
            text-align: center;
          }
        }
        &:last-child {
          & > div {
            border-bottom: none;
            padding-bottom: 0;
          }
        }
      }
    }
  }
  /deep/ .approval-flow-tooltip.tooltip.b-tooltip {
    opacity: 1;
    .tooltip-inner {
      background-color: #44A5CC !important;
      color: white !important;
      max-width: initial !important;
      padding: 0 !important;
      display: inline-block !important;
      border-radius: 4px !important;
    }
    .arrow:before {
      margin-top: 0 !important;
      margin-bottom: 0 !important;
      border-top-color: #44a5cc !important;
      border-bottom-color: #44a5cc !important;
    }
  }
  &.in-modal {
    .flow {
      padding: 0px 42px;
      padding-top: 8px;
      padding-bottom: 8px;
      background: #E5E5E5 0% 0% no-repeat padding-box;
      .w-100.user {
        padding: 0 9px;
        padding-right: 5px;
        padding-bottom: 3px;
      }
    }
  }
  &:not(.in-modal) {
    display: flex;
    justify-content: flex-end;
    margin-right: 2rem;
    width: auto;
  }
}
/*translate fade (top to down)*/
.translate-fade-down-enter-active, .translate-fade-down-leave-active {
  transition: all 250ms;
  transition-timing-function: cubic-bezier(.53,2,.36,.85);
}
.translate-fade-down-enter, .translate-fade-down-leave-active {
  opacity: 0;
}
.translate-fade-down-enter, .translate-fade-down-leave-to {
  position: absolute;
}

.translate-fade-down-enter {
  transform: translateY(-10px);
}
.translate-fade-down-leave-active {
  transform: translateY(10px);
}
.approvalTablet{
  &:not(.in-modal){
    // margin: 0 !important;
  }
  .flow {
    align-items: flex-end;
    // padding-left: 0 !important;
    & > div:not(.approveDialog_next) {
      max-width: 4.16rem;
      min-width: 4.16rem;
      max-height: 2.7rem;
      .header-flow{
        min-height: 2.7rem;
        div{
          font-size: 1rem;
          line-height: 1;
          &:last-child{
            justify-content: space-around;
            i{
              &:first-child{
                font-size: 1rem !important;
                line-height: 1.5;
                margin: 0 !important;
              }
              &:last-child{
                font-size:1.5rem !important;
              }
            }
          }
        }
      }
    }
    & > .approveDialog_next{
      max-height: 2.7rem;
      height: 100%;
      min-height: 2.7rem !important;
      align-self: flex-end !important;
    }
  }
}

</style>