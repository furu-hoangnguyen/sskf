package sskf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sskf.model.entity.MstHinmokuEntity;
import sskf.model.response.MstHinmokuReponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MstHinmokuMapper {

    public abstract MstHinmokuReponse toResponse(MstHinmokuEntity entity);
}
