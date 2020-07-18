<template>
  <section class="empbody approval_master">
    <div class="header">承認フローマスタ</div>
    <div class="controller">
      <div class="filter-as-text">
        <p>検索条件:</p>
        <span>{{filterAsText}}</span>
      </div>
      <div>
        <b-button variant="none" class="btn-white" @click="showPopupSearch = true">検索条件</b-button>
        <b-button variant="none" class="btn-white" @click="resetList">検索条件<br>リセット</b-button>
      </div>
    </div>
    <div class="employee approval_main implement_approval d-flex flex-row">
      <div class="employee">
        <div class="filter">
          <b-form-select v-model="request.sortField" :options="[{ value: 'name==asc', text: '承認フロー名(昇順)' }, { value: 'name==desc', text: '承認フロー名(降順)' }]" @change="getData"></b-form-select>
          <b-form-select v-model="request.limitNumber" :options="[{ value: '20', text: '20件' },{ value: '50', text: '50件' }]" @change="getData"></b-form-select>
        </div>
        <div class="click-indicator" v-if="isShowClickIndicator" :style="{top:  calculateIndicatorPosition()  + 'rem'}">
        </div>
        <div class="main-table" @scroll="onScrollTable">
          <div class="record"></div>
          <table>
            <thead>
            <tr>
              <th v-for="(field, index) in fields" :key="index" v-html="field.label">
                {{field.label}}
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(apd, index) in apds" :key="index" style="cursor: pointer;"  :class="{record: showArowiconForDiagram(apds[index].cd)}">
              <td v-for="(field, fieldIndex) in fields" :key="fieldIndex"
                :ref="`${apds[index].cd + fieldIndex}`"
                :id="`${apds[index].cd + fieldIndex}`"
                v-b-tooltip.hover.left.v-secondary="{title: innerTextToolTip(`${apds[index].cd + fieldIndex}`,`${apd[field.fieldName]}`)}"
                  @click="getApprovalFlowGroup(apds[index].cd, index)"
                >
                <span v-if="field.displayAs === 'checkbox'">
                  <span v-if="apd[field.fieldName] == '1'">
                      &#10004;
                  </span>
                </span>
                <span v-else  class="m-0">
                  {{apd[field.fieldName]}}
                </span>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
        <div>
          <b-pagination
              align="right"
              v-model="currentPage"
              :total-rows="totalItemsPageable"
              :per-page="request.limitNumber"
              :first-number="this.request.pageNumber == 0"
              :prev-class="{'d-none' : this.request.pageNumber == 0}"
              :last-number="this.request.pageNumber == this.totalPage"
              :next-class="{'d-none' : this.request.pageNumber == this.totalPage}"
              class='mr-5'
              @change="onPageChanged($event)" ></b-pagination>
        </div>
      </div>
        <div class="d-flex flex-column approve_diagram" v-if="!lodash.isEmpty(flowGroups)">
          <div class="d-flex justify-content-center flex-row">
            <div class="approval_modal d-flex flex-column align-items-center">
              <div class="head text-center w-100">
                申請グループ
              </div>
                <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.applyGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.applyGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
              </div>
            </div>
            <div class="approval_modal d-flex flex-column align-items-center">
              <div class="head text-center w-100">
                第1承認グループ
              </div>
              <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.firstApprovalGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.firstApprovalGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
              </div>
            </div>
            <div class="approval_modal d-flex flex-column align-items-center">
              <div class="head text-center w-100">
                第2承認グループ
              </div>
              <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.secondApprovalGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.secondApprovalGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
              </div>
            </div>
            <div class="approval_modal d-flex flex-column align-items-center">
              <div class="head text-center w-100">
                第3承認グループ
              </div>
              <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.thirdApprovalGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.thirdApprovalGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
              </div>
            </div>
            <div class="approval_modal d-flex flex-column align-items-center">
              <div class="head text-center w-100">
                決裁グループ
              </div>
              <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.settlementGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.settlementGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
              </div>
            </div>
          </div>
          <div class="d-flex flex-row justify-content-center mt-4">
            <div class="approval_modal d-flex flex-column align-items-center">
                <div class="head text-center w-100">
                  代理<br>第1承認グループ
                </div>
                <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.firstDeputyGroup)">
                  <div class="body" v-for="(item,index) in flowGroups.firstDeputyGroup" :key="index">
                    <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                    </div>
                    <div class="body_foot text-right ">
                      {{item.yakushokuNm}}
                    </div>
                  </div>
                </div>
              </div>
              <div class="approval_modal d-flex flex-column align-items-center">
                <div class="head text-center w-100">
                  代理<br>第2承認グループ
                </div>
                  <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.secondDeputyGroup)">
                    <div class="body" v-for="(item,index) in flowGroups.secondDeputyGroup" :key="index">
                      <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                      </div>
                      <div class="body_foot text-right ">
                        {{item.yakushokuNm}}
                      </div>
                    </div>
                </div>
              </div>
              <div class="approval_modal d-flex flex-column align-items-center">
                <div class="head text-center w-100">
                  代理<br>第3承認グループ
                </div>
                <div class="body_record d-flex flex-column align-items-center" v-if="!lodash.isEmpty(flowGroups.thirdDeputyGroup)">
                    <div class="body" v-for="(item,index) in flowGroups.thirdDeputyGroup" :key="index">
                      <div class="body_head text-left" v-html="checkSpaceStr(item.bumonNm)">
                      </div>
                      <div class="body_foot text-right ">
                        {{item.yakushokuNm}}
                      </div>
                    </div>
                </div>
              </div>
          </div>
      </div>
    </div>
          
