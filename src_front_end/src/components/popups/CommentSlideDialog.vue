<template>
  <div @click.stop>
    <b-sidebar v-model="showpopup" class="commentSidebar" right no-header @click.stop>
      <b-row no-gutters class="commentSidebar-header">
        <b-col cols="12" class="commentSidebar-header__title">販売未収条件 明細コメント</b-col>
        <b-col cols="12" class="commentSidebar-header__action">
          <b-button tag="a" variant="none" class="item-number" @click.stop="$emit('refer-to-item', mainItemNumber)">@{{mainItemNumber}}</b-button><!-- :href="`#${mainItemNumber}`" -->
          <b-button class="btn-slide-popup" variant="none" @click.stop="showpopup = false"> <i class="fas fa-arrow-right"></i></b-button>
        </b-col>
      </b-row>
      <b-row v-if="hasError" class="commentSidebar-error no-gutters">
        <span class="error">エラー：何も入力されていません。</span> 
      </b-row>
      <b-row v-if="!isShowForTablet" no-gutters :class="['commentSidebar-content', {bg: isCanInputComment || isShowSameCustomerCheckbox || isSameChargeCheckbox || isShowSameExclamationCheckbox}]">
        <div class="commentSidebar-content__container">
          <textarea v-if="isCanInputComment" rows="6" class="w-100" maxlength="1000" v-model="commentValue"></textarea>
          <b-col v-if="isShowSameCustomerCheckbox" cols="12" class="checkboxes">
            <b-form-checkbox
                    id="isSameStoreName"
                    name="isSameStoreName"
                    v-model="isSameStoreName"
                    value="true"
                    unchecked-value="false"
                    plain
                    size="lg"
            >同じ店舗グループ名に同じコメント</b-form-checkbox>
          </b-col>
          <b-col v-if="isSameChargeCheckbox" cols="12" class="checkboxes">
            <b-form-checkbox
                    id="isSameCharge"
                    name="isSameCharge"
                    v-model="isSameCharge"
                    value="true"
                    unchecked-value="false"
                    size="lg"
                    plain
            >同じ担当者の明細に同じコメント</b-form-checkbox>
          </b-col>
          <b-col v-if="isShowSameExclamationCheckbox" cols="12" class="checkboxes">
            <b-form-checkbox
                    id="isSameShainExclamation"
                    name="isSameShainExclamation"
                    v-model="isSameShainExclamation"
                    value="true"
                    unchecked-value="false"
                    size="lg"
                    plain
            >同じ担当者の<i class="fas fa-exclamation color_red"></i>に同じコメント</b-form-checkbox>
          </b-col>
        </div>
      </b-row>
      <b-row no-gutters class="commentSidebar_handle">
        <div class="commentSidebar_handle__container">
          <b-button v-if="isShowInputBtn" variant="none" class="w-100 text-light" @click.stop="showCommentHistory">過去のコメント<br/>を再利用する</b-button>
          <b-button variant="none" class="w-100 text-light" :disabled="commentValue == ''" @click.stop="addComment">入力する</b-button>
        </div>
      </b-row>
      <b-row no-gutters class="commentSidebar_histories" @click.stop>
        <div class="commentSidebar_histories__container customScrollBar">
          <b-col cols="12" class="item" v-for="(comment,index) in oldComments" :key="index">
            <b-row no-gutters class="py-1">
              <b-col cols="6" class="pl-1">
                <span><i class="fas fa-user-alt"></i> {{comment.shainNm}}<span v-if="comment.isDeputy">(代理人)</span></span>
              </b-col>
              <b-col cols="6" class="pr-1">[{{ $moment(comment.createdAt).format('YYYY/MM/DD hh:mm') }}]</b-col>
            </b-row>
            <b-row no-gutters class="py-1">
              <b-col cols="12" class="comment">{{comment.comment}}</b-col>
            </b-row>
            <b-row no-gutters class="py-1">
              <b-col cols="12" class="pr-2">
                <i v-if="comment.cd && comment.isCapableOfBeingDeleted==1" class="fas fa-trash-alt float-right" @click.stop="showDeletePopup(comment.cd)"></i>
              </b-col>
            </b-row>
          </b-col>
        </div>
      </b-row>
    </b-sidebar>
    <CommentHistoryDialog v-if="isShowCommentHistory" v-model="isShowCommentHistory" :store-g-cd="storeGCd" @onSelectedComment="onSelectedComment($event)"/>
    <DeleteDialog v-if="isShowPopupDelete" v-model="isShowPopupDelete" title="削除してもよろしいですか？" @delete="deleteComment" />
  </div>
  
