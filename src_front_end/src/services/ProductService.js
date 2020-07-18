import http from "../http-common";
import cookie from "vue-cookie";
var ProductService = {
  listProducts(param) {
      return http.get("/mst-hinmoku",{
        params: param
      });
  },
  listProductsClassification() {
      return http.get("/dropdown/product-classification");
  },
  listBusinessCode() {
      return http.get("/dropdown/business-code");
  },
}

export default ProductService;
