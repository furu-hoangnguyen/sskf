<template>
  <validation-provider
          v-slot="{invalid,validated}"
          :rules="rules"
          :custom-messages="messages"
  >
    <b-form-group :label="name" class="label_input" :class="{ invalidClass: invalid && validated}" :label-for="id">
      <b-form-input
              :id="id"
              :disabled="disabledField"
              :maxlength="maxlength"
              :value="value"
              :type="type"
              :placeholder="placeholder"
              :plaintext="plaintext"
              @input="onInput"
      ></b-form-input>
      <slot/>
    </b-form-group>
  </validation-provider>
</template>

<script>
  export default {
    name: "InputSskf",
    data(){
      return{
        hasChanged: false
      }
    },
    props: {
      id:{
        type: String,
        default:''
      },
      value: {
        type: [String, Number, Date],
        default: undefined
      },
      type: {
        type: String,
        default: "text"
      },
      disabledField: {
        type: Boolean,
        default: false
      },
      maxlength:{
        type:Number,
        default: null
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
      changed:{
        type: Boolean,
        default: false
      },
      messages: {
        type: Object,
        default: undefined
      },
      plaintext: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      onInput(event) {
        this.$emit("input", event);
      },
    }
  };
</script>

