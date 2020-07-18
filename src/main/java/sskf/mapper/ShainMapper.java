package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.model.entity.ShainEntity;
import sskf.model.response.BumonResponse;
import sskf.model.response.MstRelYakushokuShainResponse;
import sskf.model.response.ShainResponse;
import sskf.util.CollectionUtil;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ShainMapper {

    @Autowired
    private MstBumonMapper mstBumonMapper;

    @Autowired
    private MstRelYakushokuShainMapper mstRelYakushokuShainMapper;

    @Mapping(target = "mstBumonResponse", ignore = true)
    @Mapping(target = "mstRelYakushokuShainResponses", ignore = true)
    public abstract ShainResponse toResponse(ShainEntity entity);

    @AfterMapping
    protected void mapShain(ShainEntity entity, @MappingTarget ShainResponse shainResponse) {
        BumonResponse bumonResponse = mstBumonMapper.toResponse(entity.getMstBumonEntity());
        shainResponse.setMstBumonResponse(bumonResponse);
    }

    public ShainResponse toResponseMaster(ShainEntity entity) {
        ShainResponse shainResponse = toResponse(entity);
        if (CollectionUtil.isNotEmpty(entity.getMstRelYakushokuShainEntities())) {
            Set<MstRelYakushokuShainResponse> mstRelYakushokuShainResponses = mstRelYakushokuShainMapper.toResponses(entity.getMstRelYakushokuShainEntities());
            shainResponse.setMstRelYakushokuShainResponses(mstRelYakushokuShainResponses);
        }
        return shainResponse;
    }

    public abstract ShainEntity toEntity(ShainResponse shainResponse);

}
