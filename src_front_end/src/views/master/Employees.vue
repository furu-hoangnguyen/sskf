<template>
  <section class="empbody">
    <div class="header">社員マスタ</div>
    <div class="controller">
      <div class="filter-as-text">
        <p>検索条件:</p>
        <span>{{filterAsText}}</span>
      </div>
      <div>
        <b-button variant="none" class="btn-white" @click="showPopupSearch = true">検索条件</b-button>
        <b-button variant="none" class="btn-white" @click="resetList">検索条件<br/>リセット</b-button>
      </div>
    </div>
    <div class="employee">
      <div class="filter">
        <b-form-select
          v-model="request.sortField"
          :options="sorts"
          value-field="field"
          text-field="name"
          @change="goSelect">
        </b-form-select>

        <b-form-select
          v-model="request.limitNumber"
          :options="pageLimit"
          value-field="limit"
          text-field="text"
          @change="goSelect">
        </b-form-select>
      </div>
      <div class="main-table testTable" >
        <table @scroll="fixScrollTable" ref="contentEmployeeMaster">
          <thead>
            <tr>
              <th :style="{left:  parseInt(fixLeft)  + 'px'}" rowspan="2">社員コード</th>
              <th class="has-border" :style="{left:  parseInt(fixLeft)  + 'px'}" rowspan="2">名前</th>
              <th rowspan="2">部門</th>
              <th rowspan="2">役職</th>
              <th rowspan="2">パスワード</th>
              <th rowspan="2">削除<br>ステータス</th>
              <th colspan="11" style="text-align: left; height: auto; padding:0; padding-left:.5rem" >アラート発生トリガー</th>
              <th colspan="8" style="text-align: left; height: auto; padding:0; padding-left:.5rem">リマインドアラート発生トリガー</th>
            </tr>
            <tr>
              <th class="normal-header">申請</th>
              <th class="normal-header">修正依頼</th>
              <th class="normal-header">却下</th>
              <th class="normal-header">承認</th>
              <th class="normal-header">差し戻し</th>
              <th class="normal-header">差し戻し確認</th>
              <th class="normal-header">支払日入力</th>
              <th class="normal-header">決済確認</th>
              <th class="normal-header">申請代理人設定</th>
              <th class="normal-header">承認代理人設定</th>
              <th class="normal-header">担当者変更</th>
              <th class="normal-header">リマインドアラート 作成中</th>
              <th class="normal-header">リマインドアラート 確認待ち</th>
              <th class="normal-header">リマインドアラート 申請待ち</th>
              <th class="normal-header">リマインドアラート 承認待ち</th>
              <th class="normal-header">リマインドアラート 申請待ち(差し戻し中)</th>
              <th class="normal-header">リマインドアラート 承認待ち(差し戻し中)</th>
              <th class="normal-header">リマインドアラート 決済確認待ち</th>
              <th class="normal-header">マスタ変更適用処理</th>
            </tr>
          </thead>
          <tbody>
          <tr v-for="shain in shains" :key="shain.shainCd">
            <td class="p-0" v-for="(field, fieldIndex) in fields" :key="fieldIndex"
              :class="{ 'has-border' : fieldIndex == 1 }"
              :style="{left: parseInt(`${ fieldIndex < 2 ? fixLeft : 0 }`)   + 'px'}"
              :id="`${shain.shainCd + fieldIndex}`"
              @mouseover.self="handleHover($event,`${shain.shainCd + fieldIndex}`)"
              @mouseleave.self="handleHoverLeave(`${shain.shainCd + fieldIndex}`)"
              v-b-tooltip.hover.right.v-secondary="{title:innerTextToolTip, disabled: true}"
            >
              <span v-if="field.displayAs === 'checkbox'">
                  <span v-if="shain[field.fieldName]">
                    <i class='fas fa-check'></i>
                  </span>
                  <span v-else-if="field.fieldName === 'isDeleted'">
                    <span v-if="shain.shainCdNk"></span>
                    <span v-else>
                      <i class='fas fa-check'></i>
                    </span>
                  </span>
              </span>
              <span v-else>
                  <span v-if="field.fieldName === 'bumonNm'">
                      {{shain.mstBumonResponse.bumonNm}}
                  </span>
                  <span v-else-if="field.fieldName === 'positions'">
                    <span v-for="(position, positionIndex) in shain.mstRelYakushokuShainResponses" :key="positionIndex">
                      {{position.mstYakushokuResponse.name}}
                      <span v-if="positionIndex+1 < shain.mstRelYakushokuShainResponses.length">、 </span>
                    </span>
                  </span>
                  <span v-else>
                      {{shain[field.fieldName]}}
                  </span>
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <b-pagination
        align="right"
        v-model="currentPage"
        :total-rows="totalItemsPageable"
        :per-page="request.limitNumber"
        :first-number="currentPage == 1"
        :prev-class="{'d-none' : currentPage == 1}"
        :last-number="currentPage == totalPage || totalPage == 0"
        :next-class="{'d-none' : currentPage == totalPage || totalPage == 0}"
        class='ml-auto mr-5'
        @change="onPageChanged($event)"
      >
      </b-pagination>
    </div>
    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件
      </div>
      <b-row class="employeeNormal">
        <b-col cols="9">
          <b-form-group label="社員コード" label-for="employee_code" class="label_input searchEmployee mt-5">
            <b-form-input v-model="filter.shainCd" type="text" id="employee_code"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="名前" label-for="employee_name" class="label_input searchEmployee mt-3">
            <b-form-input v-model="filter.shainNm" type="text" id="employee_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="部門" label-for="department_name" class="label_input searchEmployee mt-3">
            <b-form-select v-model="filter.bumonCd" :options="departmentSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col cols="9">
          <b-form-group label="役職" label-for="position_name" class="label_input searchEmployee mt-3">
            <b-form-select v-model="filter.positionCd" :options="positionSelectOptions" ></b-form-select>
          </b-form-group>
        </b-col>

        <b-col cols="9">
          <b-form-group label=" " label-for="include_deleted" class="label_input searchEmployee searchEmployee_checkbox mt-3">
            <b-form-checkbox size="lg" id="include_deleted" class="lableInclude_deleted" v-model="filter.includeDeleted" plain>削除済みを含める</b-form-checkbox>
          </b-form-group>
        </b-col>
      </b-row>
      <b-row class="search_recordAdmin">

      </b-row>
      <b-row class="mt-4">
        <b-col cols="8" align-self="end">
          <b-button-group>
            <b-button variant="none" class="btn-filter btn-white" @click="resetSearchForm">リセット</b-button>
            <b-button variant="none" class="btn-filter btn-blue" @click="onSearch">検索する</b-button>
          </b-button-group>
        </b-col>
      </b-row>
    </AdminSearchDialog>
  </section>
