package com.app85soft.qiqishop.repositories.stock_batch;

import com.app85soft.qiqishop.entities.purchase_orders.QStockBatch;
import com.app85soft.qiqishop.entities.purchase_orders.StockBatch;
import com.app85soft.qiqishop.repositories.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class StockBatchRepositoryImpl extends BaseRepository implements StockBatchRepositoryCustom {
    private final QStockBatch qStockBatch = QStockBatch.stockBatch;

    @Override
    public List<StockBatch> findAvailableBatchesByModelId(int modelId) {
        return query().from(qStockBatch)
                .where(
                        qStockBatch.modelId.eq(modelId),
                        qStockBatch.quantityRemaining.gt(0),
                        qStockBatch.deleted.eq(false)
                )
                .select(qStockBatch)
                .orderBy(qStockBatch.createdAt.asc())
                .fetch();
    }
}
