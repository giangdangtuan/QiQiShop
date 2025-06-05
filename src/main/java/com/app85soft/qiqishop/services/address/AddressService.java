package com.app85soft.qiqishop.services.address;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.address.AddAddressReq;
import com.app85soft.qiqishop.dto.request.address.SetDefaultAddressReq;
import com.app85soft.qiqishop.dto.request.address.UpdateAddressReq;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.entities.address.Address;

import java.util.List;

public interface AddressService {
    void syncAll();

    AddressRes addAddress(AddAddressReq req);

    Address setDefaultAddress(SetDefaultAddressReq req);

    List<AddressRes> updateAddress(UpdateAddressReq request);

    List<AddressRes> getAddresses();

    List<AddressRes> deleteAddresses(IdsRequest request);
}
