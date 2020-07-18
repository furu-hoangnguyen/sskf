package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;
import sskf.model.request.RequestMannequinPromotionDetailsRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestMannequinPromotionDetailsMapper {

    public abstract RequestMannequinPromotionDetailsEntity toEntity(RequestMannequinPromotionDetailsRequest requestMannequinPromotionDetailsRequest);

    public abstract RequestMannequinPromotionDetailsEntity toEntityUpdated(@MappingTarget RequestMannequinPromotionDetailsEntity entity, RequestMannequinPromotionDetailsRequest request);

    public abstract RequestMannequinPromotionDetailsRequest toModelRequest(RequestMannequinPromotionDetailsEntity entity);
}
