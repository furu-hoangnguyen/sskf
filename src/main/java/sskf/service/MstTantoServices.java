package sskf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sskf.mapper.MstTantoMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstTantoEntity;
import sskf.model.response.MstTantoResponse;
import sskf.repository.MstTantoRepository;
import sskf.service.impl.BaseServiceHasSearchRSQL;

@Service
@Slf4j
public class MstTantoServices extends BaseServiceHasSearchRSQL<MstTantoEntity> {
    @Autowired
    private MstTantoRepository mstTantoRepository;

    @Autowired
    private MstTantoMapper mstTantoMapper;

    public Page<MstTantoResponse> getTantos(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: list store");
            Page<MstTantoEntity> entities = searchRSQL(mstTantoRepository,
                    searchRequest, MstTantoEntity.class, "tantoCd");
            return entities.map(mstTantoMapper::toResponse);
        } finally {
            log.info("Log End service: list store");
        }
    }
}
