package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstTorihikiEntity;
import sskf.model.response.MstTorihikiRespone;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstTorihikiMapperImpl extends MstTorihikiMapper {

    @Override
    public MstTorihikiRespone toResponse(MstTorihikiEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstTorihikiRespone mstTorihikiRespone = new MstTorihikiRespone();

        mstTorihikiRespone.setTorihikiCd( entity.getTorihikiCd() );
        mstTorihikiRespone.setTorihikiCdNk( entity.getTorihikiCdNk() );
        mstTorihikiRespone.setTorihiki1Nm( entity.getTorihiki1Nm() );
        mstTorihikiRespone.setTorihiki2Nm( entity.getTorihiki2Nm() );
        mstTorihikiRespone.setTorihikiRnm( entity.getTorihikiRnm() );
        mstTorihikiRespone.setTorihikiRknm( entity.getTorihikiRknm() );
        mstTorihikiRespone.setSeikyuFlg( entity.getSeikyuFlg() );
        mstTorihikiRespone.setChoaiKoriFlg( entity.getChoaiKoriFlg() );
        mstTorihikiRespone.setTorihikiStatus( entity.getTorihikiStatus() );
        mstTorihikiRespone.setBatchUpdateDate( entity.getBatchUpdateDate() );
        mstTorihikiRespone.setTorihikiRnmForSearch( entity.getTorihikiRnmForSearch() );
        mstTorihikiRespone.setTorihikiRknmForSearch( entity.getTorihikiRknmForSearch() );

        return mstTorihikiRespone;
    }
}
