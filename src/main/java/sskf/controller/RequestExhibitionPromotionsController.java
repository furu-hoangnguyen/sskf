package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sskf.model.request.ExhibitionPromotionsRequest;
import sskf.service.RequestExhibitionPromotionsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/exhibition-promotions")
@Slf4j
public class RequestExhibitionPromotionsController {

    @Autowired
    private RequestExhibitionPromotionsService requestExhibitionPromotionsService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid ExhibitionPromotionsRequest exhibitionPromotionsRequest) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsController create");
            ExhibitionPromotionsRequest responses = requestExhibitionPromotionsService.create(exhibitionPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsController create");
        }
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> updateCreate(@RequestPart("files") MultipartFile[] files,
                                          @RequestPart("receivablesRequest") @Valid ExhibitionPromotionsRequest exhibitionPromotionsRequest) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsController updateCreate");
            ExhibitionPromotionsRequest responses = requestExhibitionPromotionsService.updateCreate(exhibitionPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsController updateCreate");
        }

    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<?> getByRequestId(@PathVariable Long requestId, @RequestParam(required = false) String mode) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsController getByRequestId");
            ExhibitionPromotionsRequest responses = requestExhibitionPromotionsService.getByRequestId(requestId, mode);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsController getByRequestId");
        }

    }

    @PutMapping(value = "/update",consumes = "multipart/form-data")
    public ResponseEntity<?> update(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid ExhibitionPromotionsRequest exhibitionPromotionsRequest) {
        try {
            log.info("Log begin API: RequestExhibitionPromotionsController update");
            ExhibitionPromotionsRequest responses = requestExhibitionPromotionsService.update(exhibitionPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestExhibitionPromotionsController update");
        }

    }
}
