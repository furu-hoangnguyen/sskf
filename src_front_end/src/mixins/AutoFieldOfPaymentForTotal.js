export default {
  computed: {
    itemTotalForEightPercent() {
      let itemTotalForEightPercent = 0;
      if (this.$route.path.includes("account-receivables")) {
        itemTotalForEightPercent =
          this.sumAccruedAmount(this.item.detailsForAccountsReceivablesRequestList) + 
          this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax8PercentList);
      } else if(this.$route.path.includes("exhibition-promotions")) {
        itemTotalForEightPercent = this.item.requestModel.itemTotalForEightPercent;
      } else if(this.$route.path.includes("mannequin-promotions")) {
        itemTotalForEightPercent = this.item.requestModel.itemTotalForEightPercent;
      }
      return isNaN(itemTotalForEightPercent) ? 0 : itemTotalForEightPercent;
    },
    itemTotalForTenPercent() {
      let itemTotalForTenPercent = 0;
      if (this.$route.path.includes("account-receivables")) {
        itemTotalForTenPercent = this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax10PercentList) + this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestLogisticFeesList);
      } else if(this.$route.path.includes("exhibition-promotions")) {
        itemTotalForTenPercent = this.item.requestModel.itemTotalForTenPercent;
      } else if(this.$route.path.includes("mannequin-promotions")) {
        itemTotalForTenPercent = this.sumFieldForTable10Percent("costsOfMannequin");
      }
      return isNaN(itemTotalForTenPercent) ? 0 : itemTotalForTenPercent;
    },
    itemTotal() {
      return this.itemTotalForEightPercent + this.itemTotalForTenPercent;
    },
    consumptionTaxTotalForEightPercent() {
      return Math.round(this.itemTotalForEightPercent * 1.08);
    },
    consumptionTaxTotalForTenPercent() {
      return Math.round(this.itemTotalForTenPercent * 1.10);
    },
    consumptionTaxTotal() {
      let consumptionTaxTotal = this.consumptionTaxTotalForEightPercent + this.consumptionTaxTotalForTenPercent;
      return consumptionTaxTotal;
    },
    totalForEightPercent() {
      let totalForEightPercent = this.itemTotalForEightPercent + this.consumptionTaxTotalForEightPercent;
        
      return totalForEightPercent;
    },
    totalForTenPercent() {
      let totalForTenPercent = this.itemTotalForTenPercent + this.consumptionTaxTotalForTenPercent;
      return totalForTenPercent;
    },
    total() {
      return this.itemTotal + this.consumptionTaxTotal;
    },
    // initial_
    initialItemTotalForEightPercent() {
      let initialItemTotalForEightPercent = 0;
      if (this.$route.path.includes("account-receivables")) {
        initialItemTotalForEightPercent = this.sumAccruedAmount(this.item.detailsForAccountsReceivablesRequestList) + this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax8PercentList);
      } else if(this.$route.path.includes("exhibition-promotions")) {
        initialItemTotalForEightPercent = this.item.requestModel.itemTotalForEightPercent;
      } else if(this.$route.path.includes("mannequin-promotions")) {
        initialItemTotalForEightPercent = this.item.requestModel.itemTotalForEightPercent;
      }
      return isNaN(initialItemTotalForEightPercent) ? 0 : initialItemTotalForEightPercent;
    },
    initialItemTotalForTenPercent() {
      let initialItemTotalForTenPercent = 0;
      if (this.$route.path.includes("account-receivables")) {
        initialItemTotalForTenPercent = this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestTax10PercentList) + this.sumAccruedAmount(this.item.detailsForPromotionalExpensesRequestLogisticFeesList);
      } else if(this.$route.path.includes("exhibition-promotions")) {
        initialItemTotalForTenPercent = this.item.requestModel.itemTotalForTenPercent;
      } else if(this.$route.path.includes("mannequin-promotions")) {
        initialItemTotalForTenPercent = this.sumFieldForTable10Percent("costsOfMannequin");
      }
      return isNaN(initialItemTotalForTenPercent) ? 0 : initialItemTotalForTenPercent;
    },
    initialItemTotal() {
      let itemTotal = this.initialItemTotalForEightPercent + this.initialItemTotalForTenPercent;
      return itemTotal;
    },
    initialConsumptionTaxTotalForEightPercent() {
      return Math.round(this.initialItemTotalForEightPercent * 1.08);
    },
    initialConsumptionTaxTotalForTenPercent() {
      return Math.round(this.initialItemTotalForTenPercent * 1.10);
    },
    initialConsumptionTaxTotal() {
      let initialConsumptionTaxTotal = this.initialConsumptionTaxTotalForEightPercent + this.initialConsumptionTaxTotalForTenPercent;
      return initialConsumptionTaxTotal;
    },
    initialTotalForEightPercent() {
      let initialTotalForEightPercent = this.initialItemTotalForEightPercent + this.initialConsumptionTaxTotalForEightPercent;
      return initialTotalForEightPercent;
    },
    initialTotalForTenPercent() {
      let initialTotalForTenPercent = this.initialItemTotalForTenPercent + this.initialConsumptionTaxTotalForTenPercent;
      return initialTotalForTenPercent;
    },
    initialTotal() {
      return this.initialItemTotal + this.initialConsumptionTaxTotal;
    }
  },
  watch: {
    itemTotalForEightPercent(newValue) {
      this.item.requestModel.itemTotalForEightPercent = newValue;
    },
    itemTotalForTenPercent(newValue) {
      this.item.requestModel.itemTotalForTenPercent = newValue;
    },
    itemTotal(newValue) {
      this.item.requestModel.itemTotal = newValue;
    },
    consumptionTaxTotalForEightPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForEightPercent = newValue;
    },
    consumptionTaxTotalForTenPercent(newValue) {
      this.item.requestModel.consumptionTaxTotalForTenPercent = newValue;
    },
    consumptionTaxTotal(newValue) {
      this.item.requestModel.consumptionTaxTotal = newValue;
    },
    totalForEightPercent(newValue) {
      this.item.requestModel.totalForEightPercent = newValue;
    },
    totalForTenPercent(newValue) {
      this.item.requestModel.totalForTenPercent = newValue;
    },
    total(newValue) {
      this.item.requestModel.total = newValue;
    },
    initialItemTotalForEightPercent(newValue) {
      this.item.requestModel.initialItemTotalForEightPercent = newValue;
    },
    initialItemTotalForTenPercent(newValue) {
      this.item.requestModel.initialItemTotalForTenPercent = newValue;
    },
    initialItemTotal(newValue) {
      this.item.requestModel.initialItemTotal = newValue;
    },
    initialConsumptionTaxTotalForEightPercent(newValue) {
      this.item.requestModel.initialConsumptionTaxTotalForEightPercent = newValue;
    },
    initialConsumptionTaxTotalForTenPercent(newValue) {
      this.item.requestModel.initialConsumptionTaxTotalForTenPercent = newValue;
    },
    initialConsumptionTaxTotal(newValue) {
      this.item.requestModel.initialConsumptionTaxTotal = newValue;
    },
    initialTotalForEightPercent(newValue) {
      this.item.requestModel.initialTotalForEightPercent = newValue;
    },
    initialTotalForTenPercent(newValue) {
      this.item.requestModel.initialTotalForTenPercent = newValue;
    },
    initialTotal(newValue) {
      this.item.requestModel.initialTotal = newValue;
    }
  }
}