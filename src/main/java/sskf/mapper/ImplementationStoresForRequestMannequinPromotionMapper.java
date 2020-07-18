package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.ImplementationStoresForRequestMannequinPromotionEntity;
import sskf.model.request.ImplementationStoresForRequestMannequinPromotionRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ImplementationStoresForRequestMannequinPromotionMapper {
    public abstract ImplementationStoresForRequestMannequinPromotionEntity toEntity(ImplementationStoresForRequestMannequinPromotionRequest request);

    public abstract ImplementationStoresForRequestMannequinPromotionEntity toEntityUpdated(@MappingTarget ImplementationStoresForRequestMannequinPromotionEntity entity,
                                                                                          ImplementationStoresForRequestMannequinPromotionRequest request);

    public abstract ImplementationStoresForRequestMannequinPromotionRequest toModelRequest(ImplementationStoresForRequestMannequinPromotionEntity entity);
}
