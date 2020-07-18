<template>
  <b-container fluid class="login-page">
    <b-row class="title_login">ログイン画面</b-row>
    <b-row class="main-login">
      <b-col md="12" lg="12">
        <label v-if="loginData.notifyError" class="label_error">
          {{ loginData.notifyError }}
        </label>
        <b-form-group label="ID" label-for="shainCd">
          <b-form-input id="shainCd" type="text" placeholder="ID" v-model="loginData.username"></b-form-input>
        </b-form-group>
      </b-col>
      <b-col md="12" lg="12">
        <b-form-group label="Password" label-for="password">
          <b-form-input id="password" type="password" placeholder="Password" v-model="loginData.password" @keydown.enter="loginAuthentication"></b-form-input>
        </b-form-group>
      </b-col>
      <b-col md="12" lg="12">
        <b-form-checkbox v-model="loginData.rememberMe" class="form-group check_size">ログイン情報を保存する</b-form-checkbox>
      </b-col>
      <b-col md="12" lg="12"><b-button @click.stop="loginAuthentication" class="btn_submit_login">ログイン</b-button></b-col>
      <img src="@/assets/logo.png" class="logo_login">
    </b-row>
  </b-container>
</template>

<script>
  import { mapState, mapActions} from 'vuex';
  export default {
    name: "Index",
    data() {
      return {
      };
    },
    computed:{
      ...mapState('AuthenticationModule',{loginData:'data'})
    },
    methods:{
      ...mapActions('AuthenticationModule',{loginAuthentication:'authentication'}),
    },
  }
</script>
<style lang="scss" scoped>
@import "@/assets/scss/pages/_login.scss";
</style>