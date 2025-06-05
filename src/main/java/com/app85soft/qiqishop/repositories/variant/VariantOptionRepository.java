package com.app85soft.qiqishop.repositories.variant;

import com.app85soft.qiqishop.entities.model.VariantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantOptionRepository extends JpaRepository<VariantOption, Integer> {
    List<VariantOption> findByProductId(Integer productId);
}
