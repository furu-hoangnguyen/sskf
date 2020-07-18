package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import sskf.common.enums.EnumTypeCommission;
import sskf.common.enums.EnumTypeUse;
import sskf.exception.SSKFException;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.RequestModel;
import sskf.model.response.RequestAccountsReceivablesResponse;
import sskf.service.OperationHistoriesService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestAccountsReceivablesMapper {

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    public abstract RequestAccountsReceivablesResponse toRequestAccountsReceivablesResponse(RequestAccountsReceivablesEntity entity);

    @Mapping(target = "purpose", ignore = true)
    @Mapping(target = "commissionType", ignore = true)
    public abstract AccountReceivablesRequest toResponse(RequestAccountsReceivablesEntity entity);

    @AfterMapping
    public void toResponse(@MappingTarget AccountReceivablesRequest response, RequestAccountsReceivablesEntity entity) {

        RequestsEntity requestsEntity = entity.getRequestsEntity();
        if (!ObjectUtils.isEmpty(requestsEntity)) {
            RequestModel requestModel = requestMapper.toRequestModel(requestsEntity);
            String createdBy = operationHistoriesService.getCreatedBy(requestModel.getCd());
            requestModel.setCreateBy(createdBy);
            response.setRequestModel(requestModel);
        }

        String purpose = EnumTypeUse.getValueByCode(entity.getPurpose());
        response.setPurpose(purpose);

        String commissionType = EnumTypeCommission.getValueByCode(entity.getCommissionType());
        response.setCommissionType(commissionType);

    }

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cd", ignore = true)
    @Mapping(target = "requestsEntity", ignore = true)
    @Mapping(target = "requestAccountsReceivableDetailsEntities", ignore = true)
    @Mapping(target = "purpose", ignore = true)
    @Mapping(target = "commissionType", ignore = true)
    public abstract RequestAccountsReceivablesEntity toEntity(AccountReceivablesRequest receivablesRequest);

    @AfterMapping
    public void setEnum(@MappingTarget RequestAccountsReceivablesEntity entity, AccountReceivablesRequest receivablesRequest) {
        Byte purpose = EnumTypeUse.getCodeByValue(receivablesRequest.getPurpose());
        if (ObjectUtils.isEmpty(purpose)) {
            throw new SSKFException("400", "Purpose invalid!");
        }
        entity.setPurpose(purpose);
        Byte commission = EnumTypeCommission.getCodeByValue(receivablesRequest.getCommissionType());
        if (ObjectUtils.isEmpty(purpose)) {
            throw new SSKFException("400", "Commission invalid!");
        }
        entity.setCommissionType(commission);
    }

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cd", ignore = true)
    @Mapping(target = "requestsEntity", ignore = true)
    @Mapping(target = "requestAccountsReceivableDetailsEntities", ignore = true)
    @Mapping(target = "purpose", ignore = true)
    @Mapping(target = "commissionType", ignore = true)
    public abstract RequestAccountsReceivablesEntity toEntityUpdated(@MappingTarget RequestAccountsReceivablesEntity entity, AccountReceivablesRequest receivablesRequest);
}
