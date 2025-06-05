package com.app85soft.qiqishop.repositories.variant;

import com.app85soft.qiqishop.entities.model.QVariantOption;
import com.app85soft.qiqishop.entities.model.QVariantValue;
import com.app85soft.qiqishop.entities.model.VariantValue;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VariantValueRepositoryImpl  extends BaseRepository implements VariantValueRepositoryCustom {
    private final QVariantValue qVariantValue = QVariantValue.variantValue;
    private final QVariantOption qVariantOption = QVariantOption.variantOption;
    private final QProduct qProduct = QProduct.product;

    @Override
    public List<VariantValue> findByProductId(Integer productId) {
        return query()
                .selectFrom(qVariantValue)
                .join(qVariantOption)
                .on(qVariantValue.optionTypeId.eq(qVariantOption.id))
                .where(
                        qVariantOption.productId.eq(productId),
                        qVariantValue.deleted.eq(false),
                        qVariantOption.deleted.eq(false)
                )
                .fetch();
    }
}
