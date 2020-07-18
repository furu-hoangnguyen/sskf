package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.response.ApprovalFlowDetailsResponse;
import sskf.model.response.ApprovalPerson;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ApprovalFlowDetailsMapper {

    @Mapping(target = "bumonCd", source = "mstBumonEntity.bumonCd")
    @Mapping(target = "shainCd", source = "shainEntity.shainCd")
    public abstract ApprovalPerson toResponseApprovalPerson(ApprovalFlowDetailsEntity entity);

    public abstract ApprovalFlowDetailsResponse toResponse(ApprovalFlowDetailsEntity entity);

    @Mapping(target = "bumonCd", source = "entity.mstBumonEntity.bumonCd")
    @Mapping(target = "bumonNm", source = "entity.mstBumonEntity.bumonNm")
    public abstract ApprovalPerson toResponseApprovalPerson(ShainEntity entity, Byte stepNumber, Boolean isDeputy);
}
