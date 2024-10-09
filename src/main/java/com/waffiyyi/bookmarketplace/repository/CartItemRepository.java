package com.waffiyyi.bookmarketplace.repository;

import com.waffiyyi.bookmarketplace.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
