package com.app85soft.qiqishop.repositories.address;

import com.app85soft.qiqishop.entities.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>, AddressRepositoryCustom {
}
