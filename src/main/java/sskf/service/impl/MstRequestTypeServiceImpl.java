package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.mapper.MstRequestTypesMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstRequestTypesEntity;
import sskf.model.response.MstRequestTypesResponse;
import sskf.repository.MstRequestTypeRepository;
import sskf.service.MstRequestTypeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MstRequestTypeServiceImpl extends BaseServiceHasSearchRSQL<MstRequestTypesEntity> implements MstRequestTypeService {

    @Autowired
    private MstRequestTypeRepository mstRequestTypeRepository;

    @Autowired
    private MstRequestTypesMapper mstRequestTypesMapper;

    @Override
    public List<MstRequestTypesResponse> list(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin service: MstRequestTypeServiceImpl (list)");
            List<MstRequestTypesEntity> result = listRSQL(mstRequestTypeRepository,
                    searchRequest, MstRequestTypesEntity.class);
            return result.stream().map(mstRequestTypesMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("Log begin service: MstRequestTypeServiceImpl (list)");
        }
    }

}
