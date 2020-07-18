<template>
  <validation-provider
          v-slot="{invalid, validated}"
          :rules="rules"
          :custom-messages="messages">
    <b-form-group :label="name" class="label_input" :class="{ invalidClass: invalid && validated , date_disabled: disabledField}" :label-for="id">
      <date-picker
              :id="id"
              @input="onInput"
              :value="value"
              :class="['main-vselect',fieldClass]"
              :value-type="valueType"
              :disabled="disabledField"
              :format="formatDate"
              :default-value="defaultDate"
              :disabled-date="disabledDate"
              :lang="lang"></date-picker>
    </b-form-group>

  </validation-provider>
</template>

<script>
  export default {
    name: "DateSskf",
    data(){
      return{
        lang: {
          formatLocale: {
            months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            // MMM
            monthsShort:['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            // dddd
            weekdays: ['日曜日', '月曜日', '火曜日', '水曜日', '木曜日', '金曜日', '土曜日'],
            // ddd
            weekdaysShort: ['日', '月', '火', '水', '木', '金', '土'],
            // dd
            weekdaysMin: ['日', '月', '火', '水', '木', '金', '土'],
            // first day of week
            firstDayOfWeek: 0,
            // first week contains January 1st.
            firstWeekContainsDate: 1,
            // format 'a', 'A'
          },
          monthBeforeYear: false,
          yearFormat: 'YYYY 年',
        },
      }
    },
    props: {
      id:{
        type: String,
        default:''
      },
      name:{
        type: String,
        default: ' ',
      },
      value: {
        type: [String, Number, Date],
        default: undefined
      },
      valueType: {
        type: String,
        default: 'YYYY-MM-DD'
      },
      formatDate: {
        type: String,
        default: 'YYYY-MM-DD'
      },
      disabledField: {
        type: Boolean,
        default: false
      },
      rules: {
        type: Object,
        default: undefined
      },
      fieldClass: {
        type: [Object, String],
        default: ''
      },
      changed:{
        type: Boolean,
        default: false
      },
      messages: {
        type: Object,
        default: undefined
      },
      defaultDate:{
        type:[String, Number, Date],
        default: ()=>{return new Date()}
      },
      disabledDate:{
        type:[String,Function, Boolean],
        default: ()=>{}
      }
    },
    methods: {
      onInput(event) {
        this.$emit("input", event);
      },
    }
  };
</script>

