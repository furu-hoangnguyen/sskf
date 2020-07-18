package sskf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.ImportedMasterStatusEntity;
import sskf.model.response.ImportedMasterStatusResponse;
import sskf.repository.ImportedMasterStatusRepository;
import sskf.service.master.ImportedSituationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ImportedSituationServiceTest {

    @InjectMocks
    private ImportedSituationService importedSituationService;

    @Mock
    private ImportedMasterStatusRepository importedMasterStatusRepository;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder cb;
    @Mock
    CriteriaQuery<ImportedMasterStatusEntity> cq;
    @Mock
    Root<ImportedMasterStatusEntity> root;

    @Test
    public void listTest() {
        BaseSearchRequest request = new BaseSearchRequest();
        Page<ImportedMasterStatusEntity> result = Page.empty();

        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(ImportedMasterStatusEntity.class)).thenReturn(cq);
        Mockito.when(cq.from(ImportedMasterStatusEntity.class)).thenReturn(root);

        Mockito.when(importedSituationService.searchRSQL(importedMasterStatusRepository,
                request, ImportedMasterStatusEntity.class, "cd")).thenReturn(result);

        Page<ImportedMasterStatusEntity> importedMasterStatusEntities = importedSituationService.list(request);
        Assertions.assertThat(importedMasterStatusEntities).isEqualTo(result);
    }

    @Test
    public void getRequestStatusTest_With_Success() {

        List<ImportedMasterStatusEntity> importedMasterStatusEntities = new ArrayList<>();
        ImportedMasterStatusEntity importedMasterStatusEntity = new ImportedMasterStatusEntity();
        importedMasterStatusEntity.setCd(Long.valueOf(2));
        importedMasterStatusEntity.setNumber((byte) 3);
        importedMasterStatusEntity.setImportedTableName("mst_rel_yakushoku_shain");
        importedMasterStatusEntity.setCreatedAt(LocalDateTime.parse("2020-06-01T06:18:39"));
        importedMasterStatusEntity.setImportedAt(LocalDateTime.parse("2020-06-19T07:20:43"));
        importedMasterStatusEntities.add(importedMasterStatusEntity);

        Page<ImportedMasterStatusEntity> pageImportedMasterStatusEntities = new PageImpl(importedMasterStatusEntities);

        Mockito.when(importedMasterStatusRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageImportedMasterStatusEntities);

        List<ImportedMasterStatusResponse> responses = importedSituationService.getRequestStatus("mst_rel_yakushoku_shain", "3", "2020-07-01", "2020-07-01");
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    public void getRequestStatusTest_With_Fail() {

        try {
            Mockito.when(importedMasterStatusRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenThrow(new RuntimeException(""));

            List<ImportedMasterStatusResponse> responses = importedSituationService.getRequestStatus(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any());
        } catch (Exception ex) {
            Assertions.assertThat(ex).hasMessage("無効なリクエストです。リクエスト内容を確認してください。");
        }
    }

}
