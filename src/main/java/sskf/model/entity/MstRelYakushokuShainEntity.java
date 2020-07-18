package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mst_rel_yakushoku_shain", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstRelYakushokuShainEntity extends BaseTimeModel {

    @EmbeddedId
    private MstRelYakushokuShainEntityPK mstRelYakushokuShainEntityPK;

    @JsonIgnore
    @ManyToOne
    @MapsId(value = "yakusyoku_cd")
    @JoinColumn(name = "yakusyoku_cd")
    private MstYakushokuEntity mstYakushokuEntity;

    @JsonIgnore
    @ManyToOne
    @MapsId("shain_cd")
    @JoinColumn(name = "shain_cd")
    private ShainEntity shainEntity;

}
