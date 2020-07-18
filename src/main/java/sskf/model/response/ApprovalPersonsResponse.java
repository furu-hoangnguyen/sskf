package sskf.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ApprovalPersonsResponse {
    List<ApprovalPerson> applyPersons;
    List<ApprovalPerson> firstApprovalPersons;
    List<ApprovalPerson> secondApprovalPersons;
    List<ApprovalPerson> thirdApprovalPersons;
    List<ApprovalPerson> settlementApprovalPersons;
}
