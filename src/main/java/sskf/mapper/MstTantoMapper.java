package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.MstTantoEntity;
import sskf.model.response.MstTantoResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MstTantoMapper {
    public abstract MstTantoResponse toResponse(MstTantoEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget MstTantoResponse response, MstTantoEntity entity) {
        if (entity.getShainEntity() != null) {
            response.setShainNm(entity.getShainEntity().getShainNm());
        }
        if (entity.getMstBumonEntity() != null) {
            response.setBumonNm(entity.getMstBumonEntity().getBumonNm());
        }
    }
}
