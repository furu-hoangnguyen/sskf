package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstHinmokuEntity;
import sskf.model.response.MstHinmokuReponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class MstHinmokuMapperImpl extends MstHinmokuMapper {

    @Override
    public MstHinmokuReponse toResponse(MstHinmokuEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MstHinmokuReponse mstHinmokuReponse = new MstHinmokuReponse();

        mstHinmokuReponse.setHinmokuCd( entity.getHinmokuCd() );
        mstHinmokuReponse.setHinmokuCdNk( entity.getHinmokuCdNk() );
        mstHinmokuReponse.setBrandKbn( entity.getBrandKbn() );
        mstHinmokuReponse.setHanKeitaiKbn( entity.getHanKeitaiKbn() );
        mstHinmokuReponse.setHinmokuKbn( entity.getHinmokuKbn() );
        mstHinmokuReponse.setJigyoCd( entity.getJigyoCd() );
        mstHinmokuReponse.setHinmokuKnm( entity.getHinmokuKnm() );
        mstHinmokuReponse.setHinmokuRnm( entity.getHinmokuRnm() );
        mstHinmokuReponse.setHinmokuKnmForSearch( entity.getHinmokuKnmForSearch() );
        mstHinmokuReponse.setHinmokuRnmForSearch( entity.getHinmokuRnmForSearch() );
        mstHinmokuReponse.setKikaku( entity.getKikaku() );
        mstHinmokuReponse.setNisugata( entity.getNisugata() );
        mstHinmokuReponse.setNisugataForSearch( entity.getNisugataForSearch() );
        mstHinmokuReponse.setIrisu( entity.getIrisu() );
        mstHinmokuReponse.setYoryoTani( entity.getYoryoTani() );
        mstHinmokuReponse.setMfYen( entity.getMfYen() );
        mstHinmokuReponse.setHyojyunYen( entity.getHyojyunYen() );
        mstHinmokuReponse.setMishuLimit( entity.getMishuLimit() );
        mstHinmokuReponse.setCategoryHinKbn( entity.getCategoryHinKbn() );
        mstHinmokuReponse.setCategoryHinsubKbn( entity.getCategoryHinsubKbn() );
        mstHinmokuReponse.setCategorySeriesKbn( entity.getCategorySeriesKbn() );
        mstHinmokuReponse.setBatchUpdateDateHinmoku( entity.getBatchUpdateDateHinmoku() );
        mstHinmokuReponse.setUpdateDateAw( entity.getUpdateDateAw() );
        mstHinmokuReponse.setUpdateDateMb( entity.getUpdateDateMb() );

        return mstHinmokuReponse;
    }
}
