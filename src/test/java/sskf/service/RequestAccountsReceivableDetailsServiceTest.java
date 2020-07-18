package sskf.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.mapper.DetailsForAccountsReceivablesMapper;
import sskf.mapper.DetailsForPromotionalExpensesMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.DetailsForAccountsReceivablesEntity;
import sskf.model.entity.DetailsForPromotionalExpensesEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.DetailsForAccountsReceivablesRequest;
import sskf.model.request.DetailsForPromotionalExpensesRequest;
import sskf.repository.DetailsForAccountsReceivablesRepository;
import sskf.repository.DetailsForPromotionalExpensesRepository;
import sskf.repository.MstBumonRepository;
import sskf.repository.RequestAccountsReceivableDetailsRepository;
import sskf.repository.ShainRepository;
import sskf.service.impl.RequestAccountsReceivableDetailsServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class RequestAccountsReceivableDetailsServiceTest {

    @InjectMocks
    @Spy
    private RequestAccountsReceivableDetailsServiceImpl requestAccountsReceivableDetailsService;

    @Mock
    private RequestAccountsReceivableDetailsRepository requestAccountsReceivableDetailsRepository;

    @Mock
    private DetailsForAccountsReceivablesMapper detailsForAccountsReceivablesMapper;

    @Mock
    private MstBumonRepository mstBumonRepository;

    @Mock
    private ShainRepository shainRepository;

    @Mock
    private DetailsForAccountsReceivablesRepository detailsForAccountsReceivablesRepository;

    @Mock
    private DetailsForPromotionalExpensesMapper detailsForPromotionalExpensesMapper;

    @Mock
    private DetailsForPromotionalExpensesRepository detailsForPromotionalExpensesRepository;

    @Mock
    private ShainService shainService;

    @Mock
    private CommentsForDetailsService commentsForDetailsService;

    @Mock
    private  OperationHistoriesService operationHistoriesService;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager                               entityManager;
    @Mock
    CriteriaBuilder                             cb;
    @Mock
    CriteriaQuery<RequestAccountsReceivableDetailsEntity> cq;
    @Mock
    Root<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntityRoot;

    private List<DetailsForAccountsReceivablesRequest> detailsForAccountsReceivablesRequestList;

    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax8PercentList;

    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestTax10PercentList;

    private List<DetailsForPromotionalExpensesRequest> detailsForPromotionalExpensesRequestLogisticFeesList;

    private RequestsEntity requestsEntity;

    @BeforeEach
    public void setup() {
        detailsForAccountsReceivablesRequestList = new ArrayList<>();
        DetailsForAccountsReceivablesRequest detailsForAccountsReceivablesRequest = new DetailsForAccountsReceivablesRequest();
        detailsForAccountsReceivablesRequest.setCd(1L);
        detailsForAccountsReceivablesRequest.setItemNumber("1001");
        detailsForAccountsReceivablesRequest.setShainCd("NgocCD");
        detailsForAccountsReceivablesRequest.setBumonCd("AA");
        detailsForAccountsReceivablesRequestList.add(detailsForAccountsReceivablesRequest);
        detailsForPromotionalExpensesRequestTax8PercentList = new ArrayList<>();
        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequest = new DetailsForPromotionalExpensesRequest();
        detailsForPromotionalExpensesRequest.setCd(2L);
        detailsForPromotionalExpensesRequest.setItemNumber("2001");
        detailsForPromotionalExpensesRequest.setShainCd("NgocCD");
        detailsForPromotionalExpensesRequest.setBumonCd("AA");
        detailsForPromotionalExpensesRequestTax8PercentList.add(detailsForPromotionalExpensesRequest);

        detailsForPromotionalExpensesRequestTax10PercentList = new ArrayList<>();
        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequestTax10Percent = new DetailsForPromotionalExpensesRequest();
        detailsForPromotionalExpensesRequestTax10Percent.setCd(3L);
        detailsForPromotionalExpensesRequestTax10Percent.setItemNumber("3001");
        detailsForPromotionalExpensesRequestTax10Percent.setShainCd("NgocCD");
        detailsForPromotionalExpensesRequestTax10Percent.setBumonCd("AA");
        detailsForPromotionalExpensesRequestTax10PercentList.add(detailsForPromotionalExpensesRequestTax10Percent);

        detailsForPromotionalExpensesRequestLogisticFeesList = new ArrayList<>();
        DetailsForPromotionalExpensesRequest detailsForPromotionalExpensesRequesttLogisticFees = new DetailsForPromotionalExpensesRequest();
        detailsForPromotionalExpensesRequesttLogisticFees.setCd(4L);
        detailsForPromotionalExpensesRequesttLogisticFees.setItemNumber("4001");
        detailsForPromotionalExpensesRequesttLogisticFees.setShainCd("NgocCD");
        detailsForPromotionalExpensesRequesttLogisticFees.setBumonCd("AA");
        detailsForPromotionalExpensesRequestLogisticFeesList.add(detailsForPromotionalExpensesRequesttLogisticFees);

        requestsEntity = new RequestsEntity();
        Set<OperationHistoriesEntity> operationHistoriesEntities = new HashSet<>();
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        operationHistoriesEntity.setShainNm("NgocCD");
        operationHistoriesEntity.setBumonNm("AA");
        operationHistoriesEntity.setCd(1L);
        operationHistoriesEntities.add(operationHistoriesEntity);
        requestsEntity.setOperationHistoriesEntities(operationHistoriesEntities);
    }

    @Test
    public void createTest() {
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();
        requestAccountsReceivablesEntity.setRequestsEntity(requestsEntity);
        DetailsForAccountsReceivablesEntity detailsForAccountsReceivablesEntity = new DetailsForAccountsReceivablesEntity();
        AccountReceivablesRequest accountReceivablesRequest = new AccountReceivablesRequest();
        accountReceivablesRequest.setDetailsForAccountsReceivablesRequestList(detailsForAccountsReceivablesRequestList);
        accountReceivablesRequest.setDetailsForPromotionalExpensesRequestTax8PercentList(detailsForPromotionalExpensesRequestTax8PercentList);
        ShainEntity createdBy = new ShainEntity();
        createdBy.setShainNm("NgocCD");
        String userLogin = "NgocCD";
        Mockito.when(shainService.getUserNameByToken()).thenReturn(userLogin);
        Mockito.when(shainRepository.findByShainCd(userLogin)).thenReturn(createdBy);
        Mockito.when(detailsForAccountsReceivablesMapper.toEntity(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(detailsForAccountsReceivablesEntity);

        List<MstBumonEntity> mstBumonEntityList = new ArrayList<>();
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonCd("AA");
        mstBumonEntity.setBumonNm("AA");
        mstBumonEntityList.add(mstBumonEntity);
        Mockito.when(mstBumonRepository.findAllById(Mockito.anyCollection())).thenReturn(mstBumonEntityList);

        List<ShainEntity> shainEntityList = new ArrayList<>();
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainNm("NgocCD");
        shainEntity.setShainCd("NgocCD");
        shainEntityList.add(shainEntity);
        Mockito.when(shainRepository.findAllById(Mockito.anyCollection())).thenReturn(shainEntityList);

        List<DetailsForAccountsReceivablesEntity> entityUpdated = new ArrayList<>();
        DetailsForAccountsReceivablesEntity entity1 = new DetailsForAccountsReceivablesEntity();
        entity1.setCd(1L);
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity1 = new RequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity1.setItemNumber("1001");
        entity1.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity1);
        entityUpdated.add(entity1);

        Mockito.when(detailsForAccountsReceivablesRepository.findAllById(Mockito.anyCollection())).thenReturn(entityUpdated);
        Mockito.when(detailsForAccountsReceivablesMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(entity1);
        Mockito.when(detailsForAccountsReceivablesRepository.saveAll(Mockito.anyCollection())).thenReturn(entityUpdated);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);
        List<DetailsForPromotionalExpensesEntity> entityPromotionUpdated = new ArrayList<>();
        DetailsForPromotionalExpensesEntity expensesEntity = new DetailsForPromotionalExpensesEntity();
        expensesEntity.setCd(2L);
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity2 = new RequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity2.setItemNumber("2001");
        expensesEntity.setRequestAccountsReceivableDetailsEntity(requestAccountsReceivableDetailsEntity2);
        entityPromotionUpdated.add(expensesEntity);
        Mockito.when(detailsForPromotionalExpensesRepository.findAllById(Mockito.anyCollection())).thenReturn(entityPromotionUpdated);
        Mockito.when(detailsForPromotionalExpensesMapper.toEntityUpdated(Mockito.any(), Mockito.any())).thenReturn(expensesEntity);

        requestAccountsReceivableDetailsService.create(requestAccountsReceivablesEntity, accountReceivablesRequest, "作成中");

    }

    @Test
    public void updateTest() {
        RequestAccountsReceivablesEntity requestAccountsReceivablesEntity = new RequestAccountsReceivablesEntity();
        requestAccountsReceivablesEntity.setRequestsEntity(requestsEntity);
        AccountReceivablesRequest accountReceivablesRequest = new AccountReceivablesRequest();
        List<Long> accountReceivableDetailIsDeleted = Arrays.asList(1L, 2L);
        accountReceivablesRequest.setAccountReceivableDetailIsDeleted(accountReceivableDetailIsDeleted);
        accountReceivablesRequest.setDetailsForAccountsReceivablesRequestList(detailsForAccountsReceivablesRequestList);
        accountReceivablesRequest.setDetailsForPromotionalExpensesRequestTax8PercentList(detailsForPromotionalExpensesRequestTax8PercentList);

        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForRequests(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyByte())).thenReturn(operationHistoriesEntity);

        List<RequestAccountsReceivableDetailsEntity> itemsDeleted = new ArrayList<>();
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = new RequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity.setItemNumber("2002");
        itemsDeleted.add(requestAccountsReceivableDetailsEntity);
        Mockito.when(requestAccountsReceivableDetailsRepository.findAllById(Mockito.anyCollection())).thenReturn(itemsDeleted);
        Mockito.when(requestAccountsReceivableDetailsRepository.saveAll(itemsDeleted)).thenReturn(itemsDeleted);
        createTest();
        requestAccountsReceivableDetailsService.update(requestAccountsReceivablesEntity, accountReceivablesRequest, "作成中");
    }

    @Test
    public void listTest() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<RequestAccountsReceivableDetailsEntity> result = new ArrayList<>();
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = new RequestAccountsReceivableDetailsEntity();
        requestAccountsReceivableDetailsEntity.setItemNumber("1001");
        result.add(requestAccountsReceivableDetailsEntity);
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(RequestAccountsReceivableDetailsEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(RequestAccountsReceivableDetailsEntity.class)).thenReturn(requestAccountsReceivableDetailsEntityRoot);

        Mockito.when(requestAccountsReceivableDetailsService.listRSQL(requestAccountsReceivableDetailsRepository, baseSearchRequest , RequestAccountsReceivableDetailsEntity.class)).thenReturn(result);

        List<RequestAccountsReceivableDetailsEntity> response = requestAccountsReceivableDetailsService.list(baseSearchRequest);
        Assert.assertEquals(response.size(), result.size());
    }
}
