package sskf.specs;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import sskf.model.entity.ImportedMasterStatusEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ImportedMasterStatusSpecification {

    public static Specification<ImportedMasterStatusEntity> hasImportedTableName(String importedTableName) {

        if (StringUtils.isEmpty(importedTableName)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("importedTableName"), importedTableName);
    }

    public static Specification<ImportedMasterStatusEntity> hasNumber(Byte number) {

        if (ObjectUtils.isEmpty(number)) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("number"), number);
    }

    public static Specification<ImportedMasterStatusEntity> hasCreatedAt(Date fromCreatedAt, Date toCreatedAt) {
        LocalDateTime fromCreatedAtLocalDateTime = !ObjectUtils.isEmpty(fromCreatedAt) ? fromCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
        LocalDateTime toCreatedAtLocalDateTime = !ObjectUtils.isEmpty(toCreatedAt) ? toCreatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

        if (!ObjectUtils.isEmpty(fromCreatedAtLocalDateTime) && ObjectUtils.isEmpty(toCreatedAtLocalDateTime)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromCreatedAtLocalDateTime);
        } else if (ObjectUtils.isEmpty(fromCreatedAtLocalDateTime) && !ObjectUtils.isEmpty(toCreatedAtLocalDateTime)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toCreatedAtLocalDateTime);
        } else if (!ObjectUtils.isEmpty(fromCreatedAtLocalDateTime) && !ObjectUtils.isEmpty(toCreatedAtLocalDateTime)) {

            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), fromCreatedAtLocalDateTime, toCreatedAtLocalDateTime);
        }

        return null;
    }

}
