package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sskf.mapper.MstHinmokuMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstHinmokuEntity;
import sskf.model.response.MstHinmokuReponse;
import sskf.repository.MstHinmokuRepository;
import sskf.service.MstHinmokuService;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class MstHinmokuServiceImpl extends BaseServiceHasSearchRSQL<MstHinmokuEntity> implements MstHinmokuService {

    @Autowired
    private MstHinmokuRepository mstHinmokuRepository;

    @Autowired
    private MstHinmokuMapper mstHinmokuMapper;

    @Override
    public Page<MstHinmokuReponse> listPage(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: listPage hinmoku");
            Page<MstHinmokuEntity> mstHinmokuEntityPage = searchRSQL(mstHinmokuRepository,
                    searchRequest, MstHinmokuEntity.class, "hinmokuCd");
            return mstHinmokuEntityPage.map(mstHinmokuMapper::toResponse);
        } finally {
            log.info("Log End service: listPage hinmoku");
        }
    }

    @Override
    public HashMap<String, MstHinmokuReponse> listMap(BaseSearchRequest searchRequest) {
        try {
            log.info("Begin service: list hinmoku");
            List<MstHinmokuEntity> mstHinmokuEntityList = listRSQL(mstHinmokuRepository,
                    searchRequest, MstHinmokuEntity.class);
            HashMap map = new HashMap<String, MstHinmokuEntity>();
            for (MstHinmokuEntity e : mstHinmokuEntityList) map.put(e.getHinmokuCd(), mstHinmokuMapper.toResponse(e));
            return map;
        } finally {
            log.info("Log End service: list hinmoku");
        }
    }
}
