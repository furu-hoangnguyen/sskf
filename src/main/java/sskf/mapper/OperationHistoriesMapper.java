package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.response.OperationHistoriesOfMstCommentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OperationHistoriesMapper {

    @Mapping(target = "actionComment", source = "mstRequestCommentActionsEntity.name")
    public abstract OperationHistoriesOfMstCommentResponse toResponseForMstCommentActions(OperationHistoriesEntity entity);

    @AfterMapping
    public void toResponseForMstCommentActions(@MappingTarget OperationHistoriesOfMstCommentResponse response, OperationHistoriesEntity entity) {
        if (response.getActionComment().equals("確認")) {
            List<OperationHistoriesOfMstCommentResponse> historiesOfMstCommentChild = entity.getChildrenOperationHistoriesEntities()
                    .stream().map(e -> toResponseForMstCommentActions(e)).collect(Collectors.toList());
            response.setHistoriesOfMstCommentChild(historiesOfMstCommentChild);
        }
    }
}
