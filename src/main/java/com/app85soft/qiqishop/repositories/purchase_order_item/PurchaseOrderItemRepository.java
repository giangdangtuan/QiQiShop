package com.app85soft.qiqishop.repositories.purchase_order_item;

import com.app85soft.qiqishop.entities.purchase_orders.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Integer>, PurchaseOrderItemRepositoryCustom {
}
