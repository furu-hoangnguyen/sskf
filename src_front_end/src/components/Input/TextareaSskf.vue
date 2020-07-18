<template>
  <validation-provider
          v-slot="{invalid,validated}"
          :rules="rules"
          :custom-messages="messages"
  >

    <b-form-group :label="name" class="label_input label_textarea pr-0" :class="{ 'invalidTextarea': invalid && validated}"
                  :label-for="id">
      <b-textarea
              :id="id"
              :disabled="disabledField"
              :value="value"
              :type="type"
              :placeholder="placeholder"
              :rows="row"
              :maxlength="maxlength"
              @input="onInput"
      ></b-textarea>
      <slot/>
    </b-form-group>
  </validation-provider>
</template>

<script>
  export default {
    name: "TextareaSskf",
    props: {
      id: {
        type: String,
        default: ''
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
      placeholder: {
        type: String,
        default: undefined
      },
      rules: {
        type: Object,
        default: undefined
      },
      name: {
        type: String,
        default: ''
      },
      maxlength: {
        type: Number,
        default: null
      },
      changed: {
        type: Boolean,
        default: false
      },
      messages: {
        type: Object,
        default: undefined
      },
      row: {
        type: Number
      }
    },
    methods: {
      onInput(event) {
        this.$emit("input", event);
      },

    }
  };
</script>
