package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.common.Constants;
import sskf.common.enums.EnumTypeInput;
import sskf.mapper.MstBumonMapper;
import sskf.mapper.MstYakushokuMapper;
import sskf.mapper.ShainMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstHinmokuCategoriesEntity;
import sskf.model.entity.MstYakushokuEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.response.BumonResponse;
import sskf.model.response.MstStoreResponse;
import sskf.model.response.MstYakushokuResponse;
import sskf.model.response.ShainResponse;
import sskf.repository.BumonRepository;
import sskf.repository.MstHinmokuCategoriesRepository;
import sskf.repository.MstYakushokuRepository;
import sskf.repository.ShainRepository;
import sskf.service.DropdownService;
import sskf.service.MstBumonService;
import sskf.service.MstStoreServices;
import sskf.service.ShainService;
import sskf.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DropdownServiceImpl implements DropdownService {

    @Autowired
    private BumonRepository bumonRepository;

    @Autowired
    private MstBumonMapper mstBumonMapper;

    @Autowired
    private ShainService shainService;

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainMapper shainMapper;

    @Autowired
    private MstYakushokuRepository yakushokuRepository;

    @Autowired
    private MstYakushokuMapper yakushokuMapper;

    @Autowired
    private MstHinmokuCategoriesRepository mstHinmokuCategoriesRepository;

    @Autowired
    private MstStoreServices mstStoreServices;

    @Autowired
    private MstBumonService mstBumonService;

    @Override
    public List<BumonResponse> listDepartment() {
        try {
            log.info("Log begin service: listDepartment");
            List<MstBumonEntity> bumonEntityList = bumonRepository.findByBumonCdNkIsNotNull();
            return bumonEntityList.stream().map(mstBumonMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service: listDepartment");
        }
    }

    @Override
    public List<BumonResponse> listDepartmentForItem(String shainCd) {
        try {
            log.info("Log begin service DropdownServiceImpl: listDepartmentForItem");
            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
            String keySearch = "mstTantoEntities.shainEntity.shainCd=='" + shainCd + "'";
            baseSearchRequest.setKeyword(keySearch);
            List<BumonResponse> bumonEntityList = mstBumonService.listDepartmentForItem(baseSearchRequest);
            return bumonEntityList;
        } finally {
            log.info("Log end service DropdownServiceImpl: listDepartmentForItem");
        }
    }

    @Override
    public List<ShainResponse> listApplication(String bumonCd) {
        try {
            log.info("Log begin service: listApplication");
            List<ShainEntity> shainEntities = shainRepository.findByShainCdNkIsNotNullAndMstBumonEntity_BumonCd(bumonCd);
            return shainEntities.stream().map(shainMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service: listApplication");
        }
    }

    @Override
    public List<BumonResponse> listPaymentPlace() {
        try {
            log.info("Log begin service: listPaymentPlace");
            List<String> bumonCds = Arrays.asList("1201", "1202", "1203", "1301", "1302", "1401", "1402", "1501", "1502", "1601", "1602", "1701", "1702");
            List<MstBumonEntity> bumonEntityList = bumonRepository.findByBumonCdIn(bumonCds);
            return bumonEntityList.stream().map(mstBumonMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service: listPaymentPlace");
        }
    }

    @Override
    public List<MstYakushokuResponse> listPosition() {
        List<MstYakushokuEntity> positions = yakushokuRepository.findAllByIsDeletedIs(Constants.NOT_DELETED);
        return positions.stream().map(yakushokuMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ShainResponse> listChargeName(String chargeName) {
        try {
            log.info("Log begin service: listChargeName");
            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
            String searchKey = "shainNm=='%"+ chargeName +"%'" + Constants.AND_RSQL + "shainCdNk!=NULL";
            baseSearchRequest.setKeyword(searchKey);
            List<ShainEntity> shainEntities = shainService.getListShains(baseSearchRequest);
            return shainEntities.stream().map(shainMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log end service: listChargeName");
        }
    }

    @Override
    public List<String> listCategoryName(EnumTypeInput enumTypeInput) {
        try {
            log.info("Log begin service: listCategoryName");
            List<String> categoryName = new ArrayList<>();
            Byte categoryType = null;
            switch (enumTypeInput) {
                case PRODUCT_CODE:
                    return categoryName;
                case PRODUCT_SERIES:
                    categoryType = (byte) 2;
                    break;
                case PRODUCT_CATEGORY:
                    categoryType = (byte) 0;
                    break;
                case PRODUCT_SUBCATEGORY:
                    categoryType = (byte) 1;
                    break;
            }
            return mstHinmokuCategoriesRepository.findByCategoryType(categoryType).stream().map(e -> e.getName()).collect(Collectors.toList());
        } finally {
            log.info("Log end service: listCategoryName");
        }
    }

    @Override
    public List<MstHinmokuCategoriesEntity> listCategories() {
        return mstHinmokuCategoriesRepository.findAll();
    }

    @Override
    public List<MstStoreResponse> listStoreGroup(String storeGnmForSearch) {
        try {
            log.info("Log begin service: listStoreGroup");
            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
            String searchKey = "deleteFlg==0" + (StringUtil.isNotEmpty(storeGnmForSearch) ? (";storeGNmForSearch=='%" + storeGnmForSearch + "%'") : "");
            baseSearchRequest.setKeyword(searchKey);
            return mstStoreServices.listStores(baseSearchRequest);
        } finally {
            log.info("Log end service: listStoreGroup");
        }
    }
}
