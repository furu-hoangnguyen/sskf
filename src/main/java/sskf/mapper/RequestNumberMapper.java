package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.RequestNumbersEntity;
import sskf.model.response.RequestNumberResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestNumberMapper {
    public abstract RequestNumberResponse toResponse(RequestNumbersEntity entity);
}
