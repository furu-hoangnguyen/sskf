package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.model.entity.MstRequestCommentActionsEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.response.MstRequestCommentActionsResponse;
import sskf.model.response.OperationHistoriesOfMstCommentResponse;
import sskf.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MstRequestCommentActionsMapper {

    public abstract MstRequestCommentActionsResponse toResponse(MstRequestCommentActionsEntity entity);

}
