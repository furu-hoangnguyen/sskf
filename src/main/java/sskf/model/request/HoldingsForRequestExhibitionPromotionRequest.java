package sskf.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
public class HoldingsForRequestExhibitionPromotionRequest {

    private Long cd;

    private Date startOnOfEvent;

    private Date endOnOfEvent;

    private String placementNameOfEvent;

    private String placementAddressOfEvent;
}
