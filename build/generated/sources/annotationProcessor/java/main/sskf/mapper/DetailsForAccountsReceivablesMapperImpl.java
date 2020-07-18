package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.DetailsForAccountsReceivablesRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class DetailsForAccountsReceivablesMapperImpl extends DetailsForAccountsReceivablesMapper {

    @Override
    public DetailsForAccountsReceivablesEntity toEntity(DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest, RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, ShainEntity createdBy) {
        if ( detailsForAccountsReceivablesRequest == null && requestAccountsReceivablesEntity == null && createdBy == null ) {
            return null;
        }

        DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity = new DetailsForAccountsReceivablesEntity();

        if ( detailsForAccountsReceivablesRequest != null ) {
            detailsForAccountsReceivablesEntity.setHinmokuCd( detailsForAccountsReceivablesRequest.getHinmokuCd() );
            detailsForAccountsReceivablesEntity.setHinmokuRnm( detailsForAccountsReceivablesRequest.getHinmokuRnm() );
            detailsForAccountsReceivablesEntity.setYoryo( detailsForAccountsReceivablesRequest.getYoryo() );
            detailsForAccountsReceivablesEntity.setIrisu( detailsForAccountsReceivablesRequest.getIrisu() );
            detailsForAccountsReceivablesEntity.setInitialIrisu( detailsForAccountsReceivablesRequest.getInitialIrisu() );
            detailsForAccountsReceivablesEntity.setMfYen( detailsForAccountsReceivablesRequest.getMfYen() );
            detailsForAccountsReceivablesEntity.setInitialMfYen( detailsForAccountsReceivablesRequest.getInitialMfYen() );
            detailsForAccountsReceivablesEntity.setCommission( detailsForAccountsReceivablesRequest.getCommission() );
            detailsForAccountsReceivablesEntity.setInitialCommission( detailsForAccountsReceivablesRequest.getInitialCommission() );
            detailsForAccountsReceivablesEntity.setCSDiscount( detailsForAccountsReceivablesRequest.getCSDiscount() );
            detailsForAccountsReceivablesEntity.setInitialCSDiscount( detailsForAccountsReceivablesRequest.getInitialCSDiscount() );
            detailsForAccountsReceivablesEntity.setDenpyoNet( detailsForAccountsReceivablesRequest.getDenpyoNet() );
            detailsForAccountsReceivablesEntity.setInitialDenpyoNet( detailsForAccountsReceivablesRequest.getInitialDenpyoNet() );
            detailsForAccountsReceivablesEntity.setAccruedUnitPrice( detailsForAccountsReceivablesRequest.getAccruedUnitPrice() );
            detailsForAccountsReceivablesEntity.setInitialAccruedUnitPrice( detailsForAccountsReceivablesRequest.getInitialAccruedUnitPrice() );
            detailsForAccountsReceivablesEntity.setFinalTakeUnitPrice( detailsForAccountsReceivablesRequest.getFinalTakeUnitPrice() );
            detailsForAccountsReceivablesEntity.setInitialFinalTakeUnitPrice( detailsForAccountsReceivablesRequest.getInitialFinalTakeUnitPrice() );
            detailsForAccountsReceivablesEntity.setHyojyunYen( detailsForAccountsReceivablesRequest.getHyojyunYen() );
            detailsForAccountsReceivablesEntity.setInitialHyojyunYen( detailsForAccountsReceivablesRequest.getInitialHyojyunYen() );
            detailsForAccountsReceivablesEntity.setQuantityOfSoldItems( detailsForAccountsReceivablesRequest.getQuantityOfSoldItems() );
            detailsForAccountsReceivablesEntity.setInitialQuantityOfSoldItems( detailsForAccountsReceivablesRequest.getInitialQuantityOfSoldItems() );
            detailsForAccountsReceivablesEntity.setAccruedAmount( detailsForAccountsReceivablesRequest.getAccruedAmount() );
            detailsForAccountsReceivablesEntity.setInitialAccruedAmount( detailsForAccountsReceivablesRequest.getInitialAccruedAmount() );
            detailsForAccountsReceivablesEntity.setSalesAmount( detailsForAccountsReceivablesRequest.getSalesAmount() );
            detailsForAccountsReceivablesEntity.setInitialSalesAmount( detailsForAccountsReceivablesRequest.getInitialSalesAmount() );
            detailsForAccountsReceivablesEntity.setFinalMarginalProfit( detailsForAccountsReceivablesRequest.getFinalMarginalProfit() );
            detailsForAccountsReceivablesEntity.setInitialFinalMarginalProfit( detailsForAccountsReceivablesRequest.getInitialFinalMarginalProfit() );
            detailsForAccountsReceivablesEntity.setFinalMarginalProfitRatio( detailsForAccountsReceivablesRequest.getFinalMarginalProfitRatio() );
            detailsForAccountsReceivablesEntity.setInitialFinalMarginalProfitRatio( detailsForAccountsReceivablesRequest.getInitialFinalMarginalProfitRatio() );
        }

        toEntity( detailsForAccountsReceivablesEntity, detailsForAccountsReceivablesRequest, requestAccountsReceivablesEntity, createdBy );
        toEntityForUpdate( detailsForAccountsReceivablesEntity, detailsForAccountsReceivablesRequest );

        return detailsForAccountsReceivablesEntity;
    }

    @Override
    public DetailsForAccountsReceivablesEntity toEntityUpdated(DetailsForAccountsReceivablesEntity entity, DetailsForAccountsReceivablesRequest request) {
        if ( request == null ) {
            return null;
        }

        entity.setHinmokuCd( request.getHinmokuCd() );
        entity.setHinmokuRnm( request.getHinmokuRnm() );
        entity.setYoryo( request.getYoryo() );
        entity.setIrisu( request.getIrisu() );
        entity.setInitialIrisu( request.getInitialIrisu() );
        entity.setMfYen( request.getMfYen() );
        entity.setInitialMfYen( request.getInitialMfYen() );
        entity.setCommission( request.getCommission() );
        entity.setInitialCommission( request.getInitialCommission() );
        entity.setCSDiscount( request.getCSDiscount() );
        entity.setInitialCSDiscount( request.getInitialCSDiscount() );
        entity.setDenpyoNet( request.getDenpyoNet() );
        entity.setInitialDenpyoNet( request.getInitialDenpyoNet() );
        entity.setAccruedUnitPrice( request.getAccruedUnitPrice() );
        entity.setInitialAccruedUnitPrice( request.getInitialAccruedUnitPrice() );
        entity.setFinalTakeUnitPrice( request.getFinalTakeUnitPrice() );
        entity.setInitialFinalTakeUnitPrice( request.getInitialFinalTakeUnitPrice() );
        entity.setHyojyunYen( request.getHyojyunYen() );
        entity.setInitialHyojyunYen( request.getInitialHyojyunYen() );
        entity.setQuantityOfSoldItems( request.getQuantityOfSoldItems() );
        entity.setInitialQuantityOfSoldItems( request.getInitialQuantityOfSoldItems() );
        entity.setAccruedAmount( request.getAccruedAmount() );
        entity.setInitialAccruedAmount( request.getInitialAccruedAmount() );
        entity.setSalesAmount( request.getSalesAmount() );
        entity.setInitialSalesAmount( request.getInitialSalesAmount() );
        entity.setFinalMarginalProfit( request.getFinalMarginalProfit() );
        entity.setInitialFinalMarginalProfit( request.getInitialFinalMarginalProfit() );
        entity.setFinalMarginalProfitRatio( request.getFinalMarginalProfitRatio() );
        entity.setInitialFinalMarginalProfitRatio( request.getInitialFinalMarginalProfitRatio() );

        toEntityForUpdate( entity, request );

        return entity;
    }

    @Override
    public DetailsForAccountsReceivablesRequest toResponse(DetailsForAccountsReceivablesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest = new DetailsForAccountsReceivablesRequest();

        detailsForAccountsReceivablesRequest.setItemNumber( entityRequestAccountsReceivableDetailsEntityItemNumber( entity ) );
        detailsForAccountsReceivablesRequest.setIsDeleted( entityRequestAccountsReceivableDetailsEntityIsDeleted( entity ) );
        detailsForAccountsReceivablesRequest.setBumonNm( entityRequestAccountsReceivableDetailsEntityBumonNm( entity ) );
        detailsForAccountsReceivablesRequest.setBumonCd( entityRequestAccountsReceivableDetailsEntityMstBumonEntityBumonCd( entity ) );
        detailsForAccountsReceivablesRequest.setSortNumber( entityRequestAccountsReceivableDetailsEntitySortNumber( entity ) );
        detailsForAccountsReceivablesRequest.setStoreGNm( entityRequestAccountsReceivableDetailsEntityStoreGNm( entity ) );
        detailsForAccountsReceivablesRequest.setShainNm( entityRequestAccountsReceivableDetailsEntityShainNm( entity ) );
        detailsForAccountsReceivablesRequest.setIsChecked( entityRequestAccountsReceivableDetailsEntityIsChecked( entity ) );
        detailsForAccountsReceivablesRequest.setShainCd( entityRequestAccountsReceivableDetailsEntityShainEntityShainCd( entity ) );
        detailsForAccountsReceivablesRequest.setAccountReceivableDetailCd( entityRequestAccountsReceivableDetailsEntityCd( entity ) );
        detailsForAccountsReceivablesRequest.setCd( entity.getCd() );
        detailsForAccountsReceivablesRequest.setHinmokuCd( entity.getHinmokuCd() );
        detailsForAccountsReceivablesRequest.setHinmokuRnm( entity.getHinmokuRnm() );
        detailsForAccountsReceivablesRequest.setYoryo( entity.getYoryo() );
        detailsForAccountsReceivablesRequest.setIrisu( entity.getIrisu() );
        detailsForAccountsReceivablesRequest.setInitialIrisu( entity.getInitialIrisu() );
        detailsForAccountsReceivablesRequest.setMfYen( entity.getMfYen() );
        detailsForAccountsReceivablesRequest.setInitialMfYen( entity.getInitialMfYen() );
        detailsForAccountsReceivablesRequest.setCommission( entity.getCommission() );
        detailsForAccountsReceivablesRequest.setInitialCommission( entity.getInitialCommission() );
        detailsForAccountsReceivablesRequest.setCSDiscount( entity.getCSDiscount() );
        detailsForAccountsReceivablesRequest.setInitialCSDiscount( entity.getInitialCSDiscount() );
        detailsForAccountsReceivablesRequest.setDenpyoNet( entity.getDenpyoNet() );
        detailsForAccountsReceivablesRequest.setInitialDenpyoNet( entity.getInitialDenpyoNet() );
        detailsForAccountsReceivablesRequest.setAccruedUnitPrice( entity.getAccruedUnitPrice() );
        detailsForAccountsReceivablesRequest.setInitialAccruedUnitPrice( entity.getInitialAccruedUnitPrice() );
        detailsForAccountsReceivablesRequest.setFinalTakeUnitPrice( entity.getFinalTakeUnitPrice() );
        detailsForAccountsReceivablesRequest.setInitialFinalTakeUnitPrice( entity.getInitialFinalTakeUnitPrice() );
        detailsForAccountsReceivablesRequest.setHyojyunYen( entity.getHyojyunYen() );
        detailsForAccountsReceivablesRequest.setInitialHyojyunYen( entity.getInitialHyojyunYen() );
        detailsForAccountsReceivablesRequest.setQuantityOfSoldItems( entity.getQuantityOfSoldItems() );
        detailsForAccountsReceivablesRequest.setInitialQuantityOfSoldItems( entity.getInitialQuantityOfSoldItems() );
        detailsForAccountsReceivablesRequest.setAccruedAmount( entity.getAccruedAmount() );
        detailsForAccountsReceivablesRequest.setInitialAccruedAmount( entity.getInitialAccruedAmount() );
        detailsForAccountsReceivablesRequest.setSalesAmount( entity.getSalesAmount() );
        detailsForAccountsReceivablesRequest.setInitialSalesAmount( entity.getInitialSalesAmount() );
        detailsForAccountsReceivablesRequest.setFinalMarginalProfit( entity.getFinalMarginalProfit() );
        detailsForAccountsReceivablesRequest.setInitialFinalMarginalProfit( entity.getInitialFinalMarginalProfit() );
        detailsForAccountsReceivablesRequest.setFinalMarginalProfitRatio( entity.getFinalMarginalProfitRatio() );
        detailsForAccountsReceivablesRequest.setInitialFinalMarginalProfitRatio( entity.getInitialFinalMarginalProfitRatio() );

        toResponse( detailsForAccountsReceivablesRequest, entity );

        return detailsForAccountsReceivablesRequest;
    }

    private String entityRequestAccountsReceivableDetailsEntityItemNumber(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String itemNumber = requestAccountsReceivableDetailsEntity.getItemNumber();
        if ( itemNumber == null ) {
            return null;
        }
        return itemNumber;
    }

    private Byte entityRequestAccountsReceivableDetailsEntityIsDeleted(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Byte isDeleted = requestAccountsReceivableDetailsEntity.getIsDeleted();
        if ( isDeleted == null ) {
            return null;
        }
        return isDeleted;
    }

    private String entityRequestAccountsReceivableDetailsEntityBumonNm(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String bumonNm = requestAccountsReceivableDetailsEntity.getBumonNm();
        if ( bumonNm == null ) {
            return null;
        }
        return bumonNm;
    }

    private String entityRequestAccountsReceivableDetailsEntityMstBumonEntityBumonCd(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
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

    private Integer entityRequestAccountsReceivableDetailsEntitySortNumber(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Integer sortNumber = requestAccountsReceivableDetailsEntity.getSortNumber();
        if ( sortNumber == null ) {
            return null;
        }
        return sortNumber;
    }

    private String entityRequestAccountsReceivableDetailsEntityStoreGNm(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String storeGNm = requestAccountsReceivableDetailsEntity.getStoreGNm();
        if ( storeGNm == null ) {
            return null;
        }
        return storeGNm;
    }

    private String entityRequestAccountsReceivableDetailsEntityShainNm(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        String shainNm = requestAccountsReceivableDetailsEntity.getShainNm();
        if ( shainNm == null ) {
            return null;
        }
        return shainNm;
    }

    private Byte entityRequestAccountsReceivableDetailsEntityIsChecked(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
        if ( requestAccountsReceivableDetailsEntity == null ) {
            return null;
        }
        Byte isChecked = requestAccountsReceivableDetailsEntity.getIsChecked();
        if ( isChecked == null ) {
            return null;
        }
        return isChecked;
    }

    private String entityRequestAccountsReceivableDetailsEntityShainEntityShainCd(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
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

    private Long entityRequestAccountsReceivableDetailsEntityCd(DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity) {
        if ( detailsForAccountsReceivablesEntity == null ) {
            return null;
        }
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = detailsForAccountsReceivablesEntity.getRequestAccountsReceivableDetailsEntity();
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
