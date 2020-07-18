<template>
  <div>
    <b-container fluid class="no-gutters table-container">
      <div :class="[{'table-edit': !disabled && !isLocked && isCanShowAction}, {'has-data' : data && data.length > 0}]">
        <table :class="['table b-table customScrollBar']" :style="`min-height:${minHeight}`" role="table" ref="maintable">
          <thead>
            <tr>
              <th v-for="(col, i) in columns" 
                :key="i"
                v-show="isCanShowByStatusPageDetail(col)"
                :class="{'btn-icon comment': col.isComment, 'btn-icon confirm': col.isConfirm || col.isConfirmUser, 'btn-icon check-diff': col.isCheckDiffPrice}" 
                align="center"
              >
                <span v-html="col.label"></span>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(datum, _index) in data" 
              :key="datum.itemNumber"
              :id="datum.itemNumber"
              v-show="!datum.isDeleted">
              <td v-for="(column, j) in columns" 
                :key="j"
                v-show="isCanShowByStatusPageDetail(column)"
                align="center"
                :id="`${datum.itemNumber}-${keyList}-${column.field}-${j}-${_index}-${datum.cd}`"
                :class="{
                  'select-box' : column.isSelect,
                  'btn-icon comment': column.isComment,
                  'btn-icon confirm': column.isConfirm || column.isConfirmUser,
                  'btn-icon check-diff': column.isCheckDiffPrice
                }"
                :data-field="column.field"
                v-b-tooltip.hover.right.v-secondary.disabled="{title: datum[column.field], disabled: true}"
                @mouseover="mouseover($event,`${datum.itemNumber}-${keyList}-${column.field}-${j}-${_index}-${datum.cd}`, datum.itemNumber, column.field, column.isSelect)"
                @mouseleave="mouseleave(`${datum.itemNumber}-${keyList}-${column.field}-${j}-${_index}-${datum.cd}`)"
              >
                <template v-if="column.getData">
                  {{ column.getData(datum)}}
                </template>
                <template v-else-if="column.isInput">
                    <validation-provider
                    v-slot="{ invalid, validated}"
                    tag="div"
                    :rules="isFunction(column.isValidate) && column.isValidate(datum, column.field, keyList) ? column.rules : null" 
                    :custom-messages="column.messages">
                    <template v-if="column.type == 'text'">
                      <b-form-input
                        :class="[
                          isFunction(column.class) && column.class(datum, column.field, keyList, _index), 
                          {invalidClassTable: invalid && validated}]"
                        type="text"
                        :name="column.field"
                        v-model="datum[column.field]"
                        :focus="column.field == 'bumonNm'"
                        :ref="`ellipsis_${column.field}_${datum.itemNumber}`"
                        :plaintext="column.isReadOnly"
                        :minlength="column.min"
                        :maxlength="column.max"
                        :disabled="isFunction(column.disabled) && column.disabled(datum, column.field, keyList)"
                        @input="column.onInput && column.onInput(datum, _index, keyList)"
                      ></b-form-input>
                    </template>
                    <template v-else-if="column.type == 'numeric'">
                      <v-numeric
                        :class="['form-control',
                          isFunction(column.class) && column.class(datum, column.field, keyList, _index), 
                          {'change-value' : column.initialField && (datum[column.initialField] != datum[column.field])},
                          {invalidClassTable: invalid && validated}
                        ]"
                        v-model="datum[column.field]"
                        :ref="`ellipsis_${column.field}_${datum.itemNumber}`"
                        :separator="column.separator ? column.separator : ','"
                        :min="column.min"
                        :max="column.max"
                        :step="column.step ? column.step : 1"
                        :precision="column.step == 0.01 ? 2 : 0"
                        :minus="column.min < 0"
                        :disabled="isFunction(column.disabled) && column.disabled(datum, column.field, keyList)"
                        output-type="Number"
                        @input="column.onInput && column.onInput(datum, _index, keyList, column.field, column.initialField, column.step == 0.01 ? 2 : 0)"
                    ></v-numeric>
                    </template>
                  </validation-provider>
                </template>
                <template v-else-if="column.isSelect">
                  <validation-provider
                    v-slot="{ invalid, validated}"
                    tag="div"
                    :rules="isFunction(column.isValidate) && column.isValidate(datum, column.field, keyList) ? column.rules : null" 
                    :custom-messages="column.messages"
                  >
                    <v-select
                      :class="['main-vselect', isFunction(column.class) && column.class(datum, column.field, keyList,_index), {invalidClassTable: invalid && validated}]"
                      v-model="datum[column.field]"
                      :ref="`ellipsis_${column.field}_${datum.itemNumber}`"
                      :options="isFunction(column.options) ? column.options(_index, column.field, keyList) : column.options"
                      :reduce="column.reduce"
                      :filterable="false"
                      :disabled="isFunction(column.disabled) && column.disabled(datum, column.field, keyList)"
                      :clearable="true"
                      label="text"
                      @open="openSelectBox(null, null, _index, column.field, keyList, column.onOpen)"
                      @input="isFunction(column.onInput) && column.onInput(datum, _index, column.field, keyList, $event)"
                      @search="(search, loading) => { column.isOnSearch && column.onSearch(search, loading, _index, column.field, keyList)}"
                      @close="onCloseSelect"
                    >
                      <template #no-options="{}">
                        検索結果はありません
                      </template>
                      <!-- <template #search="{ attributes, events }">
                        <input
                          maxlength="1"
                          class="vs__search"
                          v-bind="attributes"
                          v-on="events"
                          :value="datum[column.field]"
                        >
                      </template> -->
                    </v-select>
                  </validation-provider>
                </template>
                <template v-else-if="column.isComment">
                  <button :class="['btn-icon', column.class]" @click.stop="column.onClick(datum, _index, keyList)">
                    <i
                      :class="[
                        'fas', 
                        (datum.hasComment || (datum.commentDetailsRequests && datum.commentDetailsRequests.length > 0)) ? 'fa-comment-dots':'fa-comment-medical',
                        column.status && column.status(datum) ? 'color_grey': 'color_red'
                      ]"
                    ></i>
                  </button>
                </template>
                <template v-else-if="column.isConfirm">
                  <button :class="['btn-icon', column.class]" @click="column.onClick(datum, keyList, _index, columns)">
                    <i :class="[
                      'fas fa-check-circle',
                      datum.isChecked
                        ? 'color_blue' 
                        : column.status && column.status(datum) == 'not-confirm'
                          ? 'color_red' : 'color_grey']"></i>
                  </button>
                </template>
                <template v-else-if="column.isConfirmUser">
                  <button :class="['btn-icon', column.class]" :confirm-user="'checked:' +datum.isConfirmUser" @click="column.onClick(datum, keyList, _index)">
                    <i :class="[
                      'fas fa-user',
                      datum.isConfirmUser
                        ? 'color_blue' 
                        : 'color_grey']"></i>
                  </button>
                </template>
                <template v-else-if="column.isCheckDiffPrice">
                  <button v-if="column.status && column.status(datum, columns)" :class="['btn-icon', column.class]">
                    <i :class="['fas fa-exclamation', column.status && column.status(datum, columns)? 'color_red' : 'color_grey']"></i>
                  </button>
                </template>
                <template v-else-if="column.isButton && column.isShow">
                  <div>
                      <b-button
                      :class="column.class"
                      :disabled="isFunction(column.disabled) && column.disabled(datum, column.field, keyList)" 
                      @click="column.onClick(datum, _index, keyList)"
                    >
                      {{ column.labelButton }}
                    </b-button>
                  </div>
                </template>
                <template v-else>
                  {{datum[column.field]}}
                </template>
              </td>
            </tr>
          </tbody>
          <tfoot v-if="isShowFooter">
            <tr>
              <slot name="tfoot">
                <td :colspan="indexOfFooter" class="text-right">小計: </td>
                <td class="dataTableFooter"><div>{{ footerData | formatNumber}}</div></td>
                <td v-if="columns.length > (indexOfFooter + 1)" :colspan="(columns.length - indexOfFooter)"></td>
              </slot>
            </tr>
          </tfoot>
        </table>
      </div>
      <div class="table-action" v-if="!disabled && !isLocked && isCanShowAction">
        <table ref="subtable" class="table b-table">
          <thead>
            <tr>
              <th :class="['table-icon', {'has-data': data && data.length > 0}]">
                <div class="icon-action-container">
                  <button v-if="!data || data.length == 0" @click="addItem(-1)">
                    <i class="fas fa-plus-circle"></i>
                  </button>
                </div>
              </th>
            </tr>
          </thead>
          <tbody v-if="data">
            <tr v-for="n in (data.length)" :key="n" v-show="!data[n-1].isDeleted">
              <td class="table-icon">
                <div class="icon-action-container">
                  <button @click="addItem(n-1)">
                    <i class="fas fa-plus-circle"></i>
                  </button>
                  <button @click="copyItem(n-1)">
                    <i class="fas fa-copy"></i>
                  </button>
                  <button v-if="data && data.length >= 2" @click="showPopupDeleteItem(n-1)">
                    <i class="fas fa-minus-circle"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
          <tfoot v-if="isShowFooter">
            <tr>
              <td class="table-icon"></td>
            </tr>
          </tfoot>
        </table>
      </div>
    </b-container>
    <DeleteDialog v-if="isShowPopupDelete" v-model="isShowPopupDelete" title="明細情報を削除しますか？" @delete="removeItem" />
  </div>
