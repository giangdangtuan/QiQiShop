package com.app85soft.qiqishop.repositories.cart;

import com.app85soft.qiqishop.entities.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>, CartRepositoryCustom {
    Cart findByUserId(Integer userId);
}
