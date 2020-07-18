<template>
  <b-modal id="confirm-create-comment-modal" v-model="isVisible" size="lg" centered hide-header hide-footer>
    <b-container fluid class="confirm-create-comment">
      <ValidationObserver ref="commentInput" v-slot="invalid">
        <div class="comment-container">
          <div class="popup-title" :class="{'no-mb': isApply || isApprove || isSendBack}">
            <div>{{ title }}</div>
            <template v-if="isApply">
              <validation-provider
                v-slot="{ invalid, validated}"
                slim
                :rules="{required: isFirstTime}"
              >
                <v-select
                  :class="['main-vselect', {invalidClass: invalid && validated}]"
                  v-model="selectApprovalFlow"
                  :options="options"
                  :clearable="false"
                  :disabled="!isFirstTime"
                  label="name"
                  @input="getApprovalFlowDetail"
                >
                  <template #no-options="{}">
                    検索結果はありません
                  </template>
                </v-select>
              </validation-provider>
            </template>
          </div>
          <template v-if="isApply">
            <ApproveRequestDialog
              :is-in-comment-modal="true"
              :step-number="stepNumber"
              :approval-cd="savedApprovalFlow && savedApprovalFlow.cd"
              :approved-flow="approvalFlow"
            ></ApproveRequestDialog>
          </template>
          <template v-if="isApprove">
            <ApproveRequestDialog
              :is-in-comment-modal="true"
              :step-number="stepNumber"
              :approval-cd="savedApprovalFlow && savedApprovalFlow.cd"
              :approved-flow="approvalFlow"
              :is-show-for-tablet="true"
              :approving-step="approvingStep"
            ></ApproveRequestDialog>
          </template>
          <template v-if="isSendBack">
            <ApproveRequestDialog
              :is-in-comment-modal="true"
              :step-number="stepNumber"
              :approval-cd="savedApprovalFlow && savedApprovalFlow.cd"
              :approved-flow="approvalFlow"
              :initial-for-send-back="stepToSendBack"
              :is-show-for-tablet="true"
              @select-step-to-send-back="setTargetToSendBack"
            ></ApproveRequestDialog>
          </template>

          <b-row class="no-gutters popup-content" v-if="!(isTablet() && isReject)">
            <div> コメント </div>
            <TextareaSskf
                id="request-comment"
                name=""
                ref="commentInput"
                v-model="comment"
                :changed="invalid.changed"
                :rules="{required: true}"
                placeholder=""
                :row="5"
                :maxlength="1000"/>
          </b-row>
          <b-row align-h="end" class="popup-footer">
              <b-button variant="none" @click="$emit('cancel', comment, isApply)" class="btn-white">戻る</b-button>
              <b-button variant="none" @click="submit" class="btn-blue"> {{btnSubmitLabel}} </b-button>
          </b-row>
        </div>
      </ValidationObserver>
      
    </b-container>
  </b-modal>
