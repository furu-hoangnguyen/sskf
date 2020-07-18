import http from "../http-common";

var MannequinPromotionService = {
    mannequinPromotions(files, param) {
        var jsonse = JSON.stringify(param);
        var blob = new Blob([jsonse], { type: "application/json" });
        let formData = new FormData();
        formData.append('receivablesRequest', blob);
        if (files) {
            files.forEach(elm => {
                formData.append('files', elm);
            })
        }
        return http.post("/mannequin-promotions", formData);
    },
    mannequinPromotionsUpdate(files, param) {
        var jsonse = JSON.stringify(param);
        var blob = new Blob([jsonse], { type: "application/json" });
        let formData = new FormData();
        formData.append('receivablesRequest', blob);
        if (files) {
            files.forEach(elm => {
                formData.append('files', elm);
            })
        }
        return http.put("/mannequin-promotions", formData);
    },
    mannequinPromotionsForUpdate(files, param) {
        var jsonse = JSON.stringify(param);
        var blob = new Blob([jsonse], { type: "application/json" });
        let formData = new FormData();
        formData.append('receivablesRequest', blob);
        if (files) {
            files.forEach(elm => {
                formData.append('files', elm);
            })
        }
        return http.put("mannequin-promotions/update", formData);
    },
    detailMannequinPromotion(id){
        return http.get("/mannequin-promotions/"+`${id}`);
    },
    
}
export default MannequinPromotionService