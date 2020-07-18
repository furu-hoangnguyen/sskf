package sskf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.Assert;
import sskf.exception.SSKFException;
import sskf.mapper.CommentsForDetailsMapper;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.entity.OperationHistoriesEntity;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.ShainEntity;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;
import sskf.repository.CommentsForDetailsRepository;
import sskf.service.impl.CommentsForDetailsServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class CommentsForDetailsServiceTest {

    @InjectMocks
    private CommentsForDetailsServiceImpl commentsForDetailsService;


    @Mock
    private CommentsForDetailsRepository commentsForDetailsRepository;

    @Mock
    private CommentsForDetailsMapper commentsForDetailsMapper;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<CommentsForDetailsEntity> cq;
    @Mock
    Root<CommentsForDetailsEntity> root;

    @Test
    public void createByRequestAccountReceivableTest() {
        Set<CommentsForDetailsEntity> commentsForDetailsEntities = new HashSet<>();
        CommentsForDetailsEntity commentsForDetailsEntity = new CommentsForDetailsEntity();
        commentsForDetailsEntities.add(commentsForDetailsEntity);
        List<CommentDetailsRequest> commentDetailsRequests = new ArrayList<>();
        CommentDetailsRequest commentDetailsRequest = new CommentDetailsRequest();
        commentDetailsRequest.setComment("AA");
        commentDetailsRequests.add(commentDetailsRequest);
        CommentsForDetailsEntity entity = new CommentsForDetailsEntity();
        Mockito.when(commentsForDetailsMapper.toEntity(Mockito.any())).thenReturn(entity);
        ShainEntity shainEntity = new ShainEntity();
        RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity = new RequestAccountsReceivableDetailsEntity();
        //OperationHistoriesEntity operationHistoriesEntity = new OperationHistoriesEntity();
        Set<CommentsForDetailsEntity> actual = commentsForDetailsService.createByRequestAccountReceivable(commentDetailsRequests, shainEntity, requestAccountsReceivableDetailsEntity);
        Assert.assertEquals(actual.size(), commentsForDetailsEntities.size());
    }

    @Test
    public void listOldCommentTest() {
        String storeGCd = "aaaa";
        List<Object> expect = new ArrayList<>();
        Mockito.when(commentsForDetailsRepository.listOldComment(storeGCd)).thenReturn(expect);
        List<Object> comments = commentsForDetailsService.listOldComment(storeGCd);
        Assert.assertEquals(comments.size(), expect.size());
    }

    @Test
    public void deleteTest() {
        Long id = 1L;
        CommentsForDetailsEntity commentsForDetailsEntity = new CommentsForDetailsEntity();
        byte b = 1;
        commentsForDetailsEntity.setIsCapableOfBeingDeleted(Byte.valueOf(b));
        Mockito.when(commentsForDetailsRepository.getOne(id)).thenReturn(commentsForDetailsEntity);
        Mockito.doNothing().when(commentsForDetailsRepository).delete(commentsForDetailsEntity);
        commentsForDetailsService.delete(id);
    }

    @Test
    public void deleteTest_With_Exception() {
        try {
            Long id = 1L;
            CommentsForDetailsEntity commentsForDetailsEntity = new CommentsForDetailsEntity();
            byte b = 2;
            commentsForDetailsEntity.setIsCapableOfBeingDeleted(Byte.valueOf(b));
            Mockito.when(commentsForDetailsRepository.getOne(id)).thenReturn(commentsForDetailsEntity);
            Mockito.doNothing().when(commentsForDetailsRepository).delete(commentsForDetailsEntity);
            commentsForDetailsService.delete(id);
        } catch (SSKFException ssk) {
            Assertions.assertThat(ssk.getMessage()).isEqualTo("Can not delete comment!");
        }
    }

    @Test
    public void listByRequestAccountTest() {
        Long id = 1L;
        List<CommentsForDetailsEntity> commentsForDetailsEntities = new ArrayList<>();
        CommentDetailResponse commentDetailResponse = new CommentDetailResponse();
        Mockito.when(commentsForDetailsRepository.findByRequestAccountsReceivableDetailsEntityCdAndIsDeletedOrderByCdAsc(Mockito.anyLong(), Mockito.anyByte())).thenReturn(commentsForDetailsEntities);
        Mockito.when(commentsForDetailsMapper.toResponse(Mockito.any())).thenReturn(commentDetailResponse);
        List<CommentDetailResponse> actual = commentsForDetailsService.listByRequestAccount(id);
        Assert.assertEquals(actual.size(), commentsForDetailsEntities.size());
    }

}
