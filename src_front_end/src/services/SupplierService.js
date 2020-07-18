import http from "../http-common";
var SupplierService = {

    listDropDownSuppliers() {
        return http.get("mst-torihiki/torihiki-rnm-for-search-dropdown");
    },
    searchSuppliers (param) {
        return http.get("mst-torihiki/torihikis", {
            params: param
        });
    },
    getRequestListReuse(param) {
        return http.get("request/get-requests/re-use?" + param);
    }
}

export default SupplierService;
