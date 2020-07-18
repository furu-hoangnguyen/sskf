package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.mapper.MstHinmokuMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstHinmokuEntity;
import sskf.model.response.MstHinmokuReponse;
import sskf.repository.MstHinmokuRepository;
import sskf.service.impl.MstHinmokuServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@ExtendWith(SpringExtension.class)
public class MstHinmokuServiceTest {

    @InjectMocks
    private MstHinmokuServiceImpl mstHinmokuService;

    @Mock
    private MstHinmokuRepository mstHinmokuRepository;

    @Mock
    private MstHinmokuMapper mstHinmokuMapper;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstHinmokuEntity> cq;
    @Mock
    Root<MstHinmokuEntity> root;

    @Test
    public void listPageTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Page<MstHinmokuEntity> expect = new Page<MstHinmokuEntity>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super MstHinmokuEntity, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<MstHinmokuEntity> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<MstHinmokuEntity> iterator() {
                return null;
            }
        };
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstHinmokuEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstHinmokuEntity.class)).thenReturn(root);

        Mockito.when(mstHinmokuService.searchRSQL(mstHinmokuRepository, baseSearchRequest, MstHinmokuEntity.class,"hinmokuCd")).thenReturn(expect);
        MstHinmokuReponse mstHinmokuReponse = new MstHinmokuReponse();
        Mockito.when(mstHinmokuMapper.toResponse(Mockito.any())).thenReturn(mstHinmokuReponse);

        Page<MstHinmokuReponse> actual = mstHinmokuService.listPage(baseSearchRequest);
        Assert.assertEquals(actual, null);
    }

    @Test
    public void listMapTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstHinmokuEntity> expect = new ArrayList<>();
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstHinmokuEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstHinmokuEntity.class)).thenReturn(root);

        Mockito.when(mstHinmokuService.listRSQL(mstHinmokuRepository, baseSearchRequest, MstHinmokuEntity.class)).thenReturn(expect);
        MstHinmokuReponse mstHinmokuReponse = new MstHinmokuReponse();
        Mockito.when(mstHinmokuMapper.toResponse(Mockito.any())).thenReturn(mstHinmokuReponse);

        HashMap<String, MstHinmokuReponse> actual = mstHinmokuService.listMap(baseSearchRequest);
        Assert.assertEquals(actual.size(), expect.size());
    }
}
