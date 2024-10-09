package com.waffiyyi.bookmarketplace.service;



import com.waffiyyi.bookmarketplace.dtos.AuthResponse;
import com.waffiyyi.bookmarketplace.dtos.LoginDTO;
import com.waffiyyi.bookmarketplace.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;


public interface UserService {
  ResponseEntity<AuthResponse> register(User user);
  ResponseEntity<AuthResponse> loginUser(LoginDTO req);
   User findUserByEmail(String email);
  User findUserByJWTToken(String jwt);


}
