package sskf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sskf.model.request.AccountReceivablesConfirmRequest;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.SalesChargeEditRequest;
import sskf.service.RequestAccountReceivablesService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account-receivables")
@Slf4j
public class RequestAccountReceivablesController {

    @Autowired
    private RequestAccountReceivablesService requestAccountReceivablesService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid AccountReceivablesRequest receivablesRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController create");
            AccountReceivablesRequest responses = requestAccountReceivablesService.create(receivablesRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController create");
        }

    }


    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> updateCreate(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid AccountReceivablesRequest receivablesRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController updateCreate");
            AccountReceivablesRequest responses = requestAccountReceivablesService.updateCreate(receivablesRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController updateCreate");
        }

    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<?> getByRequestId(@PathVariable Long requestId, @RequestParam(required = false) String mode) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController getByRequestId");
            AccountReceivablesRequest responses = requestAccountReceivablesService.getByRequestId(requestId, mode);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController getByRequestId");
        }

    }

    @PutMapping(value = "/confirm")
    public ResponseEntity<?> confirm(@RequestBody AccountReceivablesConfirmRequest receivablesConfirmRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController confirm");
            AccountReceivablesRequest responses = requestAccountReceivablesService.confirm(receivablesConfirmRequest);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController confirm");
        }

    }

    @PutMapping(value = "/update",consumes = "multipart/form-data")
    public ResponseEntity<?> update(@RequestPart("files") MultipartFile[] files,
                                    @RequestPart("receivablesRequest") @Valid AccountReceivablesRequest receivablesRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController update");
            AccountReceivablesRequest responses = requestAccountReceivablesService.update(receivablesRequest, files);
            return ResponseEntity.ok(responses);
        } finally {
            log.info("Log end API: RequestAccountReceivablesController update");
        }

    }

    @PutMapping(value = "/request-sales-charge-edit")
    public ResponseEntity<?> requestSalesChargeEdit(@RequestBody SalesChargeEditRequest salesChargeEditRequest) {
        try {
            log.info("Log begin API: RequestAccountReceivablesController requestSalesChargeEdit");
            requestAccountReceivablesService.requestSalesChargeEdit(salesChargeEditRequest);
            return ResponseEntity.ok("requestSalesChargeEdit success");
        } finally {
            log.info("Log end API: RequestAccountReceivablesController requestSalesChargeEdit");
        }

    }

}
