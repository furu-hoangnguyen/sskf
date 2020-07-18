import Vue from "vue";
import formatDigitNumber from "@/helper/formatDigitNumber";
const getFirstRowByType = (item, type) => {
  if(type == "マネキン") {
    return `
      <div class="row px-4 py-1">
        <div class="pl-0 col-sm-12">
          <span>
            <div role="group" class="form-group label_input">
              <label for="subject" class="d-block">表題</label>
              <div class="bv-no-focus-ring">
                <div class="mx-datepicker disabled main-vselect" id="subject">
                  <div class="mx-input-wrapper">
                    <input disabled="disabled" placeholder="" name="date" type="text" value="${item.subject}" autocomplete="off" class="mx-input">
                  </div>
                </div>
              </div>
            </div>
          </span>
        </div>
      </div>
    `;
  } else if(type == "展示会・協賛") {
    return `
      <div class="row px-4 py-1">
        <div class="pl-0 col-sm-4">
          <span>
            <div role="group" class="form-group label_input">
              <label for="subject" class="d-block">表題</label>
              <div class="bv-no-focus-ring">
                <div class="mx-datepicker disabled main-vselect" id="subject">
                  <div class="mx-input-wrapper">
                    <input disabled="disabled" placeholder="" name="date" type="text" value="${item.subject}" autocomplete="off" class="mx-input">
                  </div>
                </div>
              </div>
            </div>
          </span>
        </div>
      </div>
    `;
  } else {
    return `
      <div class="row px-4 py-1">
        <div class="pl-0 col-sm-4">
          <span>
            <div role="group" class="form-group label_input">
              <label for="targetYear" class="d-block">対象年月</label>
              <div class="bv-no-focus-ring">
                <div class="mx-datepicker disabled main-vselect" id="targetYear">
                  <div class="mx-input-wrapper">
                    <input disabled="disabled" placeholder="" name="date" type="text" value="${Vue.moment(item.targetOn).format("YYYY/MM")}" autocomplete="off" class="mx-input">
                  </div>
                </div>
              </div>
            </div>
          </span>
        </div>
      </div>
    `;
  }
}
const getContentStoreForMannequin = (storeGNm) => {
  return `
    <div class="row px-4 py-1">
      <div class="pl-0 col-sm-12">
        <span>
          <div role="group" class="form-group label_input">
            <label for="subject" class="d-block">店舗グループ名</label>
            <div class="bv-no-focus-ring">
              <div class="mx-datepicker disabled main-vselect" id="subject">
                <div class="mx-input-wrapper">
                  <input disabled="disabled" placeholder="" name="date" type="text" value="${storeGNm}" autocomplete="off" class="mx-input">
                </div>
              </div>
            </div>
          </div>
        </span>
      </div>
    </div>
  `;
}
const getContentPurposeForOtherType = (purpose, purposeOfOthers) => {
  return `
    <div class="row px-4 py-1 mt-3">
      <div class="pl-0 col-sm-4">
        <div class="purpose-container">
          <span>
            <div role="group" class="form-group label_input">
              <label for="purpose" class="d-block">用途</label>
              <div class="bv-no-focus-ring">
                <input type="text" disabled="disabled" readonly="readonly" value="${purpose}" class="form-control">
              </div>
            </div>
          </span>
        </div>
      </div>
      <div class="pl-0 col-sm-5 other-purpose" style="display: ${purpose == "その他" ? "inherit" : "none"}; margin-left: -15px;">
        <span class="fieldDependent">
          <div role="group" class="form-group label_input">
            <div class="bv-no-focus-ring">
              <input type="text" disabled="disabled" readonly="readonly" value="${purposeOfOthers}" class="form-control">
            </div>
          </div>
        </span>
      </div>
    </div>
  `;
}
const RequestForPrint = (item) => {
let type = item.requestModel.requestTypeName;
let template = `
  <section class="request">
    <div class="request-header">
      <div class="request-header-container">
        <div class="title">
          <h4>
            ${type == "マネキン" ? "販促金(マネキン)申請" : type == "展示会・協賛" ? "販促金(展示会・協賛)申請" : "販売未収金申請"}
          </h4>
          <div class="number">
            <label class="application-num">申請番号: ${item.requestModel.requestNumberResponse?.cd ? formatDigitNumber(item.requestModel.requestNumberResponse?.cd) : ""}</label>
            <label class="payment-num">決済番号:  ${item.requestModel.settlementNumber ? item.requestModel.settlementNumber : ""}</label>
          </div>
        </div>
      </div>
    </div>
    <div class="request-body customScrollBar container-fluid">
      <div class="sskf_collapse container-fluid no-gutters">
        <div class="row sskf_collapse_header p-1" aria-controls="sskf_7bacze" aria-expanded="true" role="button">
          <div class="pl-0 col-auto" style="margin-top: 15px;">
            販促金管理項目
            <i  class="fas fa-angle-down pl-1"></i>
          </div>
        </div>
        <div  id="sskf_7bacze" class="sskf_collapse_body collapse show">
          ${ getFirstRowByType(item, type) }
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-12">
              <span>
                <div role="group" class="form-group label_input">
                  <label class="d-block">取引先名</label>
                  <div class="bv-no-focus-ring">
                    <input type="text" disabled="disabled" readonly="readonly" value="${item.requestModel.torihikiNm}" class="form-control">
                  </div>
                </div>
              </span>
            </div>
          </div>
          ${type == "マネキン" ? getContentStoreForMannequin(item.storeGNm) : getContentPurposeForOtherType(item.purpose, item.purposeOfOthers)}
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-4">
              <span separator=",">
                <div role="group" class="form-group label_input">
                  <label for="request_billing_amount" class="d-block">請求金額(円)</label>
                  <div class="bv-no-focus-ring">
                    <input placeholder="" type="tel" id="request_billing_amount" disabled="disabled" step="1" autocomplete="off" class="form-control" style="text-align: right;" value="${item.requestModel.billingAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                  </div>
                </div>
              </span>
            </div>
            <div class="col-sm-4">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">請求日</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${Vue.moment(item.requestModel.billingOn).format("YYYY/MM")}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="sskf_collapse container-fluid no-gutters">
        <div class="row sskf_collapse_header p-1" aria-controls="sskf_7bacze" aria-expanded="true" role="button">
          <div  class="pl-0 col-auto" style="margin-top: 2rem !important;">
            支払い情報
            <i class="fas fa-angle-down pl-1"></i>
          </div>
        </div>
        <div class="sskf_collapse_body collapse show">
          <div class="row px-4 py-1">
            <div class="pl-0 col-sm-4">
              <span>
                <div role="group" class="form-group label_input">
                  <label for="targetYear" class="d-block">支払場所</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker disabled main-vselect" id="targetYear">
                      <div class="mx-input-wrapper">
                        <input disabled="disabled" placeholder="" type="text" value="${item.requestModel.paymentPlace}" autocomplete="off" class="mx-input">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
            <div class="col-sm-4">
              <span>
                <div role="group" class="form-group label_input">
                  <label for="targetYear" class="d-block">支払い方法</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker disabled main-vselect" id="targetYear">
                      <div class="mx-input-wrapper">
                        <input disabled="disabled" placeholder="" type="text" value="${item.requestModel.paymentMethod}" autocomplete="off" class="mx-input">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
            <div class="pl-0 col-sm-4 other-purpose" style="display: ${item.requestModel.paymentMethod == "その他" ? "inherit" : "none"}; margin-left: -15px;"  >
              <span class="fieldDependent">
                <div role="group" class="form-group label_input">
                  <div class="bv-no-focus-ring">
                    <input type="text" disabled="disabled" readonly="readonly" value="${item.requestModel.paymentOtherMethod}" class="form-control">
                  </div>
                </div>
              </span>
            </div>
          </div>
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-4">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">支払予定日</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${Vue.moment(item.requestModel.scheduledPaymentOn).format("YYYY/MM/DD")}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
            <div class="col-sm-4">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">支払日</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.paymentOn ? Vue.moment(item.requestModel.paymentOn).format("YYYY/MM/DD") : ""}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
          </div>
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-12">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">支払先</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.paymentDestination}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
          </div>
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-12">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">銀行名</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.bankName}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
          </div>
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-4">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">口座番号</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.bankAccountNumber}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
            <div class="pl-0 col-sm-8">
              <span disabled="disabled">
                <div role="group" class="form-group label_input">
                  <label class="d-block">口座名義</label>
                  <div class="bv-no-focus-ring">
                    <div class="mx-datepicker main-vselect">
                      <div class="mx-input-wrapper">
                        <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.bankAccountName}">
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
          </div>
          <div class="row px-4 py-1 mt-3">
            <div class="pl-0 col-sm-7">
              <table class="table_payment table_payment_1">
                <thead>
                  <tr>
                    <th><label>支払金額</label></th>
                    <th>8%対象品目(円)</th>
                    <th>10%対象品目(円)</th>
                    <th>合計金額(円)</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>品目</td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.itemTotalForEightPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.itemTotalForTenPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.itemTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td>消費税</td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.consumptionTaxTotalForEightPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.consumptionTaxTotalForTenPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.consumptionTaxTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td>合計金額</td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.totalForEightPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.totalForTenPercent.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                    <td>
                      <span disabled="disabled">
                        <div role="group" class="form-group label_input fieldPayment">
                          <div class="bv-no-focus-ring">
                            <div class="mx-datepicker main-vselect">
                              <div class="mx-input-wrapper">
                                <input placeholder="" disabled="disabled" name="date" type="text" autocomplete="off" class="mx-input" value="${item.requestModel.total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}">
                              </div>
                            </div>
                          </div>
                        </div>
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
`;
  return template;
}

export default RequestForPrint;