<template>
  <validation-provider
          v-slot="{invalid,validated}"
          :rules="rules"
          :mode="custom"
          :custom-messages="messages"
  >
    <b-form-group :label="name" class="label_input" :label-for="id" :class="{ invalidClassSelect: invalid && validated , disabledClass: disabledField}">
      <v-select
              ref="vueSelect"
              v-model="dropdownValue"
              label="text"
              :reduce="(text) => text.value"
              :options="optionsVueSelect"
              :class="['main-vselect',fieldClass]"
              :disabled="disabledField"
              :filterable="filterable"
              @change="onChange"
              @open="$emit('onOpen')"
              @close="$emit('onClose')"
              @search="(value)=>{$emit('search',value)}"
      >
        <template #no-options="{}">
          検索結果はありません
        </template>
      </v-select>
      <slot/>
    </b-form-group>
  </validation-provider>
</template>

<script>
  import convertOptionsVueSelect from '@/helper/convertOptionsVueSelect';
  export default {
    name: "SelectSskf",
    props: {
      name:{
        type: String,
        default:''
      },
      id:{
        type: String,
        default:''
      },
      value: {
        type: [String, Number, Date],
        default: undefined
      },
      options: {
        type: Array,
        default: null
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
      fieldClass:{
        type:[Object,String],
        default:''
      },
      changed:{
        type: Boolean,
        default: false
      },
      optionsLabel:{
        type: String,
        default:''
      },
      optionsValue:{
        type:String
      },
      isNormalSelect:{
        type:Boolean,
        default: true
      },
      clearable: {
        type: Boolean,
        default: false
      },
      messages: {
        type: Object,
        default: undefined
      },
      filterable:{
        type:Boolean,
        default:false
      }
    },
    data() {
      return {
        hasFocus: false
      };
    },
    computed:{
      optionsVueSelect(){
        if(this.isNormalSelect){
          return convertOptionsVueSelect.convertOptionsArray(this.options)
        }else {
          return convertOptionsVueSelect.convertOptions(this.options, this.optionsLabel, this.optionsValue)
        }
      },
      dropdownValue: {
        get() {
          return this.value;
        },
        set(newValue) {
          this.$emit('input', newValue);
          this.$emit('change', newValue);
        }
      }
    },
    methods: {
      onChange(event){
        if(event){
          this.hasChanged = true
        }
      },
      custom() {
        return { on: ['search:blur', 'input'] };
      },
      onBlur(event) {
        this.$emit("search",event)
      }
    },
    mounted() {
      // if (this.$refs.vueSelect.value === undefined) {
      //   this.$refs.vueSelect.clearSelection();
      // }
    }
  };
</script>
<style lang="scss">
  
</style>
