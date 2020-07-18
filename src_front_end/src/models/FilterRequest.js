import formatNumber from "@/helper/formatNumber";

export default class FilterRequest {

  constructor(applyNumber, settlementNumber, requestType, relatedPerson, confirmPerson, applicant,
    approver, status, supplierName, appliedAtMin, appliedAtMax, paymentScheduledDateMin, paymentScheduledDateMax, numberOfStagnancyDay,
    amountMin, amountMax) {

    //21, number
    this.applyNumber = applyNumber;

    //22, number
    this.settlementNumber = settlementNumber;

    //23, search text, select
    this.requestType = requestType;

    //24, text
    this.relatedPerson = relatedPerson;

    //25, text
    this.confirmPerson = confirmPerson;

    //26, text
    this.applicant = applicant;

    //27, text
    this.approver = approver;

    //28, text
    this.status = status;

    //29, text
    this.supplierName = supplierName;

    //30, date
    this.appliedAtMin = appliedAtMin;
    this.appliedAtMax = appliedAtMax;

    //31, date
    this.paymentScheduledDateMin = paymentScheduledDateMin;
    this.paymentScheduledDateMax = paymentScheduledDateMax;

    //32, number
    this.numberOfStagnancyDay = numberOfStagnancyDay;

    //33, number
    this.amountMin = amountMin;
    this.amountMax = amountMax;
  }

}

FilterRequest.prototype.toString = function() {
    let string = '';
    
    //21, number
    if (!this.isEmptyString(this.applyNumber)) {
      string += '申請番号: ' + this.applyNumber;
    }

    //22, number
    if (!this.isEmptyString(this.settlementNumber)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '決済番号: ' + this.settlementNumber;
    }

    //23, text, select
    if (!this.isEmptyString(this.requestType)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '請求タイプ: ' + this.requestType;
    }

    //24, text
    if (!this.isEmptyString(this.relatedPerson)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '関係者: ' + this.relatedPerson;
    }

    //25, text
    if (!this.isEmptyString(this.confirmPerson)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '確認者: ' + this.confirmPerson;
    }

    //26, text
    if (!this.isEmptyString(this.applicant)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '申請者: ' + this.applicant;
    }
    
    //27, text
    if (!this.isEmptyString(this.approver)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '承認者: ' + this.approver;
    }

    //28, text
    if (!this.isEmptyString(this.status)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += 'ステータス: ' + this.status;
    }

    //29, text
    if (!this.isEmptyString(this.supplierName)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '取引先名: ' + this.supplierName;
    }

    //30, date
    if (!this.isEmptyString(this.appliedAtMin) || !this.isEmptyString(this.appliedAtMax)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '申請日 : ';

      if (!this.isEmptyString(this.appliedAtMin)) {
        string += this.appliedAtMin;
      }
      
      string += ' ~ ';

      if (!this.isEmptyString(this.appliedAtMax)) {
        string += this.appliedAtMax;
      }
    }

    //31, date
    if (!this.isEmptyString(this.paymentScheduledDateMin) || !this.isEmptyString(this.paymentScheduledDateMax)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '支払予定日 : ';

      if (!this.isEmptyString(this.paymentScheduledDateMin)) {
        string += this.paymentScheduledDateMin;
      }
      
      string += ' ~ ';

      if (!this.isEmptyString(this.paymentScheduledDateMax)) {
        string += this.paymentScheduledDateMax;
      }
    }

    //32, text
    if (!this.isEmptyString(this.numberOfStagnancyDay)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '停滞日数: ' + this.numberOfStagnancyDay;
    }

    //33, number
    if (!this.isEmptyString(this.amountMin) || !this.isEmptyString(this.amountMax)) {
      string += !this.isEmptyString(string) ? ' / ' : '';
      string += '金額 : ';

      if (!this.isEmptyString(this.amountMin)) {
        string += formatNumber(this.amountMin);
      }
      
      string += ' ~ ';

      if (!this.isEmptyString(this.amountMax)) {
        string += formatNumber(this.amountMax);
      }
    }

    return string;
}

FilterRequest.prototype.isEmpty = function() {
  let properties = Object.entries(this);

  for (var i = 0; i < properties.length; i++) {
    if (!this.isEmptyString(properties[i][1])) {
      return false;
    }
  }

  return true;
}

FilterRequest.prototype.isEmptyString = function(str) {
  return (str === undefined || str === '' || str === null);
}

