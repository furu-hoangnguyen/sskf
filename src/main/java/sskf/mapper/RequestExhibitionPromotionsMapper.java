package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.model.entity.HoldingsForRequestExhibitionPromotionEntity;
import sskf.model.entity.ImplementationStoresForRequestMannequinPromotionEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.model.request.HoldingsForRequestExhibitionPromotionRequest;
import sskf.model.request.ImplementationStoresForRequestMannequinPromotionRequest;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestMannequinPromotionDetailsRequest;
import sskf.model.request.RequestModel;
import sskf.model.response.RequestExhibitionPromotionsResponse;
import sskf.service.OperationHistoriesService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestExhibitionPromotionsMapper {

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private HoldingsForRequestExhibitionPromotionMapper holdingsMapper;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    public abstract RequestExhibitionPromotionsResponse toResponse(RequestExhibitionPromotionsEntity entity);

    public abstract RequestExhibitionPromotionsEntity toEntity(ExhibitionPromotionsRequest request);

    public abstract RequestExhibitionPromotionsEntity toEntityUpdated(@MappingTarget RequestExhibitionPromotionsEntity entity, ExhibitionPromotionsRequest request);

    public abstract ExhibitionPromotionsRequest toResponseModel(RequestExhibitionPromotionsEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget ExhibitionPromotionsRequest response, RequestExhibitionPromotionsEntity entity) {

        RequestsEntity requestsEntity = entity.getRequestsEntity();
        if (ObjectUtil.isNotEmpty(requestsEntity)) {
            RequestModel requestModel = requestMapper.toRequestModel(requestsEntity);
            String createdBy = operationHistoriesService.getCreatedBy(requestModel.getCd());
            requestModel.setCreateBy(createdBy);
            response.setRequestModel(requestModel);

        }
        Set<HoldingsForRequestExhibitionPromotionEntity> holdings = entity.getHoldingsForRequestExhibitionPromotionEntities();
        if(CollectionUtil.isNotEmpty(holdings)) {
            List<HoldingsForRequestExhibitionPromotionRequest> holdingsList = holdings.stream().map(holdingsMapper::toModelRequest).collect(Collectors.toList());
            response.setHoldingsForRequestExhibitionPromotionRequestList(holdingsList);
        }

    }
}
