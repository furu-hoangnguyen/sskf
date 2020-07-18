<template>
   <section class="empbody">
    <div class="header">ユーザ情報</div>

    <b-container fluid class="fixScroll">
      <ValidationObserver ref="updateShainInformation" v-slot="{ errors, invalid }">
        <div class="mx-5">
          <div class="user_infor_input">
            <b-row v-if="(invalid && handleFormError(errors).length > 0) || getErrorFormListField(fieldTableUser).length > 0 || getErrorFormListField(fieldTableUserRemind).length > 0"
              class="errorsMessagesField px-4 mt-4"
            >
              <b-col
                v-for="(error, index) in handleFormError(errors)"
                :key="error + index"
                sm="12"
              >
                エラー：{{ error }}
              </b-col>

              <b-col
                v-for="(error, index) in getErrorFormListField(fieldTableUser)"
                :key="error + index"
                sm="12"
              >
                エラー：{{ error }}
              </b-col>

              <b-col
                v-for="(error, index) in getErrorFormListField(fieldTableUserRemind)"
                :key="error + index"
                sm="12"
              >
                エラー：{{ error }}
              </b-col>
            </b-row>

            <b-row :class="['px-4 py-3', {'mt-4': handleFormError(errors).length === 0}]">
              <b-col md="5">
                <!-- 2. shain.shain_cd -->
                <b-form-group label="社員コード" label-for="input_shain_cd" class="label_input">
                  <b-form-input
                    type="text"
                    id="input_shain_cd"
                    :value="shainResponse.shainCd"
                    readonly
                  >
                  </b-form-input>
                </b-form-group>
              </b-col>

              <b-col md="5">
                <!-- 3. shain.shain_nm -->
                <b-form-group label="名前" label-for="input_shain_nm" class="label_input">
                  <b-form-input
                    type="text"
                    id="input_shain_nm"
                    :value="shainResponse.shainNm"
                    readonly
                  >
                  </b-form-input>
                </b-form-group>
              </b-col>
            </b-row>

            <b-row class="px-4 py-3">
              <b-col md="5">
                <!-- 4. mst_bumon.bumon_nm on shain.bumon_cd -->
                <b-form-group label="部門" label-for="input_bumon_nm" class="label_input">
                  <b-form-input
                    v-if="shainResponse.mstBumonResponse"
                    type="text"
                    id="input_bumon_nm"
                    :value="shainResponse.mstBumonResponse.bumonNm"
                    readonly
                  >
                  </b-form-input>
                </b-form-group>
              </b-col>

              <b-col md="5">
                <!-- 5. mst_yakushoku.name on shain.shain_cd -->
                <b-form-group label="役職" label-for="input_mst_yakushoku_name" class="label_input">
                  <b-form-input
                    type="text"
                    id="input_mst_yakushoku_name"
                    :value="getMstYakushokuName()"
                    readonly
                  >
                  </b-form-input>
                </b-form-group>
              </b-col>
            </b-row>

            <b-row class="px-4 py-3">
              <b-col md="5">
                <!-- 6. shain.password -->
                <validation-provider
                  rules="required|min:8|max:20|regex"
                  :custom-messages="{
                    required: 'パスワードは半角英数字8文字以上で入力して下さい。',
                    min: 'パスワードは半角英数字8文字以上で入力して下さい。',
                    max: 'パスワードは半角英数字8文字以上で入力して下さい。',
                    regex: 'パスワードは半角英数字8文字以上で入力して下さい。'
                  }"
                  v-slot="{ invalid, validated }"
                >
                  <b-form-group
                    label="パスワード"
                    label-for="input_password"
                    :class="['label_input', { invalidClass: invalid && validated}]"
                  >
                    <b-form-input
                      type="password"
                      id="input_password"
                      v-model="shainResponse.password"
                      placeholder="********"
                    >
                    </b-form-input>
                  </b-form-group>
                </validation-provider>
              </b-col>
            </b-row>
          </div>

          <div class="user_infor_check">
            <div class="px-4 py-3">
              <div class="h6 mt-5">アラート通知変更可否</div>

              <collapse>
                <template slot="collapse-header">
                  <span class="pl-5">アラート処理</span>
                </template>

                <template slot="collapse-body">
                  <b-row class="account_receivables_condition">
                    <b-col cols="auto" class="pl-0 account_receivables_condition_table user_content_table_edit">
                      <b-table-simple>
                        <b-thead>
                          <b-tr>
                            <b-th>発生トリガー</b-th>
                            <b-th>通知</b-th>
                          </b-tr>
                        </b-thead>

                        <b-tbody>
                          <b-tr v-for="item in fieldTableUser"
                            :key="hashCode(item.field)"
                            :class="['border_bottom', { invalidClass: item.isError }]"
                          >
                            <b-td>{{ item.name }}</b-td>
                            <b-td>
                              <b-form-checkbox
                                :id="hashCode(item.field)"
                                v-model="shainResponse[item.field]"
                                class="position_check"
                                button button-variant="white"
                                @change="chooseCheckbox(item.field)"
                              >
                                <div class="border_check">
                                  <i v-if="shainResponse[item.field] === 1" class="fas fa-check"></i>
                                </div>
                              </b-form-checkbox>
                            </b-td>
                          </b-tr>
                        </b-tbody>
                      </b-table-simple>
                    </b-col>
                  </b-row>
                </template>
              </collapse>

              <collapse>
                <template slot="collapse-header">
                  <span class="pl-5">リマインドアラート処理</span>
                </template>

                <template slot="collapse-body">
                  <b-row class="account_receivables_condition">
                    <b-col cols="auto" class="pl-0 account_receivables_condition_table user_content_table_edit">
                      <b-table-simple>
                        <b-thead>
                          <b-tr>
                            <b-th>発生トリガー</b-th>
                            <b-th>通知</b-th>
                          </b-tr>
                        </b-thead>

                        <b-tbody>
                          <b-tr v-for="item in fieldTableUserRemind"
                            :key="hashCode(item.field)"
                            :class="['border_bottom', { invalidClass: item.isError }]"
                          >
                            <b-td>{{ item.name }}</b-td>
                            <b-td>
                              <b-form-checkbox
                                :id="hashCode(item.field)"
                                v-model="shainResponse[item.field]"
                                class="position_check"
                                button button-variant="white"
                                @change="chooseCheckbox(item.field)"
                              >
                                <div class="border_check">
                                  <i v-if="shainResponse[item.field] === 1" class="fas fa-check"></i>
                                </div>
                              </b-form-checkbox>
                            </b-td>
                          </b-tr>
                        </b-tbody>
                      </b-table-simple>
                    </b-col>
                  </b-row>
                </template>
              </collapse>
            </div>
          </div>
        </div>

        <div class="btnForRequest">
          <b-button variant="none" class="btn-blue" @click="updateShainInformation()">
            保存
          </b-button>
        </div>
      </ValidationObserver>
    </b-container>

    <NormalProcessingDialog v-model="showPopupNormalProcess" title="ユーザ情報を保存しました。"/>
    <ErrorProcessingDialog v-model="showPopupErrorProcess" title="コメント入力時にエラーが発生しました。"/>
   </section>

