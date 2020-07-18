package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_accounts_receivables", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestAccountsReceivablesEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "target_on")
    private Date targetOn;

    @Column(name = "purpose")
    private Byte purpose;

    @Column(name = "purpose_of_others")
    private String purposeOfOthers;

    @Column(name = "commission_type")
    private Byte commissionType;

    @Column(name = "remarks")
    private String remarks;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "requestAccountsReceivablesEntity", fetch = FetchType.LAZY)
    private Set<RequestAccountsReceivableDetailsEntity> requestAccountsReceivableDetailsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAccountsReceivablesEntity that = (RequestAccountsReceivablesEntity) o;
        return cd == that.cd &&
                purpose == that.purpose &&
                commissionType == that.commissionType &&
                Objects.equals(targetOn, that.targetOn) &&
                Objects.equals(purposeOfOthers, that.purposeOfOthers) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, targetOn, purpose, purposeOfOthers, commissionType, remarks, createdAt, updatedAt);
    }
}
