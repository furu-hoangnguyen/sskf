package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.response.ApprovalFlowDetailsResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class ApprovalFlowDetailsMapperImpl extends ApprovalFlowDetailsMapper {

    @Override
    public ApprovalFlowDetailsResponse toResponse(ApprovalFlowDetailsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ApprovalFlowDetailsResponse approvalFlowDetailsResponse = new ApprovalFlowDetailsResponse();

        approvalFlowDetailsResponse.setBumonNm( entity.getBumonNm() );
        approvalFlowDetailsResponse.setShainNm( entity.getShainNm() );

        return approvalFlowDetailsResponse;
    }
}
