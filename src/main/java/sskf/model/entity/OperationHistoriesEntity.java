package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "operation_histories", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class OperationHistoriesEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "is_done_by_system")
    private Byte isDoneBySystem;

    @Column(name = "bumon_nm")
    private String bumonNm;

    @Column(name = "shain_nm")
    private String shainNm;

    @Column(name = "is_deputy")
    private Boolean isDeputy;

    @Column(name = "comment")
    private String comment;

    @Column(name ="step_number")
    private Byte stepNumber;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mst_request_comment_action_cd")
    private MstRequestCommentActionsEntity mstRequestCommentActionsEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_operation_history_cd")
    private OperationHistoriesEntity parentOperationHistoriesEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "parentOperationHistoriesEntity", fetch = FetchType.LAZY)
    private Set<OperationHistoriesEntity> childrenOperationHistoriesEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "operationHistoriesEntity", fetch = FetchType.LAZY)
    private Set<CommentsForDetailsEntity> commentsForDetailsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationHistoriesEntity that = (OperationHistoriesEntity) o;
        return cd == that.cd &&
                Objects.equals(isDoneBySystem, that.isDoneBySystem) &&
                Objects.equals(bumonNm, that.bumonNm) &&
                Objects.equals(shainNm, that.shainNm) &&
                Objects.equals(isDeputy, that.isDeputy) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, isDoneBySystem, bumonNm, shainNm, isDeputy, comment, createdAt);
    }
}
