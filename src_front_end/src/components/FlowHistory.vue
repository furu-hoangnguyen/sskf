<template>
  <collapse>
    <template v-slot:collapse-header>フロー履歴</template>
    <template v-slot:collapse-body>
      <b-container fluid class="no-gutters histories">
        <template v-for="(history, index) in historiesRender" >
          <b-row
            :key="`history-log-${index}`"
            :class="['pl-0 histories-container', getBorderColor(history.actionComment)]">
            <div class="stepName">{{ history.actionComment }}</div>
            <div :class="['flow-container', {'group-action': history.actions}]">
              <div>
                  <div v-for="(action, j) in history.actions" :key="j" :class="['dropdown-toggle-item', {show: !checkEmpty(comments) && commentCd == history.cd}]">
                    <div class="list-log">
                      <span :key="`action-icon-${j}-${action.cd}`" class="icon"><i class="fas fa-user"></i></span>
                      <span :key="`action-bumon-${j}-${action.cd}`" class="bumon">{{ action.bumonNm }}</span>
                      <span :key="`action-shain${j}-${action.cd}`" class="shain">{{action.isDeputy == 1 ? '代理人' : ''}} {{ action.shainNm}}</span>
                      <span :key="`action-time-${j}-${action.cd}`" class="time">[{{action.createdAt | $_mixins_formatDate("YYYY/MM/DD HH:mm") }}]</span>
                      <span :key="`action-menu-comment${j}-${action.cd}`" v-if="j == 0" class="action list-comment" @click.stop="showStatement(history.cd, index, j)">
                        <i class="fas fa-align-justify"></i>
                      </span>
                    </div>
                    <div class="comment">
                      <p class="pre">{{ action.comment }}</p>
                    </div>
                </div>
              </div>
              <div v-if="!checkEmpty(comments) && commentCd && index == indexSelected" class="history-detail" :style="`margin-top: ${indexChild*30}px`" :index-j="indexChild">
                <span class="action list-comment" @click.stop="showStatement()">
                  <i class="fas fa-align-justify"></i>
                </span>
                <div v-for="comment in comments" :key="comment.cd">
                  <div class="comment-item">
                    <span class="item-number">@{{comment.itemNumber}}</span>
                    <span>{{comment.bumonNm}}</span>
                    <span>{{comment.isDeputy == 1 ? '代理人' : ''}} {{comment.shainNm}}</span>
                    <span>[{{comment.createdAt | $_mixins_formatDate("YYYY/MM/DD HH:mm") }}]</span>
                  </div>
                  <div class="commentItem1">
                    <span class="pre">{{comment.comment}}</span>
                    <span v-if="comment.isCapableOfBeingDeleted == 1 "><i @click="showPopupDeleteComment(comment.cd)" class="fas fa-trash-alt"></i></span>
                  </div>
                </div>
              </div>
            </div>
          </b-row>
          <b-row :key="`history-log-next-${index}`" class="py-2 next-flow" :index="index" v-if="index < historiesRender.length-1">
            <i class="fas fa-caret-down"></i>
          </b-row>
        </template>
      </b-container>
      <DeleteDialog v-if="showPopupDelete" v-model="showPopupDelete" @delete="deleteComment">
      </DeleteDialog>
    </template>
  </collapse>
</template>
<script>
import MstRequestCommentActions from "@/services/master/MstRequestCommentActions";
import CommentService from "@/services/CommentService";
import isEmpty from 'lodash/isEmpty';
import forEach from 'lodash/forEach';
import includes from 'lodash/includes';
import FormatDate from "@/mixins/FormatDate";