<!--        <div class="approval_flow">-->
<!--          <b-container fluid class="bv-example-row p-0">-->
<!--            <b-row no-gutters align-h="center">-->
<!--              <b-col cols="2" class="px-2 table_adminApproval_r" v-for="(item,index) in data_flows" :key="index">-->
<!--                <div class="table_adminApproval_h">-->
<!--                  {{item.f1}}-->
<!--                </div>-->
<!--                <div class="table_adminApproval_h ">-->
<!--                  <div v-html="$options.filters.downInlineSpace(item.f2)"></div>-->
<!--                  <div class="text-right"> {{item.f3}}</div>-->
<!--                </div>-->
<!--              </b-col>-->
<!--            </b-row>-->
<!--          </b-container>-->
<!--        </div>-->
    <AdminSearchDialog v-model="showPopupSearch">
      <div class="supplieName">検索条件
      </div>
      <b-row no-gutters>
        <b-col cols="7">
          <b-form-group label="承認フロー名" label-for="approval_flow_name" class="label_input searchEmployee searchproduct mt-5">
            <b-form-input v-model="filter.name" type="text" id="approval_flow_name"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col cols="7">
          <b-form-group label="対象部署" label-for="target_department"
                        class="label_input searchEmployee searchproduct mt-3">
            <b-select v-model="filter.approvalFlowBumonCd" id="target_department" :options="departmentSelectOptions"></b-select>
          </b-form-group>
        </b-col>
        <b-col cols="6">
          <b-form-group label="部門" label-for="department"
                        class="label_input searchEmployee searchproduct  mt-3">
            <b-select  v-model="filter.approvalFlowDetailsBumonCd" id="department" class="ml-1" :options="departmentSelectOptions"></b-select>
          </b-form-group>
        </b-col>
        <b-col cols="6" >
          <b-form-group label="役職" label-for="position"
                        class="label_input searchEmployee searchproduct search_position mt-3 ">
            <b-select id="position" v-model="filter.yakushokuCd" :options="positionSelectOptions"></b-select>
          </b-form-group>
        </b-col>
        <b-col cols="12">
          <div class="supplieName mt-4">ロールチェックボックス</div>
          <b-row no-gutters class="checkbox_searchFlow pl-5 mt-2">
            <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.applyGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">申請グループ</b-form-checkbox></b-col>
            <b-col cols="4"> <b-form-checkbox size="lg"  plain v-model="filter.firstApproveGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">第1承認グループ</b-form-checkbox></b-col>
            <b-col cols="4"> <b-form-checkbox size="lg" plain  v-model="filter.secondApproveGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">第2承認グループ</b-form-checkbox></b-col>
          </b-row> <b-row no-gutters class="checkbox_searchFlow pl-5 mt-2">
          <b-col cols="4"> <b-form-checkbox size="lg" plain  v-model="filter.thirdApproveGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">第3承認グループ</b-form-checkbox></b-col>
          <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.settlementGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">決裁グループ</b-form-checkbox></b-col>
        </b-row>

        </b-col>
        <b-col cols="12">
          <div class="supplieName mt-4">代理チェックボックス</div>
          <b-row no-gutters class="checkbox_searchFlow pl-5 mt-2">
            <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.deputyFirstGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">代理第1承認グループ</b-form-checkbox></b-col>
            <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.deputySecondGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">代理第2承認グループ</b-form-checkbox></b-col>
            <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.deputyThirdGroup" :disabled="filter.yakushokuCd == '' && filter.approvalFlowDetailsBumonCd == ''">代理第3承認グループ</b-form-checkbox></b-col>
          </b-row>
        </b-col>
      </b-row>
      <b-row no-gutters class="checkbox_searchFlow mt-5">
        <b-col cols="4"> <b-form-checkbox size="lg" plain v-model="filter.isIncludeDeleted">削除済みを含める</b-form-checkbox></b-col>
      </b-row>
      <b-row class="mt-4">
        <b-col cols="8" align-self="end">
          <b-button-group>
            <b-button variant="none" class="btn-filter btn-white setSize" @click="resetSearchForm">リセット</b-button>
            <b-button variant="none" class=" ml-3 btn-filter btn-blue setSize" @click="onSearch">検索する</b-button>
          </b-button-group>
        </b-col>
      </b-row>
    </AdminSearchDialog>
    <b-popover target="61" variant="danger" triggers="focus">
      <template v-slot:title>Danger!</template>
      Danger variant popover
    </b-popover>
  </section>
