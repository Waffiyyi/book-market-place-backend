package com.waffiyyi.bookmarketplace.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private Long id;
   private String title;
   private String author;
   @ElementCollection
   private List<String> categories;
   private String genre;
   private Date dateReleased;
   private float rating = 0.0F;
   private String description;
   private Double price;
   private String image;
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private boolean inStock = true;
}