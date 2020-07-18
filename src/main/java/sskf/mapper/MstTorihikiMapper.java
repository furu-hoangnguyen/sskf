package sskf.mapper;

import org.mapstruct.Mapper;
import sskf.model.entity.MstTorihikiEntity;
import sskf.model.response.MstTorihikiRespone;

@Mapper(componentModel = "spring")
public abstract class MstTorihikiMapper {
    public abstract MstTorihikiRespone toResponse(MstTorihikiEntity entity);
}
