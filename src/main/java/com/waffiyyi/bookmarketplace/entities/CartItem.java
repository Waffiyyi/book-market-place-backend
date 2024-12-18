package com.waffiyyi.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long Id;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    private int quantity;

    private Double totalPrice;

   @Override
   public String toString() {
      return "CartItem{" +
        "id=" + Id +
        ", bookTitle='" + book.getTitle() + '\'' +
        ", quantity=" + quantity +
        ", totalPrice=" + totalPrice +
        '}';
   }

}
