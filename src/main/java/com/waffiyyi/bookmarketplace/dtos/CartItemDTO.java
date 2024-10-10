package com.waffiyyi.bookmarketplace.dtos;

import com.waffiyyi.bookmarketplace.entities.Book;
import lombok.Data;

@Data
public class CartItemDTO {
   private Long id;
   private Book book;
   private String bookTitle;
   private int quantity;
   private double totalPrice;
}
