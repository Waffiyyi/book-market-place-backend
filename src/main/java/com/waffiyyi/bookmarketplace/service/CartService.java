package com.waffiyyi.bookmarketplace.service;

import com.waffiyyi.bookmarketplace.dtos.AddCartItemRequest;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.CartItem;

;

public interface CartService {
    CartItem addItemToCart(AddCartItemRequest req, String jwt);

    CartItem updateCartItemQuantity(Long cartItemId, int quantity);

    Cart removeItemFromCart(Long cartItemId, String jwt);

    Double calculateCartTotal(Cart cart);

    Cart findCartById(Long id);

    Cart findCartByUserId(Long userId);

    Cart clearCart(String jwt);

}
