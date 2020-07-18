<template>
  <transition name="slide" v-if="isVisible">
  <div>
    <b-modal v-model="isVisible" 
      id="error-process-modal" 
      size="lg" 
      top
      hide-header
      hide-footer
      hide-backdrop
      no-close-on-esc
      no-close-on-backdrop
      lazy
      content-class="error-process">
      <b-container fluid>
        <slot>
          <p v-html="title"></p>
        </slot>
      </b-container>
    </b-modal>
  </div>
  </transition>
</template>

<script>
  export default {
    name: "ErrorProcessingDialog",
    model: {
      prop: "value",
      event: "change"
    },
    props: {
      title: {
        type: String,
        default: ""
      },
      value: {
        type: Boolean,
        default: false
      },
      timeout: {
        type: Number,
        default: 2000
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
              this.updateModel();
            }, this.timeout)
          } else {
            clearTimeout(this.timeoutShowDialog);
          }
        }
      }
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
    },
    beforeDestroy() {
      if (this.isVisible) {
        this.isVisible = false;
      }
    },
  }
</script>

<style lang="scss">
#error-process-modal {
.error-process {
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
  z-index: 9999;
  margin: 0 auto;
  transform: translateX(-50%);
  .modal-body {
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  /deep/p {
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