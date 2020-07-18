package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.ObjectUtils;
import sskf.common.Constants;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.request.AccountReceivablesDetailRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AccountReceivablesDetailMapper {

    @Mapping(target = "isChecked", defaultValue = "0")
    public abstract RequestAccountsReceivableDetailsEntity toEntity(AccountReceivablesDetailRequest accountReceivablesDetailRequest);

    public abstract AccountReceivablesDetailRequest toResponse(RequestAccountsReceivableDetailsEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget AccountReceivablesDetailRequest response, RequestAccountsReceivableDetailsEntity entity) {
        response.setShainCd(entity.getShainEntity().getShainCd());
        response.setBumonCd(entity.getMstBumonEntity().getBumonCd());
    }

    @Mapping(target = "isChecked", ignore = true)
    public abstract RequestAccountsReceivableDetailsEntity toEntityUpdated(@MappingTarget RequestAccountsReceivableDetailsEntity entity, AccountReceivablesDetailRequest accountReceivablesDetailRequest);

    @AfterMapping
    public RequestAccountsReceivableDetailsEntity toEntity(@MappingTarget RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity, AccountReceivablesDetailRequest accountReceivablesDetailRequest) {

        if (ObjectUtils.isEmpty(accountReceivablesDetailRequest.getIsDeleted())) {
            requestAccountsReceivableDetailsEntity.setIsDeleted(Constants.NOT_DELETED);
        }

        return requestAccountsReceivableDetailsEntity;
    }
}
