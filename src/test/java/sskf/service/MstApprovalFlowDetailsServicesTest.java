package sskf.service;

import cz.jirutka.rsql.parser.ast.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.annotations.BeforeClass;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.MstApprovalFlowDetailsEntity;
import sskf.model.entity.MstBumonEntity;
import sskf.model.entity.MstYakushokuEntity;
import sskf.repository.MstApprovalFlowDetailsRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MstApprovalFlowDetailsServicesTest {

    @InjectMocks
    MstApprovalFlowDetailsServices mstApprovalFlowDetailsServices;

    @Mock
    MstApprovalFlowDetailsRepository mstApprovalFlowDetailsRepository;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<MstApprovalFlowDetailsEntity> cq;
    @Mock
    Root<MstApprovalFlowDetailsEntity> root;
    @Mock
    Node rootNode;


    @Test
    public void findApprovalFlowsGroupWithStepNumber1Test() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstApprovalFlowDetailsEntity> entities = findApprovalFlowGroup((byte) 1);
        Mockito.when(mstApprovalFlowDetailsServices.listRSQL(mstApprovalFlowDetailsRepository, baseSearchRequest, MstApprovalFlowDetailsEntity.class)).thenReturn(entities);
        mstApprovalFlowDetailsServices.findApprovalFlowsGroup("AA");
    }

    @Test
    public void findApprovalFlowsGroupWithStepNumber2Test() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstApprovalFlowDetailsEntity> entities = findApprovalFlowGroup((byte) 2);
        Mockito.when(mstApprovalFlowDetailsServices.listRSQL(mstApprovalFlowDetailsRepository, baseSearchRequest, MstApprovalFlowDetailsEntity.class)).thenReturn(entities);
        mstApprovalFlowDetailsServices.findApprovalFlowsGroup("AA");
    }

    @Test
    public void findApprovalFlowsGroupWithStepNumber3Test() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstApprovalFlowDetailsEntity> entities = findApprovalFlowGroup((byte) 3);
        Mockito.when(mstApprovalFlowDetailsServices.listRSQL(mstApprovalFlowDetailsRepository, baseSearchRequest, MstApprovalFlowDetailsEntity.class)).thenReturn(entities);
        mstApprovalFlowDetailsServices.findApprovalFlowsGroup("AA");
    }

    @Test
    public void findApprovalFlowsGroupWithStepNumber4Test() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstApprovalFlowDetailsEntity> entities = findApprovalFlowGroup((byte) 4);
        Mockito.when(mstApprovalFlowDetailsServices.listRSQL(mstApprovalFlowDetailsRepository, baseSearchRequest, MstApprovalFlowDetailsEntity.class)).thenReturn(entities);
        mstApprovalFlowDetailsServices.findApprovalFlowsGroup("AA");
    }

    @Test
    public void findApprovalFlowsGroupWithStepNumber5Test() {
        BaseSearchRequest baseSearchRequest = new BaseSearchRequest();
        List<MstApprovalFlowDetailsEntity> entities = findApprovalFlowGroup((byte) 5);
        Mockito.when(mstApprovalFlowDetailsServices.listRSQL(mstApprovalFlowDetailsRepository, baseSearchRequest, MstApprovalFlowDetailsEntity.class)).thenReturn(entities);
        mstApprovalFlowDetailsServices.findApprovalFlowsGroup("AA");
    }
    private List<MstApprovalFlowDetailsEntity> findApprovalFlowGroup(Byte stepNumber) {

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(MstApprovalFlowDetailsEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(MstApprovalFlowDetailsEntity.class)).thenReturn(root);
        List<MstApprovalFlowDetailsEntity> entities = new ArrayList<>();
        MstApprovalFlowDetailsEntity entity = new MstApprovalFlowDetailsEntity();
        MstBumonEntity mstBumonEntity = new MstBumonEntity();
        mstBumonEntity.setBumonNm("bumon");
        entity.setMstBumonEntity(mstBumonEntity);
        MstYakushokuEntity mstYakushokuEntity = new MstYakushokuEntity();
        mstYakushokuEntity.setName("mstYakushokuEntity");
        entity.setMstYakushokuEntity(mstYakushokuEntity);
        entity.setStepNumber(stepNumber);
        entity.setIsDeputy((byte) 0);
        entities.add(entity);
        return entities;
    }
}

