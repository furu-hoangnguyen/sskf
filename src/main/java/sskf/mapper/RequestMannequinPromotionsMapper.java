package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.model.entity.ImplementationStoresForRequestMannequinPromotionEntity;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ImplementationStoresForRequestMannequinPromotionRequest;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestMannequinPromotionDetailsRequest;
import sskf.model.request.RequestModel;
import sskf.model.response.RequestMannequinPromotionsResponse;
import sskf.service.OperationHistoriesService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestMannequinPromotionsMapper {

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private ImplementationStoresForRequestMannequinPromotionMapper implementationStoresMapper;

    @Autowired
    private RequestMannequinPromotionDetailsMapper requestMannequinPromotionDetailsMapper;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    public abstract RequestMannequinPromotionsResponse toResponse(RequestMannequinPromotionsEntity entity);

    public abstract MannequinPromotionsRequest toResponseModel(RequestMannequinPromotionsEntity entity);

    public abstract RequestMannequinPromotionsEntity toEntity(MannequinPromotionsRequest request);

    public abstract RequestMannequinPromotionsEntity toEntityUpdated(@MappingTarget RequestMannequinPromotionsEntity entity, MannequinPromotionsRequest request);

    @AfterMapping
    public void toResponse(@MappingTarget MannequinPromotionsRequest response, RequestMannequinPromotionsEntity entity) {

        RequestsEntity requestsEntity = entity.getRequestsEntity();
        if (ObjectUtil.isNotEmpty(requestsEntity)) {
            RequestModel requestModel = requestMapper.toRequestModel(requestsEntity);
            String createdBy = operationHistoriesService.getCreatedBy(requestModel.getCd());
            requestModel.setCreateBy(createdBy);
            response.setRequestModel(requestModel);
        }
        Set<ImplementationStoresForRequestMannequinPromotionEntity> implementationSet = entity.getImplementationStoresForRequestMannequinPromotionEntities();
        if(CollectionUtil.isNotEmpty(implementationSet)) {
            List<ImplementationStoresForRequestMannequinPromotionRequest> implementationList = implementationSet.stream().map(implementationStoresMapper::toModelRequest).collect(Collectors.toList());
            response.setImplementationStoresForRequestMannequinPromotionRequestList(implementationList);
        }

        Set<RequestMannequinPromotionDetailsEntity> requestMannequinPromotionDetailsEntitySet = entity.getRequestMannequinPromotionDetailsEntities();
        if(CollectionUtil.isNotEmpty(requestMannequinPromotionDetailsEntitySet)) {
            List<RequestMannequinPromotionDetailsRequest> implementationList = requestMannequinPromotionDetailsEntitySet.stream().map(requestMannequinPromotionDetailsMapper::toModelRequest).collect(Collectors.toList());
            response.setRequestMannequinPromotionDetailsRequestList(implementationList);
        }
    }

}
