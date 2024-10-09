package com.waffiyyi.bookmarketplace.dtos;

import com.waffiyyi.bookmarketplace.entities.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String jwt;
    private String message;
    private User user;


}
