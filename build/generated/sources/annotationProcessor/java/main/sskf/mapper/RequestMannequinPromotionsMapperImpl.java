package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.response.RequestMannequinPromotionsResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RequestMannequinPromotionsMapperImpl extends RequestMannequinPromotionsMapper {

    @Override
    public RequestMannequinPromotionsResponse toResponse(RequestMannequinPromotionsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestMannequinPromotionsResponse requestMannequinPromotionsResponse = new RequestMannequinPromotionsResponse();

        requestMannequinPromotionsResponse.setSubject( entity.getSubject() );

        return requestMannequinPromotionsResponse;
    }
}
