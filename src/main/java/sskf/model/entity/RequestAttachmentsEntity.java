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
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_attachments", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestAttachmentsEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_attachment_cd")
    private Long requestAttachmentCd;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(name = "displayed_file_name")
    private String displayedFileName;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestAttachmentsEntity that = (RequestAttachmentsEntity) o;
        return requestAttachmentCd == that.requestAttachmentCd &&
                Objects.equals(attachmentPath, that.attachmentPath) &&
                Objects.equals(displayedFileName, that.displayedFileName) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestAttachmentCd, attachmentPath, displayedFileName, createdAt);
    }
}
