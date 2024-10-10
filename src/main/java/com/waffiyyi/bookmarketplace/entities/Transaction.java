package com.waffiyyi.bookmarketplace.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Transaction {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private Long id;
   private Double amountOfPurchase;
   @OneToMany(fetch = FetchType.EAGER)
   private List<Book> booksPurchased;
   private Date datePurchased;
   @ManyToOne
   private User user;
}