export default {
  name: "FlowHistory",
  mixins: [FormatDate],
  data() {
    return {
      histories: [],
      comments: [],
      commentCd: null,
      indexSelected: null,
      indexChild: 0,
      groupActionsText: "ファイル変更",
      showPopupDelete:false,
      deleteCd:null
    }
  },
  computed: {
    historiesRender() {
      let histories = [];
      let lastAction = undefined;
      let confirmHistoryIndex = -1;
      forEach(this.histories, (value, index) => {
        if(lastAction == this.groupActionsText && value.actionComment == this.groupActionsText) {
          histories[confirmHistoryIndex].actions.push(value);
        } else if(lastAction != this.groupActionsText && value.actionComment == this.groupActionsText) {
          let confirmAction = {
            cd: value.cd,
            actionComment: this.groupActionsText,
            actions: [value]
          }
          histories.push(confirmAction);
          confirmHistoryIndex = histories.length - 1;
        } else if(value.actionComment == "確認") {
          let action = {
            cd: value.cd,
            actionComment: value.actionComment,
            actions: value.historiesOfMstCommentChild
          }
          histories.push(action);
        } else {
          let action = {
            cd: value.cd,
            actionComment: value.actionComment == "承認"
              ? (value.stepNumber < 5 ?`第${value.stepNumber - 1}承認` : "決裁")
              : value.actionComment,
            actions: [value]
          }
          histories.push(action);
        }
        lastAction = value.actionComment;
      });
      return histories;
    },
  },
  watch:{
    showPopupDelete(newValue){
      if(!newValue){
        this.deleteCd = null;
      }
    }
  },
  created() {
    this.getRequestCommentActions()
  },
  methods: {
    showPopupDeleteComment(cd){
      this.deleteCd = cd;
      this.showPopupDelete = true
    },
    getRequestCommentActions() {
      if (this.$route.params.cd) {
        MstRequestCommentActions.getRequestCommentActions(this.$route.params.cd)
          .then(response => {
            this.histories = response.data;
          })
          .catch(err => {
          });
      }
    },
    getBorderColor(historyName) {
      if("差し戻し" == historyName) {
        return "border-red";
      } else if(includes(["担当交代", "代理人追加", "ファイル変更", "支払日入力", "代理人削除"], historyName)) {
        return "border-black";
      } else {
        return "";
      }
    },
    showStatement(cd, index = undefined, indexChild = undefined) {
      this.indexSelected = index;
      this.indexChild = indexChild || 0;
      if (cd == this.commentCd || !cd) {
        this.commentCd = null;
        this.comments = [];
      } else {
        this.commentCd = null;
        this.commentCd = cd
        this.getCommentByOperationHistoryCd(cd);
      }
    },
    getCommentByOperationHistoryCd(cd) {
      var param = {
        pageNumber: null,
        limitNumber: null,
        sortField: null,
        keyword: "operationHistoriesEntity.cd==" + cd
      };
      CommentService.listComment(param).then(response => {
        this.comments = response.data;
      }).catch(err => {
      })
    },
    checkEmpty(list) {
      return isEmpty(list);
    },
    deleteComment(){
      if(this.deleteCd){
        CommentService.deleteComment(this.deleteCd).then((res)=>{
          if(res.data){
              this.commentCd = null;
              this.comments = [];
              this.deleteCd = null;
              this.showPopupDelete = false;
          }
      }).catch(err =>{
          this.deleteCd = null;
          this.showPopupDelete = false;
          throw(err)
      })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.fa-trash-alt{
  cursor: pointer;
}
.histories {
  color: #030303;
  .histories-container {
    border: 1px solid #44A5CC;
    border-radius: 3px;
    display: flex;
    flex-wrap: nowrap;
    padding-bottom: 0.4rem;
    &.border-red {
      border-color: #DF6561;
      .stepName {
        background: #DF6561;
      }
    }
    &.border-black {
      border-color: #707070;
      .stepName {
        background: #707070;
      }
    }
    .stepName {
      background-color: #44A5CC;
      color: white;
      font-size: 1rem;
      text-align: center;
      width: auto;
      min-width: 6.25rem;
      padding: 0 2rem;
      height: 1.7rem;
      border-radius: 3px;
      white-space: nowrap;
    }
    .flow-container {
      margin-left: 1.875rem;
      margin-right: 1.875rem;
      width: 100%;
      .list-log {
        position: relative;
        border-bottom: 1px solid #707070;
      }
      &.group-action {
        width: 100%;
        position: relative;
        display: flex;
        flex-wrap: nowrap;
        & > div {
          flex-grow: 1;
        }
        .dropdown-toggle-item {
          width: 100%;
          & > div {
            display: flex;
            justify-content: flex-start;
            margin-top: 0.5rem;
            align-items: baseline;
          }
          &.show {
            display: flex;
            flex-wrap: wrap;
            align-items: baseline;
            position: relative;
            & > div {
              width: 100%;
              &.list-log {
                flex-grow: 1;
                span.action {
                  display: none;
                }
                span.time {
                  flex-grow: 1;
                }
              }
              span.action {
                flex-grow: 0;
              }
            }
            & ~ .dropdown-toggle-item {
              width: auto;
              width: 100%;
              span.action {
                flex-grow: 1;
              }
            }
          }
        }
        .history-detail {
          background: transparent;
          margin-left: 1rem;
          border-left: 1px solid #707070;
          display: flex;
          flex-wrap: wrap;
          position: static;
          right: 0;
          width: 100%;
          max-width: 45%;
          align-content: baseline;
          span.action {
            display: inline-block !important;
            width: 100%;
            text-align: left;
            & ~ div {
              margin-left: 1rem;
              width: 100%;
              .commentItem1 {
                border-bottom: 1px solid #707070;
                display: flex;
                justify-content: space-between;
              }
            }
          }
          .comment-item {
            span:not(:last-child) {
              padding-right: 0.5rem;
            }
          }
        }
      }
      span {
        font-size: 0.75rem;
        display: inline-block;
        white-space: nowrap;
        &.icon {
          padding-right: 15px;
        }
        &.bumon {
          min-width: 8.25rem;
          padding-right: 2rem;
        }
        &.shain {
          min-width: 3rem;
          padding-right: 2.5rem;
        }
        &.action {
          cursor: pointer;
          flex-grow: 1;
          text-align: right;
        }
        &.item-number {
          color: #44A5CC
        }
        &:empty {
          background: red;
        }
      }
      .comment {
        font-size: 0.75rem;
      }
      .pre {
        white-space: pre-line;
        word-break: break-all;
        font-size: 0.75rem;
        margin: 0;
      }
    }
  }
  .next-flow {
    max-width: 6.25rem;
    display: flex;
    justify-content: center;
  }
  i {
    color: #707070;
    &.fa-caret-down {
      font-size: 1.375rem;
    }
    &.fa-user,
    &.fa-align-justify {
      font-size: 1rem;
    }
  }
}
</style>