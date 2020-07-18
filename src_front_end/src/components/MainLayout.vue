<template>
  <b-container fluid :class="['bv-example-row p-0 main', {'main-tablet': isTabletDevice}]">
    <b-row no-gutters class="main-header" align-h="between" align-v="stretch" >
      <template v-if="isTabletDevice">
        <b-row cols="auto" align-v="center" class="header-title">
          <template v-if="!isLogged">
            <p class="my-0 login">
              未収金処理システム
            </p>
          </template>
          <template v-else class="headerTablet">
            <div @click="activeNum = 1" :class="['home_tablet',{active_tablet: activeNum == 1}]">
              <router-link to="/" tag="p" class="my-0">
                <div class="d-flex flex-row">
                  <i class="fas fa-home align-self-end"></i>
                  <div class="h-100">未収金<br>処理システム</div>
                </div>
              </router-link>
            </div>
            <div @click="activeNum = 7">
              <router-link
                to="/request/approve"
                tag="div" 
                cols="auto"
                class="main-nav_function tabletNav"
                active-class="active_tablet"
              >
              承認する <span class="main-nav_count">{{ countRequest.waitingApprove }}</span>
            </router-link >
            </div>
          </template>
        </b-row>
      </template>
      <template v-else>
        <b-row cols="auto" class="header-title">
          <b-col cols="auto" @click="activeNum = 1">
            <router-link to="/" tag="p" class="my-0">
              未収金<br/>
              処理システム
            </router-link>
          </b-col>
        </b-row>
      </template>
      <b-col cols="auto" align-v="center" :class="['header-menu dropdownUser',{'header_tablet': isTabletDevice}]" v-if="isLogged">
        <b-dropdown no-caret variant="none">
          <template v-slot:button-content>
            <i class="fas fa-user iconUser"></i>
            <span class="mx-2">{{ loginUser.shainNm }}</span>
            <i class="fas fa-angle-down"></i>
          </template>
          <b-dropdown-item>
            <router-link to="/user/edit" tag="p" class="my-0">
              ユーザ情報を見る
            </router-link>
          </b-dropdown-item>
          <b-dropdown-item v-if="!isTabletDevice">
            <router-link to="/master/menu" tag="p" class="my-0">
              マスタメニューを見る
            </router-link>
          </b-dropdown-item>
          <b-dropdown-item href="javascript:void(0)" @click="logout()">ログアウトする</b-dropdown-item>
        </b-dropdown>
      </b-col>
    </b-row>
    <div class="main-body customScrollBar">
      <div v-if="isLogged && !isTabletDevice" class="main-nav" :class="{ collapseNav: hascollpase }">
        <div class="main-nav-container customScrollBar">
          <router-link 
            to="/" tag="div"
            class="homeNav p-0 text-center"
            :class="{activeNav : $route.path == '/' }">
            <div><i class="fas fa-home"></i></div>
            <p class="my-0">ホーム</p>
          </router-link>
          <div class="request" :class="{activeNav : getParentRoutePath() == '/request' }">
            <i class="fas fa-money-bill-wave"></i>請求
          </div>
          <router-link to="/request/list" tag="div" active-class="active_function" class="main-nav_function">
            申請一覧
          </router-link>
          <router-link to="/request/create" tag="div" active-class="active_function" class="main-nav_function">
            作成する<span class="main-nav_count">{{ countRequest.create }}</span>
          </router-link>
          <router-link to="/request/confirm" tag="div" active-class="active_function" class="main-nav_function">
            確認する <span class="main-nav_count">{{ countRequest.waitingConfirm }}</span>
          </router-link>
          <router-link to="/request/apply" tag="div" active-class="active_function" class="main-nav_function">
            申請する <span class="main-nav_count">{{ countRequest.waitingApply }}</span>
          </router-link>
          <router-link to="/request/approve" tag="div" active-class="active_function" class="main-nav_function">
            承認する <span class="main-nav_count">{{ countRequest.waitingApprove }}</span>
          </router-link>
          <router-link v-if="isUserAccountingCharge()" to="/request/confirm-settlement" tag="div" active-class="active_function" class="main-nav_function accounting-charge">
            決済確認する<span class="main-nav_count">{{ countRequest.waitingConfirmSettlement }}</span>
          </router-link>
        </div>
        <div class="collapse-menu">
          <button type="button" class="collapsebtn" @click.stop="hascollpase = !hascollpase">
            <i :class="['fas', !hascollpase ? 'fa-arrow-left' : 'fa-arrow-right']"></i>
          </button>
        </div>
      </div>
      <div :class="['main-content', {'not-logged': !isLogged}]">
        <router-view></router-view>
        <!-- <div class="collapse-menu">
          <button type="button" class="collapsebtn" @click.stop="hascollpase = !hascollpase">
            <i :class="['fas', !hascollpase ? 'fa-arrow-left' : 'fa-arrow-right']"></i>
          </button>
        </div> -->
      </div>
    </div>
  </b-container>
</template>

<script>
  import isTablet from "@/helper/isTabletDevice";
  import {mapState, mapGetters, mapActions} from "vuex";
  import TokenService from "@/services/TokenService";

  export default {
    name: "LayoutSskf",
    data() {
      return {
        hascollpase: false,
        activeNum: 1,
      };
    },
    methods: {
      ...mapActions('AuthenticationModule', ['logout', 'setLoginUser']),
      ...mapActions({
        countRequestList: "CountRequestModule/countRequestList"
      }),
      getParentRoutePath(){
        if(!this.$route.matched[0]){
          return ''
        }
        return this.$route.matched[0].path
      },
      isUserAccountingCharge() {
        return TokenService.isUserAccountingCharge();
      }
    },
    computed: {
      isTabletDevice() {
        return isTablet();
      },
      ...mapState({
        countRequest: state => state.CountRequestModule.countRequest,
        loginUser: state => state.AuthenticationModule.loginUser
      }),
      ...mapGetters({
        isLoggedGetter: "AuthenticationModule/isLogged"
      }),
      isLogged(){
        return this.$route.name !== 'login' && this.isLoggedGetter;
      },
    },
    created() {

    },
    updated() {
      if(this.isLogged) {
        this.countRequestList();
      }
      if(!this.loginUser && TokenService.getUserLogin()) {
        this.setLoginUser(TokenService.getUserLogin());
      }
    },
  };
</script>

<style lang="scss" scoped>
  // .main {
  //   min-width: 100vw !important;
  //   min-height: 100vh !important;
  //   overflow: auto;
  // }
</style>