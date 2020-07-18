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
import sskf.config.jwt.JwtTokenUtil;
import sskf.mapper.MstRelYakushokuShainMapper;
import sskf.mapper.ShainMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstRelYakushokuShainEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.response.MstRelYakushokuShainResponse;
import sskf.model.response.ShainResponse;
import sskf.repository.ShainRepository;
import sskf.service.impl.ShainServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@ExtendWith(SpringExtension.class)
public class ShainServiceImplTest {

    @InjectMocks
    private ShainServiceImpl shainService;

    @Mock
    private ShainRepository shainRepository;

    @Mock
    private ShainMapper shainMapper;

    @Mock
    private MstRelYakushokuShainMapper mstRelYakushokuShainMapper;

    @Mock
    HttpServletRequest request;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<ShainEntity> cq;
    @Mock
    Root<ShainEntity> shainEntityRoot;

    @Test
    public void getInformationTest() {
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("NgocCDS");
        shainEntity.setShainNm("Son Ngoc");
        ShainResponse shainResponse = new ShainResponse();
        shainResponse.setShainCd("NgocCDS");
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("Header AAAAAAA");
        Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.anyString())).thenReturn("jwt");
        Mockito.when(shainRepository.findByShainCd(Mockito.anyString())).thenReturn(shainEntity);
        Mockito.when(shainMapper.toResponse(shainEntity)).thenReturn(shainResponse);

        ShainResponse actual = shainService.getInformation();

        Assert.assertEquals(shainResponse.getShainCd(), actual.getShainCd());
    }

    @Test
    public void listShainsTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Page<ShainEntity> result = new Page<ShainEntity>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super ShainEntity, ? extends U> converter) {
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
            public List<ShainEntity> getContent() {
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
            public Iterator<ShainEntity> iterator() {
                return null;
            }
        };

        ShainResponse shainResponse = new ShainResponse();
        shainResponse.setShainCd("NgocCD");
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(ShainEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(ShainEntity.class)).thenReturn(shainEntityRoot);

        Mockito.when(shainService.searchRSQL(shainRepository, baseSearchRequest , ShainEntity.class, "shainCd")).thenReturn(result);

        Mockito.when(shainMapper.toResponse(Mockito.any())).thenReturn(shainResponse);

        Page<ShainResponse> response = shainService.listShains(baseSearchRequest);

        Assert.assertEquals(response, null);
    }

    @Test
    public void getUserNameByTokenTest() {
       Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("Bearer AAAAAAA");
       Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.anyString())).thenReturn("Admin");
       String actual = shainService.getUserNameByToken();
        Assert.assertEquals(actual, "Admin");
    }

    @Test
    public void getListShainsTest() {
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(ShainEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(ShainEntity.class)).thenReturn(shainEntityRoot);
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<ShainEntity> result = new ArrayList<>();
        Mockito.when(shainService.listRSQL(shainRepository, baseSearchRequest , ShainEntity.class)).thenReturn(result);
        List<ShainEntity> actual = shainService.getListShains(baseSearchRequest);
        Assert.assertEquals(actual.size(), result.size());
    }

    @Test
    public void updateShainInformationTest() {
        ShainResponse shainResponse = new ShainResponse();
        shainResponse.setShainCd("118602");

        ShainEntity shainEntityOld = new ShainEntity();
        ShainEntity shainEntityNew = new ShainEntity();
        ShainEntity shainEntity = new ShainEntity();

        MstRelYakushokuShainEntity mstRelYakushokuShainEntity = new MstRelYakushokuShainEntity();
        shainEntity.setMstRelYakushokuShainEntities(Set.of(mstRelYakushokuShainEntity));

        Set<MstRelYakushokuShainResponse> mstRelYakushokuShainResponses = new HashSet<>();

        ShainResponse shainResponseNew = new ShainResponse();
        shainResponseNew.setMstRelYakushokuShainResponses(mstRelYakushokuShainResponses);

        Mockito.when(shainRepository.findByShainCd(shainResponse.getShainCd())).thenReturn(shainEntityOld);
        Mockito.when(shainMapper.toEntity(shainResponse)).thenReturn(shainEntityNew);
        Mockito.when(shainRepository.save(shainEntityNew)).thenReturn(shainEntity);
        Mockito.when(shainMapper.toResponse(shainEntity)).thenReturn(shainResponseNew);
        Mockito.when(mstRelYakushokuShainMapper.toResponses(shainEntity.getMstRelYakushokuShainEntities())).thenReturn(mstRelYakushokuShainResponses);

        ShainResponse shainResponseActual = shainService.updateShainInformation(shainResponse);

        Assert.assertEquals(shainResponseNew, shainResponseActual);
    }
}
