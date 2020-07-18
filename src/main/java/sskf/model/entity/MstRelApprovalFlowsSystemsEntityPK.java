package sskf.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class MstRelApprovalFlowsSystemsEntityPK implements Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "approvalflow_cd")
    private MstApprovalFlowsEntity mstApprovalFlowsEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "system_cd")
    private MstSystemsEntity mstSystemsEntity;

}
