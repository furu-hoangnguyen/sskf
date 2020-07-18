package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstTantoEntity;
import sskf.model.response.MstTantoResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstTantoMapperImpl extends MstTantoMapper {

    @Override
    public MstTantoResponse toResponse(MstTantoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstTantoResponse mstTantoResponse = new MstTantoResponse();

        mstTantoResponse.setTantoCd( entity.getTantoCd() );
        mstTantoResponse.setTantoStatus( entity.getTantoStatus() );
        mstTantoResponse.setBatchUpdateDate( entity.getBatchUpdateDate() );

        toResponse( mstTantoResponse, entity );

        return mstTantoResponse;
    }
}
