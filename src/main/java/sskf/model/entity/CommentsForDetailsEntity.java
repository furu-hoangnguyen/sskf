package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments_for_details", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class CommentsForDetailsEntity extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "is_duplicated")
    private Byte isDuplicated;

    @Column(name = "is_capable_of_being_deleted")
    private Byte isCapableOfBeingDeleted;

    @Column(name = "shain_nm")
    private String shainNm;

    @Column(name = "bumon_nm")
    private String bumonNm;

    @Column(name = "is_deputy")
    private Byte isDeputy;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operation_history_cd")
    private OperationHistoriesEntity operationHistoriesEntity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_accounts_receivable_detail_cd")
    private RequestAccountsReceivableDetailsEntity requestAccountsReceivableDetailsEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

    public CommentsForDetailsEntity() {
        this.isDeleted = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentsForDetailsEntity that = (CommentsForDetailsEntity) o;
        return cd == that.cd &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(isDuplicated, that.isDuplicated) &&
                Objects.equals(isCapableOfBeingDeleted, that.isCapableOfBeingDeleted) &&
                Objects.equals(shainNm, that.shainNm) &&
                Objects.equals(bumonNm, that.bumonNm) &&
                Objects.equals(isDeputy, that.isDeputy) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, isDeleted, isDuplicated, isCapableOfBeingDeleted, shainNm, bumonNm, isDeputy, comment, createdAt);
    }
}
