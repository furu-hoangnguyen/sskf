package sskf.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import sskf.common.enums.EnumPaymentMethod;
import sskf.model.FileModel;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestAttachmentsEntity;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.RequestModel;
import sskf.model.response.RequestResponse;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestMapper {

    @Autowired
    private RequestAccountsReceivablesMapper requestAccountsReceivablesMapper;

    @Autowired
    private RequestExhibitionPromotionsMapper requestExhibitionPromotionsMapper;

    @Autowired
    private RequestMannequinPromotionsMapper requestMannequinPromotionsMapper;

    @Autowired
    private ApprovalFlowDetailsMapper approvalFlowDetailsMapper;

    @Autowired
    private RequestAttachmentsMapper requestAttachmentsMapper;

    @Autowired
    private MstRequestStatusesMapper mstRequestStatusesMapper;

    @Autowired
    private RequestNumberMapper requestNumberMapper;

    public abstract List<RequestResponse> toResponseList(List<RequestsEntity> entities);

    @Mapping(target = "requestAccountsReceivablesResponse", ignore = true)
    @Mapping(target = "requestExhibitionPromotionsResponse", ignore = true)
    @Mapping(target = "requestMannequinPromotionsResponse", ignore = true)
    @Mapping(target = "approvalFlowDetailsResponse", ignore = true)
    @Mapping(source = "mstRequestTypesEntity", target = "mstRequestTypesResponse")
    @Mapping(source = "mstRequestStatusesEntity", target = "mstRequestStatusesResponse")
    public abstract RequestResponse toResponse(RequestsEntity entity);

    @AfterMapping
    protected void mapRequestAccountsReceivables(RequestsEntity entity, @MappingTarget RequestResponse requestResponse) {

        if (CollectionUtil.isNotEmpty(entity.getRequestAccountsReceivablesEntities())) {
            RequestAccountsReceivablesEntity requestAccountsReceivablesEntities = entity.getRequestAccountsReceivablesEntities().stream().findFirst().get();
            requestResponse.setRequestAccountsReceivablesResponse(requestAccountsReceivablesMapper.toRequestAccountsReceivablesResponse(requestAccountsReceivablesEntities));
        }

        if (CollectionUtil.isNotEmpty(entity.getRequestExhibitionPromotionsEntities())) {
            RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity = entity.getRequestExhibitionPromotionsEntities().stream().findFirst().get();
            requestResponse.setRequestExhibitionPromotionsResponse(requestExhibitionPromotionsMapper.toResponse(requestExhibitionPromotionsEntity));
        }

        if (CollectionUtil.isNotEmpty(entity.getRequestMannequinPromotionsEntities())) {
            RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = entity.getRequestMannequinPromotionsEntities().stream().findFirst().get();
            requestResponse.setRequestMannequinPromotionsResponse(requestMannequinPromotionsMapper.toResponse(requestMannequinPromotionsEntity));
        }

        if (CollectionUtil.isNotEmpty(entity.getApprovalFlowDetailsEntities())) {
            Optional<ApprovalFlowDetailsEntity> approvalFlowDetailsEntityOptional = entity.getApprovalFlowDetailsEntities().stream().filter(e -> e.getStepNumber() == 1 && !e.getIsDeputy() ).findFirst();

            if (approvalFlowDetailsEntityOptional.isPresent()) {
                ApprovalFlowDetailsEntity approvalFlowDetailsEntity = approvalFlowDetailsEntityOptional.get();
                requestResponse.setApprovalFlowDetailsResponse(approvalFlowDetailsMapper.toResponse(approvalFlowDetailsEntity));
            }
        }
    }

    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "isSentBack",defaultValue = "0")
    @Mapping(target = "startedEditAt", ignore = true)
    @Mapping(target = "paymentOn", ignore = true)
    @Mapping(target = "updatedAtAfterAccountingCheck", ignore = true)
    public abstract RequestsEntity toEntity(RequestModel requestModel);

    @AfterMapping
    public void setEnum(@MappingTarget RequestsEntity entity, RequestModel requestModel) {
        Byte paymentMethod = EnumPaymentMethod.getCodeByValue(requestModel.getPaymentMethod());
        entity.setPaymentMethod(paymentMethod);
        if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && !requestModel.getIsTemp()) {
            entity.setEditShainEntity(null);
            entity.setStartedEditAt(null);
            entity.setIsAlertedForRemind((byte) 0);
            entity.setUpdatedStatusAt(LocalDateTime.now());
        }
    }

    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "startedEditAt", ignore = true)
    @Mapping(target = "paymentOn", ignore = true)
    @Mapping(target = "updatedAtAfterAccountingCheck", ignore = true)
    public abstract RequestsEntity toEntityUpdated(@MappingTarget RequestsEntity entity, RequestModel requestModel);

    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "editShainName", source = "editShainEntity.shainNm")
    @Mapping(target = "editShainCd", source = "editShainEntity.shainCd")
    @Mapping(target = "startedEditAt", ignore = true)
    @Mapping(target = "mstApprovalFlows", source = "mstApprovalFlowsEntity")
    @Mapping(target = "requestTypeName", source = "entity.mstRequestTypesEntity.name")
    public abstract RequestModel toRequestModel(RequestsEntity entity);

    @AfterMapping
    public void toRequestModel(@MappingTarget RequestModel requestModel, RequestsEntity entity) {

        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntityList = entity.getApprovalFlowDetailsEntities().stream().collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(approvalFlowDetailsEntityList)) {
            for (ApprovalFlowDetailsEntity approvalFlowDetailsEntity : approvalFlowDetailsEntityList) {
                if (approvalFlowDetailsEntity.getStepNumber() == 1 && approvalFlowDetailsEntity.getIsDeputy().equals(false)) {
                    ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
                    MstBumonEntity bumon = approvalFlowDetailsEntity.getMstBumonEntity();
                    approvalFlowDetailsRequest.setBumonCd(bumon.getBumonCd());
                    approvalFlowDetailsRequest.setBumonNm(bumon.getBumonNm());
                    ShainEntity shain = approvalFlowDetailsEntity.getShainEntity();
                    approvalFlowDetailsRequest.setShainCd(shain.getShainCd());
                    approvalFlowDetailsRequest.setShainNm(shain.getShainNm());
                    requestModel.setApprovalFlowDetailsRequest(approvalFlowDetailsRequest);
                    break;
                }
            }
        }
        if (ObjectUtil.isNotEmpty(entity.getStartedEditAt())) {
            Long timeLocked = Timestamp.valueOf(entity.getStartedEditAt().plusHours(1)).getTime() - Timestamp.valueOf(LocalDateTime.now()).getTime();
            requestModel.setStartedEditAt(timeLocked);
        }

        String paymentMethod = EnumPaymentMethod.getValueByCode(entity.getPaymentMethod());
        requestModel.setPaymentMethod(paymentMethod);
        Set<RequestAttachmentsEntity> requestAttachmentsEntitiesSet = entity.getRequestAttachmentsEntities();
        if (CollectionUtil.isNotEmpty(requestAttachmentsEntitiesSet)) {
            List<FileModel> fileModelList = requestAttachmentsEntitiesSet.stream().map(requestAttachmentsMapper::toFileModel).collect(Collectors.toList());
            requestModel.setFileModelList(fileModelList);
        }
        if (CollectionUtil.isNotEmpty(entity.getRequestNumbersEntities())) {
            requestModel.setRequestNumberResponse(requestNumberMapper.toResponse(entity.getRequestNumbersEntities().stream().findFirst().get()));
        }
        requestModel.setMstRequestStatusesResponse(mstRequestStatusesMapper.toResponse(entity.getMstRequestStatusesEntity()));
    }
}
