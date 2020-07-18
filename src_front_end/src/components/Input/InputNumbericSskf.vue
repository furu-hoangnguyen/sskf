<template>
  <validation-provider
          v-slot="{invalid,validated}"
          :rules="rules"
          :custom-messages="messages">
    <b-form-group :label="name" :label-for="id" class="label_input" :class="[classField,{ invalidClass: invalid && validated}]">
      <v-numeric v-if="!emptyValue || hasFocus"
                 ref="inputNumber"
                 :separator="separatorStr"
                 :id="id"
                 class="form-control"
                 :disabled="disabledField"
                 :value="value"
                 :placeholder="placeholder"
                 :min="min"
                 :max="max"
                 :focus="hasFocus"
                 :step="step"
                 :precision="precision"
                 autocomplete="off"
                 @input="onInput"
                 @focus="hasFocus=true"
                 @blur="hasFocus=false"
      ></v-numeric>
      <input v-else type="text" class="form-control pr-3" :value="emptyValue" @focus="onFocusInputTemp"/>
    </b-form-group>
  </validation-provider>
</template>

<script>
  export default {
    name: "InputNumbericSskf",
    props: {
      classField:{
        type:[Array,Object,String]
      },
      id:{
        type: String,
        default:''
      },
      value: {
        type: [String, Number],
        default: undefined
      },
      type: {
        type: String,
        default: "number"
      },
      disabledField: {
        type: Boolean,
        default: false
      },
      placeholder: {
        type: String,
        default: undefined
      },
      rules: {
        type: Object,
        default: undefined
      },
      name:{
        type: String,
        default:''
      },
      separatorStr:{
        type:String,
        default: ","
      },
      min:{
        type:Number,
        default: 0
      },
      max:{
        type:Number,
        default: Number.MAX_SAFE_INTEGER
      },
      messages: {
        type: Object,
        default: undefined
      },
      emptyValue: {
        type: [String, Number],
        default: undefined
      },
      step: {
        type: Number,
        default: 1
      },
      precision: {
        type: Number,
        default: 0
      },
    },
    data() {
      return {
        hasFocus: false
      };
    },
    methods: {
      onInput(event) {
        this.$emit("input", event);
      },
      onFocusInputTemp(event) {
        this.hasFocus=true;
        setTimeout(() => {
          this.$refs.inputNumber.$el.focus();
        }, 100);
      }
    }
  };
</script>
