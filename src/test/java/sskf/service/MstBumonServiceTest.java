package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.mapper.MstBumonMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstBumonEntity;
import sskf.model.response.BumonResponse;
import sskf.repository.MstBumonRepository;
import sskf.service.master.MstBumonServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstBumonServiceTest {

    @InjectMocks
    private MstBumonServiceImpl mmstBumonServiceImpl;

    @Mock
    private MstBumonRepository mstBumonRepository;

    @Mock
    private MstBumonMapper mstBumonMapper;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstBumonEntity> cq;
    @Mock
    Root<MstBumonEntity> root;

    @Test
    public void listDepartmentForItemTest() {
        BaseSearchRequest searchRequest = new BaseSearchRequest();

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstBumonEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstBumonEntity.class)).thenReturn(root);

        List<MstBumonEntity> result = new ArrayList<>();
        Mockito.when(mmstBumonServiceImpl.listRSQL(mstBumonRepository, searchRequest , MstBumonEntity.class)).thenReturn(result);

        List<BumonResponse> actual = mmstBumonServiceImpl.listDepartmentForItem(searchRequest);
        Assert.assertEquals(actual.size(), result.size());
    }

    @Test
    public void listDepartmentByStepNumberTest() {
        Byte stepNumber = 2;
        BaseSearchRequest searchRequest = new BaseSearchRequest();

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstBumonEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstBumonEntity.class)).thenReturn(root);

        List<MstBumonEntity> result = new ArrayList<>();
        Mockito.when(mmstBumonServiceImpl.listDistinctRSQL(mstBumonRepository, searchRequest , MstBumonEntity.class)).thenReturn(result);

        List<BumonResponse> actual = mmstBumonServiceImpl.listDepartmentByStepNumber(stepNumber);
        Assert.assertEquals(actual.size(), result.size());
    }

}
