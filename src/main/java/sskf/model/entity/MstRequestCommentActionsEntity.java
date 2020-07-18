package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_request_comment_actions", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstRequestCommentActionsEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "name")
    private String name;

    @Column(name = "auto_comment")
    private String autoComment;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy="mstRequestCommentActionsEntity", fetch = FetchType.LAZY)
    private Set<OperationHistoriesEntity> operationHistoriesEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MstRequestCommentActionsEntity that = (MstRequestCommentActionsEntity) o;
        return cd == that.cd &&
                Objects.equals(name, that.name) &&
                Objects.equals(autoComment, that.autoComment) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, name, autoComment, createdAt);
    }
}
