package sskf.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sskf.model.basemodel.BaseTimeModel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mst_rel_approvalflows_systems", schema = "ssk_accounts_receivable")
@EntityListeners(AuditingEntityListener.class)
public class MstRelApprovalFlowsSystemsEntity extends BaseTimeModel {

    @EmbeddedId
    private MstRelApprovalFlowsSystemsEntityPK mstRelApprovalflowsSystemsEntityPK;

}