</template>

<script>
import ShainService from "@/services/ShainService";
import ShainResponse from "@/models/ShainResponse";
import hashCode from "@/helper/getHashFromString";
import { extend } from 'vee-validate';

extend("regex", {
  validate: value => {
    let patt = new RegExp("^[0-9a-zA-Z]+$");
    return patt.test(value);
  }
});

export default {
  name: "Edit",
  data() {
    return {
      shainResponse: new ShainResponse(),
      fieldTableUser: [
        {//11
          name: "申請",
          field: "isAlertedForApplication",
          isError: false,
          msgError: "申請　無効な入力です。"
        },
        {//12
          name: "修正依頼",
          field: "isAlertedForModificationRequest",
          isError: false,
          msgError: "修正依頼 無効な入力です。"
        },
        {//13
          name: "却下",
          field: "isAlertedForRejection",
          isError: false,
          msgError: "却下 無効な入力です。"
        },
        {//14
          name: "承認",
          field: "isAlertedForApproval",
          isError: false,
          msgError: "承認 無効な入力です。"
        },
        {//15
          name: "差し戻し",
          field: "isAlertedForSendingRequestBack",
          isError: false,
          msgError: "差し戻し 無効な入力です。"
        },
        {//16
          name: "差し戻し確認",
          field: "isAlertedForConfirmingSendRequestBack",
          isError: false,
          msgError: "差し戻し確認 無効な入力です。"
        },
        {//17
          name: "支払日入力",
          field: "isAlertedForInputPaymentDate",
          isError: false,
          msgError: "支払日入力 無効な入力です。"
        },
        {//18
          name: "決済確認",
          field: "isAlertedForConfirmingSettlement",
          isError: false,
          msgError: "決済確認 無効な入力です。"
        },
        {//19
          name: "申請代理人設定",
          field: "isAlertedForApplicationDeputy",
          isError: false,
          msgError: "申請代理人設定 無効な入力です。"
        },
        {//20
          name: "承認代理人設定",
          field: "isAlertedForApprovalDeputy",
          isError: false,
          msgError: "承認代理人設定 無効な入力です。"
        },
        {//21
          name: "担当者変更",
          field: "isAlertedForChangingCharge",
          isError: false,
          msgError: "担当者変更 無効な入力です。"
        }
      ],
      fieldTableUserRemind: [
        {//23
          name: "リマインドアラート　作成中",
          field: "isAlertedForBeingCreated",
          isError: false,
          msgError: "リマインドアラート　作成中 無効な入力です。"
        },
        {//24
          name: "リマインドアラート　確認待ち",
          field: "isAlertedForWaitingConfirmation",
          isError: false,
          msgError: "リマインドアラート　確認待ち 無効な入力です。"
        },
        {//25
          name: "リマインドアラート　申請待ち",
          field: "isAlertedForWaitingApplication",
          isError: false,
          msgError: "リマインドアラート　申請待ち 無効な入力です。"
        },
        {//26
          name: "リマインドアラート　承認待ち",
          field: "isAlertedForWaitingApproval",
          isError: false,
          msgError: "リマインドアラート　承認待ち 無効な入力です。"
        },
        {//27
          name: "リマインドアラート　申請待ち(差し戻し中)",
          field: "isAlertedForWaitingApplicationOnSendingBack",
          isError: false,
          msgError: "リマインドアラート　申請待ち(差し戻し中) 無効な入力です。"
        },
        {//28
          name: "リマインドアラート　承認待ち(差し戻し中)",
          field: "isAlertedForWaitingApprovalOnSendingBack",
          isError: false,
          msgError: "リマインドアラート　承認待ち(差し戻し中) 無効な入力です。"
        },
        {//29
          name: "リマインドアラート　決済確認待ち",
          field: "isAlertedForWaitingConfirmingSettlement",
          isError: false,
          msgError: "リマインドアラート　決済確認待ち 無効な入力です。"
        },
        {//30
          name: "マスタ変更適用処理",
          field: "isAlertedForUpdatingDatabase",
          isError: false,
          msgError: "マスタ変更適用処理 無効な入力です。"
        }
      ],
      showPopupNormalProcess: false,
      showPopupErrorProcess: false
    }
  },
  methods: {
    getInformation() {
      ShainService.getInformation()
      .then(res => {
        Object.assign(this.shainResponse, res.data);
      })
      .catch(err => {
        console.log(err);
      });
    },
    getMstYakushokuName() {
      if (this.shainResponse.mstRelYakushokuShainResponses && this.shainResponse.mstRelYakushokuShainResponses.length) {
        let name = [];
        this.shainResponse.mstRelYakushokuShainResponses.forEach(item => {
          name.push(item.mstYakushokuResponse.name);
        });

        return name.join("、");
      }

      return "";
    },
    hashCode(s) {
      return hashCode(s);
    },
    chooseCheckbox(field) {
      if (this.shainResponse[field] === 1) {
        this.shainResponse[field] = 0;
      } else {
        this.shainResponse[field] = 1;
      }
    },
    async updateShainInformation() {
      let isError = await this.$refs.updateShainInformation.validate();

      this.fieldTableUser.forEach(item => {
        if (this.shainResponse[item.field] !== 1 && this.shainResponse[item.field] !== 0) {
          item.isError = true;
        } else {
          item.isError = false;
        }
      });

      this.fieldTableUserRemind.forEach(item => {
        if (this.shainResponse[item.field] !== 1 && this.shainResponse[item.field] !== 0) {
          item.isError = true;
        } else {
          item.isError = false;
        }
      });

      if (isError && this.getErrorFormListField(this.fieldTableUser).length === 0 && 
          this.getErrorFormListField(this.fieldTableUserRemind).length === 0) {

        this.showPopupNormalProcess = false;
        this.showPopupErrorProcess = false;

        ShainService.updateShainInformation(this.shainResponse)
        .then(res => {
          Object.assign(this.shainResponse, res.data);
          this.showPopupNormalProcess = true;
        })
        .catch(err => {
          console.log(err);
          this.showPopupErrorProcess = true;
        });
      }
    },
    handleFormError(err) {
      if (err) {
        return Object.values(err).flat();
      }
      return [];
    },
    getErrorFormListField(field) {
      let errorTableUsers = [];
      
      field.forEach(item => {
        if (item.isError) {
          errorTableUsers.push(item.msgError);
        }
      });

      return errorTableUsers;
    }
  },
  created() {
    this.getInformation();
  }
}
</script>

