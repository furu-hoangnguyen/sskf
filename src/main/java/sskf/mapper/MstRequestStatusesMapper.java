package sskf.mapper;

import org.mapstruct.Mapper;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.response.MstRequestStatusesResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MstRequestStatusesMapper {

    public abstract MstRequestStatusesResponse toResponse(MstRequestStatusesEntity entity);

    public abstract List<MstRequestStatusesResponse> toResponseList(List<MstRequestStatusesEntity> entities);

}
