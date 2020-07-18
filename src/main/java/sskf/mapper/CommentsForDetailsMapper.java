package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;
import sskf.util.ObjectUtil;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentsForDetailsMapper {

    @Mapping(target = "isCapableOfBeingDeleted", constant = "1")
    public abstract CommentsForDetailsEntity toEntity(CommentDetailsRequest commentDetailsRequest);

    public abstract CommentDetailResponse toResponse(CommentsForDetailsEntity entity);

    @AfterMapping
    public void setItemNumber(@MappingTarget CommentDetailResponse response, CommentsForDetailsEntity entity) {
        if (ObjectUtil.isNotEmpty(entity.getRequestAccountsReceivableDetailsEntity())) {
            response.setItemNumber(entity.getRequestAccountsReceivableDetailsEntity().getItemNumber());
        }
    }
}
