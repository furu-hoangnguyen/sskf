package sskf.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ImportedMasterStatusResponse {

    private Long cd;

    @JsonProperty("imported_table_name")
    private String importedTableName;

    @JsonProperty("step_number")
    private Byte number;

    @JsonProperty("imported_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date importedAt;

    @JsonProperty("scheduled_imported_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;

}
