package com.waffiyyi.bookmarketplace.service;

import com.waffiyyi.bookmarketplace.dtos.AddCartItemRequest;
import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.CartItemDTO;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.CartItem;

;

public interface CartService {
   CartItemDTO addItemToCart(AddCartItemRequest req, String jwt);

    CartItemDTO updateCartItemQuantity(Long cartItemId, int quantity);

    CartDTO removeItemFromCart(Long cartItemId, String jwt);

    Double calculateCartTotal(Cart cart);

    Cart findCartById(Long id);

   CartDTO findCartByUserId(Long userId);

    Cart clearCart(String jwt);

}
