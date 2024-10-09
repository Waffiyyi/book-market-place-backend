package com.waffiyyi.bookmarketplace.dtos;

import lombok.*;

@Getter
@Setter
public class CategoryDTO {
   private String categories;
   private Double rating;

   public CategoryDTO(String categories, Double rating) {
      this.categories = categories;
      this.rating = rating;
   }
}
