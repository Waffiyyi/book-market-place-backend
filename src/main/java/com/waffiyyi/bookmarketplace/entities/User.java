package com.waffiyyi.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "users")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Schema(accessMode = Schema.AccessMode.READ_ONLY)
   private Long id;
   private String username;
   private String email;
   @JsonIgnore
   private String password;
}
