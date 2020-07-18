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
@Table(name = "holdings_for_request_exhibition_promotion", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class HoldingsForRequestExhibitionPromotionEntity extends BaseTimeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd")
    private Long cd;

    @Column(name = "start_on_of_event")
    private Date startOnOfEvent;

    @Column(name = "end_on_of_event")
    private Date endOnOfEvent;

    @Column(name = "placement_name_of_event")
    private String placementNameOfEvent;

    @Column(name = "placement_address_of_event")
    private String placementAddressOfEvent;

    @Column(name = "sort_number")
    private Long sortNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "request_exhibition_promotion_cd")
    private RequestExhibitionPromotionsEntity requestExhibitionPromotionsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoldingsForRequestExhibitionPromotionEntity that = (HoldingsForRequestExhibitionPromotionEntity) o;
        return cd == that.cd &&
                Objects.equals(startOnOfEvent, that.startOnOfEvent) &&
                Objects.equals(endOnOfEvent, that.endOnOfEvent) &&
                Objects.equals(placementNameOfEvent, that.placementNameOfEvent) &&
                Objects.equals(placementAddressOfEvent, that.placementAddressOfEvent) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cd, startOnOfEvent, endOnOfEvent, placementNameOfEvent, placementAddressOfEvent, createdAt, updatedAt);
    }
}
