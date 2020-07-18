package sskf.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "implementation_stores_for_request_mannequin_promotion", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class ImplementationStoresForRequestMannequinPromotionEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "store_name_of_event")
    private String storeNameOfEvent;

    @Column(name = "run_event_on")
    private Date runEventOn;

    @Column(name = "sort_number")
    private Long sortNumber;

    @ManyToOne
    @JoinColumn(name = "request_mannequin_promotion_cd")
    private RequestMannequinPromotionsEntity requestMannequinPromotionsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImplementationStoresForRequestMannequinPromotionEntity that = (ImplementationStoresForRequestMannequinPromotionEntity) o;
        return cd == that.cd &&
                Objects.equals(storeNameOfEvent, that.storeNameOfEvent) &&
                Objects.equals(runEventOn, that.runEventOn) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, storeNameOfEvent, runEventOn, createdAt, updatedAt);
    }
}
