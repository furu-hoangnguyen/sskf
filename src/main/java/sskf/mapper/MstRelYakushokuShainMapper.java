package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import sskf.model.entity.MstRelYakushokuShainEntity;
import sskf.model.response.MstRelYakushokuShainResponse;
import sskf.model.response.MstYakushokuResponse;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class MstRelYakushokuShainMapper {

    @Autowired
    private MstYakushokuMapper mstYakushokuMapper;

    public abstract Set<MstRelYakushokuShainResponse> toResponses(Set<MstRelYakushokuShainEntity> entities);

    @Mapping(target = "mstYakushokuResponse", ignore = true)
    public abstract MstRelYakushokuShainResponse toResponse(MstRelYakushokuShainEntity entity);

    @AfterMapping
    protected void mapMstRelYakushokuShain(MstRelYakushokuShainEntity entity, @MappingTarget MstRelYakushokuShainResponse mstRelYakushokuShainResponse) {
        if (!ObjectUtils.isEmpty(entity.getMstRelYakushokuShainEntityPK())) {
            MstYakushokuResponse mstYakushokuResponse = mstYakushokuMapper.toResponse(entity.getMstYakushokuEntity());
            mstRelYakushokuShainResponse.setMstYakushokuResponse(mstYakushokuResponse);
        }

    }

}
