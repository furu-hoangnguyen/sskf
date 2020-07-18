package sskf.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterSelectOptionResponse {
    public FilterSelectOptionResponse(List<MstYakushokuResponse> positions, List<BumonResponse> departments) {
        this.positions = positions;
        this.departments = departments;
    }
    private List<MstYakushokuResponse> positions;
    private List<BumonResponse> departments;
}
