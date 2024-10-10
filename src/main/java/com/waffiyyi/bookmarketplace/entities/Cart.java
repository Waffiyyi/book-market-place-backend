package com.waffiyyi.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private Long Id;
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)

   @OneToOne
   private User customer;

   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private Double total;

   @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnore
   private List<CartItem> items = new ArrayList<>();

   @Override
   public String toString() {
      return "Cart{" +
        "id=" + Id +
        ", total=" + total +
        ", items=" + items.size() +
        '}';
   }
}
