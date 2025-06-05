package com.app85soft.qiqishop.repositories.variant;

import com.app85soft.qiqishop.entities.model.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, Integer>, VariantValueRepositoryCustom {
    List<VariantValue> findByOptionTypeId(Integer optionTypeId);
}
