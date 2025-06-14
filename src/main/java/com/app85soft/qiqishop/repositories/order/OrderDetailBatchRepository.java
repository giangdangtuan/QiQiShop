package com.app85soft.qiqishop.repositories.order;

import com.app85soft.qiqishop.entities.order.OrderDetailBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailBatchRepository extends JpaRepository<OrderDetailBatch, Integer> {
}
