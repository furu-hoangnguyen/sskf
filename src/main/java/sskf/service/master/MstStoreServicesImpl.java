package sskf.service.master;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sskf.mapper.MstStoreMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstStoreEntity;
import sskf.model.response.MstStoreResponse;
import sskf.repository.MstStoreRepository;
import sskf.service.MstStoreServices;
import sskf.service.impl.BaseServiceHasSearchRSQL;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MstStoreServicesImpl extends BaseServiceHasSearchRSQL<MstStoreEntity> implements MstStoreServices {
    @Autowired
    private MstStoreRepository mstStoreRepository;

    @Autowired
    private MstStoreMapper mstStoreMapper;

    @Override
    public Page<MstStoreResponse> getStores(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: getStores");
            Page<MstStoreEntity> entities = searchRSQL(mstStoreRepository,
                    searchRequest, MstStoreEntity.class, "storeCd");
            return entities.map(mstStoreMapper::toResponse);
        } finally {
            log.info("Log End service: getStores");
        }
    }

    @Override
    public List<MstStoreResponse> listStores(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: listStores");
            List<MstStoreEntity> entities = listRSQL(mstStoreRepository,
                    searchRequest, MstStoreEntity.class);
            return entities.stream().map(mstStoreMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log End service: listStores");
        }
    }
}
