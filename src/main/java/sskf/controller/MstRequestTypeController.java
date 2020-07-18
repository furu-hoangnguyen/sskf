package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.response.MstRequestTypesResponse;
import sskf.service.MstRequestTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/mst-request-type")
@Slf4j
public class MstRequestTypeController {

    @Autowired
    private MstRequestTypeService mstRequestTypeService;
    @GetMapping("/list")
    public ResponseEntity<?> list(BaseSearchRequest searchRequest) {
        try {
            log.info("Log begin API: /mst-request-type/list");
            List<MstRequestTypesResponse> responses = mstRequestTypeService.list(searchRequest);
            return ResponseEntity.ok(responses);

        } finally {
            log.info("Log end API: /mst-request-type/list");
        }

    }
}
