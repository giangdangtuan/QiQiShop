package com.app85soft.qiqishop.repositories.order;

import com.app85soft.qiqishop.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {
    boolean existsByUserId(int userId);
}
