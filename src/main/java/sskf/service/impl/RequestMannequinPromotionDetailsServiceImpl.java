package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sskf.mapper.RequestMannequinPromotionDetailsMapper;
import sskf.model.entity.RequestMannequinPromotionDetailsEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.model.request.RequestMannequinPromotionDetailsRequest;
import sskf.repository.RequestMannequinPromotionDetailsRepository;
import sskf.service.RequestMannequinPromotionDetailsService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestMannequinPromotionDetailsServiceImpl implements RequestMannequinPromotionDetailsService {

    @Autowired
    private RequestMannequinPromotionDetailsRepository requestMannequinPromotionDetailsRepository;

    @Autowired
    private RequestMannequinPromotionDetailsMapper requestMannequinPromotionDetailsMapper;

    @Override
    @Transactional
    public void setRequestMannequinPromotionDetailsForMannequin(RequestMannequinPromotionsEntity requestMannequinPromotionsEntity, MannequinPromotionsRequest mannequinPromotionsRequest) {
        try {
            log.info("Begin service RequestMannequinPromotionDetailsServiceImpl: setRequestMannequinPromotionDetailsForMannequin");
            if (CollectionUtil.isNotEmpty(mannequinPromotionsRequest.getRequestMannequinPromotionDetailsRequestIsDeletedList())) {
                delete(mannequinPromotionsRequest.getRequestMannequinPromotionDetailsRequestIsDeletedList());
            }
            List<RequestMannequinPromotionDetailsRequest> requestMannequinPromotionDetailsRequestList = mannequinPromotionsRequest.getRequestMannequinPromotionDetailsRequestList();
            if (CollectionUtil.isNotEmpty(requestMannequinPromotionDetailsRequestList)) {
                HashMap<Long, RequestMannequinPromotionDetailsRequest> requestMannequinPromotionDetailsRequestListMap = requestMannequinPromotionDetailsRequestList.stream().collect(Collectors.toMap(RequestMannequinPromotionDetailsRequest::getCd, detailForAccount -> detailForAccount, (a, b) -> b, HashMap::new));
                List<Long> cdUpdated = new ArrayList<>();
                RequestMannequinPromotionDetailsEntity newEntity = null;
                List<RequestMannequinPromotionDetailsEntity> inserted = new ArrayList<>();
                for (RequestMannequinPromotionDetailsRequest detail : requestMannequinPromotionDetailsRequestList) {
                    if (ObjectUtil.isEmpty(detail.getCd())) {
                        newEntity = requestMannequinPromotionDetailsMapper.toEntity(detail);
                        newEntity.setRequestMannequinPromotionsEntity(requestMannequinPromotionsEntity);
                        inserted.add(newEntity);
                    } else {
                        cdUpdated.add(detail.getCd());
                    }
                }
                List<RequestMannequinPromotionDetailsEntity> updatedList = requestMannequinPromotionDetailsRepository.findAllById(cdUpdated);
                for (RequestMannequinPromotionDetailsEntity updated : updatedList) {
                    RequestMannequinPromotionDetailsRequest requestMannequinPromotionDetailsRequest = requestMannequinPromotionDetailsRequestListMap.get(updated.getCd());
                    requestMannequinPromotionDetailsMapper.toEntityUpdated(updated, requestMannequinPromotionDetailsRequest);
                }
                if (CollectionUtil.isNotEmpty(inserted)) {
                    inserted.addAll(updatedList);
                } else {
                    inserted = updatedList;
                }
                requestMannequinPromotionDetailsRepository.saveAll(inserted);
            }
        } finally {
            log.info("End service RequestMannequinPromotionDetailsServiceImpl: setRequestMannequinPromotionDetailsForMannequin");
        }
    }

    private void delete(List<Long> listCd) {
        List<RequestMannequinPromotionDetailsEntity> isDeleted = requestMannequinPromotionDetailsRepository.findAllById(listCd);
        requestMannequinPromotionDetailsRepository.deleteAll(isDeleted);
    }
}
