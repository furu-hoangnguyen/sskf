package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.response.RequestAccountsReceivablesResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RequestAccountsReceivablesMapperImpl extends RequestAccountsReceivablesMapper {

    @Override
    public RequestAccountsReceivablesResponse toRequestAccountsReceivablesResponse(RequestAccountsReceivablesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestAccountsReceivablesResponse requestAccountsReceivablesResponse = new RequestAccountsReceivablesResponse();

        requestAccountsReceivablesResponse.setCd( entity.getCd() );
        requestAccountsReceivablesResponse.setTargetOn( entity.getTargetOn() );
        if ( entity.getPurpose() != null ) {
            requestAccountsReceivablesResponse.setPurpose( String.valueOf( entity.getPurpose() ) );
        }
        requestAccountsReceivablesResponse.setPurposeOfOthers( entity.getPurposeOfOthers() );
        requestAccountsReceivablesResponse.setCommissionType( entity.getCommissionType() );
        requestAccountsReceivablesResponse.setRemarks( entity.getRemarks() );

        return requestAccountsReceivablesResponse;
    }

    @Override
    public AccountReceivablesRequest toResponse(RequestAccountsReceivablesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AccountReceivablesRequest accountReceivablesRequest = new AccountReceivablesRequest();

        accountReceivablesRequest.setCd( entity.getCd() );
        accountReceivablesRequest.setTargetOn( entity.getTargetOn() );
        accountReceivablesRequest.setPurposeOfOthers( entity.getPurposeOfOthers() );
        accountReceivablesRequest.setRemarks( entity.getRemarks() );

        toResponse( accountReceivablesRequest, entity );

        return accountReceivablesRequest;
    }

    @Override
    public RequestAccountsReceivablesEntity toEntity(AccountReceivablesRequest receivablesRequest) {
        if ( receivablesRequest == null ) {
            return null;
        }

        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();

        requestAccountsReceivablesEntity.setTargetOn( receivablesRequest.getTargetOn() );
        requestAccountsReceivablesEntity.setPurposeOfOthers( receivablesRequest.getPurposeOfOthers() );
        requestAccountsReceivablesEntity.setRemarks( receivablesRequest.getRemarks() );

        setEnum( requestAccountsReceivablesEntity, receivablesRequest );

        return requestAccountsReceivablesEntity;
    }

    @Override
    public RequestAccountsReceivablesEntity toEntityUpdated(RequestAccountsReceivablesEntity entity, AccountReceivablesRequest receivablesRequest) {
        if ( receivablesRequest == null ) {
            return null;
        }

        entity.setTargetOn( receivablesRequest.getTargetOn() );
        entity.setPurposeOfOthers( receivablesRequest.getPurposeOfOthers() );
        entity.setRemarks( receivablesRequest.getRemarks() );

        setEnum( entity, receivablesRequest );

        return entity;
    }
}
