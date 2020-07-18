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
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shain_addresses", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class ShainAddressEntity extends BaseTimeModel {

    @Id
    @Column(name = "shain_cd")
    private String shainCd;

    @Column(name = "mail_address")
    private String mailAddress;

    @OneToOne(mappedBy = "shainAddressEntity")
    private ShainEntity shainEntity;

}
