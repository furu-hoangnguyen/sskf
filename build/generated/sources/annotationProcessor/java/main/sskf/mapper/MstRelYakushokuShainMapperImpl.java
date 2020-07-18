package sskf.mapper;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstRelYakushokuShainEntity;
import sskf.model.response.MstRelYakushokuShainResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstRelYakushokuShainMapperImpl extends MstRelYakushokuShainMapper {

    @Override
    public Set<MstRelYakushokuShainResponse> toResponses(Set<MstRelYakushokuShainEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        Set<MstRelYakushokuShainResponse> set = new HashSet<MstRelYakushokuShainResponse>( Math.max( (int) ( entities.size() / .75f ) + 1, 16 ) );
        for ( MstRelYakushokuShainEntity mstRelYakushokuShainEntity : entities ) {
            set.add( toResponse( mstRelYakushokuShainEntity ) );
        }

        return set;
    }

    @Override
    public MstRelYakushokuShainResponse toResponse(MstRelYakushokuShainEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstRelYakushokuShainResponse mstRelYakushokuShainResponse = new MstRelYakushokuShainResponse();

        mapMstRelYakushokuShain( entity, mstRelYakushokuShainResponse );

        return mstRelYakushokuShainResponse;
    }
}
