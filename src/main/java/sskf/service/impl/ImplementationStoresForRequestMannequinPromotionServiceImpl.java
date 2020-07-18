package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sskf.mapper.ImplementationStoresForRequestMannequinPromotionMapper;
import sskf.model.entity.ImplementationStoresForRequestMannequinPromotionEntity;
import sskf.model.entity.RequestMannequinPromotionsEntity;
import sskf.model.request.ImplementationStoresForRequestMannequinPromotionRequest;
import sskf.model.request.MannequinPromotionsRequest;
import sskf.repository.ImplementationStoresForRequestMannequinPromotionRepository;
import sskf.service.ImplementationStoresForRequestMannequinPromotionService;
import sskf.util.CollectionUtil;
import sskf.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImplementationStoresForRequestMannequinPromotionServiceImpl implements ImplementationStoresForRequestMannequinPromotionService {

    @Autowired
    private ImplementationStoresForRequestMannequinPromotionRepository repository;

    @Autowired
    private ImplementationStoresForRequestMannequinPromotionMapper mapper;

    @Override
    @Transactional
    public void setImplementationStoresForMannequin(RequestMannequinPromotionsEntity requestMannequinPromotionsEntity, MannequinPromotionsRequest mannequinPromotionsRequest) {
        try {
            log.info("Begin service ImplementationStoresForRequestMannequinPromotionServiceImpl: setRequestMannequinPromotionDetailsForMannequin");
            if (CollectionUtil.isNotEmpty(mannequinPromotionsRequest.getImplementationStoresForRequestMannequinPromotionRequestIsDeletedList())) {
                delete(mannequinPromotionsRequest.getImplementationStoresForRequestMannequinPromotionRequestIsDeletedList());
            }
            List<ImplementationStoresForRequestMannequinPromotionRequest> implementationList = mannequinPromotionsRequest.getImplementationStoresForRequestMannequinPromotionRequestList();
            if (CollectionUtil.isNotEmpty(implementationList)) {
                HashMap<Long, ImplementationStoresForRequestMannequinPromotionRequest> implementMap = implementationList.stream().collect(Collectors.toMap(ImplementationStoresForRequestMannequinPromotionRequest::getCd, detailForAccount -> detailForAccount, (a, b) -> b, HashMap::new));
                List<Long> cdUpdated = new ArrayList<>();
                ImplementationStoresForRequestMannequinPromotionEntity newEntity = null;
                List<ImplementationStoresForRequestMannequinPromotionEntity> inserted = new ArrayList<>();
                for (ImplementationStoresForRequestMannequinPromotionRequest detail : implementationList) {
                    if (ObjectUtil.isEmpty(detail.getCd())) {
                        newEntity = mapper.toEntity(detail);
                        newEntity.setRequestMannequinPromotionsEntity(requestMannequinPromotionsEntity);
                        inserted.add(newEntity);
                    } else {
                        cdUpdated.add(detail.getCd());
                    }
                }
                List<ImplementationStoresForRequestMannequinPromotionEntity> updateds = repository.findAllById(cdUpdated);
                for (ImplementationStoresForRequestMannequinPromotionEntity updated : updateds) {
                    ImplementationStoresForRequestMannequinPromotionRequest requestUpdated = implementMap.get(updated.getCd());
                    mapper.toEntityUpdated(updated, requestUpdated);
                }
                if (CollectionUtil.isNotEmpty(inserted)) {
                    inserted.addAll(updateds);
                } else {
                    inserted = updateds;
                }
                repository.saveAll(inserted);
            }
        } finally {
            log.info("End service ImplementationStoresForRequestMannequinPromotionServiceImpl: setRequestMannequinPromotionDetailsForMannequin");
        }
    }

    private void delete(List<Long> listCd) {
        List<ImplementationStoresForRequestMannequinPromotionEntity> isDeleted = repository.findAllById(listCd);
        repository.deleteAll(isDeleted);
    }
}
