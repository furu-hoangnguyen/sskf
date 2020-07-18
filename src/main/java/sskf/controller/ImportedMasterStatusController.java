package sskf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.response.ImportedMasterStatusResponse;
import sskf.service.master.ImportedSituationService;

import java.util.List;

@RestController
public class ImportedMasterStatusController {

    @Autowired
    private ImportedSituationService importedSituationService;

    @GetMapping("api/get-imported-master-status")
    public ResponseEntity<List<ImportedMasterStatusResponse>> getRequestStatus(@RequestParam(value = "table_names", required = false) String importedTableName,
                                                                               @RequestParam(value = "step_number", required = false) String number,
                                                                               @RequestParam(value = "from_date", required = false) String fromCreatedAt,
                                                                               @RequestParam(value = "to_date", required = false) String toCreatedAt) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("request_status", "200");
        List<ImportedMasterStatusResponse> importedMasterStatusResponses = importedSituationService.getRequestStatus(importedTableName, number, fromCreatedAt, toCreatedAt);

        return ResponseEntity.ok().headers(responseHeaders).body(importedMasterStatusResponses);
    }

}
