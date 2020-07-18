package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ShainAddressEntity;
import sskf.repository.ShainAddressRepository;
import sskf.service.ShainAddressService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShainAddressServiceImpl extends BaseServiceHasSearchRSQL<ShainAddressEntity> implements ShainAddressService {

    @Autowired
    private ShainAddressRepository shainAddressRepository;

    public Set<String> listEmail(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Begin service ShainAddressServiceImpl: listEmail");
            List<ShainAddressEntity> shainAddressEntityList = listRSQL(shainAddressRepository, baseSearchRequest, ShainAddressEntity.class);
            Set<String> emails = shainAddressEntityList.stream().map(e ->e.getMailAddress()).collect(Collectors.toSet());
            return emails;
        } finally {
            log.info("End service ShainAddressServiceImpl: listEmail");
        }
    }

}
