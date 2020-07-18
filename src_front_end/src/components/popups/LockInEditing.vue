<template>
  <transition name="slide" v-if="isVisible">
    <div>
      <b-modal v-model="isVisible" 
        id="lock-editing-modal" 
        size="lg" 
        top
        hide-header
        hide-footer
        hide-backdrop
        no-close-on-esc
        no-close-on-backdrop
        lazy
        content-class="lock-editing">
        <b-container fluid>
          <p>{{ lockUser }}さんが編集しています。<br/>
        自動ロック解除まで、残り{{ lockStartTime | formatMinutes}}分です。</p>
        </b-container>
      </b-modal>
    </div>
  </transition>
</template>

<script>
  export default {
    name: "LockEditingDialog",
    model: {
      prop: "value",
      event: "change"
    },
    props: {
      lockUser: {
        type: String,
        default: ""
      },
      lockStartTime: {
        type: [Number, String],
        default: undefined
      },
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        isVisible: false,
        timeoutShowDialog: null
      }
    },
    watch: {
      value(newValue, oldValue) {
        if(newValue != oldValue ) {
          this.isVisible = newValue;
          if(newValue) {
            this.timeoutShowDialog = setTimeout(() => {
              this.isVisible = false;
              this.updateModel(false);
            }, 2000)
          } else {
            clearTimeout(this.timeoutShowDialog);
          }
        }
      }
    },
    filters: {
      formatMinutes(miliseconds) {
        return Math.ceil(miliseconds/(1000*60));
      }
    },
    mounted() {
      this.$nextTick(() => {
        if (this.lockStartTime > 0) {
          this.isVisible = this.value;
        } else {
          this.updateModel(false);
        }
      });
    },
    methods: {
      updateModel(val) {
        if (val !== this.value) {
          this.$emit('change', val)
        }
      },
    },
    beforeDestroy() {
      if (this.isVisible) {
        this.isVisible = false;
      }
    },
  }
</script>

<style lang="scss">
#lock-editing-modal {
  .lock-editing {
    position: fixed;
    width: 30%;
    min-width: 31.25rem;
    height: 250px;
    top: 0;
    left: 50%;
    background-color: #f47272;
    border-radius: 0px 0px 15px 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    transform: translateX(-50%);
    .modal-body {
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    p {
      margin-left: 10%;
      text-align: left;
      letter-spacing: 0px;
      line-height: 1.5rem;
      font-size: 1rem;
      font-weight: 700;
      color: #FFFFFF;
      opacity: 1;
    }
  }
}
.slide-leave-to, .slide-enter {
  max-height: 0px;
  transition-duration: 0.6s;
}

.slide-leave, .slide-enter-to {
  max-height: 250px;
  transition-duration: 0.6s;
}
</style>