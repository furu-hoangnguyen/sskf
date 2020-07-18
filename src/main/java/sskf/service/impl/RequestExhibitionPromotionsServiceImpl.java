package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.RequestExhibitionPromotionsMapper;
import sskf.model.EmailModel;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.RequestExhibitionPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.model.request.RequestModel;
import sskf.repository.RequestExhibitionPromotionsRepository;
import sskf.repository.RequestsRepository;
import sskf.service.EmailServices;
import sskf.service.HoldingsForRequestExhibitionPromotionService;
import sskf.service.OperationHistoriesService;
import sskf.service.RequestExhibitionPromotionsService;
import sskf.service.RequestService;
import sskf.service.ShainAddressService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.Set;

@Service
@Slf4j
public class RequestExhibitionPromotionsServiceImpl implements RequestExhibitionPromotionsService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestExhibitionPromotionsRepository requestExhibitionPromotionsRepository;

    @Autowired
    private RequestExhibitionPromotionsMapper requestExhibitionPromotionsMapper;

    @Autowired
    private HoldingsForRequestExhibitionPromotionService holdingsForRequestExhibitionPromotionService;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private ShainAddressService shainAddressService;

    @Autowired
    private OperationHistoriesService operationHistoriesService;
    @Override
    @Transactional
    public ExhibitionPromotionsRequest create(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsServiceImpl create");
            RequestModel requestModel = exhibitionPromotionsRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.create(requestModel, files, Constants.EXHIBITION);
            RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = requestExhibitionPromotionsMapper.toEntity(exhibitionPromotionsRequest);
            exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
            exhibitionPromotionsEntity = requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity);

            holdingsForRequestExhibitionPromotionService.setHoldingsForRequestExhibition(exhibitionPromotionsEntity, exhibitionPromotionsRequest);

            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
            }

            if (!exhibitionPromotionsRequest.getRequestModel().getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(exhibitionPromotionsRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatePromotion(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }

            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestExhibitionPromotionsMapper.toResponseModel(exhibitionPromotionsEntity);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsServiceImpl create");
        }
    }

    @Override
    @Transactional
    public ExhibitionPromotionsRequest updateCreate(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsServiceImpl updateCreate");
            if (ObjectUtils.isEmpty(exhibitionPromotionsRequest.getCd())) {
                throw new SSKFException("400", "exhibitionPromotionsRequest cd required!");
            }

            RequestModel requestModel = exhibitionPromotionsRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.updateCreate(requestModel, files, Constants.EXHIBITION);

            RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = requestExhibitionPromotionsRepository.getOne(exhibitionPromotionsRequest.getCd());
            if (ObjectUtil.isEmpty(exhibitionPromotionsEntity)) {
                throw new SSKFException("RequestExhibitionPromotionsEntity", "RequestExhibitionPromotionsEntity not found!");
            }
            exhibitionPromotionsEntity = requestExhibitionPromotionsMapper.toEntityUpdated(exhibitionPromotionsEntity, exhibitionPromotionsRequest);

            exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
            exhibitionPromotionsEntity = requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity);

            holdingsForRequestExhibitionPromotionService.setHoldingsForRequestExhibition(exhibitionPromotionsEntity, exhibitionPromotionsRequest);

            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
            }

            if (!exhibitionPromotionsRequest.getRequestModel().getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(exhibitionPromotionsRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatePromotion(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }

            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestExhibitionPromotionsMapper.toResponseModel(exhibitionPromotionsEntity);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsServiceImpl updateCreate");
        }
    }

    @Override
    @Transactional
    public ExhibitionPromotionsRequest update(ExhibitionPromotionsRequest exhibitionPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Begin service RequestExhibitionPromotionsServiceImpl: update");
            if (ObjectUtils.isEmpty(exhibitionPromotionsRequest.getCd())) {
                throw new SSKFException("400", "exhibitionPromotionsRequest cd required!");
            }
            RequestModel requestModel = exhibitionPromotionsRequest.getRequestModel();
            requestModel.setRequestTypeName("展示会・協賛");
            RequestsEntity requestsEntity = requestService.update(requestModel, files);
            RequestExhibitionPromotionsEntity exhibitionPromotionsEntity = requestExhibitionPromotionsRepository.getOne(exhibitionPromotionsRequest.getCd());
            if (ObjectUtil.isEmpty(exhibitionPromotionsEntity)) {
                throw new SSKFException("RequestExhibitionPromotionsEntity", "RequestExhibitionPromotionsEntity not found!");
            }
            exhibitionPromotionsEntity.setRequestsEntity(requestsEntity);
            exhibitionPromotionsEntity = requestExhibitionPromotionsMapper.toEntityUpdated(exhibitionPromotionsEntity, exhibitionPromotionsRequest);
            exhibitionPromotionsEntity = requestExhibitionPromotionsRepository.save(exhibitionPromotionsEntity);
            holdingsForRequestExhibitionPromotionService.setHoldingsForRequestExhibition(exhibitionPromotionsEntity, exhibitionPromotionsRequest);

            return requestExhibitionPromotionsMapper.toResponseModel(exhibitionPromotionsEntity);
        } finally {
            log.info("End service RequestExhibitionPromotionsServiceImpl: update");
        }
    }

    @Override
    public ExhibitionPromotionsRequest getByRequestId(Long requestCd, String mode) {
        try {
            log.info("Begin service RequestExhibitionPromotionsServiceImpl: getByRequestId");

            RequestsEntity requestsEntity = requestsRepository.findByCdAndIsDeleted(requestCd, Constants.NOT_DELETED);
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "RequestsEntity not existed!");
            }
            if (CollectionUtil.isEmpty(requestsEntity.getRequestExhibitionPromotionsEntities())) {
                throw new SSKFException("404", "RequestExhibitionPromotions not existed!");
            }

            if (Constants.EDIT.equals(mode)) {
                requestService.lockRequest(requestsEntity);
            }
            RequestExhibitionPromotionsEntity entity = requestsEntity.getRequestExhibitionPromotionsEntities().stream().findFirst().get();

            return  requestExhibitionPromotionsMapper.toResponseModel(entity);
        } finally {
            log.info("End service RequestExhibitionPromotionsServiceImpl: getByRequestId");
        }
    }

    private Set<String> getEmailsWhenCompleteCreate(ExhibitionPromotionsRequest exhibitionPromotionsRequest) {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String searchKey = "shainCd==" + exhibitionPromotionsRequest.getRequestModel().getApprovalFlowDetailsRequest().getShainCd();
        baseSearchRequest.setKeyword(searchKey);
        return shainAddressService.listEmail(baseSearchRequest);
    }
}
