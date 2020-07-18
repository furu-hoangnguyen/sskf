package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request_mannequin_promotions", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class RequestMannequinPromotionsEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "subject")
    private String subject;

    @Column(name = "store_g_cd")
    private String storeGCd;

    @Column(name = "store_g_nm")
    private String storeGNm;

    @Column(name = "content_of_implementation_stores")
    private String contentOfImplementationStores;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_cd")
    private RequestsEntity requestsEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "requestMannequinPromotionsEntity", fetch = FetchType.LAZY)
    private Set<RequestMannequinPromotionDetailsEntity> requestMannequinPromotionDetailsEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "requestMannequinPromotionsEntity", fetch = FetchType.LAZY)
    private Set<ImplementationStoresForRequestMannequinPromotionEntity> implementationStoresForRequestMannequinPromotionEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMannequinPromotionsEntity that = (RequestMannequinPromotionsEntity) o;
        return cd == that.cd &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(storeGCd, that.storeGCd) &&
                Objects.equals(storeGNm, that.storeGNm) &&
                Objects.equals(contentOfImplementationStores, that.contentOfImplementationStores) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, subject, storeGCd, storeGNm, contentOfImplementationStores, createdAt, updatedAt);
    }
}
