package sskf.mapper;

import org.mapstruct.Mapper;
import sskf.model.entity.MstBumonEntity;
import sskf.model.response.BumonResponse;

@Mapper(componentModel = "spring")
public abstract class MstBumonMapper {

    public abstract BumonResponse toResponse(MstBumonEntity entity);
}
