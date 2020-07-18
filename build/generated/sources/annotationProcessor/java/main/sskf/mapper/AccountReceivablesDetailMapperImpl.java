package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.request.AccountReceivablesDetailRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class AccountReceivablesDetailMapperImpl extends AccountReceivablesDetailMapper {

    @Override
    public RequestAccountsReceivableDetailsEntity toEntity(AccountReceivablesDetailRequest accountReceivablesDetailRequest) {
        if ( accountReceivablesDetailRequest == null ) {
            return null;
        }

        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = new RequestAccountsReceivableDetailsEntity();

        requestAccountsReceivableDetailsEntity.setItemNumber( accountReceivablesDetailRequest.getItemNumber() );
        requestAccountsReceivableDetailsEntity.setStoreGNm( accountReceivablesDetailRequest.getStoreGNm() );
        requestAccountsReceivableDetailsEntity.setShainNm( accountReceivablesDetailRequest.getShainNm() );
        requestAccountsReceivableDetailsEntity.setBumonNm( accountReceivablesDetailRequest.getBumonNm() );
        if ( accountReceivablesDetailRequest.getIsChecked() != null ) {
            requestAccountsReceivableDetailsEntity.setIsChecked( accountReceivablesDetailRequest.getIsChecked() );
        }
        else {
            requestAccountsReceivableDetailsEntity.setIsChecked( (byte) 0 );
        }
        requestAccountsReceivableDetailsEntity.setIsDeleted( accountReceivablesDetailRequest.getIsDeleted() );
        requestAccountsReceivableDetailsEntity.setSortNumber( accountReceivablesDetailRequest.getSortNumber() );

        RequestAccountsReceivableDetailsEntity target = toEntity( requestAccountsReceivableDetailsEntity, accountReceivablesDetailRequest );
        if ( target != null ) {
            return target;
        }

        return requestAccountsReceivableDetailsEntity;
    }

    @Override
    public AccountReceivablesDetailRequest toResponse(RequestAccountsReceivableDetailsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AccountReceivablesDetailRequest accountReceivablesDetailRequest = new AccountReceivablesDetailRequest();

        accountReceivablesDetailRequest.setItemNumber( entity.getItemNumber() );
        accountReceivablesDetailRequest.setStoreGNm( entity.getStoreGNm() );
        accountReceivablesDetailRequest.setShainNm( entity.getShainNm() );
        accountReceivablesDetailRequest.setBumonNm( entity.getBumonNm() );
        accountReceivablesDetailRequest.setSortNumber( entity.getSortNumber() );
        accountReceivablesDetailRequest.setIsDeleted( entity.getIsDeleted() );
        accountReceivablesDetailRequest.setIsChecked( entity.getIsChecked() );

        toResponse( accountReceivablesDetailRequest, entity );

        return accountReceivablesDetailRequest;
    }

    @Override
    public RequestAccountsReceivableDetailsEntity toEntityUpdated(RequestAccountsReceivableDetailsEntity entity, AccountReceivablesDetailRequest accountReceivablesDetailRequest) {
        if ( accountReceivablesDetailRequest == null ) {
            return null;
        }

        entity.setItemNumber( accountReceivablesDetailRequest.getItemNumber() );
        entity.setStoreGNm( accountReceivablesDetailRequest.getStoreGNm() );
        entity.setShainNm( accountReceivablesDetailRequest.getShainNm() );
        entity.setBumonNm( accountReceivablesDetailRequest.getBumonNm() );
        if ( accountReceivablesDetailRequest.getIsChecked() != null ) {
            entity.setIsChecked( accountReceivablesDetailRequest.getIsChecked() );
        }
        else {
            entity.setIsChecked( (byte) 0 );
        }
        entity.setIsDeleted( accountReceivablesDetailRequest.getIsDeleted() );
        entity.setSortNumber( accountReceivablesDetailRequest.getSortNumber() );

        RequestAccountsReceivableDetailsEntity target = toEntity( entity, accountReceivablesDetailRequest );
        if ( target != null ) {
            return target;
        }

        return entity;
    }
}