</template>

<script>

  import MasterService from "@/services/master/MasterService";
  import _ from 'lodash';
  export default {
    name: "ApprovalFlow",
    data() {
      return {
        reIndex:0,
        lodash:_,
        textTooltip:" ",
        currentPage: 1,
        totalPage: 0,
        totalItemsPageable: 0,
        filterAsText: '',
        departmentSelectOptions: [],
        positionSelectOptions: [],
        apds: [],
        flowGroups: {},
        filter: {
          name: '',
          approvalFlowBumonCd: '',
          approvalFlowDetailsBumonCd: '',
          yakushokuCd: '',
          applyGroup: false,
          firstApproveGroup: false,
          secondApproveGroup: false,
          thirdApproveGroup: false,
          settlementGroup: false,
          deputyFirstGroup: false,
          deputySecondGroup: false,
          deputyThirdGroup: false,
          isIncludeDeleted: false
        },
        request: {
          sortField: 'name==asc',
          pageNumber: 0,
          limitNumber: 20
        },
        showPopupSearch: false,
        fixLeft: 0,
        fields: [ { fieldName: 'name', label: '承認フロー名', displayAs: 'text'},
          { fieldName: 'bumonNm', label: '対象部署', displayAs: 'text'},
          { fieldName: 'isDeleted', label: '削除<br />ステータス', displayAs: 'checkbox'}
        ],
        isShowClickIndicator: false,
        scrollTopPosition: 0,
        selectedRecordIndex: -1
      };
    },
    methods: {
      innerTextToolTip(id,text){
        id = parseInt(id)
        if(id && this.$refs[id] && this.$refs[id][0]){
          if (this.$refs[id][0].scrollWidth > this.$refs[id][0].clientWidth) {
            this.$root.$emit('bv::enable::tooltip');
            this.$root.$emit('bv::show::tooltip', id);
            return text
          }else{
            return ''
          }
        }else{
          return ''
        }
      },
      getData(){
        MasterService.getMasterApproveDetails(this.request).then(res => {
          this.apds = res.data.content;
          this.totalItemsPageable = res.data.totalElements;
          this.totalPage = res.data.totalPages - 1;
          this.isShowClickIndicator = false;
          this.scrollTopPosition = 0;
          this.selectedRecordIndex = -1;
          this.flowGroups = {};
        }).catch(err => {
          console.log(err);
        });
      },
      getFilterSelectOptions() {
        this.departmentSelectOptions = [{ value: '', text: '' }];
        MasterService.getDepartmentSelectOptions().then(res => {
          console.log(res);
          res.data.forEach(department => {
            this.departmentSelectOptions.push({ value: department.bumonCd, text: department.bumonNm });
          });
        }).catch(err => {
          console.log(err);
        });

        this.positionSelectOptions = [{ value: '', text: '' }];
        MasterService.getPositionSelectOptions().then(res => {
          console.log(res);
          res.data.forEach(position => {
            this.positionSelectOptions.push({ value: position.cd, text: position.name });
          });
        }).catch(err => {
          console.log(err);
        });
      },
      onScrollTable(event) {
        this.scrollTopPosition = event.target.scrollTop;
      },
      calculateIndicatorPosition() {
        let pxToRem = this.scrollTopPosition / parseFloat(getComputedStyle(document.documentElement).fontSize);
        let position = this.selectedRecordIndex * 1.625 + 4.5 - pxToRem;
        if (position < 1.625) {
          return -1000;
        }
        return position;
      },
      onSearch() {
        let filterItems = [];
        let filterText = [];
        this.currentPage = 1;
        this.request.pageNumber = 0;

        if (this.filter.name) {
          filterItems.push("name=='%" + this.filter.name + "%'");
          filterText.push('承認フロー名: ' + this.filter.name);
        }

        if (this.filter.approvalFlowBumonCd) {
          filterItems.push("mstBumonEntity.bumonCd=='" + this.filter.approvalFlowBumonCd + "'");
          let selectedDepartment = this.departmentSelectOptions.find(de => de.value == this.filter.approvalFlowBumonCd);
          filterText.push('対象部署: ' + selectedDepartment.text);
        }

        if (this.filter.approvalFlowDetailsBumonCd) {
          filterItems.push("mstApprovalFlowDetailsEntity.mstBumonEntity.bumonCd=='" + this.filter.approvalFlowDetailsBumonCd + "'");
          let selectedDepartment = this.departmentSelectOptions.find(de => de.value == this.filter.approvalFlowDetailsBumonCd);
          filterText.push('部門: ' + selectedDepartment.text);
        }

        if (this.filter.yakushokuCd) {
          filterItems.push("mstApprovalFlowDetailsEntity.mstYakushokuEntity.cd=='" + this.filter.yakushokuCd + "'");
          let selectedYakushoku = this.positionSelectOptions.find(pos => pos.value == this.filter.yakushokuCd);
          filterText.push('役職: ' + selectedYakushoku.text);
        }

        let checkBoxFilter = [];

        if (this.filter.applyGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==1;mstApprovalFlowDetailsEntity.isDeputy==0)");
          filterText.push('申請グループ');
        }

        if (this.filter.firstApproveGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==2;mstApprovalFlowDetailsEntity.isDeputy==0)");
          filterText.push('第1承認グループ');
        }

        if (this.filter.secondApproveGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==3;mstApprovalFlowDetailsEntity.isDeputy==0)");
          filterText.push('第2承認グループ');
        }

        if (this.filter.thirdApproveGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==4;mstApprovalFlowDetailsEntity.isDeputy==0)");
          filterText.push('第3承認グループ');
        }

        if (this.filter.settlementGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==5;mstApprovalFlowDetailsEntity.isDeputy==0)");
          filterText.push('決裁グループ');
        }

        if (this.filter.deputyFirstGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==2;mstApprovalFlowDetailsEntity.isDeputy==1)");
          filterText.push('代理第1承認グループ');
        }

        if (this.filter.deputySecondGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==3;mstApprovalFlowDetailsEntity.isDeputy==1)");
          filterText.push('代理第2承認グループ');
        }

        if (this.filter.deputyThirdGroup && (this.filter.yakushokuCd || this.filter.approvalFlowDetailsBumonCd)) {
          checkBoxFilter.push("(mstApprovalFlowDetailsEntity.stepNumber==4;mstApprovalFlowDetailsEntity.isDeputy==1)");
          filterText.push('代理第3承認グループ');
        }

        if (checkBoxFilter.length > 0) {
          if (checkBoxFilter.length > 1) {
            filterItems.push("(" + checkBoxFilter.join(",") + ")");
          } else {
            filterItems.push(checkBoxFilter.join(","));
          }
        }

        if (this.filter.isIncludeDeleted) {
          filterText.push('削除済みを含める');
        } else {
          filterItems.push("isDeleted==0");
        }

        this.request.keyword = filterItems.join(";");
        console.log(this.request.keyword);
        this.filterAsText = filterText.join(" / ");
        this.getData();
        this.showPopupSearch = false;
      },
      onPageChanged(event) {
        this.request.pageNumber = event - 1;
        this.getData();
      },
      resetSearchForm() {
        this.filter.name = '';
        this.filter.approvalFlowBumonCd = '';
        this.filter.approvalFlowDetailsBumonCd = '';
        this.filter.yakushokuCd = '';
        this.filter.applyGroup = false;
        this.filter.firstApproveGroup = false;
        this.filter.secondApproveGroup = false;
        this.filter.thirdApproveGroup = false;
        this.filter.settlementGroup = false;
        this.filter.deputyFirstGroup = false;
        this.filter.deputySecondGroup = false;
        this.filter.deputyThirdGroup = false;
        this.filter.isIncludeDeleted = false;

        this.filterAsText = '';
        this.request.keyword = '';
      },
      resetList() {
        this.currentPage = 1;
        this.request.pageNumber = 0;
        this.resetSearchForm();
        this.getData();
      },
      getApprovalFlowGroup(approvalFlowCd, recordIndex) {
        this.reIndex = approvalFlowCd
        MasterService.getMasterApproveDetailsGroup( {mstApprovalFlowCd: approvalFlowCd}).then(res => {
          this.flowGroups = res.data;
          this.isShowClickIndicator = true;
          this.selectedRecordIndex = recordIndex;
        }).catch(err => {
          console.log(err);
        });
      },
      checkSpaceStr(str){
        let t = '';
        t = str.split('　').join(' ').split(' ')
        // .join('<br>');
        let arr = []
        for (let index = 0; index < t.length; index++) {
            if( t[index] !== ''){
                arr.push(t[index])
            }else if(str[index] == '' && str[index + 1]){
                arr.push(t[index])
            }
        }
        return arr.join('<br>')
      },
      showArowiconForDiagram(a){
        // :class="{record: showArowiconForDiagram(apds[index].cd)}
        return a == this.reIndex && !this.lodash.isEmpty(this.flowGroups) 
      }
    },
    created() {
      this.getFilterSelectOptions();
      this.getData();
    }
  };
</script>

<style lang='scss' scoped>
.empbody {
  & > .employee {
    flex-wrap: nowrap;
    & > .employee {
      flex: 0 0 22.5rem;
      max-width: 22.5rem;
      & > .filter {
        padding-right: 0;
      }
    }
  }
}
  .click-indicator {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    z-index: 10;
    width: 0px;
    height: 0px;
    border-top: 11px solid transparent;
    border-bottom: 11px solid transparent;
    border-right: 35px solid #E5E5E5;
  }
</style>