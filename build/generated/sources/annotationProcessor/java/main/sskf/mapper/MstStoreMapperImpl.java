package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstStoreEntity;
import sskf.model.response.MstStoreResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstStoreMapperImpl extends MstStoreMapper {

    @Override
    public MstStoreResponse toResponse(MstStoreEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstStoreResponse mstStoreResponse = new MstStoreResponse();

        mstStoreResponse.setStoreCd( entity.getStoreCd() );
        mstStoreResponse.setStoreGCd( entity.getStoreGCd() );
        mstStoreResponse.setStoreGNm( entity.getStoreGNm() );
        mstStoreResponse.setUpdateDateAw( entity.getUpdateDateAw() );
        mstStoreResponse.setUpdateDateMb( entity.getUpdateDateMb() );

        toResponse( mstStoreResponse, entity );

        return mstStoreResponse;
    }
}
