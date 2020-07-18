package sskf.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.MstApprovalFlowsEntity;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.RequestModel;
import sskf.model.response.MstApprovalFlowsResponse;
import sskf.model.response.MstRequestStatusesResponse;
import sskf.model.response.MstRequestTypesResponse;
import sskf.model.response.RequestResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RequestMapperImpl extends RequestMapper {

    @Override
    public List<RequestResponse> toResponseList(List<RequestsEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RequestResponse> list = new ArrayList<RequestResponse>( entities.size() );
        for ( RequestsEntity requestsEntity : entities ) {
            list.add( toResponse( requestsEntity ) );
        }

        return list;
    }

    @Override
    public RequestResponse toResponse(RequestsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestResponse requestResponse = new RequestResponse();

        requestResponse.setMstRequestTypesResponse( mstRequestTypesEntityToMstRequestTypesResponse( entity.getMstRequestTypesEntity() ) );
        requestResponse.setMstRequestStatusesResponse( mstRequestStatusesEntityToMstRequestStatusesResponse( entity.getMstRequestStatusesEntity() ) );
        if ( entity.getCd() != null ) {
            requestResponse.setCd( entity.getCd().intValue() );
        }
        requestResponse.setSettlementNumber( entity.getSettlementNumber() );
        requestResponse.setIsSentBack( entity.getIsSentBack() );
        requestResponse.setMstTorihikiCd( entity.getMstTorihikiCd() );
        requestResponse.setTorihikiNm( entity.getTorihikiNm() );
        if ( entity.getBillingAmount() != null ) {
            requestResponse.setBillingAmount( entity.getBillingAmount() );
        }
        if ( entity.getInitialBillingAmount() != null ) {
            requestResponse.setInitialBillingAmount( entity.getInitialBillingAmount() );
        }
        requestResponse.setBillingOn( entity.getBillingOn() );
        requestResponse.setPaymentPlace( entity.getPaymentPlace() );
        requestResponse.setPaymentOtherMethod( entity.getPaymentOtherMethod() );
        requestResponse.setScheduledPaymentOn( entity.getScheduledPaymentOn() );
        requestResponse.setPaymentOn( entity.getPaymentOn() );
        requestResponse.setPaymentDestination( entity.getPaymentDestination() );
        requestResponse.setBankName( entity.getBankName() );
        requestResponse.setItemTotalForEightPercent( entity.getItemTotalForEightPercent() );
        requestResponse.setInitialItemTotalForEightPercent( entity.getInitialItemTotalForEightPercent() );
        requestResponse.setItemTotalForTenPercent( entity.getItemTotalForTenPercent() );
        requestResponse.setInitialItemTotalForTenPercent( entity.getInitialItemTotalForTenPercent() );
        requestResponse.setConsumptionTaxTotalForEightPercent( entity.getConsumptionTaxTotalForEightPercent() );
        requestResponse.setInitialConsumptionTaxTotalForEightPercent( entity.getInitialConsumptionTaxTotalForEightPercent() );
        requestResponse.setConsumptionTaxTotalForTenPercent( entity.getConsumptionTaxTotalForTenPercent() );
        requestResponse.setInitialConsumptionTaxTotalForTenPercent( entity.getInitialConsumptionTaxTotalForTenPercent() );
        requestResponse.setItemTotal( entity.getItemTotal() );
        requestResponse.setInitialItemTotal( entity.getInitialItemTotal() );
        requestResponse.setConsumptionTaxTotal( entity.getConsumptionTaxTotal() );
        requestResponse.setInitialConsumptionTaxTotal( entity.getInitialConsumptionTaxTotal() );
        requestResponse.setTotalForEightPercent( entity.getTotalForEightPercent() );
        requestResponse.setInitialTotalForEightPercent( entity.getInitialTotalForEightPercent() );
        requestResponse.setTotalForTenPercent( entity.getTotalForTenPercent() );
        requestResponse.setInitialTotalForTenPercent( entity.getInitialTotalForTenPercent() );
        requestResponse.setTotal( entity.getTotal() );
        requestResponse.setInitialTotal( entity.getInitialTotal() );
        requestResponse.setUpdatedStatusAt( entity.getUpdatedStatusAt() );
        requestResponse.setRequestedAt( entity.getRequestedAt() );

        mapRequestAccountsReceivables( entity, requestResponse );

        return requestResponse;
    }

    @Override
    public RequestsEntity toEntity(RequestModel requestModel) {
        if ( requestModel == null ) {
            return null;
        }

        RequestsEntity requestsEntity = new RequestsEntity();

        requestsEntity.setCd( requestModel.getCd() );
        if ( requestModel.getIsSentBack() != null ) {
            requestsEntity.setIsSentBack( requestModel.getIsSentBack() );
        }
        else {
            requestsEntity.setIsSentBack( (byte) 0 );
        }
        requestsEntity.setMstTorihikiCd( requestModel.getMstTorihikiCd() );
        requestsEntity.setTorihikiNm( requestModel.getTorihikiNm() );
        requestsEntity.setBillingAmount( requestModel.getBillingAmount() );
        requestsEntity.setBillingOn( requestModel.getBillingOn() );
        requestsEntity.setPaymentPlace( requestModel.getPaymentPlace() );
        requestsEntity.setPaymentOtherMethod( requestModel.getPaymentOtherMethod() );
        requestsEntity.setScheduledPaymentOn( requestModel.getScheduledPaymentOn() );
        requestsEntity.setPaymentOn( requestModel.getPaymentOn() );
        requestsEntity.setPaymentDestination( requestModel.getPaymentDestination() );
        requestsEntity.setBankName( requestModel.getBankName() );
        requestsEntity.setBankAccountNumber( requestModel.getBankAccountNumber() );
        requestsEntity.setBankAccountName( requestModel.getBankAccountName() );
        requestsEntity.setItemTotalForEightPercent( requestModel.getItemTotalForEightPercent() );
        requestsEntity.setInitialItemTotalForEightPercent( requestModel.getInitialItemTotalForEightPercent() );
        requestsEntity.setItemTotalForTenPercent( requestModel.getItemTotalForTenPercent() );
        requestsEntity.setInitialItemTotalForTenPercent( requestModel.getInitialItemTotalForTenPercent() );
        requestsEntity.setConsumptionTaxTotalForEightPercent( requestModel.getConsumptionTaxTotalForEightPercent() );
        requestsEntity.setInitialConsumptionTaxTotalForEightPercent( requestModel.getInitialConsumptionTaxTotalForEightPercent() );
        requestsEntity.setConsumptionTaxTotalForTenPercent( requestModel.getConsumptionTaxTotalForTenPercent() );
        requestsEntity.setInitialConsumptionTaxTotalForTenPercent( requestModel.getInitialConsumptionTaxTotalForTenPercent() );
        requestsEntity.setItemTotal( requestModel.getItemTotal() );
        requestsEntity.setInitialItemTotal( requestModel.getInitialItemTotal() );
        requestsEntity.setConsumptionTaxTotal( requestModel.getConsumptionTaxTotal() );
        requestsEntity.setInitialConsumptionTaxTotal( requestModel.getInitialConsumptionTaxTotal() );
        requestsEntity.setTotalForEightPercent( requestModel.getTotalForEightPercent() );
        requestsEntity.setInitialTotalForEightPercent( requestModel.getInitialTotalForEightPercent() );
        requestsEntity.setTotalForTenPercent( requestModel.getTotalForTenPercent() );
        requestsEntity.setInitialTotalForTenPercent( requestModel.getInitialTotalForTenPercent() );
        requestsEntity.setTotal( requestModel.getTotal() );
        requestsEntity.setInitialTotal( requestModel.getInitialTotal() );
        requestsEntity.setRequestedAt( requestModel.getRequestedAt() );

        setEnum( requestsEntity, requestModel );

        return requestsEntity;
    }

    @Override
    public RequestsEntity toEntityUpdated(RequestsEntity entity, RequestModel requestModel) {
        if ( requestModel == null ) {
            return null;
        }

        entity.setCd( requestModel.getCd() );
        entity.setIsSentBack( requestModel.getIsSentBack() );
        entity.setMstTorihikiCd( requestModel.getMstTorihikiCd() );
        entity.setTorihikiNm( requestModel.getTorihikiNm() );
        entity.setBillingAmount( requestModel.getBillingAmount() );
        entity.setBillingOn( requestModel.getBillingOn() );
        entity.setPaymentPlace( requestModel.getPaymentPlace() );
        entity.setPaymentOtherMethod( requestModel.getPaymentOtherMethod() );
        entity.setScheduledPaymentOn( requestModel.getScheduledPaymentOn() );
        entity.setPaymentOn( requestModel.getPaymentOn() );
        entity.setPaymentDestination( requestModel.getPaymentDestination() );
        entity.setBankName( requestModel.getBankName() );
        entity.setBankAccountNumber( requestModel.getBankAccountNumber() );
        entity.setBankAccountName( requestModel.getBankAccountName() );
        entity.setItemTotalForEightPercent( requestModel.getItemTotalForEightPercent() );
        entity.setInitialItemTotalForEightPercent( requestModel.getInitialItemTotalForEightPercent() );
        entity.setItemTotalForTenPercent( requestModel.getItemTotalForTenPercent() );
        entity.setInitialItemTotalForTenPercent( requestModel.getInitialItemTotalForTenPercent() );
        entity.setConsumptionTaxTotalForEightPercent( requestModel.getConsumptionTaxTotalForEightPercent() );
        entity.setInitialConsumptionTaxTotalForEightPercent( requestModel.getInitialConsumptionTaxTotalForEightPercent() );
        entity.setConsumptionTaxTotalForTenPercent( requestModel.getConsumptionTaxTotalForTenPercent() );
        entity.setInitialConsumptionTaxTotalForTenPercent( requestModel.getInitialConsumptionTaxTotalForTenPercent() );
        entity.setItemTotal( requestModel.getItemTotal() );
        entity.setInitialItemTotal( requestModel.getInitialItemTotal() );
        entity.setConsumptionTaxTotal( requestModel.getConsumptionTaxTotal() );
        entity.setInitialConsumptionTaxTotal( requestModel.getInitialConsumptionTaxTotal() );
        entity.setTotalForEightPercent( requestModel.getTotalForEightPercent() );
        entity.setInitialTotalForEightPercent( requestModel.getInitialTotalForEightPercent() );
        entity.setTotalForTenPercent( requestModel.getTotalForTenPercent() );
        entity.setInitialTotalForTenPercent( requestModel.getInitialTotalForTenPercent() );
        entity.setTotal( requestModel.getTotal() );
        entity.setInitialTotal( requestModel.getInitialTotal() );
        entity.setRequestedAt( requestModel.getRequestedAt() );

        setEnum( entity, requestModel );

        return entity;
    }

    @Override
    public RequestModel toRequestModel(RequestsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestModel requestModel = new RequestModel();

        requestModel.setEditShainCd( entityEditShainEntityShainCd( entity ) );
        requestModel.setMstApprovalFlows( mstApprovalFlowsEntityToMstApprovalFlowsResponse( entity.getMstApprovalFlowsEntity() ) );
        requestModel.setEditShainName( entityEditShainEntityShainNm( entity ) );
        requestModel.setCd( entity.getCd() );
        requestModel.setMstTorihikiCd( entity.getMstTorihikiCd() );
        requestModel.setTorihikiNm( entity.getTorihikiNm() );
        requestModel.setIsSentBack( entity.getIsSentBack() );
        requestModel.setBillingAmount( entity.getBillingAmount() );
        requestModel.setBillingOn( entity.getBillingOn() );
        requestModel.setPaymentPlace( entity.getPaymentPlace() );
        requestModel.setPaymentOtherMethod( entity.getPaymentOtherMethod() );
        requestModel.setScheduledPaymentOn( entity.getScheduledPaymentOn() );
        requestModel.setPaymentOn( entity.getPaymentOn() );
        requestModel.setPaymentDestination( entity.getPaymentDestination() );
        requestModel.setBankName( entity.getBankName() );
        requestModel.setBankAccountNumber( entity.getBankAccountNumber() );
        requestModel.setBankAccountName( entity.getBankAccountName() );
        requestModel.setItemTotalForEightPercent( entity.getItemTotalForEightPercent() );
        requestModel.setInitialItemTotalForEightPercent( entity.getInitialItemTotalForEightPercent() );
        requestModel.setItemTotalForTenPercent( entity.getItemTotalForTenPercent() );
        requestModel.setInitialItemTotalForTenPercent( entity.getInitialItemTotalForTenPercent() );
        requestModel.setConsumptionTaxTotalForEightPercent( entity.getConsumptionTaxTotalForEightPercent() );
        requestModel.setInitialConsumptionTaxTotalForEightPercent( entity.getInitialConsumptionTaxTotalForEightPercent() );
        requestModel.setConsumptionTaxTotalForTenPercent( entity.getConsumptionTaxTotalForTenPercent() );
        requestModel.setInitialConsumptionTaxTotalForTenPercent( entity.getInitialConsumptionTaxTotalForTenPercent() );
        requestModel.setItemTotal( entity.getItemTotal() );
        requestModel.setInitialItemTotal( entity.getInitialItemTotal() );
        requestModel.setConsumptionTaxTotal( entity.getConsumptionTaxTotal() );
        requestModel.setInitialConsumptionTaxTotal( entity.getInitialConsumptionTaxTotal() );
        requestModel.setTotalForEightPercent( entity.getTotalForEightPercent() );
        requestModel.setInitialTotalForEightPercent( entity.getInitialTotalForEightPercent() );
        requestModel.setTotalForTenPercent( entity.getTotalForTenPercent() );
        requestModel.setInitialTotalForTenPercent( entity.getInitialTotalForTenPercent() );
        requestModel.setTotal( entity.getTotal() );
        requestModel.setInitialTotal( entity.getInitialTotal() );
        requestModel.setRequestedAt( entity.getRequestedAt() );

        toRequestModel( requestModel, entity );

        return requestModel;
    }

    protected MstRequestTypesResponse mstRequestTypesEntityToMstRequestTypesResponse(MstRequestTypesEntity mstRequestTypesEntity) {
        if ( mstRequestTypesEntity == null ) {
            return null;
        }

        MstRequestTypesResponse mstRequestTypesResponse = new MstRequestTypesResponse();

        if ( mstRequestTypesEntity.getCd() != null ) {
            mstRequestTypesResponse.setCd( mstRequestTypesEntity.getCd().intValue() );
        }
        mstRequestTypesResponse.setName( mstRequestTypesEntity.getName() );
        if ( mstRequestTypesEntity.getNumber() != null ) {
            mstRequestTypesResponse.setNumber( mstRequestTypesEntity.getNumber() );
        }

        return mstRequestTypesResponse;
    }

    protected MstRequestStatusesResponse mstRequestStatusesEntityToMstRequestStatusesResponse(MstRequestStatusesEntity mstRequestStatusesEntity) {
        if ( mstRequestStatusesEntity == null ) {
            return null;
        }

        MstRequestStatusesResponse mstRequestStatusesResponse = new MstRequestStatusesResponse();

        mstRequestStatusesResponse.setCd( mstRequestStatusesEntity.getCd() );
        mstRequestStatusesResponse.setName( mstRequestStatusesEntity.getName() );

        return mstRequestStatusesResponse;
    }

    private String entityEditShainEntityShainCd(RequestsEntity requestsEntity) {
        if ( requestsEntity == null ) {
            return null;
        }
        ShainEntity editShainEntity = requestsEntity.getEditShainEntity();
        if ( editShainEntity == null ) {
            return null;
        }
        String shainCd = editShainEntity.getShainCd();
        if ( shainCd == null ) {
            return null;
        }
        return shainCd;
    }

    protected MstApprovalFlowsResponse mstApprovalFlowsEntityToMstApprovalFlowsResponse(MstApprovalFlowsEntity mstApprovalFlowsEntity) {
        if ( mstApprovalFlowsEntity == null ) {
            return null;
        }

        MstApprovalFlowsResponse mstApprovalFlowsResponse = new MstApprovalFlowsResponse();

        mstApprovalFlowsResponse.setCd( mstApprovalFlowsEntity.getCd() );
        mstApprovalFlowsResponse.setName( mstApprovalFlowsEntity.getName() );
        mstApprovalFlowsResponse.setIsDeleted( mstApprovalFlowsEntity.getIsDeleted() );

        return mstApprovalFlowsResponse;
    }

    private String entityEditShainEntityShainNm(RequestsEntity requestsEntity) {
        if ( requestsEntity == null ) {
            return null;
        }
        ShainEntity editShainEntity = requestsEntity.getEditShainEntity();
        if ( editShainEntity == null ) {
            return null;
        }
        String shainNm = editShainEntity.getShainNm();
        if ( shainNm == null ) {
            return null;
        }
        return shainNm;
    }
}
