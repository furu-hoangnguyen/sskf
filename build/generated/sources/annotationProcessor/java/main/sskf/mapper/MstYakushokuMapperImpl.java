package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstYakushokuEntity;
import sskf.model.response.MstYakushokuResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstYakushokuMapperImpl extends MstYakushokuMapper {

    @Override
    public MstYakushokuResponse toResponse(MstYakushokuEntity mstYakushokuEntity) {
        if ( mstYakushokuEntity == null ) {
            return null;
        }

        MstYakushokuResponse mstYakushokuResponse = new MstYakushokuResponse();

        mstYakushokuResponse.setCd( mstYakushokuEntity.getCd() );
        mstYakushokuResponse.setIsDeleted( mstYakushokuEntity.getIsDeleted() );
        mstYakushokuResponse.setName( mstYakushokuEntity.getName() );

        return mstYakushokuResponse;
    }
}