</template>
<script>
import maxBy from "lodash/maxBy";
import isFunction from "lodash/isFunction";
import includes from "lodash/includes";
import findIndex from "lodash/findIndex";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
export default {
  name: "DataTable",
  props: {
    keyList: {
      type: String,
      default: ""
    },
    columns: {
      type: Array,
      default: () => []
    },
    data: {
      type: Array,
      default: () => []
    },
    disabled: {
      type: Boolean,
      default: false
    },
    isShowAddItemIcon: {
      type: Boolean,
      default: true
    },
    isShowCopyItemIcon: {
      type: Boolean,
      default: true
    },
    isShowRemoveItemIcon: {
      type: Boolean,
      default: true
    },
    isShowFooter: {
      type: Boolean,
      default: true
    },
    footerData: {
      type: Number,
      default: undefined
    },
    initialMaxItemNumber: {
      type: Number,
      default: 0
    },
    isLocked: {
      type: Boolean,
      default: false
    },
    requestStatus: {
      type: String,
      default: ""
    },
  },
  mixins: [getStatusOfPageDetailRequest],
  data() {
    return {
      maxItemNumber: 0,
      isFunction: isFunction,
      minHeight: 0,
      selectedIndex: -1,
      isShowPopupDelete: false,
      indexOfFooter: 0
    }
  },
  filters: {
    formatNumber(value) {
      if(value) {
        return new Intl.NumberFormat('en-US').format(value);
      }
      return '';
    }
  },
  computed: {
    isCanShowAction() {
      return includes([2, 4, 6, 9], this.statusPage);
    }
  },
  created() {
    this.maxItemNumber = this.initialMaxItemNumber;
    this.indexOfFooter = findIndex(this.columns,(item) =>{ return item.field === "accruedAmount"})
  },
  methods: {
    getMaxItemNumber() {
      let maxItemNumber = maxBy(this.data, 'itemNumber');
      return parseInt(maxItemNumber && parseInt(maxItemNumber.itemNumber) > this.maxItemNumber
        ? maxItemNumber.itemNumber
        : this.maxItemNumber
      );
    },
    addItem(index) {
      this.maxItemNumber = this.getMaxItemNumber();
      this.$emit("add-item", index, this.keyList, this.maxItemNumber);
      this.maxItemNumber = this.getMaxItemNumber();
    },
    copyItem(index) {
      this.maxItemNumber = this.getMaxItemNumber();
      this.$emit("copy-item", index, this.keyList, this.maxItemNumber);
      this.maxItemNumber = this.getMaxItemNumber();
    },
    removeItem() {
      if(this.selectedIndex >= 0) {
        this.$emit('remove-item', this.selectedIndex, this.keyList);
        this.isShowPopupDelete = false;
      }
    },
    showPopupDeleteItem(index) {
      this.selectedIndex = index;
      this.isShowPopupDelete = true;
    },
    isCanShowByStatusPageDetail(column) {
      if(column.isComment) {
        return includes(this.$route.path, "account-receivables");
      } else if(column.isCheckDiffPrice) {
        return !includes([1, 2, 10], this.statusPage); // && lech guideline
      } else {
        return true;
      }
    },
    mouseover(event, target, itemNumber, field, isSelectBox = false) {
      if(event) {
        event.preventDefault();
      }
      if (this.isShowTooltip(itemNumber, field, isSelectBox)) {
        this.$root.$emit(' bv::enable::tooltip');
        this.$root.$emit('bv::show::tooltip', target);
      }
    },
    mouseleave(target) {
      this.$root.$emit('bv::hide::tooltip', target);
      this.$root.$emit('bv::disable::tooltip');
    },
    isShowTooltip(itemNumber, field, isSelectBox) {
      // Find refs
      let elements = this.$refs[`ellipsis_${field}_${itemNumber}`];
      if(elements) {
        elements = Array.isArray(elements) ? elements : [elements];
        let isScroll = false;
        isScroll = elements.some(ele => {
          if(isSelectBox) {
            let actualScrollElements = ele.$el.getElementsByClassName("vs__selected");
            return actualScrollElements && actualScrollElements.length > 0 && actualScrollElements[0].clientWidth < actualScrollElements[0].scrollWidth;
          } else {
            return ele.$el.clientWidth < ele.$el.scrollWidth;
          }
        });
        return isScroll;
      }
      return false;
    },
    openSelectBox(search, loading, index, field, listName, openFunc) {
      this.minHeight = '15rem';
      if(isFunction(openFunc)) {
        openFunc(search, loading, index, field, listName);
      }
    },
    onCloseSelect() {
      this.minHeight = 0;
    },
  },
}
</script>
<style lang="scss" scoped>
.table-container {
  display: flex;
  flex-wrap: nowrap;
  & > div {
    flex-grow: 1;
    max-width: 100%;
    &.table-edit {
      max-width: calc(100% - 3.5rem);
      &.has-data {
        max-width: calc(100% - 8rem);
      }
    }
    &.table-action{
      min-width: 3.5rem;
      max-width: 8rem;
      padding-left: 0.5rem;
    }
  }
  .table {
    overflow-y: hidden; /*visible*/
    position: relative;
    display: block;
    width: 100%;
    padding-left: 0px;
    border-collapse: separate;
    border-spacing: 0px;
    transition: all .7s;
    th, td {
      text-align: center;
      white-space: nowrap;
      vertical-align: middle;
      font-size: 0.75rem;
      &.table-icon{
        min-width: calc(3rem - 0.5rem);
        max-width: calc(8rem - 0.5rem);
        right: 0;
        background: #fff !important;
        border-color: #fff !important;
        min-width: 3rem;
        &.has-data {
          min-width: calc(8rem - 0.5rem);
          max-width: calc(8rem - 0.5rem);
        }
        .icon-action-container {
          min-height: 1.125rem;
          display: flex;
          justify-content: space-evenly;
          button {
            background: transparent;
            i {
              font-size: 1.275rem;
              color: rgb(128, 128, 128);
            }
          }
        }
      }
    }
    td.table-icon .icon-action-container {
      min-height: 1.875rem;
    } 
    thead > tr > th {
      background: #707070;
      color: #fff;
      font-weight: 400;
      padding: .25rem 0;
      line-height: 1.3;
      &:not(.confirm):not(.comment):not(.check-diff):not(.table-icon) {
        min-width: 6.25rem;
        max-width: 6.25rem;
        white-space: normal;
      }
      &:last-child {
        width: 100%;
      }
    }
    tbody > tr > td {
      font-weight: 400;
      padding: 2px;
      color: #030303;
      overflow: hidden;
      &:not(.confirm):not(.comment):not(.check-diff):not(.table-icon) {
        min-width: 6.25rem;
        max-width: 6.25rem;
      }
      &.select-box {
        overflow: visible;
      }
      &[data-field="storeGNm"],
      &[data-field="shainNm"],
      &[data-field="hinmokuCd"] {
        /deep/ .v-select .vs__dropdown-toggle .vs__actions{
          display: none !important;
        }
      }
      i {
        font-size: 1.375rem !important;
      }
      & > div {
        position: relative;
        max-width: 100%;
        /deep/ & > input {
          max-width: 100%;
          z-index: 900;
          font-size: 0.75rem;
          color: #030303 !important;
          border: 1px solid #707070;
          border-radius: 0;
          padding:5px;
          text-overflow: ellipsis;
          overflow: hidden;
          &:focus,
          &:active {
            outline:none;
          }
          &.change-value {
            background: #FCE655 !important;
          }
          &.form-control-plaintext {
            border: none;
            background: #E5E5E5;
          }
        }
        button {
          font-size: 0.75rem;
          padding:5px;
        }
      } 
      /deep/ .v-select {
        border: 1px solid #707070;
        position: relative;
        & > .vs__dropdown-toggle {
          min-width: 100%;
          max-width: 100%;
          padding: 0 5px;
          border: none;
          min-height: 1.75rem;
          height: 100%;
          overflow: hidden;
          padding: 0;
          line-height: 1.75rem;
          .vs__selected-options {
            height: inherit;
            flex-wrap: nowrap;
            z-index: 10;
            line-height: inherit;
            span {
              height: inherit;
              text-overflow: ellipsis;
              max-width: 100%;
              display: inline-block;
              line-height: inherit;
            }
            input {
              border: none;
              width: 0;
            }
          }
        }
        & > .vs__dropdown-menu {
          z-index: 9999;
          max-height: 8rem;
          visibility: visible !important;
          display: block !important;
          left: auto;
          right: 0;
          overflow-x: hidden;
          position: static;
          & > li {
            min-width: 12rem;
            & > * {
              min-width: 100%;
            }
          }
        }
        &.vs--disabled .vs__dropdown-toggle,
        &.vs--disabled .vs__actions {
          background-color: #E5E5E5 !important;
          .vs__open-indicator {
            background: inherit;
          }
        }
        &.vs--open {
          z-index: 9999;
        }
      }
      button {
        width: auto;
        height: auto;
        &.btn-icon {
          min-width: unset;
          width: auto;
          background: transparent;
        }
      }
      .itemNumber {
        color: #030303;
        text-decoration: none;
        cursor: default;
      }
    }
    tbody > tr.refering * {
      background: #FCE655 !important;
    }
    tfoot > tr > td {
      border-bottom: 1px solid #dee2e6;
    }
  }
 
}
</style>