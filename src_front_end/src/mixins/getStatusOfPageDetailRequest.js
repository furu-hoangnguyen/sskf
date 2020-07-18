import isTablet from "@/helper/isTabletDevice";
import includes from "lodash/includes";
export default {
  computed: {
    statusPage(){
      let status = this.requestStatus;
      if(!status) {
        if(this.$route.path.includes("/create")){
          status = "作成中";
        } else {
          return null;
        }
      }
      switch (status) {
        case "作成中": {
          console.log("thaoht", status)
          if(this.$route.params && (this.$route.params.mode == "edit" || this.$route.params.mode == "reuse")) {
            return 2;
          } else if(!this.$route.params.cd && !this.disabled) {
            return 2;
          } else if(this.modeForCreateRequest == "confirmCreate") {
            return 1;
          } else {
            return 1;
          }
        }
        case "確認待ち": {
          if(this.$route.params && this.$route.params.mode == "edit") {
            return 4;
          } else {
            return 3;
          }
        }
        case "申請待ち": {
          if(this.$route.params && this.$route.params.mode == "edit") {
            return 6;
          } else {
            return 5;
          }
        }
        case "承認待ち": {
          if(this.$route.params && this.$route.params.mode == "edit") {
            if(isTablet()) {
              return 10;
            } else if(this.item &&
              this.item.requestModel &&
              this.item.requestModel.updatedAtAfterAccountingCheck &&
              includes(["確認待ち", "申請待ち", "承認待ち"], this.requestStatus)
            ) {
              return 11;
            } else {
              return 9;
            }
          } else {
            if(isTablet()) {
              return 8;
            } else {
              return 7;
            }
          }
        }
        case "却下": {
          return 14;
        }
        case "決済確認待ち": {
          if(this.$route.params && this.$route.params.mode == "edit") {
            return 13;
          } else {
            return 12;
          }
        }
        case "支払済": {
          return 15;
        }
        default:
          return 1;
      }
    }
  },
}