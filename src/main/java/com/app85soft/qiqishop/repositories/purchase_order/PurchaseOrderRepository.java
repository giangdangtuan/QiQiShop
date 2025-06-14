package com.app85soft.qiqishop.repositories.purchase_order;

import com.app85soft.qiqishop.entities.purchase_orders.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer>, PurchaseOrderRepositoryCustom {
}
