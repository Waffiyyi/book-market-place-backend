package com.waffiyyi.bookmarketplace.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
   private Long id;
   private Long userId;
   private double total;
   private List<CartItemDTO> items;
}
