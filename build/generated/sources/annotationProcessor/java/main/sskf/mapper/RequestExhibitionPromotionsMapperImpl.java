package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.response.RequestExhibitionPromotionsResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RequestExhibitionPromotionsMapperImpl extends RequestExhibitionPromotionsMapper {

    @Override
    public RequestExhibitionPromotionsResponse toResponse(RequestExhibitionPromotionsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestExhibitionPromotionsResponse requestExhibitionPromotionsResponse = new RequestExhibitionPromotionsResponse();

        requestExhibitionPromotionsResponse.setSubject( entity.getSubject() );

        return requestExhibitionPromotionsResponse;
    }
}
