package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;
import sskf.model.entity.RequestAccountsReceivableDetailsEntity;
import sskf.model.entity.RequestAccountsReceivablesEntity;
import sskf.model.request.AccountReceivablesConfirmRequest;
import sskf.model.request.AccountReceivablesRequest;

import java.util.List;

public interface RequestAccountsReceivableDetailsService {

    void create(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, AccountReceivablesRequest accountReceivablesRequest, String status);

    void update(RequestAccountsReceivablesEntity requestAccountsReceivablesEntity, AccountReceivablesRequest accountReceivablesRequest, String status);

    void confirm(AccountReceivablesConfirmRequest receivablesConfirmRequest);

    List<RequestAccountsReceivableDetailsEntity> list(BaseSearchRequest baseSearchRequest);
}
