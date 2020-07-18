<template>
<transition name="slide" v-if="isVisible">
    <div>
      <b-modal v-model="isVisible" 
        id="timeout-lock-edit-modal" 
        size="lg" 
        top
        hide-header
        hide-footer
        hide-backdrop
        no-close-on-esc
        no-close-on-backdrop
        lazy
        content-class="timeout-lock-edit">
        <b-container fluid>
          <p>
        編集開始から1時間が経過しました。<br/>
        自動的に遷移します。
      </p>
        </b-container>
      </b-modal>
    </div>
  </transition>
</template>

<script>
  export default {
    name: "TimeoutLockEditing",
    props: {
      value: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        isVisible: false
      }
    },
    watch: {
      value(newValue, oldValue) {
        if(newValue != oldValue ) {
          this.isVisible = newValue;
          if(newValue) {
            setTimeout(() => {
              this.isVisible = false;
              this.$router.push("/");
            }, 3000);
          }
        }
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.isVisible = this.value;
      });
    },
    beforeDestroy() {
      if (this.isVisible) {
        this.isVisible = false;
      }
    },
    beforeRouteLeave (to, from, next) {
      setTimeout(() => {
        next();
      }, 600)
    }
  }
</script>

<style lang="scss">
#timeout-lock-edit-modal {
  .timeout-lock-edit {
    position: fixed;
    min-width: 31.25rem;
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