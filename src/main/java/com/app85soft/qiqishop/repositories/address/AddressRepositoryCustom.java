package com.app85soft.qiqishop.repositories.address;

import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.entities.address.Address;

import java.util.List;

public interface AddressRepositoryCustom {
    Address getAddressToUpdate(int addressId, int userId);

    Address getAddressToUnsetDefault(int userId);

    List<Address> findByUserIdAndDeletedFalse(int userId);

    List<AddressRes> getAddresses(int userId);

    AddressRes getAddress(int addressId, int userId);

    AddressRes getDefaultAddress(int userId);

    void deleteAddresses(List<Integer> addressIds);

    List<Integer> getAllIdToCheckExist(List<Integer> addressIds, int userId);

}
