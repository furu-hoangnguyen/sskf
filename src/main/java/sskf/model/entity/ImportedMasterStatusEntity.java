package sskf.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imported_master_status", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class ImportedMasterStatusEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "imported_table_name")
    private String importedTableName;

    @Column(name = "number")
    private Byte number;

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportedMasterStatusEntity that = (ImportedMasterStatusEntity) o;
        return cd == that.cd &&
                number == that.number &&
                Objects.equals(importedTableName, that.importedTableName) &&
                Objects.equals(importedAt, that.importedAt) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, importedTableName, number, importedAt, createdAt);
    }
}
