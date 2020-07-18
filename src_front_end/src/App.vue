<template>
  <section v-if="!isHideLayout" :class="['section-app', {show: lazyLoaded}, {hidden:isHideLayout}]">
    <transition name="fade"> 
      <b-overlay :show="lockScreen" rounded="lg" opacity="1">
        <template v-slot:overlay>
          <div class="d-flex align-items-center">
            <i class="fas fa-sync fa-spin"></i>
          </div>
        </template>
        <MainLayout></MainLayout>
      </b-overlay>
    </transition>
  </section>
  <router-view v-else></router-view>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "App",
  data() {
    return {
      lazyLoaded: false
    }
  },
  computed:{
    ...mapState('AuthenticationModule',['lockScreen']),
    ...mapGetters({
      isLogged: "AuthenticationModule/isLogged"
    }),
    isHideLayout() {
      return this.$route.meta.showLayout == false;
    }
  },
  methods: {
    ...mapActions({
      countRequestList: "CountRequestModule/countRequestList"
    }),
  },
  created() {
    
  },
  mounted() {
    setTimeout(() => {
      this.lazyLoaded = true;
    }, 300)
  }
};
</script>

<style lang="scss" scoped>
  .fa-spin{
    font-size: 3rem;
  }
</style>
<style>
.section-app {
display: none;
}
.section-app.show {
  display: block;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 1s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
</style>
