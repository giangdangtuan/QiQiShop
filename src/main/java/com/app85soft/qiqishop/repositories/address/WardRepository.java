package com.app85soft.qiqishop.repositories.address;

import com.app85soft.qiqishop.entities.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    Optional<Ward> findByCode(String code);
    List<Ward> findAllByDistrictId(int districtId);
}
