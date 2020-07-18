package sskf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sskf.common.Constants;
import sskf.config.jwt.JwtTokenUtil;
import sskf.mapper.MstRelYakushokuShainMapper;
import sskf.mapper.ShainMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ShainEntity;
import sskf.model.response.MstRelYakushokuShainResponse;
import sskf.model.response.ShainResponse;
import sskf.repository.ApprovalFlowDetailsRepository;
import sskf.repository.ShainRepository;
import sskf.service.ShainService;
import sskf.util.CollectionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShainServiceImpl extends BaseServiceHasSearchRSQL<ShainEntity> implements ShainService {

    @Autowired
    private ShainRepository shainRepository;

    @Autowired
    private ShainMapper shainMapper;

    @Autowired
    HttpServletRequest request;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;

    @Autowired
    private MstRelYakushokuShainMapper mstRelYakushokuShainMapper;

    @Override
    public ShainResponse getInformation() {
        try {
            log.info("Begin service: getInformation");
            ShainEntity shainEntity = shainRepository.findByShainCd(getUserNameByToken());
            ShainResponse shainResponse = shainMapper.toResponse(shainEntity);

            if (CollectionUtil.isNotEmpty(shainEntity.getMstRelYakushokuShainEntities())) {
                Set<MstRelYakushokuShainResponse> mstRelYakushokuShainResponses = mstRelYakushokuShainMapper.toResponses(shainEntity.getMstRelYakushokuShainEntities());
                shainResponse.setMstRelYakushokuShainResponses(mstRelYakushokuShainResponses);
            }
            return shainResponse;
        } finally {
            log.info("End service: getInformation");
        }
    }

    @Override
    public Page<ShainResponse> listShains(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Begin service: listPageShain");
            Page<ShainEntity> result = searchRSQL(shainRepository, baseSearchRequest, ShainEntity.class, "shainCd");
            return result.map(shainMapper::toResponse);
        } finally {
            log.info("End service: listPageShain");
        }
    }

    @Override
    public Page<ShainResponse> listShainsMaster(BaseSearchRequest baseSearchRequest) {
        Page<ShainEntity> result = searchRSQL(shainRepository, baseSearchRequest, ShainEntity.class, "shainCd");
        return result.map(shainMapper::toResponseMaster);
    }

    @Override
    public String getUserNameByToken() {
        try {
            log.info("Begin service: getUserNameByToken");
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            return jwtTokenUtil.getUsernameFromToken(jwtToken);
        } finally {
            log.info("End service: getUserNameByToken");
        }
    }

    @Override
    public String getBumonByToken() {
        try {
            log.info("Begin service: getUserNameByToken");
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            return jwtTokenUtil.getBumonFromToken(jwtToken);
        } finally {
            log.info("End service: getUserNameByToken");
        }
    }


    @Override
    public List<ShainEntity> getListShains(BaseSearchRequest baseSearchRequest) {
        try {
            log.info("Begin service: getListShains");
            List<ShainEntity> result = listRSQL(shainRepository, baseSearchRequest, ShainEntity.class);
            return result;
        } finally {
            log.info("End service: getListShains");
        }
    }

    @Override
    public ShainEntity getLoggedInShainEntity() {
        return shainRepository.findByShainCd(getUserNameByToken());
    }

    @Override
    public List<ShainResponse> listShainForApproval(Byte stepNumber, String bumonCd, Long requestCd) {
        try {
            log.info("Begin service: listShainForApproval");
            BaseSearchRequest baseSearchRequest = new BaseSearchRequest();

            String searchKey = "shainCdNk!=null";
            List<String> shainCds = approvalFlowDetailsRepository.getShainCdByStepNumberAndRequestsCd(stepNumber, requestCd);
            if (CollectionUtil.isNotEmpty(shainCds)) {
                String shainCdNotIn = "shainCd=out=("+ String.join(",", shainCds) + ")";
                searchKey = searchKey + Constants.AND_RSQL + shainCdNotIn;
            }

            if (stepNumber != 1) {
                searchKey = searchKey + Constants.AND_RSQL + "mstTantoEntities.tantoStatus=='0'"
                        + Constants.AND_RSQL + "mstTantoEntities.mstBumonEntity.bumonCd=='" + bumonCd +"'";
            }
            baseSearchRequest.setKeyword(searchKey);
            List<ShainEntity> shainEntityList = listDistinctRSQL(shainRepository, baseSearchRequest, ShainEntity.class);

            return shainEntityList.stream().map(shainMapper::toResponse).collect(Collectors.toList());
        } finally {
            log.info("End service: listShainForApproval");
        }

    }

    @Override
    public String getCurrentUserBumonCd() {
        try {
            log.info("Begin service: getCurrentUserRole");
            ShainEntity shainEntity = shainRepository.findByShainCd(getUserNameByToken());
            return shainEntity.getMstBumonEntity().getBumonCd();
        } finally {
            log.info("End service: getCurrentUserRole");
        }
    }

    @Override
    public ShainResponse updateShainInformation(ShainResponse shainResponse) {
        try {
            log.info("Begin service: updateShainInformation");

            ShainEntity shainEntityOld = shainRepository.findByShainCd(shainResponse.getShainCd());
            ShainEntity shainEntityNew = shainMapper.toEntity(shainResponse);

            if (StringUtils.isEmpty(shainEntityNew.getPassword())) {
                shainEntityNew.setPassword(shainEntityOld.getPassword());
            }

            // set createdAt
            shainEntityNew.setCreatedAt(shainEntityOld.getCreatedAt());

            // set batchUpdateDate
            shainEntityNew.setBatchUpdateDate(shainEntityOld.getBatchUpdateDate());

            // set mstBumonEntity
            shainEntityNew.setMstBumonEntity(shainEntityOld.getMstBumonEntity());

            // set operationHistoriesEntities
            shainEntityNew.setOperationHistoriesEntities(shainEntityOld.getOperationHistoriesEntities());

            // set commentsForDetailsEntities
            shainEntityNew.setCommentsForDetailsEntities(shainEntityOld.getCommentsForDetailsEntities());

            // set approvalFlowDetailsEntities
            shainEntityNew.setApprovalFlowDetailsEntities(shainEntityOld.getApprovalFlowDetailsEntities());

            // set requestAccountsReceivableDetailsEntities
            shainEntityNew.setRequestAccountsReceivableDetailsEntities(shainEntityOld.getRequestAccountsReceivableDetailsEntities());

            // set mstTantoEntities
            shainEntityNew.setMstTantoEntities(shainEntityOld.getMstTantoEntities());

            // set mstRelYakushokuShainEntities
            shainEntityNew.setMstRelYakushokuShainEntities(shainEntityOld.getMstRelYakushokuShainEntities());

            // set requestsEntities
            shainEntityNew.setRequestsEntities(shainEntityOld.getRequestsEntities());

            // set shainAddressEntity
            shainEntityNew.setShainAddressEntity(shainEntityOld.getShainAddressEntity());

            ShainEntity shainEntity = shainRepository.save(shainEntityNew);
            ShainResponse shainResponseNew = shainMapper.toResponse(shainEntity);

            if (CollectionUtil.isNotEmpty(shainEntity.getMstRelYakushokuShainEntities())) {
                Set<MstRelYakushokuShainResponse> mstRelYakushokuShainResponses = mstRelYakushokuShainMapper.toResponses(shainEntity.getMstRelYakushokuShainEntities());
                shainResponseNew.setMstRelYakushokuShainResponses(mstRelYakushokuShainResponses);
            }

            return shainResponseNew;
        } finally {
            log.info("End service: updateShainInformation");
        }
    }

}
