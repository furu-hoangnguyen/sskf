package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.DetailsForPromotionalExpensesEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.DetailsForPromotionalExpensesRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class DetailsForPromotionalExpensesMapperImpl extends DetailsForPromotionalExpensesMapper {

    @Override
    public DetailsForPromotionalExpensesRequest toResponse(DetailsForPromotionalExpensesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = new DetailsForPromotionalExpensesRequest();

        detailsForPromotionalExpensesRequest.setItemNumber( entityRequestAccountsReceivableDetailsEntityItemNumber( entity ) );
        detailsForPromotionalExpensesRequest.setBumonNm( entityRequestAccountsReceivableDetailsEntityBumonNm( entity ) );
        detailsForPromotionalExpensesRequest.setBumonCd( entityRequestAccountsReceivableDetailsEntityMstBumonEntityBumonCd( entity ) );
        detailsForPromotionalExpensesRequest.setSortNumber( entityRequestAccountsReceivableDetailsEntitySortNumber( entity ) );
        detailsForPromotionalExpensesRequest.setIsChecked( entityRequestAccountsReceivableDetailsEntityIsChecked( entity ) );
        detailsForPromotionalExpensesRequest.setIsDeleted( entityRequestAccountsReceivableDetailsEntityIsDeleted( entity ) );
        detailsForPromotionalExpensesRequest.setStoreGNm( entityRequestAccountsReceivableDetailsEntityStoreGNm( entity ) );
        detailsForPromotionalExpensesRequest.setShainNm( entityRequestAccountsReceivableDetailsEntityShainNm( entity ) );
        detailsForPromotionalExpensesRequest.setShainCd( entityRequestAccountsReceivableDetailsEntityShainEntityShainCd( entity ) );
        detailsForPromotionalExpensesRequest.setAccountReceivableDetailCd( entityRequestAccountsReceivableDetailsEntityCd( entity ) );
        detailsForPromotionalExpensesRequest.setCd( entity.getCd() );
        detailsForPromotionalExpensesRequest.setCategoryName( entity.getCategoryName() );
        detailsForPromotionalExpensesRequest.setHinmokuCd( entity.getHinmokuCd() );
        detailsForPromotionalExpensesRequest.setHinmokuRnm( entity.getHinmokuRnm() );
        detailsForPromotionalExpensesRequest.setNisugata( entity.getNisugata() );
        detailsForPromotionalExpensesRequest.setAccruedAmount( entity.getAccruedAmount() );
        detailsForPromotionalExpensesRequest.setInitialAccruedAmount( entity.getInitialAccruedAmount() );

        toResponse( detailsForPromotionalExpensesRequest, entity );

        return detailsForPromotionalExpensesRequest;
    }

    private String entityRequestAccountsReceivableDetailsEntityItemNumber(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
        if ( itemNumber == null ) {
            return null;
        }
        return itemNumber;
    }

    private String entityRequestAccountsReceivableDetailsEntityBumonNm(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String bumonNm = requestAccountsReceivableDetailsEntity.getBumonNm();
        if ( bumonNm == null ) {
            return null;
        }
        return bumonNm;
    }

    private String entityRequestAccountsReceivableDetailsEntityMstBumonEntityBumonCd(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        MstBumonEntity mstBumonEntity = requestAccountsReceivableDetailsEntity.getMstBumonEntity();
        if ( mstBumonEntity == null ) {
            return null;
        }
        String bumonCd = mstBumonEntity.getBumonCd();
        if ( bumonCd == null ) {
            return null;
        }
        return bumonCd;
    }

    private Integer entityRequestAccountsReceivableDetailsEntitySortNumber(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Integer sortNumber = requestAccountsReceivableDetailsEntity.getSortNumber();
        if ( sortNumber == null ) {
            return null;
        }
        return sortNumber;
    }

    private Byte entityRequestAccountsReceivableDetailsEntityIsChecked(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Byte isChecked = requestAccountsReceivableDetailsEntity.getIsChecked();
        if ( isChecked == null ) {
            return null;
        }
        return isChecked;
    }

    private Byte entityRequestAccountsReceivableDetailsEntityIsDeleted(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Byte isDeleted = requestAccountsReceivableDetailsEntity.getIsDeleted();
        if ( isDeleted == null ) {
            return null;
        }
        return isDeleted;
    }

    private String entityRequestAccountsReceivableDetailsEntityStoreGNm(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String storeGNm = requestAccountsReceivableDetailsEntity.getStoreGNm();
        if ( storeGNm == null ) {
            return null;
        }
        return storeGNm;
    }

    private String entityRequestAccountsReceivableDetailsEntityShainNm(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String shainNm = requestAccountsReceivableDetailsEntity.getShainNm();
        if ( shainNm == null ) {
            return null;
        }
        return shainNm;
    }

    private String entityRequestAccountsReceivableDetailsEntityShainEntityShainCd(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        ShainEntity shainEntity = requestAccountsReceivableDetailsEntity.getShainEntity();
        if ( shainEntity == null ) {
            return null;
        }
        String shainCd = shainEntity.getShainCd();
        if ( shainCd == null ) {
            return null;
        }
        return shainCd;
    }

    private Long entityRequestAccountsReceivableDetailsEntityCd(DetailsForPromotionalExpensesEntity detailsForPromotionalExpensesEntity) {
        if ( detailsForPromotionalExpensesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForPromotionalExpensesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Long cd = requestAccountsReceivableDetailsEntity.getCd();
        if ( cd == null ) {
            return null;
        }
        return cd;
    }
}
