import http from "../../http-common";
var MasterService = {
    getImportedSituationMasterStatus(param) {
      return http.get("/master/get-imported-situation-status",{
        params: param
      });
    },

    getFilterSelectOptions()  {
        return http.get("/master/get-shain-filter-select-option");
    },

    getDepartmentSelectOptions(){
        return http.get("dropdown/department");
    },

    getPositionSelectOptions() {
        return http.get("dropdown/position");
    },

    getEmployeeFilterSelectOptions()  {
        return http.get("/dropdown/category-all");
    },

    getShains(filter)  {
        return http.get("/shain/get-shains-master",{
            params: filter
        });
    },

    getStores(request) {
        return http.get("/master/get-stores",{
            params: request
        });
    },

    getTantos(request) {
        return http.get("/master/get-tantos",{
            params: request
        });
    },

    getSupliers (param) {
        return http.get("/mst-torihiki/torihikis", {
            params: param
        });
    },

    getMasterApproveDetails (param) {
        return http.get("/master/get-approval-flows", {
            params: param
        });
    },

    getMasterApproveDetailsGroup (param) {
        return http.get("/master/get-approval-flows-group", {
            params: param
        });
    }
}

export default MasterService;
