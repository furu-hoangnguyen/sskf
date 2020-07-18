package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import sskf.common.enums.EnumItem;
import sskf.exception.SSKFException;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.AccountReceivablesDetailRequest;
import sskf.model.request.DetailsForAccountsReceivablesRequest;
import sskf.service.CommentsForDetailsService;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class DetailsForAccountsReceivablesMapper {

    @Autowired
    private CommentsForDetailsService commentsForDetailsService;


    @Autowired
    private AccountReceivablesDetailMapper accountReceivablesDetailMapper;

    @Autowired
    private CommentsForDetailsMapper commentsForDetailsMapper;

    @Mapping(target = "typeOfItem", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cd", ignore = true)
    public abstract DetailsForAccountsReceivablesEntity toEntity(DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest, RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, ShainEntity createdBy);

    @AfterMapping
    public void toEntity(@MappingTarget DetailsForAccountsReceivablesEntity entity, DetailsForAccountsReceivablesRequest request, RequestAccountsReceivablesEntity requestAccountsReceivablesEntity,  ShainEntity createdBy) {
        Byte item = EnumItem.getCodeByValue(request.getTypeOfItem());
        if (ObjectUtils.isEmpty(item)) {
            throw new SSKFException("400", "Item invalid!");
        }
        entity.setTypeOfItem(item);
        AccountReceivablesDetailRequest accountReceivablesDetailRequest = new AccountReceivablesDetailRequest();
        accountReceivablesDetailRequest.setAccountReceivableDetailCd(request.getAccountReceivableDetailCd());
        accountReceivablesDetailRequest.setItemNumber(request.getItemNumber());
        accountReceivablesDetailRequest.setStoreGNm(request.getStoreGNm());
        accountReceivablesDetailRequest.setShainCd(request.getShainCd());
        accountReceivablesDetailRequest.setShainNm(request.getShainNm());
        accountReceivablesDetailRequest.setBumonCd(request.getBumonCd());
        accountReceivablesDetailRequest.setBumonNm(request.getBumonNm());
        accountReceivablesDetailRequest.setSortNumber(request.getSortNumber());
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = accountReceivablesDetailMapper.toEntity(accountReceivablesDetailRequest);
        requestAccountsReceivableDetailsEntity.setRequestAccountsReceivablesEntity(requestAccountsReceivablesEntity);
        entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
    }

    @Mapping(target = "typeOfItem", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cd", ignore = true)
    public abstract DetailsForAccountsReceivablesEntity toEntityUpdated(@MappingTarget DetailsForAccountsReceivablesEntity entity, DetailsForAccountsReceivablesRequest request);

    @AfterMapping
    public void toEntityForUpdate(@MappingTarget DetailsForAccountsReceivablesEntity entity, DetailsForAccountsReceivablesRequest request) {
        Byte item = EnumItem.getCodeByValue(request.getTypeOfItem());
        if (ObjectUtils.isEmpty(item)) {
            throw new SSKFException("400", "Item invalid!");
        }

        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = entity.getRequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity = accountReceivablesDetailMapper.toEntityUpdated(requestAccountsReceivableDetailsEntity, request);
        entity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity);
    }


    @Mapping(target = "typeOfItem", ignore = true)
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
    public abstract DetailsForAccountsReceivablesRequest toResponse(DetailsForAccountsReceivablesEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget DetailsForAccountsReceivablesRequest response, DetailsForAccountsReceivablesEntity entity) {
        String item = EnumItem.getValueByCode(entity.getTypeOfItem());
        response.setTypeOfItem(item);

    }
}
