package com.waffiyyi.bookmarketplace.service.serviceImpl;

import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.CartItemDTO;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.CartItem;

import java.util.stream.Collectors;

public class CartMapper {
   public static CartDTO toDTO(Cart cart) {
      CartDTO dto = new CartDTO();
      dto.setId(cart.getId());
      dto.setUserId(cart.getCustomer().getId());
      dto.setTotal(cart.getTotal());
      dto.setItems(cart.getItems().stream()
                       .map(CartMapper::toCartItemDTO)
                       .collect(Collectors.toList()));
      return dto;
   }

   public static CartItemDTO toCartItemDTO(CartItem cartItem) {
      CartItemDTO dto = new CartItemDTO();
      dto.setId(cartItem.getId());
      dto.setBook(cartItem.getBook());
      dto.setBookTitle(cartItem.getBook().getTitle());
      dto.setQuantity(cartItem.getQuantity());
      dto.setTotalPrice(cartItem.getTotalPrice());
      return dto;
   }
}

