package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.MstApprovalFlowsEntity;
import sskf.model.response.MstApprovalFlowsResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MstApprovalFlowsMapper {
    public abstract MstApprovalFlowsResponse toResponse(MstApprovalFlowsEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget MstApprovalFlowsResponse response, MstApprovalFlowsEntity entity) {
        if (entity.getMstBumonEntity() != null) {
            response.setBumonNm(entity.getMstBumonEntity().getBumonNm());
        }
    }
}
