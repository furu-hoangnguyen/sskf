package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstBumonEntity;
import sskf.model.response.BumonResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstBumonMapperImpl extends MstBumonMapper {

    @Override
    public BumonResponse toResponse(MstBumonEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BumonResponse bumonResponse = new BumonResponse();

        bumonResponse.setBumonCd( entity.getBumonCd() );
        bumonResponse.setBumonCdNk( entity.getBumonCdNk() );
        bumonResponse.setBumonNm( entity.getBumonNm() );
        bumonResponse.setBumonKnm( entity.getBumonKnm() );
        bumonResponse.setBumonRnm( entity.getBumonRnm() );

        return bumonResponse;
    }
}
