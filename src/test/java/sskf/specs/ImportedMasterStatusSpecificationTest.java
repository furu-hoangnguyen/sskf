package sskf.specs;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ObjectUtils;
import sskf.model.entity.ImportedMasterStatusEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static sskf.specs.ImportedMasterStatusSpecification.hasCreatedAt;
import static sskf.specs.ImportedMasterStatusSpecification.hasImportedTableName;
import static sskf.specs.ImportedMasterStatusSpecification.hasNumber;

@ExtendWith(SpringExtension.class)
public class ImportedMasterStatusSpecificationTest {

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Mock
    private CriteriaQuery queryMock;

    @Mock
    private Root<ImportedMasterStatusEntity> rootMock;

    @Test
    public void hasImportedTableName_With_Success1() {
        String importedTableName = "mst_tanto";

        Path importedTableNamePathMock = mock(Path.class);;
        when(rootMock.get("importedTableName")).thenReturn(importedTableNamePathMock);

        Predicate importedTableNamePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.equal(rootMock.get("importedTableName"), importedTableName)).thenReturn(importedTableNamePredicateMock);

        Specification<ImportedMasterStatusEntity> actual = hasImportedTableName(importedTableName);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(importedTableNamePredicateMock);
    }

    @Test
    public void hasImportedTableName_With_Success2() {
        Specification<ImportedMasterStatusEntity> actual = hasImportedTableName(null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasNumber_With_Success1() {
        Byte number = Byte.valueOf("3");

        Path numberPathMock = mock(Path.class);
        when(rootMock.get("number")).thenReturn(numberPathMock);

        Predicate numberPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.equal(rootMock.get("number"), number)).thenReturn(numberPredicateMock);

        Specification<ImportedMasterStatusEntity> actual = hasNumber(number);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(numberPredicateMock);
    }

    @Test
    public void hasNumber_With_Success2() {
        Specification<ImportedMasterStatusEntity> actual = hasNumber(null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

    @Test
    public void hasCreatedAt_With_Success1() {
        Date fromCreatedAt = new Date();
        LocalDateTime fromCreatedAtLocalDateTime = !ObjectUtils.isEmpty(fromCreatedAt) ? fromCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        Path fromCreatedAtPathMock = mock(Path.class);
        when(rootMock.get("createdAt")).thenReturn(fromCreatedAtPathMock);

        Predicate fromCreatedAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.greaterThanOrEqualTo(rootMock.get("createdAt"), fromCreatedAtLocalDateTime)).thenReturn(fromCreatedAtPredicateMock);

        Specification<ImportedMasterStatusEntity> actual = hasCreatedAt(fromCreatedAt, null);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(fromCreatedAtPredicateMock);
    }

    @Test
    public void hasCreatedAt_With_Success2() {
        Date toCreatedAt = new Date();
        LocalDateTime toCreatedAtLocalDateTime = !ObjectUtils.isEmpty(toCreatedAt) ? toCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        Path toCreatedAtPathMock = mock(Path.class);
        when(rootMock.get("createdAt")).thenReturn(toCreatedAtPathMock);

        Predicate toCreatedAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.lessThanOrEqualTo(rootMock.get("createdAt"), toCreatedAtLocalDateTime)).thenReturn(toCreatedAtPredicateMock);

        Specification<ImportedMasterStatusEntity> actual = hasCreatedAt(null, toCreatedAt);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(toCreatedAtPredicateMock);
    }

    @Test
    public void hasCreatedAt_With_Success3() {
        Date fromCreatedAt = new Date();
        LocalDateTime fromCreatedAtLocalDateTime = !ObjectUtils.isEmpty(fromCreatedAt) ? fromCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        Date toCreatedAt = new Date();
        LocalDateTime toCreatedAtLocalDateTime = !ObjectUtils.isEmpty(toCreatedAt) ? toCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        Predicate createdAtPredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.between(rootMock.get("createdAt"), fromCreatedAtLocalDateTime, toCreatedAtLocalDateTime)).thenReturn(createdAtPredicateMock);

        Specification<ImportedMasterStatusEntity> actual = hasCreatedAt(fromCreatedAt, toCreatedAt);
        Predicate actualPredicate = actual.toPredicate(rootMock, queryMock, criteriaBuilderMock);

        Assertions.assertThat(actualPredicate).isEqualTo(createdAtPredicateMock);
    }

    @Test
    public void hasCreatedAt_With_Success4() {
        Specification<ImportedMasterStatusEntity> actual = hasCreatedAt(null, null);
        Assertions.assertThat(actual).isEqualTo(null);
    }

}
