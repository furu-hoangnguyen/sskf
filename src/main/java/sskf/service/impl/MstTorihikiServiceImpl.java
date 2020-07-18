package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sskf.mapper.MstTorihikiMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstTorihikiEntity;
import sskf.model.response.MstTorihikiRespone;
import sskf.repository.MstTorihikiRepository;
import sskf.service.MstTorihikiService;

import java.util.List;

@Service
@Slf4j
public class MstTorihikiServiceImpl extends BaseServiceHasSearchRSQL<MstTorihikiEntity> implements MstTorihikiService {

    @Autowired
    private MstTorihikiRepository mstTorihikiRepository;

    @Autowired
    private MstTorihikiMapper mstTorihikiMapper;

    @Override
    public List<String> listTorihikiRnmForSearch(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin service: listTorihikiRnmForSearch");
            List<String> mstTorihikiEntityList = mstTorihikiRepository.listTorihikiRnmForSearch();
            return mstTorihikiEntityList;
        } finally {
            log.info("Log end service: listTorihikiRnmForSearch)");
        }

    }

    @Override
    public Page<MstTorihikiRespone> listTorihiki(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin service: listTorihiki");
            Page<MstTorihikiEntity> mstTorihikiEntityList = searchRSQL(mstTorihikiRepository,
                    searchRequest, MstTorihikiEntity.class, "torihikiCd");
            return mstTorihikiEntityList.map(mstTorihikiMapper::toResponse);
        } finally {
            log.info("Log end service: listTorihiki");
        }
    }
}
