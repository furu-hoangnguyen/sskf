package sskf.service;

import sskf.common.enums.EnumTypeInput;
import sskf.model.entity.MstHinmokuCategoriesEntity;
import sskf.model.response.BumonResponse;
import sskf.model.response.MstStoreResponse;
import sskf.model.response.MstYakushokuResponse;
import sskf.model.response.ShainResponse;

import java.util.List;

public interface DropdownService {
    List<BumonResponse> listDepartment();

    List<BumonResponse> listDepartmentForItem(String shainCd);

    List<ShainResponse> listApplication(String bumonCd);

    List<BumonResponse> listPaymentPlace();

    List<MstYakushokuResponse> listPosition();

    List<ShainResponse> listChargeName(String chargeName);

    List<String> listCategoryName(EnumTypeInput enumTypeInput);

    List<MstHinmokuCategoriesEntity> listCategories();

    List<MstStoreResponse> listStoreGroup(String storeGnmForSearch);
}