</template>

<script>
import MasterService from "@/services/master/MasterService";
export default {
  name: "Employees",
  data() {
    return {
      textTooltip: " ",
      positionSelectOptions: [],
      departmentSelectOptions: [],
      currentPage: 1,
      totalPage: 0,
      totalItemsPageable: 0,
      filterAsText: '',
      shains: [],
      filter: {
        shainCd: '',
        shainNm: '',
        bumonCd: '',
        positionCd: '',
        includeDeleted: false
      },
      request: {
        sortField: 'shainCd==asc',
        pageNumber: 0,
        limitNumber: 20
      },
      showPopupSearch: false,
      fixLeft: 0,
      fields: [ { fieldName: 'shainCd', label: '社員コード', displayAs: 'text'}, //3
        { fieldName: 'shainNm', label: '名前', displayAs: 'text'}, // 4
        { fieldName: 'bumonNm', label: '部門', displayAs: 'text'}, // 5
        { fieldName: 'positions', label: '役職', displayAs: 'text'}, // 6
        { fieldName: 'password', label: 'パスワード', displayAs: 'text'}, // 7
        { fieldName: 'isDeleted', label: '削除ステータス', displayAs: 'checkbox'}, // 8, (shain_cd_nk != null => no delete => no show "check") or (shain_cd_nk == null => delete => show "check")
        { fieldName: 'isAlertedForApplication', label:'申請', displayAs: 'checkbox'}, // 10
        { fieldName: 'isAlertedForModificationRequest', label: '修正依頼', displayAs: 'checkbox'}, // 11 
        { fieldName: 'isAlertedForRejection', label: '却下', displayAs: 'checkbox'}, // 12
        { fieldName: 'isAlertedForApproval', label: '承認', displayAs: 'checkbox'}, // 13
        { fieldName: 'isAlertedForSendingRequestBack', label: '差し戻し', displayAs: 'checkbox'}, // 14
        { fieldName: 'isAlertedForConfirmingSendRequestBack', label: '差し戻し確認', displayAs: 'checkbox'}, // 15
        { fieldName: 'isAlertedForInputPaymentDate', label: '支払日入力', displayAs: 'checkbox'}, // 16
        { fieldName: 'isAlertedForConfirmingSettlement', label: '決済確認', displayAs: 'checkbox'}, // 17
        { fieldName: 'isAlertedForApplicationDeputy', label: '申請代理人設定', displayAs: 'checkbox'}, // 18
        { fieldName: 'isAlertedForApprovalDeputy', label: '承認代理人設定', displayAs: 'checkbox'}, // 19
        { fieldName: 'isAlertedForChangingCharge', label: '担当者変更', displayAs: 'checkbox'}, // 20
        { fieldName: 'isAlertedForBeingCreated', label: '作成中', displayAs: 'checkbox'}, // 22
        { fieldName: 'isAlertedForWaitingConfirmation', label: '確認待ち', displayAs: 'checkbox'}, // 23
        { fieldName: 'isAlertedForWaitingApplication', label: '申請待ち', displayAs: 'checkbox'}, // 24
        { fieldName: 'isAlertedForWaitingApproval', label: '承認待ち', displayAs: 'checkbox'}, // 25
        { fieldName: 'isAlertedForWaitingApplicationOnSendingBack', label: '申請待ち(差し戻し中)', displayAs: 'checkbox'}, // 26
        { fieldName: 'isAlertedForWaitingApprovalOnSendingBack', label: '承認待ち(差し戻し中)', displayAs: 'checkbox'}, // 27
        { fieldName: 'isAlertedForWaitingConfirmingSettlement', label: '決済確認待ち', displayAs: 'checkbox'}, // 28
        { fieldName: 'isAlertedForUpdatingDatabase', label: 'マスタ変更適用処理', displayAs: 'checkbox'} // 29
      ],
      sorts: [
        {
          field: 'shainCd==asc',
          name: '社員コード(昇順)' 
        },
        {
          field: 'shainCd==desc',
          name: '社員コード(降順)'
        }
      ],
      pageLimit: [
        {
          text: "20件",
          limit: 20
        },
        {
          text: "50件",
          limit: 50
        }
      ]
    };
  },
  methods: {
    getData(){
      this.onScrollTop();

      MasterService.getShains(this.request).then(res => {
        console.log(res);
        this.shains = res.data.content;
        this.totalItemsPageable = res.data.totalElements;
        this.totalPage = res.data.totalPages;
      }).catch(err => {
        console.log(err);
      });
    },
    getFilterSelectOptions() {
      this.departmentSelectOptions = [{ value: '', text: '' }];
      this.positionSelectOptions = [{ value: '', text: '' }];
      MasterService.getFilterSelectOptions().then(res => {
        res.data.positions.forEach(position => {
          this.positionSelectOptions.push({ value: position.cd, text: position.name });
        });
        res.data.departments.forEach(department => {
          this.departmentSelectOptions.push({ value: department.bumonCd, text: department.bumonNm });
        });
      }).catch(err => {
        console.log(err);
      });
    },
    onSearch() {
        let filterItems = [];
        let filterText = [];
        this.currentPage = 1;
        this.request.pageNumber = 0;
        this.request.sortField = "shainCd==asc";
        this.request.limitNumber = 20;

        if (this.filter.shainCd) {
          filterItems.push("shainCd=='" + this.filter.shainCd + "%'");
          filterText.push('社員コード:' + this.filter.shainCd);
        }
        if (this.filter.shainNm) {
          filterItems.push("shainNm=='%" + this.filter.shainNm + "%'");
          filterText.push('名前:' + this.filter.shainNm);
        }
        if (this.filter.bumonCd) {
          filterItems.push("mstBumonEntity.bumonCd=='" + this.filter.bumonCd + "'");
          filterText.push('部門:' + this.departmentSelectOptions.find( dp => dp.value == this.filter.bumonCd).text);
        }
        if (this.filter.positionCd) {
          filterItems.push("mstRelYakushokuShainEntities.mstYakushokuEntity.cd=='" + this.filter.positionCd + "'");
          filterText.push('役職:' + this.positionSelectOptions.find( po => po.value == this.filter.positionCd).text);
        }
        if (this.filter.includeDeleted) {
          filterText.push('削除済みを含める');
        } else {
          filterItems.push("(shainCdNk!='';shainCdNk!=null)");
        }
        this.request.keyword = filterItems.join(";");
        this.filterAsText = filterText.join(" / ");
        this.getData();
        this.showPopupSearch = false;
    },
    onPageChanged(event) {
      this.request.pageNumber = event - 1;
      this.getData();
    },
    resetSearchForm() {
      this.filter.shainCd = '';
      this.filter.shainNm = '';
      this.filter.bumonCd = '';
      this.filter.positionCd = '';
      this.filter.includeDeleted = false;
      this.request.keyword = "shainCdNk!='';shainCdNk!=null";
    },
    resetList() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.request.sortField = "shainCd==asc";
      this.request.limitNumber = 20;
      this.resetSearchForm();
      this.getData();
    },
    fixScrollTable(event) {
      this.fixLeft = event.target.scrollLeft;
    },
    goSelect() {
      this.currentPage = 1;
      this.request.pageNumber = 0;
      this.getData();
    },
    handleHoverLeave(id) {
      if(id){
        this.$root.$emit('bv::hide::tooltip', id);
        this.$root.$emit('bv::disable::tooltip');
        this.textTooltip = " ";
      }
    },
    handleHover(event, id) {
      if(id){
        let elm1 = event.target;
        let text = elm1?.innerText ? elm1?.innerText : "";
        if ( elm1?.scrollWidth > elm1?.clientWidth) {
          this.textTooltip = text;
          this.$root.$emit(' bv::enable::tooltip');
          this.$root.$emit('bv::show::tooltip', id);
        }
      }
    },
    innerTextToolTip(){
      return this.textTooltip;
    },
    onScrollTop() {
      if (this.$refs.contentEmployeeMaster) {
        this.$refs.contentEmployeeMaster.scrollTop = 0;
      }
    }
  },
  created() {
    this.getFilterSelectOptions();
    this.request.keyword = "shainCdNk!='';shainCdNk!=null";
    this.getData();
  }
};
</script>

<style lang='scss' scoped>
.normal-header {
  z-index: 0 !important;
  border-top: none;
  height: auto !important;
  padding: 0 .3rem !important;
}

</style>