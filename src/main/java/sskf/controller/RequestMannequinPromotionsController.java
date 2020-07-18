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
import sskf.model.request.MannequinPromotionsRequest;
import sskf.service.RequestMannequinPromotionsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mannequin-promotions")
@Slf4j
public class RequestMannequinPromotionsController {

    @Autowired
    private RequestMannequinPromotionsService requestMannequinPromotionsService;


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid MannequinPromotionsRequest mannequinPromotionsRequest) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsController create");
            MannequinPromotionsRequest responses = requestMannequinPromotionsService.create(mannequinPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsController create");
        }
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> updateCreate(@RequestPart("files") MultipartFile[] files,
                                          @RequestPart("receivablesRequest") @Valid MannequinPromotionsRequest mannequinPromotionsRequest) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsController updateCreate");
            MannequinPromotionsRequest responses = requestMannequinPromotionsService.updateCreate(mannequinPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsController updateCreate");
        }

    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<?> getByRequestId(@PathVariable Long requestId, @RequestParam(required = false) String mode) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsController getByRequestId");
            MannequinPromotionsRequest responses = requestMannequinPromotionsService.getByRequestId(requestId, mode);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsController getByRequestId");
        }

    }

    @PutMapping(value = "/update",consumes = "multipart/form-data")
    public ResponseEntity<?> update(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid MannequinPromotionsRequest mannequinPromotionsRequest) {
        try {
            log.info("Log begin API: RequestMannequinPromotionsController update");
            MannequinPromotionsRequest responses = requestMannequinPromotionsService.update(mannequinPromotionsRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestMannequinPromotionsController update");
        }

    }
}
