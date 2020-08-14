package com.bestseller.starbux.repository;

import com.bestseller.starbux.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByCartItemId(long itemId);
}
