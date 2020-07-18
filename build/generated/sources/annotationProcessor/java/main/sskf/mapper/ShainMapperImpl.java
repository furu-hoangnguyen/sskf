package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.ShainEntity;
import sskf.model.response.ShainResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class ShainMapperImpl extends ShainMapper {

    @Override
    public ShainResponse toResponse(ShainEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ShainResponse shainResponse = new ShainResponse();

        shainResponse.setShainCd( entity.getShainCd() );
        shainResponse.setShainCdNk( entity.getShainCdNk() );
        shainResponse.setShainNm( entity.getShainNm() );
        shainResponse.setPassword( entity.getPassword() );
        shainResponse.setIsAlertedForApplication( entity.getIsAlertedForApplication() );
        shainResponse.setIsAlertedForModificationRequest( entity.getIsAlertedForModificationRequest() );
        shainResponse.setIsAlertedForRejection( entity.getIsAlertedForRejection() );
        shainResponse.setIsAlertedForApproval( entity.getIsAlertedForApproval() );
        shainResponse.setIsAlertedForSendingRequestBack( entity.getIsAlertedForSendingRequestBack() );
        shainResponse.setIsAlertedForConfirmingSendRequestBack( entity.getIsAlertedForConfirmingSendRequestBack() );
        shainResponse.setIsAlertedForInputPaymentDate( entity.getIsAlertedForInputPaymentDate() );
        shainResponse.setIsAlertedForConfirmingSettlement( entity.getIsAlertedForConfirmingSettlement() );
        shainResponse.setIsAlertedForApplicationDeputy( entity.getIsAlertedForApplicationDeputy() );
        shainResponse.setIsAlertedForApprovalDeputy( entity.getIsAlertedForApprovalDeputy() );
        shainResponse.setIsAlertedForChangingCharge( entity.getIsAlertedForChangingCharge() );
        shainResponse.setIsAlertedForBeingCreated( entity.getIsAlertedForBeingCreated() );
        shainResponse.setIsAlertedForWaitingConfirmation( entity.getIsAlertedForWaitingConfirmation() );
        shainResponse.setIsAlertedForWaitingApplication( entity.getIsAlertedForWaitingApplication() );
        shainResponse.setIsAlertedForWaitingApproval( entity.getIsAlertedForWaitingApproval() );
        shainResponse.setIsAlertedForWaitingApplicationOnSendingBack( entity.getIsAlertedForWaitingApplicationOnSendingBack() );
        shainResponse.setIsAlertedForWaitingApprovalOnSendingBack( entity.getIsAlertedForWaitingApprovalOnSendingBack() );
        shainResponse.setIsAlertedForWaitingConfirmingSettlement( entity.getIsAlertedForWaitingConfirmingSettlement() );
        shainResponse.setIsAlertedForUpdatingDatabase( entity.getIsAlertedForUpdatingDatabase() );

        mapShain( entity, shainResponse );

        return shainResponse;
    }

    @Override
    public ShainEntity toEntity(ShainResponse shainResponse) {
        if ( shainResponse == null ) {
            return null;
        }

        ShainEntity shainEntity = new ShainEntity();

        shainEntity.setShainCd( shainResponse.getShainCd() );
        shainEntity.setShainCdNk( shainResponse.getShainCdNk() );
        shainEntity.setShainNm( shainResponse.getShainNm() );
        shainEntity.setPassword( shainResponse.getPassword() );
        shainEntity.setIsAlertedForApplication( shainResponse.getIsAlertedForApplication() );
        shainEntity.setIsAlertedForModificationRequest( shainResponse.getIsAlertedForModificationRequest() );
        shainEntity.setIsAlertedForRejection( shainResponse.getIsAlertedForRejection() );
        shainEntity.setIsAlertedForApproval( shainResponse.getIsAlertedForApproval() );
        shainEntity.setIsAlertedForSendingRequestBack( shainResponse.getIsAlertedForSendingRequestBack() );
        shainEntity.setIsAlertedForConfirmingSendRequestBack( shainResponse.getIsAlertedForConfirmingSendRequestBack() );
        shainEntity.setIsAlertedForInputPaymentDate( shainResponse.getIsAlertedForInputPaymentDate() );
        shainEntity.setIsAlertedForConfirmingSettlement( shainResponse.getIsAlertedForConfirmingSettlement() );
        shainEntity.setIsAlertedForApplicationDeputy( shainResponse.getIsAlertedForApplicationDeputy() );
        shainEntity.setIsAlertedForApprovalDeputy( shainResponse.getIsAlertedForApprovalDeputy() );
        shainEntity.setIsAlertedForChangingCharge( shainResponse.getIsAlertedForChangingCharge() );
        shainEntity.setIsAlertedForBeingCreated( shainResponse.getIsAlertedForBeingCreated() );
        shainEntity.setIsAlertedForWaitingConfirmation( shainResponse.getIsAlertedForWaitingConfirmation() );
        shainEntity.setIsAlertedForWaitingApplication( shainResponse.getIsAlertedForWaitingApplication() );
        shainEntity.setIsAlertedForWaitingApproval( shainResponse.getIsAlertedForWaitingApproval() );
        shainEntity.setIsAlertedForWaitingApplicationOnSendingBack( shainResponse.getIsAlertedForWaitingApplicationOnSendingBack() );
        shainEntity.setIsAlertedForWaitingApprovalOnSendingBack( shainResponse.getIsAlertedForWaitingApprovalOnSendingBack() );
        shainEntity.setIsAlertedForWaitingConfirmingSettlement( shainResponse.getIsAlertedForWaitingConfirmingSettlement() );
        shainEntity.setIsAlertedForUpdatingDatabase( shainResponse.getIsAlertedForUpdatingDatabase() );

        return shainEntity;
    }
}
