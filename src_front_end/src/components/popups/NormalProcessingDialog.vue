<template>
  <transition name="slide" v-if="isVisible">
  <div>
    <b-modal v-model="isVisible" 
      id="normal-process-modal" 
      size="lg" 
      top
      hide-header
      hide-footer
      hide-backdrop
      no-close-on-esc
      no-close-on-backdrop
      lazy
      content-class="normalProcess">
      <b-container fluid> <p v-html="title"></p> </b-container>
    </b-modal>
  </div>
  </transition>
</template>

<script>
  export default {
    name: "NormalProcessingDialog",
    data() {
      return {
        isVisible: false,
        timeoutShowDialog: null
      }
    },
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
            }, 2000)
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
#normal-process-modal {
  .normalProcess {
    position: fixed;
    min-width: 31.25rem;
    width: 30%;
    height: 250px;
    top: 0;
    left:50%;
    background-color: #707070;
    border-radius: 0px 0px 15px 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
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