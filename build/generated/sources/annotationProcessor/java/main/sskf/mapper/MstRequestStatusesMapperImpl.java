package sskf.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.response.MstRequestStatusesResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstRequestStatusesMapperImpl extends MstRequestStatusesMapper {

    @Override
    public MstRequestStatusesResponse toResponse(MstRequestStatusesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstRequestStatusesResponse mstRequestStatusesResponse = new MstRequestStatusesResponse();

        mstRequestStatusesResponse.setCd( entity.getCd() );
        mstRequestStatusesResponse.setName( entity.getName() );

        return mstRequestStatusesResponse;
    }

    @Override
    public List<MstRequestStatusesResponse> toResponseList(List<MstRequestStatusesEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MstRequestStatusesResponse> list = new ArrayList<MstRequestStatusesResponse>( entities.size() );
        for ( MstRequestStatusesEntity mstRequestStatusesEntity : entities ) {
            list.add( toResponse( mstRequestStatusesEntity ) );
        }

        return list;
    }
}
