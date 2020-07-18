package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import sskf.common.Constants;
import sskf.exception.SSKFException;
import sskf.mapper.RequestMannequinPromotionsMapper;
import sskf.model.EmailModel;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestModel;
import sskf.repository.RequestMannequinPromotionsRepository;
import sskf.repository.RequestsRepository;
import sskf.service.EmailServices;
import sskf.service.ImplementationStoresForRequestMannequinPromotionService;
import sskf.service.OperationHistoriesService;
import sskf.service.RequestMannequinPromotionDetailsService;
import sskf.service.RequestMannequinPromotionsService;
import sskf.service.RequestService;
import sskf.service.ShainAddressService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.Set;

@Service
@Slf4j
public class RequestMannequinPromotionsServiceImpl extends BaseServiceHasSearchRSQL<RequestMannequinPromotionsEntity> implements RequestMannequinPromotionsService {

    @Autowired
    private RequestMannequinPromotionsRepository requestMannequinPromotionsRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestMannequinPromotionsMapper requestMannequinPromotionsMapper;

    @Autowired
    private RequestMannequinPromotionDetailsService requestMannequinPromotionDetailsService;

    @Autowired
    private ImplementationStoresForRequestMannequinPromotionService implementationStoresForRequestMannequinPromotionService;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private ShainAddressService shainAddressService;

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private OperationHistoriesService operationHistoriesService;

    @Override
    @Transactional
    public MannequinPromotionsRequest create(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsServiceImpl create");
            RequestModel requestModel = mannequinPromotionsRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.create(requestModel, files, Constants.MANNEQUIN);

            RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = requestMannequinPromotionsMapper.toEntity(mannequinPromotionsRequest);
            requestMannequinPromotionsEntity.setRequestsEntity(requestsEntity);
            requestMannequinPromotionsEntity = requestMannequinPromotionsRepository.save(requestMannequinPromotionsEntity);

            requestMannequinPromotionDetailsService.setRequestMannequinPromotionDetailsForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
            implementationStoresForRequestMannequinPromotionService.setImplementationStoresForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);

            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
            }

            if (!mannequinPromotionsRequest.getRequestModel().getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(mannequinPromotionsRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatePromotion(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }

            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestMannequinPromotionsMapper.toResponseModel(requestMannequinPromotionsEntity);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsServiceImpl create");
        }
    }

    @Override
    @Transactional
    public MannequinPromotionsRequest updateCreate(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsServiceImpl updateCreate");
            if (ObjectUtils.isEmpty(mannequinPromotionsRequest.getCd())) {
                throw new SSKFException("400", "MannequinPromotionsRequest cd required!");
            }

            RequestModel requestModel = mannequinPromotionsRequest.getRequestModel();
            RequestsEntity requestsEntity = requestService.updateCreate(requestModel, files, Constants.MANNEQUIN);

            RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = requestMannequinPromotionsRepository.getOne(mannequinPromotionsRequest.getCd());
            requestMannequinPromotionsEntity = requestMannequinPromotionsMapper.toEntityUpdated(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
            requestMannequinPromotionsEntity.setRequestsEntity(requestsEntity);
            requestMannequinPromotionsEntity = requestMannequinPromotionsRepository.save(requestMannequinPromotionsEntity);

            requestMannequinPromotionDetailsService.setRequestMannequinPromotionDetailsForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
            implementationStoresForRequestMannequinPromotionService.setImplementationStoresForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);

            if (ObjectUtil.isNotEmpty(requestModel.getIsTemp()) && requestModel.getIsTemp()) {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成途中保存", null, false, null);
            } else {
                operationHistoriesService.setOperationHistoriesForRequests(requestsEntity, "作成", null, false, null);
            }
            if (ObjectUtil.isEmpty(requestModel.getIsTemp())) {
                throw new SSKFException("isTemp", "isTemp is required!");
            }
            if (!requestModel.getIsTemp()) {
                Set<String> emails = getEmailsWhenCompleteCreate(mannequinPromotionsRequest);
                if (CollectionUtil.isNotEmpty(emails)) {
                    EmailModel emailModel = new EmailModel(emails).buildEmailForCreatePromotion(requestsEntity.getCd());
                    emailServices.sendEmail(emailModel);
                }

            }
            requestService.uploadFileForRequests(files, requestsEntity, Boolean.FALSE);
            return requestMannequinPromotionsMapper.toResponseModel(requestMannequinPromotionsEntity);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsServiceImpl updateCreate");
        }
    }

    @Override
    @Transactional
    public MannequinPromotionsRequest update(MannequinPromotionsRequest mannequinPromotionsRequest, MultipartFile[] files) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsServiceImpl update");
            if (ObjectUtils.isEmpty(mannequinPromotionsRequest.getCd())) {
                throw new SSKFException("400", "MannequinPromotionsRequest cd required!");
            }

            RequestModel requestModel = mannequinPromotionsRequest.getRequestModel();
            requestModel.setRequestTypeName("マネキン");
            RequestsEntity requestsEntity = requestService.update(requestModel, files);
            RequestMannequinPromotionsEntity requestMannequinPromotionsEntity = requestMannequinPromotionsRepository.getOne(mannequinPromotionsRequest.getCd());
            requestMannequinPromotionsEntity = requestMannequinPromotionsMapper.toEntityUpdated(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
            requestMannequinPromotionsEntity.setRequestsEntity(requestsEntity);
            requestMannequinPromotionsEntity = requestMannequinPromotionsRepository.save(requestMannequinPromotionsEntity);

            requestMannequinPromotionDetailsService.setRequestMannequinPromotionDetailsForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);
            implementationStoresForRequestMannequinPromotionService.setImplementationStoresForMannequin(requestMannequinPromotionsEntity, mannequinPromotionsRequest);

            return requestMannequinPromotionsMapper.toResponseModel(requestMannequinPromotionsEntity);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsServiceImpl update");
        }
    }

    @Override
    public MannequinPromotionsRequest getByRequestId(Long requestId, String mode) {
        try {
            log.info("Begin service RequestMannequinPromotionsServiceImpl: getByRequestId");

            RequestsEntity requestsEntity = requestsRepository.findByCdAndIsDeleted(requestId, Constants.NOT_DELETED);
            if (ObjectUtils.isEmpty(requestsEntity)) {
                throw new SSKFException("404", "RequestsEntity not existed!");
            }
            if (CollectionUtil.isEmpty(requestsEntity.getRequestMannequinPromotionsEntities())) {
                throw new SSKFException("404", "mannequinPromotionsEntity not existed!");
            }

            if (Constants.EDIT.equals(mode)) {
                requestService.lockRequest(requestsEntity);
            }
            RequestMannequinPromotionsEntity mannequinPromotionsEntity = requestsEntity.getRequestMannequinPromotionsEntities().stream().findFirst().get();

            return  requestMannequinPromotionsMapper.toResponseModel(mannequinPromotionsEntity);
        } finally {
            log.info("End service RequestMannequinPromotionsServiceImpl: getByRequestId");
        }
    }

    private Set<String> getEmailsWhenCompleteCreate(MannequinPromotionsRequest mannequinPromotionsRequest) {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        String searchKey = "shainCd==" + mannequinPromotionsRequest.getRequestModel().getApprovalFlowDetailsRequest().getShainCd();
        baseSearchRequest.setKeyword(searchKey);
        return shainAddressService.listEmail(baseSearchRequest);
    }
}