</template>
<script>
import { mapState, mapActions } from "vuex";
import isTablet from "@/helper/isTabletDevice";
import RequestService from '@/services/RequestService';
export default {
  name: "CommentConfirmRequest",
  model: {
    prop: "value",
    event: "change"
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    btnSubmitLabel: {
      type: String,
      default: "確認を完了する"
    },
    title: {
      type: String,
      default: "請求内容を確認します。"
    },
    requestCd: {
      type: [String, Number],
      default: undefined
    },
    requestBumonCd: {
      type: [String, Number],
      default: undefined
    },
    requestBumonName: {
      type: String,
      default: undefined
    },
    requestComment: {
      type: String,
      default: ""
    },
    isApply: {
      type: Boolean,
      default: false
    },
    isApprove: {
      type: Boolean,
      default: false
    },
    isSendBack: {
      type: Boolean,
      default: false
    },
    isReject: {
      type: Boolean,
      default: false
    },
    isFirstTime: {
      type: Boolean,
      default: true // Requested = null
    },
    savedApprovalFlow: {
      type: Object,
      default: undefined
    },
    stepNumber: {
      type: [String, Number],
      default: 0
    },
    approvingStep: {
      type: Number,
      default: 0
    }
    
  },
  data() {
    return {
      isTablet: isTablet,
      isVisible: false,
      comment: this.requestComment,
      selectApprovalFlow: undefined,
      options: [],
      approvalFlow: {},
      stepToSendBack: 2
    }
  },
  watch: {
    value(newValue) {
      this.isVisible = newValue;
    },
    isVisible(newValue) {
      if(newValue) {
        if(this.isFirstTime && this.requestBumonCd) {
          this.selectApprovalFlow = undefined;
          this.approvalFlow = undefined;
          this.getListApprovalFlows(this.requestBumonCd)
            .then(response => {
              this.options = response;
            })
            .catch(err => {
              this.options = [];
            });
        } else {
          this.getSavedApprovedPersons();
        }
      } else {
        this.updateModel(false);
      }
    },
    requestComment(newValue) {
      this.comment = newValue;
    },
    savedApprovalFlow(newValue) {
      if(!this.isFirstTime && newValue) {
        this.selectApprovalFlow = newValue;
      }
    },
    stepNumber(newValue, oldValue) {
      if(newValue && newValue != oldValue) {
        if(this.isSendBack) {
          this.stepToSendBack = this.stepNumber > 2 ? 2 : 1
        }
      }
    }
  },
  methods: {
    ...mapActions("DropdownModule", ["getListApprovalFlows"]),
    getApprovalFlowDetail() {
      if(this.requestCd && this.selectApprovalFlow) {
        RequestService.getApprovalFlowDetail(this.selectApprovalFlow.cd, this.requestCd)
          .then(response => {
            this.approvalFlow = response.data;
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    getSavedApprovedPersons() {
      if(this.requestCd) {
        RequestService.getSavedApprovalPersons(this.requestCd)
        .then(response => {
          this.approvalFlow = response.data;
        })
        .catch(err => {
          console.log(err);
        });
      }
    },
    setTargetToSendBack(step) {
      this.stepToSendBack = step;
    },
    isHasData(key) {
      return this.approvalFlow[key] && this.approvalFlow[key].length > 0;
    },
    submit() {
      this.$refs.commentInput.validate()
      .then(isValidated => {
        if(isValidated) {
          if(this.isApply) {
            this.$emit('submit', this.comment, this.selectApprovalFlow?.cd);
          } else if(this.isApprove) {
            this.$emit('submit', this.comment);
          } else if(this.isSendBack) {
            this.$emit('submit', this.comment, this.stepToSendBack);
          } else {
            this.$emit('submit', this.comment);
          }
        }
      });
    },
    updateModel(val) {
      if (val !== this.value) {
        this.$emit('change', val)
      }
    }
  }
}
</script>
<style lang="scss">
#confirm-create-comment-modal {
  .modal-dialog {
    min-width: 50rem;
    max-width: 50rem;
  }
  .confirm-create-comment {
    .comment-container {
      margin: 3rem 6rem;
      .popup-title {
        margin-bottom: 3rem;
        display: flex;
        & > div{
          &:first-child {
            white-space: nowrap;
            align-self: flex-end;
          }
        }
        &.no-mb {
          margin-bottom: 0.5rem;
        }
      }
      .v-select {
        &.vs--disabled {
          cursor: default;
          .vs__selected-options {
            height: 2rem;
            width: 60%;
            cursor: default;
          }
        }
      }
      #approval-flow {
        .flow {
          display: flex;
          min-height: 5rem;
          background: #E5E5E5 0% 0% no-repeat padding-box;
          padding: 10px 42px;
          padding-right: 10px;
          margin-bottom: 1rem;
          & > div {
            flex: auto;
            background: #fff;
            color: #707070;
            &.approveDialog_next {
              background: transparent;
              max-width: 5%;
              min-width: 5%;
              align-self: center;
              display: flex;
              justify-content: center;
            }
            &:not(.approveDialog_next) {
              max-width: 15%;
              min-width: 15%;
              font-size: 0.75rem;
              padding: 3px;
              display: flex;
              flex-flow: column nowrap;
              & > div.w-100 {
                justify-content: flex-end;
              }
              & > div.w-100:last-child {
                display: flex;
              }
              & > div.w-100:first-child {
                justify-content: space-between;
              }
              i {
                font-size: 1.375rem;
                font-weight: 700;
                &.fa-user-alt{
                  font-size: 1.6875rem;
                }
                &.fa-file-alt {
                  margin-right: 10px;
                }
              }
              &#approval-flow-1 {
                i.fa-user-alt{
                  color: #44A5CC;
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
            list-style: none;
            padding: 0;
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
            }
          }
        }
        .approval-flow-tooltip.tooltip.b-tooltip {
          opacity: 1;
          .tooltip-inner {
            background-color: #44A5CC;
            color: white;
            max-width: initial;
            padding: 0;
            display: inline-block;
            border-radius: 4px;
          }
          .arrow:before {
            margin-top: 0;
            margin-bottom: 0;
            border-top-color: #44a5cc;
            border-bottom-color: #44a5cc;
          }
        }
      }
      .popup-content {
        flex-wrap: wrap;
          & > * {
            flex: 1 0 100%;
            width: 100%;
          }
        & > span {
          display: block;
          width: 100% ;
          .label_input > div {
            width: 100%;
          }
        }
      }
      .popup-footer {
        margin-top: 3rem;
        button {
          padding: 1rem 2.5rem;
          margin-left: 2rem;
          width: auto;
          height: auto;
        }
      }
    }
  }
}
</style>