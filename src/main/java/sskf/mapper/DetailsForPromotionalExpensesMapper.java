package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.common.enums.EnumBrandClassification;
import sskf.common.enums.EnumClassification;
import sskf.common.enums.EnumPromotionExpenses;
import sskf.common.enums.EnumTypeInput;
import sskf.model.entity.DetailsForPromotionalExpensesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.DetailsForPromotionalExpensesRequest;
import sskf.util.ObjectUtil;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class DetailsForPromotionalExpensesMapper {

    @Autowired
    private AccountReceivablesDetailMapper accountReceivablesDetailMapper;

    public DetailsForPromotionalExpensesEntity toEntity(DetailsForPromotionalExpensesRequest request, RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, ShainEntity createdBy) {
        if ( request == null ) {
            return null;
        }
        DetailsForPromotionalExpensesEntity entity = new DetailsForPromotionalExpensesEntity();
        Byte promotion = EnumPromotionExpenses.getCodeByValue(request.getTypeOfPromotionalExpenses());
        entity.setTypeOfPromotionalExpenses(promotion);
        Byte typeOfInput = EnumTypeInput.getCodeByValue(request.getTypeOfInput());
        entity.setTypeOfInput(typeOfInput);
        if (!EnumTypeInput.PRODUCT_CODE.code.equals(typeOfInput)) {
            Byte classification = EnumClassification.getCodeByValue(request.getClassification());
            entity.setClassification(classification);
            Byte brandClassification = EnumBrandClassification.getCodeByValue(request.getBrandClassification());
            entity.setBrandClassification(brandClassification);
            entity.setCategoryName(request.getCategoryName());
        }

        entity.setHinmokuCd(request.getHinmokuCd());
        entity.setHinmokuRnm(request.getHinmokuRnm());
        entity.setNisugata(request.getNisugata());
        entity.setAccruedAmount(request.getAccruedAmount());
        entity.setInitialAccruedAmount(request.getInitialAccruedAmount());

        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = accountReceivablesDetailMapper.toEntity(request);
        requestAccountsReceivableDetailsEntity.setRequestAccountsReceivablesEntity(requestAccountsReceivablesEntity);
        entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
        return entity;
    }

    public DetailsForPromotionalExpensesEntity toEntityUpdated(@MappingTarget DetailsForPromotionalExpensesEntity entity, DetailsForPromotionalExpensesRequest request) {
        if ( request == null ) {
            return null;
        }

        Byte promotion = EnumPromotionExpenses.getCodeByValue(request.getTypeOfPromotionalExpenses());
        entity.setTypeOfPromotionalExpenses(promotion);
        Byte typeOfInput = EnumTypeInput.getCodeByValue(request.getTypeOfInput());
        entity.setTypeOfInput(typeOfInput);
        if (!EnumTypeInput.PRODUCT_CODE.code.equals(typeOfInput)) {
            Byte classification = EnumClassification.getCodeByValue(request.getClassification());
            entity.setClassification(classification);
            Byte brandClassification = EnumBrandClassification.getCodeByValue(request.getBrandClassification());
            entity.setBrandClassification(brandClassification);
            entity.setCategoryName(request.getCategoryName());
        }

        entity.setHinmokuCd(request.getHinmokuCd());
        entity.setHinmokuRnm(request.getHinmokuRnm());
        entity.setNisugata(request.getNisugata());
        entity.setAccruedAmount(request.getAccruedAmount());
        entity.setInitialAccruedAmount(request.getInitialAccruedAmount());


        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = entity.getRequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity = accountReceivablesDetailMapper.toEntityUpdated(requestAccountsReceivableDetailsEntity, request);
        entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
        return entity;
    }


    @Mapping(target = "typeOfPromotionalExpenses", ignore = true)
    @Mapping(target = "typeOfInput", ignore = true)
    @Mapping(target = "classification", ignore = true)
    @Mapping(target = "brandClassification", ignore = true)
    @Mapping(target = "accountReceivableDetailCd", source = "requestAccountsReceivableDetailsEntity.cd")
    @Mapping(target = "itemNumber", source = "requestAccountsReceivableDetailsEntity.itemNumber")
    @Mapping(target = "storeGNm", source = "requestAccountsReceivableDetailsEntity.storeGNm")
    @Mapping(target = "storeGCd", source = "requestAccountsReceivableDetailsEntity.storeGCd")
    @Mapping(target = "shainCd", source = "requestAccountsReceivableDetailsEntity.shainEntity.shainCd")
    @Mapping(target = "shainNm", source = "requestAccountsReceivableDetailsEntity.shainNm")
    @Mapping(target = "bumonCd", source = "requestAccountsReceivableDetailsEntity.mstBumonEntity.bumonCd")
    @Mapping(target = "bumonNm", source = "requestAccountsReceivableDetailsEntity.bumonNm")
    @Mapping(target = "sortNumber", source = "requestAccountsReceivableDetailsEntity.sortNumber")
    @Mapping(target = "isDeleted", source = "requestAccountsReceivableDetailsEntity.isDeleted")
    @Mapping(target = "isChecked", source = "requestAccountsReceivableDetailsEntity.isChecked")
    public abstract DetailsForPromotionalExpensesRequest toResponse(DetailsForPromotionalExpensesEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget DetailsForPromotionalExpensesRequest response, DetailsForPromotionalExpensesEntity entity) {
        String typeOfPromotional = EnumPromotionExpenses.getValueByCode(entity.getTypeOfPromotionalExpenses());
        response.setTypeOfPromotionalExpenses(typeOfPromotional);
        String typeOfInput = EnumTypeInput.getValueByCode(entity.getTypeOfInput());
        response.setTypeOfInput(typeOfInput);

        if (ObjectUtil.isNotEmpty(entity.getClassification())) {
            String classification = EnumClassification.getValueByCode(entity.getClassification());
            response.setClassification(classification);
        }

        if (ObjectUtil.isNotEmpty(entity.getBrandClassification())) {
            String brandClassification = EnumBrandClassification.getValueByCode(entity.getBrandClassification());
            response.setBrandClassification(brandClassification);
        }

    }
}
