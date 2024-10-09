package com.waffiyyi.bookmarketplace.repository;

import com.waffiyyi.bookmarketplace.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
   Cart findByCustomerId(Long id);
}
