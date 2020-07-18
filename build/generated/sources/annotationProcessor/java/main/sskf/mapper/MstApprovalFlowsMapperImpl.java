package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstApprovalFlowsEntity;
import sskf.model.response.MstApprovalFlowsResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstApprovalFlowsMapperImpl extends MstApprovalFlowsMapper {

    @Override
    public MstApprovalFlowsResponse toResponse(MstApprovalFlowsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstApprovalFlowsResponse mstApprovalFlowsResponse = new MstApprovalFlowsResponse();

        mstApprovalFlowsResponse.setCd( entity.getCd() );
        mstApprovalFlowsResponse.setName( entity.getName() );
        mstApprovalFlowsResponse.setIsDeleted( entity.getIsDeleted() );

        toResponse( mstApprovalFlowsResponse, entity );

        return mstApprovalFlowsResponse;
    }
}
