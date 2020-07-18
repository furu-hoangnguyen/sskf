package sskf.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.mapper.MstTantoMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstTantoEntity;
import sskf.model.response.MstTantoResponse;
import sskf.repository.MstTantoRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstTantoServicesTest {

    @InjectMocks
    MstTantoServices mstTantoServices;

    @Mock
    private MstTantoRepository mstTantoRepository;

    @Mock
    private MstTantoMapper mstTantoMapper;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstTantoEntity> cq;
    @Mock
    Root<MstTantoEntity> root;

    @Test
    public void getTantosTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstTantoEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstTantoEntity.class)).thenReturn(root);
        List<MstTantoEntity> entityList = new ArrayList<>();
        MstTantoEntity mstTantoEntity = new MstTantoEntity();
        mstTantoEntity.setTantoCd("CD");
        entityList.add(mstTantoEntity);
        PageImpl<MstTantoEntity> entities = new PageImpl<>(entityList);
        Mockito.when(mstTantoServices.searchRSQL(mstTantoRepository, baseSearchRequest, MstTantoEntity.class, "tantoCd")).thenReturn(entities);
        MstTantoResponse mstTantoResponse = mstTantoMapper.toResponse(mstTantoEntity);
        Mockito.when(mstTantoMapper.toResponse(Mockito.any())).thenReturn(mstTantoResponse);
        Page<MstTantoResponse> actual = mstTantoServices.getTantos(baseSearchRequest);
        Assert.assertEquals(actual.getTotalPages(), entities.getTotalPages());
    }
}
