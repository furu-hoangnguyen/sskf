package sskf.mapper;

import org.mapstruct.Mapper;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.response.MstRequestTypesResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MstRequestTypesMapper {

    public abstract MstRequestTypesResponse toResponse(MstRequestTypesEntity entity);

    public abstract List<MstRequestTypesResponse> toResponseList(List<MstRequestTypesEntity> entities);

}
