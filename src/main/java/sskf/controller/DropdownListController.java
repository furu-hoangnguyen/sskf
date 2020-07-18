package sskf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.common.enums.EnumBrandClassification;
import sskf.common.enums.EnumBusinessCode;
import sskf.common.enums.EnumClassification;
import sskf.common.enums.EnumItem;
import sskf.common.enums.EnumPaymentMethod;
import sskf.common.enums.EnumProductClassification;
import sskf.common.enums.EnumPromotionExpenses;
import sskf.common.enums.EnumTypeCommission;
import sskf.common.enums.EnumTypeInput;
import sskf.common.enums.EnumTypeUse;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstHinmokuCategoriesEntity;
import sskf.model.response.BumonResponse;
import sskf.model.response.DropdownResponse;
import sskf.model.response.MstApprovalFlowsResponse;
import sskf.model.response.MstStoreResponse;
import sskf.model.response.MstYakushokuResponse;
import sskf.model.response.ShainResponse;
import sskf.service.DropdownService;
import sskf.service.MstApprovalFlowsServices;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/api/dropdown")
@RestController
public class DropdownListController {

    @Autowired
    private DropdownService dropdownService;

    @Autowired
    private MstApprovalFlowsServices mstApprovalFlowsServices;
    
    @GetMapping("/items")
    public ResponseEntity<?> listItem() {
        EnumItem[] items = EnumItem.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumItem e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/use-type")
    public ResponseEntity<?> listTypeUse() {
        EnumTypeUse[] items = EnumTypeUse.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumTypeUse e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/commission-type")
    public ResponseEntity<?> listTypeCommission() {
        EnumTypeCommission[] items = EnumTypeCommission.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumTypeCommission e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/department")
    public ResponseEntity<?> listDepartment() {
        List<BumonResponse> responses = dropdownService.listDepartment();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/department-for-item")
    public ResponseEntity<?> listDepartmentForItem(String shainCd) {
        List<BumonResponse> responses = dropdownService.listDepartmentForItem(shainCd);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/position") //yakushoku
    public ResponseEntity<?> listPosition() {
        List<MstYakushokuResponse> responses = dropdownService.listPosition();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/application")
    public ResponseEntity<?> listApplication(String bumonCd) {
        List<ShainResponse> responses = dropdownService.listApplication(bumonCd);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/payment-place")
    public ResponseEntity<?> listPaymentPlace() {
        List<BumonResponse> responses = dropdownService.listPaymentPlace();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/payment-method")
    public ResponseEntity<?> listPaymentMethod() {
        EnumPaymentMethod[] items = EnumPaymentMethod.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumPaymentMethod e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/charge-name")
    public ResponseEntity<?> listChargeName(String chargeName) {
        List<ShainResponse> responses = dropdownService.listChargeName(chargeName);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/promotion-expenses")
    public ResponseEntity<?> listPromotionExpenses() {
        EnumPromotionExpenses[] items = EnumPromotionExpenses.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumPromotionExpenses e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/type-input")
    public ResponseEntity<?> listTypeInput() {
        EnumTypeInput[] items = EnumTypeInput.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumTypeInput e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/classification")
    public ResponseEntity<?> listClassification() {
        EnumClassification[] items = EnumClassification.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumClassification e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/brand-classification")
    public ResponseEntity<?> listBrandClassification() {
        EnumBrandClassification[] items = EnumBrandClassification.class.getEnumConstants();
        List<String> values = new ArrayList<>();
        for (EnumBrandClassification e: items) {
            values.add(e.value);
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<?> listCategory(String typeInput) {
        EnumTypeInput enumTypeInput = EnumTypeInput.value(typeInput);
        List<String> categoryName = dropdownService.listCategoryName(enumTypeInput);
        return new ResponseEntity<>(categoryName, HttpStatus.OK);
    }

    @GetMapping("/category-all")
    public ResponseEntity<?> listCategories() {
        List<MstHinmokuCategoriesEntity> categories = dropdownService.listCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/product-classification")
    public ResponseEntity<?> listProductClassification() {
        EnumProductClassification[] items = EnumProductClassification.class.getEnumConstants();
        List<DropdownResponse> values = new ArrayList<>();
        values.add(new DropdownResponse(null, "全て"));
        for (EnumProductClassification e: items) {
            values.add(new DropdownResponse(e.code, e.value));
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/business-code")
    public ResponseEntity<?> listBusinessCode() {
        EnumBusinessCode[] items = EnumBusinessCode.class.getEnumConstants();
        List<DropdownResponse> values = new ArrayList<>();
        values.add(new DropdownResponse(null, "全て"));
        for (EnumBusinessCode e: items) {
            values.add(new DropdownResponse(e.code, e.value));
        }
        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @GetMapping("/store-group")
    public ResponseEntity<?> listStoreGroup(String storeGnmForSearch) {
        List<MstStoreResponse> response = dropdownService.listStoreGroup(storeGnmForSearch);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/approval-flows")
    public ResponseEntity<?> getApprovalFlows(String bumonCd) {
        BaseSearchRequest request = new BaseSearchRequest();
        request.setKeyword("mstBumonEntity.bumonCd=='" + bumonCd + "';isDeleted==0");
        request.setLimitNumber(50);
        request.setPageNumber(0);
        request.setSortField("name==asc");
        Page<MstApprovalFlowsResponse> approvalFlows = mstApprovalFlowsServices.getApprovalFlows(request);
        return ResponseEntity.ok(approvalFlows);
    }
}
