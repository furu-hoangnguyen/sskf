package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.HoldingsForRequestExhibitionPromotionEntity;
import sskf.model.request.HoldingsForRequestExhibitionPromotionRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class HoldingsForRequestExhibitionPromotionMapper {

    public abstract HoldingsForRequestExhibitionPromotionEntity toEntity(HoldingsForRequestExhibitionPromotionRequest request);

    public abstract HoldingsForRequestExhibitionPromotionEntity toEntityUpdated(@MappingTarget HoldingsForRequestExhibitionPromotionEntity entity,
                                                                                HoldingsForRequestExhibitionPromotionRequest request);

    public abstract HoldingsForRequestExhibitionPromotionRequest toModelRequest(HoldingsForRequestExhibitionPromotionEntity entity);
}
