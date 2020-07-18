package sskf.service;

import org.springframework.web.multipart.MultipartFile;
import sskf.model.request.AccountReceivablesConfirmRequest;
import sskf.model.request.AccountReceivablesRequest;
import sskf.model.request.SalesChargeEditRequest;

public interface RequestAccountReceivablesService {

    AccountReceivablesRequest create(AccountReceivablesRequest receivablesRequest, MultipartFile[] files);

    AccountReceivablesRequest updateCreate(AccountReceivablesRequest receivablesRequest, MultipartFile[] files);

    AccountReceivablesRequest getByRequestId(Long requestId, String mode);

    AccountReceivablesRequest confirm(AccountReceivablesConfirmRequest receivablesConfirmRequest);

    AccountReceivablesRequest update(AccountReceivablesRequest receivablesRequest, MultipartFile[] files);

    void requestSalesChargeEdit(SalesChargeEditRequest salesChargeEditRequest);
}
