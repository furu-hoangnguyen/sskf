package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.MstStoreEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.response.MstStoreResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MstStoreMapper {

    public abstract MstStoreResponse toResponse(MstStoreEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget MstStoreResponse response, MstStoreEntity entity) {
        if (entity.getMstTantoEntity() != null && entity.getMstTantoEntity().getShainEntity() != null) {
            ShainEntity shainEntity = entity.getMstTantoEntity().getShainEntity();
            response.setShainNm(shainEntity.getShainNm());
            response.setShainCd(shainEntity.getShainCd());
        }
    }
}
