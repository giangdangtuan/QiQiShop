package com.app85soft.qiqishop.repositories.stock_batch;

import com.app85soft.qiqishop.entities.purchase_orders.StockBatch;

import java.util.List;

public interface StockBatchRepositoryCustom {
    List<StockBatch> findAvailableBatchesByModelId(int modelId);
}