<style lang="scss" scoped>

.header {
  font-size: 1.25rem;
}

.account_receivables_condition {
  margin-left: 5rem;
  margin-top: 1rem;
}

.account_receivables_condition_table {
  table {
    width: auto;
    overflow: unset;
    margin-bottom: 0;
    padding-bottom: 1.5rem;

    thead {
      th {
        border-bottom-width: 0;
      }
    }
    tr {
      th, td {
        font-size: 0.8rem;
        border-top: 0;

        &:first-child {
          text-align: left;
          padding-left: 1.5rem;
        }

        &:last-child {
          text-align: right;
          padding-right: 1.5rem;
        }
      }
    }

    .border_check {
      width: 2rem;
      height: 2rem;
      padding: 4px;
      border: 1px solid grey;
      border-radius: 2px;
      background-color: #fff;
    }

    .position_check {
      margin-right: -5px;
      margin-left: 10rem;
    }

    .border_bottom {
      border-bottom: 1px solid grey;
    }

    .invalidClass {
      td {
        color: #fff;
        border-bottom: 1px solid #fff;
      }
      
      .border_check {
        border: 1px solid #fff;
      }
    }
  }
}

</style>

<style lang="scss">

.user_content_table_edit {
  table {
    .btn-white {
      border-width: 0 !important;
      border-color: #fff !important;
    }

    .btn.focus {
      box-shadow: none;
    }

    .position_check {
      .btn-white {
        background-color: transparent;
      }
    }
  }
}

</style>