<template>
  <b-modal id="old-comments-modal" v-model="isShowing" size="xl" centered hide-header hide-footer>
    <b-container fluid class="bv-example-row history-comment" id="container-record" >
      <div class="m-3">
        <div class="popup-title">コメント候補</div>
        <div class="container-record customScrollBar">
          <b-row class="search_record mt-1 no-gutters">
            <b-col cols="12" v-for="(comment,index) in historyComments" 
              :key="index"
              :id="`comment-item-container-${index}`"
              class="comment-item no-gutters">
              <label
              :id="`comment-item-${index}`"
              :ref="`comment-item-${index}`"
              class="comment-history-item"
              @mouseover="mouseover($event, `comment-item-${index}`)"
              @mouseleave="mouseleave(`comment-item-${index}`)"
              @dbclick.stop="$emit('onSelectedComment', comment[0])">
                {{comment[0]}}
              </label>
              <b-tooltip
                :target="`comment-item-${index}`"
                :title="comment[0]"
                triggers="click"
                placement="top"
                custom-class="comment-tooltip customScrollBar"
                container="container-record"></b-tooltip>
            </b-col>
          </b-row>
        </div>
        <b-row class="btn-close no-gutters" align-h="end">
            <b-button variant="none" @click.stop="isShowing = false" class="btn-white">戻る</b-button>
        </b-row>
      </div>
    </b-container>
  </b-modal>
</template>
<script>
import {mapState} from "vuex";
import CommentService from "@/services/CommentService";
export default {
  name: "CommentHistoryDialog",
  data() {
    return {
      isShowing: this.value,
      historyComments: []
    };
  },
  props: {
    value: {
      type: Boolean
    },
    storeGCd: {
      type: [Number, String],
      default: undefined
    }
  },
  methods: {
    getOldComments () {
      if(this.storeGCd) {
        CommentService.getOldComments(this.storeGCd).then(res => {
            this.historyComments = res.data;
          }).catch(err => {
            console.log(err);
          });
      }
    },
    mouseover(event, target){
      if(event) {
        event.preventDefault();
      }
      if(this.isShowTooltip(target)) {
        this.$root.$emit(' bv::enable::tooltip');
        this.$root.$emit('bv::show::tooltip', target);
      } else {
        this.mouseleave(target);
      }
    },
    mouseleave(target){
      this.$root.$emit('bv::hide::tooltip', target);
      this.$root.$emit('bv::disable::tooltip');
    },
    isShowTooltip(target) {
      let element = this.$refs[target];
      if(element) {
        element = Array.isArray(element) ? element : [element];
        let isScroll = false;
        isScroll = element.some(ele => {
          return ele.clientWidth < ele.scrollWidth;
        });
        return isScroll;
      }
      return false;
    }
  },
  computed:{
    ...mapState({
      loginUser: state => state.AuthenticationModule.loginUser
    }),
  },
  watch: {
    isShowing () {
      this.$emit("input", this.isShowing);
    }
  },
  created() {
    this.getOldComments();
  }
};
</script>
<style lang="scss">
@import "@/assets/scss/pages/_modal-comment-histories.scss";
</style>
