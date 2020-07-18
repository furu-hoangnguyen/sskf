package sskf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import org.testng.collections.Sets;
import sskf.mapper.ApprovalFlowDetailsMapper;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ApprovalFlowDetailsEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstRequestStatusesEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.ApprovalFlowDetailsRequest;
import sskf.model.request.ReplaceChargePerSonsIsDeputyModel;
import sskf.model.response.ApprovalPerson;
import sskf.repository.ApprovalFlowDetailsRepository;
import sskf.repository.MstBumonRepository;
import sskf.repository.RequestsRepository;
import sskf.repository.ShainRepository;
import sskf.service.impl.ApprovalFlowDetailsServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class ApprovalFlowDetailsServiceTest {

    @InjectMocks
    private ApprovalFlowDetailsServiceImpl approvalFlowDetailsService;

    @Mock
    private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;

    @Mock
    private ShainRepository shainRepository;

    @Mock
    private MstBumonRepository mstBumonRepository;

    @Mock
    private ApprovalFlowDetailsMapper approvalFlowDetailsMapper;

    @Mock
    private RequestsRepository requestsRepository;

    @Mock
    private ShainService shainService;

    @Mock
    private OperationHistoriesService operationHistoriesService;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<ApprovalFlowDetailsEntity> cq;
    @Mock
    Root<ApprovalFlowDetailsEntity> root;


    @Test
    public void createUserApplyTest() {
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        approvalFlowDetailsRequest.setBumonCd("Bumon");
        RequestsEntity requestsEntity = new RequestsEntity();

        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("NgocCD");
        Mockito.when(shainRepository.getOne(Mockito.anyString())).thenReturn(shainEntity);
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        Mockito.when(mstBumonRepository.getOne(Mockito.anyString())).thenReturn(mstBumonEntity);

        ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();
        approvalFlowDetailsEntity.setCd(1L);
        Mockito.when(approvalFlowDetailsRepository.save(Mockito.any())).thenReturn(approvalFlowDetailsEntity);

        ApprovalFlowDetailsEntity actual = approvalFlowDetailsService.createUserApply(approvalFlowDetailsRequest, requestsEntity);

        Assert.assertEquals(actual.getCd(), approvalFlowDetailsEntity.getCd());

    }

    @Test
    public void updateUserApplyTest() {
        ApprovalFlowDetailsRequest approvalFlowDetailsRequest = new ApprovalFlowDetailsRequest();
        approvalFlowDetailsRequest.setShainCd("NgocCD");
        approvalFlowDetailsRequest.setBumonCd("Bumon");
        RequestsEntity requestsEntity = new RequestsEntity();
        Set<ApprovalFlowDetailsEntity> approvalFlowDetailsEntitySet = Sets.newHashSet();
        ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();
        approvalFlowDetailsEntity.setStepNumber((byte)1);
        approvalFlowDetailsEntity.setIsDeputy(false);
        approvalFlowDetailsEntitySet.add(approvalFlowDetailsEntity);
        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("NgocCD");
        Mockito.when(shainRepository.getOne(Mockito.anyString())).thenReturn(shainEntity);
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        Mockito.when(mstBumonRepository.getOne(Mockito.anyString())).thenReturn(mstBumonEntity);

        approvalFlowDetailsEntity.setCd(1L);
        Mockito.when(approvalFlowDetailsRepository.save(Mockito.any())).thenReturn(approvalFlowDetailsEntity);

        ApprovalFlowDetailsEntity actual = approvalFlowDetailsService.updateUserApply(approvalFlowDetailsRequest, requestsEntity);

        Assert.assertEquals(actual.getCd(), approvalFlowDetailsEntity.getCd());

    }

    @Test
    public void listTest() {
        List<ApprovalFlowDetailsEntity> expect = new ArrayList<>();
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(ApprovalFlowDetailsEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(ApprovalFlowDetailsEntity.class)).thenReturn(root);
        Mockito.when(approvalFlowDetailsService.listRSQL(approvalFlowDetailsRepository, baseSearchRequest , ApprovalFlowDetailsEntity.class)).thenReturn(expect);
        List<ApprovalFlowDetailsEntity> actual = approvalFlowDetailsService.list(baseSearchRequest);
        Assert.assertEquals(actual.size(), expect.size());
    }

    @Test
    public void listResponseTest() {
        List<ApprovalFlowDetailsEntity> expect = new ArrayList<>();
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(ApprovalFlowDetailsEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(ApprovalFlowDetailsEntity.class)).thenReturn(root);
        Mockito.when(approvalFlowDetailsService.listRSQL(approvalFlowDetailsRepository, baseSearchRequest , ApprovalFlowDetailsEntity.class)).thenReturn(expect);
        ApprovalPerson approvalPerson = new ApprovalPerson();
        Mockito.when(approvalFlowDetailsMapper.toResponseApprovalPerson(Mockito.any())).thenReturn(approvalPerson);
        List<ApprovalPerson> actual = approvalFlowDetailsService.listResponse(baseSearchRequest);
        Assert.assertEquals(actual.size(), expect.size());
    }

    @Test
    public void replaceChargePersonsTest() {
        ReplaceChargePerSonsIsDeputyModel request = new ReplaceChargePerSonsIsDeputyModel();
        ApprovalFlowDetailsEntity chargePersonMain = new ApprovalFlowDetailsEntity();
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("承認待ち");
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        chargePersonMain.setRequestsEntity(requestsEntity);
        chargePersonMain.setStepNumber((byte) 1);
        Mockito.when(approvalFlowDetailsRepository.getOne(Mockito.any())).thenReturn(chargePersonMain);

        Mockito.when(approvalFlowDetailsRepository.getOne(Mockito.any())).thenReturn(chargePersonMain);

        Mockito.when(requestsRepository.getOne(Mockito.any())).thenReturn(requestsEntity);
        Mockito.when(shainService.getUserNameByToken()).thenReturn("NgoCDS");
        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntityList = Arrays.asList(chargePersonMain, chargePersonMain);
        checkPermissionToChangeChargeTest();
        Mockito.when(approvalFlowDetailsRepository.saveAll(Mockito.anyCollection())).thenReturn(approvalFlowDetailsEntityList);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForApprovalFlowsDeputy(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(operationHistoriesEntity);
        approvalFlowDetailsService.replaceChargePersons(request);
    }

    @Test
    public void deleteChargeIsDeputyTest() {
        ApprovalFlowDetailsEntity chargePersonMain = new ApprovalFlowDetailsEntity();
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("承認待ち");
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        chargePersonMain.setRequestsEntity(requestsEntity);
        chargePersonMain.setStepNumber((byte)1);
        chargePersonMain.setIsDeputy(Boolean.TRUE);
        Mockito.when(approvalFlowDetailsRepository.getOne(Mockito.any())).thenReturn(chargePersonMain);
        Mockito.doNothing().when(approvalFlowDetailsRepository).delete(chargePersonMain);
        OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Mockito.when(operationHistoriesService.setOperationHistoriesForApprovalFlowsDeputy(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(operationHistoriesEntity);
        approvalFlowDetailsService.deleteChargeIsDeputy(1L);
    }

    @Test
    public void insertTest() {
        Long requestCd =1L;
        RequestsEntity requestsEntity = new RequestsEntity();
        requestsEntity.setCd(1L);
        requestsEntity.setStepNumber((byte)1);
        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("承認待ち");
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        ApprovalPerson approvalPerson = new ApprovalPerson();
        approvalPerson.setStepNumber((byte)1);
        Mockito.when(requestsRepository.getOne(Mockito.any())).thenReturn(requestsEntity);

        ShainEntity shainEntity = new ShainEntity();
        shainEntity.setShainCd("NgocCD");
        Mockito.when(shainRepository.getOne(Mockito.any())).thenReturn(shainEntity);

        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonCd("11001");
        Mockito.when(mstBumonRepository.getOne(Mockito.any())).thenReturn(mstBumonEntity);
        ApprovalFlowDetailsEntity entity = new ApprovalFlowDetailsEntity();
        Mockito.when(approvalFlowDetailsRepository.save(entity)).thenReturn(entity);
        approvalFlowDetailsService.insert(requestCd, approvalPerson);
    }

    private void checkPermissionToChangeChargeTest() {
        RequestsEntity requestsEntity = new RequestsEntity();
        MstRequestStatusesEntity mstRequestStatusesEntity = new MstRequestStatusesEntity();
        mstRequestStatusesEntity.setName("承認待ち");
        requestsEntity.setMstRequestStatusesEntity(mstRequestStatusesEntity);
        Mockito.when(requestsRepository.getOne(Mockito.anyLong())).thenReturn(requestsEntity);
        String userLogin = "NgocCDS";
        Mockito.when(shainService.getUserNameByToken()).thenReturn(userLogin);
        List<ApprovalFlowDetailsEntity> approvalFlowDetailsEntities = new ArrayList<>();
        ApprovalFlowDetailsEntity approvalFlowDetailsEntity = new ApprovalFlowDetailsEntity();
        approvalFlowDetailsEntity.setCd(1l);
        approvalFlowDetailsEntities.add(approvalFlowDetailsEntity);
        Mockito.when(approvalFlowDetailsRepository
                .findByRequestsEntityAndStepNumberInAndShainEntity_ShainCd(Mockito.any(), Mockito.anyList(), Mockito.anyString())).thenReturn(approvalFlowDetailsEntities);
    }
}
