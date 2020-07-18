<template>
 <div>
    <InputNumbericSskf separator="," name="請求金額(円)" id="billing_t"
                                  
                                    v-model="billingAmount"/>
 </div>
</template>

<script>
  export default {
    name: "Approve",
    data() {
      return {
        billingAmount:undefined,
        testNumberric: undefined,
        options:[{"bumonCd":"1201","bumonCdNk":"1201","bumonNm":"東京支店  業務用営業課","bumonKnm":"ﾄｳｷｮｳｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"業務用東京"},{"bumonCd":"1202","bumonCdNk":"1202","bumonNm":"東京支店  家庭用営業課","bumonKnm":"ﾄｳｷｮｳｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭用東京"},{"bumonCd":"1203","bumonCdNk":"1203","bumonNm":"東京支店  北関東営業所","bumonKnm":"ｷﾀｶﾝﾄｳｴｲｷﾞｮｼｮ","bumonRnm":"北関東"},{"bumonCd":"1301","bumonCdNk":"1301","bumonNm":"静岡支店  業務用営業課","bumonKnm":"ｼｽﾞｵｶｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ　","bumonRnm":"業務用静岡"},{"bumonCd":"1302","bumonCdNk":"1302","bumonNm":"静岡支店  家庭用営業課","bumonKnm":"ｼｽﾞｵｶｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭用静岡"},{"bumonCd":"1401","bumonCdNk":"1401","bumonNm":"名古屋支店  業務用営業課","bumonKnm":"ﾅｺﾞﾔｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"業務名古屋"},{"bumonCd":"1402","bumonCdNk":"1402","bumonNm":"名古屋支店　家庭用営業課","bumonKnm":"ﾅｺﾞﾔｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭名古屋"},{"bumonCd":"1501","bumonCdNk":"1501","bumonNm":"大阪支店  業務用営業課","bumonKnm":"ｵｵｻｶｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"業務用大阪"},{"bumonCd":"1502","bumonCdNk":"1502","bumonNm":"大阪支店　家庭用営業課","bumonKnm":"ｵｵｻｶｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭用大阪"},{"bumonCd":"1601","bumonCdNk":"1601","bumonNm":"福岡支店  業務用営業課","bumonKnm":"ﾌｸｵｶｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"業務用福岡"},{"bumonCd":"1602","bumonCdNk":"1602","bumonNm":"福岡支店　家庭用営業課","bumonKnm":"ﾌｸｵｶｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭用福岡"},{"bumonCd":"1701","bumonCdNk":"1701","bumonNm":"那覇支店　業務用営業課","bumonKnm":"ﾅﾊｼﾃﾝ ｷﾞｮｳﾑﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"業務用那覇"},{"bumonCd":"1702","bumonCdNk":"1702","bumonNm":"那覇支店　家庭用営業課","bumonKnm":"ﾅﾊｼﾃﾝ ｶﾃｲﾖｳｴｲｷﾞｮｳｶ","bumonRnm":"家庭用那覇"}]
      }
    },
  }
</script>

<style scoped>

</style>