</template>
<script>
import isTablet from "@/helper/isTabletDevice";
import CommentService from "@/services/CommentService";
import CommentHistoryDialog from "./CommentHistoryDialog";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
import includes from "lodash/includes";
import {mapState} from "vuex";
export default {
  name: "CommentSlideDialog",
  mixins: [getStatusOfPageDetailRequest],
  components: {CommentHistoryDialog},
  props: {
    mainItemId: {
      type: Number
    },
    mainItemNumber: {
      type: [String,Number]
    },
    value: {
      type: Boolean
    },
    initComment: {
      type: String
    },
    requestStatus: {
      type: String,
      default: ""
    },
    isRequestSendBack: {
      type: Boolean,
      default: false
    },
    isFirstTimeApply: {
      type: Boolean,
      default: false
    },
    stepNumber: {
      type: Number,
      default: 1
    },
    storeGCd: {
      type: [Number, String],
      default: undefined
    }
  },
  data() {
    return {
      isSameStoreName: false,
      isSameCharge: false,
      isSameShainExclamation: false,
      isShowCommentHistory: false,
      showpopup: this.value,
      commentValue: "",
      oldComments: [],
      selectedCommentCd: null,
      isShowPopupDelete: false,
      hasError: false
    };
  },
  computed: {
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser
    }),
    isShowForTablet() {
      return isTablet();
    },
    isCanComment() {
      if(this.isRequestSendBack) {
        return this.loginUser &&
          this.loginUser.roles &&
          (
            (this.loginUser.roles.includes('applyPersons') && this.stepNumber == 1) ||
            (this.loginUser.roles.includes("firstApprovalPersons") && this.stepNumber <= 2) ||
            (this.loginUser.roles.includes("secondApprovalPersons") && this.stepNumber <= 3) ||
            (this.loginUser.roles.includes("thirdApprovalPersons") && this.stepNumber <= 4)
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
            return this.loginUser &&
              this.loginUser.roles &&
              (
                (this.loginUser.roles.includes("firstApprovalPersons") && this.stepNumber == 2) ||
                (this.loginUser.roles.includes("secondApprovalPersons") && this.stepNumber <= 3) ||
                (this.loginUser.roles.includes("thirdApprovalPersons") && this.stepNumber <= 4) ||
                (this.loginUser.roles.includes("settlementApprovalPersons") && this.stepNumber <= 5)
              );
          case "決済確認待ち":
            return this.loginUser && this.loginUser.roles && this.loginUser.roles.includes("confirmSettlementPersons");
          default:
            return false;
        }
      }
    },
    isCanInputComment() {
      return includes([2,3,5,7,12], this.statusPage) && this.isCanComment;
    },
    isShowSameCustomerCheckbox() {
      return includes([2,3,5,7,12], this.statusPage) && this.isCanComment;
    },
    isSameChargeCheckbox(){
      return includes([2,3,5,7,12], this.statusPage) && this.isCanComment;
    },
    isShowSameExclamationCheckbox(){
      return includes([2,3,5,7,12], this.statusPage) && this.isCanComment;
    },
    isShowInputBtn(){
      return !this.isShowForTablet && this.isCanComment;
    },
    isShowReuseCommentBtn() {
      return !this.isShowForTablet && this.requestStatus == "確認待ち" && this.isFirstTimeApply;
    }
  },
  watch: {
    value() {
      this.showpopup = this.value;
    },
    showpopup() {
      this.$emit('refer-to-item', null);
      this.$emit("input", this.showpopup);
    },
    mainItemNumber(newValue, oldValue) {
      if(newValue && newValue != oldValue) {
        this.fetchOldComments();
      }
    }
  },
  created() {
    if(this.initComment){this.commentValue = this.initComment}
    this.fetchOldComments();
  },
  mounted() {
    this.$nextTick(() => {
      if(typeof document === "object") {
        this.showpopup
          ? document.body.addEventListener('click', this.closeDialog)
          : document.body.removeEventListener('click', this.closeDialog);
      }
    });
  },
  methods: {
    fetchOldComments() {
      if (this.mainItemId) {
        CommentService.getCommentsOfAnItem(this.mainItemId).then(res => {
            this.oldComments = res.data || [];
        }).catch(err => {
          console.log(err);
        });
      }
    },
    showCommentHistory() {
      if(this.storeGCd) {
        this.isShowCommentHistory = true;
      }
    },
    closeDialog(event) {
      // if(!this.isShowCommentHistory){
      //   this.showpopup = false;
      // }
    },
    onSelectedComment(comment) {
      this.isShowCommentHistory = false;
      this.commentValue = comment;
    },
    addComment() {
      const temp = this.commentValue;
      if(this.commentValue == "") {
        this.hasError = true;
        return;
      }
      let newComment = {
        itemCode: this.mainItemNumber,
        comment: temp,
        isSameStoreName: this.isSameStoreName,
        isSameShain: this.isSameCharge,
        isSameShainExclamation: this.isSameShainExclamation
      }
      this.$emit("onAddedComment", newComment);
      setTimeout(() => {
        this.fetchOldComments();
        this.commentValue = "";
      }, 300)
    },
    showDeletePopup(commentCd) {
      this.selectedCommentCd = commentCd;
      if(this.selectedCommentCd) {
        this.isShowPopupDelete = true;
      }
    },
    deleteComment() {
      if (this.selectedCommentCd) {
        CommentService.deleteComment(this.selectedCommentCd)
        .then(res => {
          this.oldComments = this.oldComments.filter( (comment) => {
            return comment.cd !== this.selectedCommentCd;
          });
          this.selectedCommentCd = null;
          this.isShowPopupDelete = false;
        }).catch(err => {
          console.log(err);
        });
      }
    }
  },
  destroyed () {
    document.body.removeEventListener('click', this.closeDialog);
  }
};
</script>
<style lang="scss" scoped>
@import "@/assets/scss/pages/_comment-sidebar.scss";
</style>