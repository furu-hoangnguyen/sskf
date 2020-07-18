<template>
  <transition name="slide" v-if="isVisible"> 
    <div class="leave-page-warning">
      <div class="container">
        <div class="header"></div>
        <div class="body">
          <div><img :src="require(`@/assets/browser-icons/${browserName}.png`)" alt=""/></div>
          <div>
            <p><b>このサイトを離れますか？</b><br/>行った変更が保存されない可能性があります。</p>
          </div>
        </div>
        <div class="action">
          <b-button class="btn-cancel" @click="cancel">キャンセル</b-button>
          <b-button class="btn-ok" variant="primary" @click="leave">このページを離れる</b-button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import browser from "@/helper/detectBrowser"
  export default {
    name: "LeavePageWarning",
    data() {
      return {
        isVisible: false,
        browserName: "warning"
      }
    },
    model: {
      prop: "value",
      event: "change"
    },
    props: {
      value: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      value(newValue, oldValue) {
        if(newValue != oldValue ) {
          this.isVisible = newValue;
        }
      },
      isVisible(newValue) {
        this.updateModel(newValue);
      },
    },
    created() {
      this.browserName = browser();
    },
    mounted() {
      this.$nextTick(() => {
        this.isVisible = this.value;
      });
    },
    methods: {
      updateModel(val) {
        if (val !== this.value) {
          this.$emit('change', val)
        }
      },
      cancel() {
        this.isVisible = false;
        this.$emit("cancel");
        this.updateModel(false);
      },
      leave() {
        this.isVisible = false;
        this.$emit('leave-page');
      }
    },
    beforeDestroy() {
      if (this.isVisible) {
        this.isVisible = false;
      }
    },
  }
</script>

<style lang="scss" scoped>
.leave-page-warning {
  position: fixed;
  width: 100vw;
  height: 100vh;
  min-width: 100vw;
  min-height: 100vh;
  overflow: hidden;
  top: 50%;
  left: 50%;
  background-color: rgba(0, 0, 0, 0.5);;
  border-radius: 0px 0px 15px 15px;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  transform: translate(-50%,-50%);
  margin: 0;
   & > div {
    width: 27rem;
    height: 10rem;
    min-width: 27rem;
    min-height: 10rem;
    max-width: 27rem;
    max-height: 10rem;
    margin: auto;
    background: rgb(33, 37, 41);;
    border-radius: 6px;
    color: #fff;
    margin: 0;
    padding: 0;
    font-size: 0.75rem;
    .header {
      height: 2rem;
      border-bottom: 2px solid #000;
      background: inherit;
      border-radius: 6px 6px 0 0;
    }
    .body {
      padding: 15px;
      display: flex;
      flex-flow: row nowrap;
      & > div {
        &:first-child {
          margin: 0 1rem;
        }
        img {
          width: 3.5rem;
          height: 3.5rem;
          min-width: 3.5rem;
          min-height: 3.5rem;
          max-width: 3.5rem;
          max-height: 3.5rem;
        }
        p {
          margin-bottom: 0;
        }
      }
      
    }
    .action {
      display: flex;
      flex-wrap: nowrap;
      justify-content: flex-end;
      button {
        font-size: 0.75rem;
        margin: 0 1rem;
        padding: 0.2rem 1.5rem;
        border-radius: 4px;
      }
    }
    
   }
}
.slide-leave-to, .slide-enter {
  transition-duration: 0s;
}

.slide-leave, .slide-enter-to {
  transition-duration: 0s;
}
</style>