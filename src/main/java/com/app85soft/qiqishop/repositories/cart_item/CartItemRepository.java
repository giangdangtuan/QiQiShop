package com.app85soft.qiqishop.repositories.cart_item;

import com.app85soft.qiqishop.entities.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>, CartItemRepositoryCustom {
    CartItem findByCartIdAndModelId(Integer cardId, Integer modelId);
    List<CartItem> findAllByCartId(Integer cartId);
}
