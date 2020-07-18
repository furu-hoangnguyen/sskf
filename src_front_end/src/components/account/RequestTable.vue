<template>
  <b-row class="px-4 py-1 mt-3 account_receivables_condition">
    <b-col md="10" class="pl-0 account_receivables_condition_table">
      <b-table-simple responsive>
        <b-thead>
          <b-tr>
            <b-th v-for="(item, index) in fields" :key="index">{{ item }}</b-th>
          </b-tr>
        </b-thead>
        <b-tbody>
          <b-tr v-for="(_item, _index) in list" :key="_index">
            <b-td v-for="(item, index) in _item" :key="index" align="center">
              <b-input
                v-if="item.type !== 'button' && item.type !== 'select'"
                :type="item.type"
                v-model="item.value"
                :plaintext="item.plaintext"
                :readonly="item.readonly"
                :class="item.class"
              ></b-input>
              <button
                v-if="item.type == 'button'"
                :class="item.class"
                :disabled="item.disabled"
                @click="item['function'](_index, index)"
              >
                <i
                  v-if="item.icon"
                  :class="[
                    item.icon,
                    {
                      color_red: item.hascomment == 0,
                      color_blue: item.hascomment == 1,
                      color_grey: item.hascomment == 2,
                    },
                  ]"
                ></i>
                {{ item.name }}
              </button>
              <v-select
                v-if="item.type == 'select'"
                v-model="item.value"
                label="text"
                :reduce="text => text.value"
                :options="item.options"
                :class="item.class"
                class="main-vselect account-create"
              ></v-select>
            </b-td>
          </b-tr>
        </b-tbody>
      </b-table-simple>
    </b-col>
    <b-col md="2" class="pl-0 account_receivables_condition_table account_receivables_condition_action">
      <b-table-simple responsive>
        <b-thead>
          <b-tr>
            <b-th> </b-th>
          </b-tr>
        </b-thead>
        <b-tbody>
          <b-tr v-for="index in list.length" :key="index" >
            <b-td align="center">
              <i class="fas fa-plus-circle" @click="$emit('addItem', index)"></i>
              <i class="fas fa-copy"></i>
              <i class="fas fa-minus-circle" @click="$emit('removeItem',index-1)"></i>
            </b-td>
          </b-tr>
        </b-tbody>
      </b-table-simple>
    </b-col>
  </b-row>
</template>

<script>
export default {
  name: "RequestTable",
  data() {
    return {
      
    };
  },
  props: {
    fields: {
      type: Array,
      required: true
    },
    list:{
      type: Array
    }
  },
  methods: {
    
  },
  created() {
    if (!this.list.length) {
      this.$emit('addItem', 0);
    }
  }
};
</script>

<style lang="scss" scoped>

</style>
