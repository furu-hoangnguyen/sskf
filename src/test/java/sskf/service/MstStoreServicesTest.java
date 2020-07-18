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
import sskf.mapper.MstStoreMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstStoreEntity;
import sskf.model.response.MstStoreResponse;
import sskf.repository.MstStoreRepository;
import sskf.service.master.MstStoreServicesImpl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstStoreServicesTest {

    @InjectMocks
    MstStoreServicesImpl mstStoreServices;

    @Mock
    private MstStoreRepository mstStoreRepository;

    @Mock
    private MstStoreMapper mstStoreMapper;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstStoreEntity> cq;
    @Mock
    Root<MstStoreEntity> root;

    @Test
    public void getStoresTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstStoreEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstStoreEntity.class)).thenReturn(root);
        List<MstStoreEntity> entityList = new ArrayList<>();
        MstStoreEntity mstStoreEntity = new MstStoreEntity();
        mstStoreEntity.setStoreCd("CD");
        entityList.add(mstStoreEntity);
        PageImpl<MstStoreEntity> entities = new PageImpl<>(entityList);
        Mockito.when(mstStoreServices.searchRSQL(mstStoreRepository, baseSearchRequest, MstStoreEntity.class, "storeCd")).thenReturn(entities);
        MstStoreResponse mstStoreResponse = mstStoreMapper.toResponse(mstStoreEntity);
        Mockito.when(mstStoreMapper.toResponse(Mockito.any())).thenReturn(mstStoreResponse);
        Page<MstStoreResponse> actual = mstStoreServices.getStores(baseSearchRequest);
        Assert.assertEquals(actual.getTotalPages(), entities.getTotalPages());
    }

    @Test
    public void listStoresTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstStoreEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstStoreEntity.class)).thenReturn(root);
        List<MstStoreEntity> entityList = new ArrayList<>();
        MstStoreEntity mstStoreEntity = new MstStoreEntity();
        mstStoreEntity.setStoreCd("CD");
        entityList.add(mstStoreEntity);
        Mockito.when(mstStoreServices.listRSQL(mstStoreRepository, baseSearchRequest, MstStoreEntity.class)).thenReturn(entityList);
        MstStoreResponse mstStoreResponse = mstStoreMapper.toResponse(mstStoreEntity);
        Mockito.when(mstStoreMapper.toResponse(Mockito.any())).thenReturn(mstStoreResponse);
        List<MstStoreResponse>  actual = mstStoreServices.listStores(baseSearchRequest);
        Assert.assertEquals(actual.size(), entityList.size());
    }
}
