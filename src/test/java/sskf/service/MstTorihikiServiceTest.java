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
import sskf.mapper.MstTorihikiMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstTorihikiEntity;
import sskf.model.response.MstTorihikiRespone;
import sskf.repository.MstTorihikiRepository;
import sskf.service.impl.MstTorihikiServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstTorihikiServiceTest {

    @InjectMocks
    private MstTorihikiServiceImpl mstTorihikiService;

    @Mock
    private MstTorihikiRepository mstTorihikiRepository;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstTorihikiEntity> cq;
    @Mock
    Root<MstTorihikiEntity> root;
    @Mock
    private MstTorihikiMapper mstTorihikiMapper;

    @Test
    public void listTorihikiRnmForSearchTest() {
        BaseSearchRequest searchRequest = new BaseSearchRequest();
        List<String> torihikiList = new ArrayList<>();
        torihikiList.add("name 1");
        Mockito.when(mstTorihikiRepository.listTorihikiRnmForSearch()).thenReturn(torihikiList);
        List<String> result = mstTorihikiService.listTorihikiRnmForSearch(searchRequest);
        Assert.assertEquals(torihikiList.size(), result.size());
    }

    @Test
    public void listTorihikiTest() {
        BaseSearchRequest searchRequest = new BaseSearchRequest();
        List<MstTorihikiEntity> mstTorihikiEntityList = new ArrayList<>();
        MstTorihikiEntity mst = new MstTorihikiEntity();
        mst.setTorihikiCd("AAA");
        mstTorihikiEntityList.add(mst);
        PageImpl<MstTorihikiEntity> mstTorihikiEntityPage = new PageImpl<>(mstTorihikiEntityList);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstTorihikiEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstTorihikiEntity.class)).thenReturn(root);
        Mockito.when(mstTorihikiService.searchRSQL(mstTorihikiRepository, searchRequest , MstTorihikiEntity.class, "torihikiCd")).thenReturn(mstTorihikiEntityPage);
        MstTorihikiRespone mstTorihikiRespone = new MstTorihikiRespone();
        mstTorihikiRespone.setTorihikiCd("AAA");
        Mockito.when(mstTorihikiMapper.toResponse(Mockito.any())).thenReturn(mstTorihikiRespone);
        Page<MstTorihikiRespone> result = mstTorihikiService.listTorihiki(searchRequest);
        Assert.assertEquals(mstTorihikiEntityPage.getTotalPages(), result.getTotalPages());
    }
}
