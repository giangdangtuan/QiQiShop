package com.app85soft.qiqishop.repositories.address;

import com.app85soft.qiqishop.entities.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Optional<District> findByCode(String code);
    List<District> findAllByProvinceId(int provinceId);
}
