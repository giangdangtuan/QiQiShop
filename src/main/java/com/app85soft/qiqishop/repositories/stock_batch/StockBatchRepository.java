package com.app85soft.qiqishop.repositories.stock_batch;

import com.app85soft.qiqishop.entities.purchase_orders.StockBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBatchRepository extends JpaRepository<StockBatch, Integer>, StockBatchRepositoryCustom {
}
