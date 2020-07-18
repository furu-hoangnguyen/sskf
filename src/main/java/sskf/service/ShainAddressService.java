package sskf.service;

import sskf.model.basemodel.BaseSearchRequest;

import java.util.Set;

public interface ShainAddressService {

    Set<String> listEmail(BaseSearchRequest baseSearchRequest);
}
