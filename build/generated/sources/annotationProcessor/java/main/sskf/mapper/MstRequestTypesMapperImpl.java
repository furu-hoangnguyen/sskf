package sskf.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.response.MstRequestTypesResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstRequestTypesMapperImpl extends MstRequestTypesMapper {

    @Override
    public MstRequestTypesResponse toResponse(MstRequestTypesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstRequestTypesResponse mstRequestTypesResponse = new MstRequestTypesResponse();

        if ( entity.getCd() != null ) {
            mstRequestTypesResponse.setCd( entity.getCd().intValue() );
        }
        mstRequestTypesResponse.setName( entity.getName() );
        if ( entity.getNumber() != null ) {
            mstRequestTypesResponse.setNumber( entity.getNumber() );
        }

        return mstRequestTypesResponse;
    }

    @Override
    public List<MstRequestTypesResponse> toResponseList(List<MstRequestTypesEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MstRequestTypesResponse> list = new ArrayList<MstRequestTypesResponse>( entities.size() );
        for ( MstRequestTypesEntity mstRequestTypesEntity : entities ) {
            list.add( toResponse( mstRequestTypesEntity ) );
        }

        return list;
    }
}
