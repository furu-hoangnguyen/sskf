package sskf.mapper;

import org.mapstruct.Mapper;
import sskf.model.entity.MstYakushokuEntity;
import sskf.model.response.MstYakushokuResponse;

@Mapper(componentModel = "spring")
public abstract class MstYakushokuMapper {

    public abstract MstYakushokuResponse toResponse(MstYakushokuEntity mstYakushokuEntity);

}
