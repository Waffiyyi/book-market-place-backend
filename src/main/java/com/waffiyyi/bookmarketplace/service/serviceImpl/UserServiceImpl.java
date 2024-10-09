package com.waffiyyi.bookmarketplace.service.serviceImpl;

import com.waffiyyi.bookmarketplace.config.JwtProvider;
import com.waffiyyi.bookmarketplace.dtos.AuthResponse;
import com.waffiyyi.bookmarketplace.dtos.LoginDTO;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.User;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.exception.UserNotFoundException;
import com.waffiyyi.bookmarketplace.repository.CartRepository;
import com.waffiyyi.bookmarketplace.repository.UserRepository;
import com.waffiyyi.bookmarketplace.service.UserService;
import com.waffiyyi.bookmarketplace.validations.EmailValidator;
import com.waffiyyi.bookmarketplace.validations.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final JwtProvider jwtProvider;
   private final CustomUserDetailsService customUserDetailsService;
   private final CartRepository cartRepository;


   @Override
   @Transactional
   public ResponseEntity<AuthResponse> register(User user) {
      log.info("user"+user);
      if (!EmailValidator.isValid(user.getEmail())) {
         throw new BadRequestException("Invalid email format", HttpStatus.BAD_REQUEST);
      }

      if (!PasswordValidator.isValid(user.getPassword())) {
         throw new BadRequestException(
           "Invalid password format, password must include at " +
             "least one uppercase letter, one lowercase letter, and one digit ",
           HttpStatus.BAD_REQUEST);
      }

      User isEmailExist = userRepository.findByEmail(user.getEmail());
      if (isEmailExist != null) {
         throw new BadRequestException("Email is already used with another account",
                                       HttpStatus.BAD_REQUEST);
      }
      if (user.getUsername() == null) {
         throw new BadRequestException("Please enter your username",
                                       HttpStatus.BAD_REQUEST);

      }
      Optional<User> username = userRepository.findByUsername(user.getUsername());
      if (username.isPresent()) {
         throw new BadRequestException("Username taken", HttpStatus.BAD_REQUEST);
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      User savedUser = userRepository.save(user);
      Cart cart = new Cart();
      cart.setCustomer(savedUser);
      cartRepository.save(cart);
      UserDetails userDetails = customUserDetailsService.loadUserByUsername(
        savedUser.getEmail());

      Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);

      String jwt = jwtProvider.generateToken(authentication);

      AuthResponse response = AuthResponse.builder()
                                          .jwt(jwt)
                                          .message("Register success")
                                          .user(savedUser)
                                          .build();

      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @Override

   public ResponseEntity<AuthResponse> loginUser(LoginDTO req) {
      String username = req.getUsername();
      String password = req.getPassword();
      Authentication authentication = authenticate(username, password);
      User user = findUserByEmail(username);


      String jwt = jwtProvider.generateToken(authentication);


      AuthResponse response = AuthResponse.builder()
                                          .jwt(jwt)
                                          .message("Login success")
                                          .user(user)
                                          .build();
      return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @Override
   public User findUserByJWTToken(String jwt) {
      String email = jwtProvider.getEmailFromJwtToken(jwt);
      User user = findUserByEmail(email);
      return user;
   }

   @Override
   public User findUserByEmail(String email) {
      User user = userRepository.findByEmail(email);

      if (user == null) {
         throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
      }
      return user;
   }

   private Authentication authenticate(String username, String password) {
      UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

      if (userDetails == null) {
         throw new BadRequestException("Invalid username....", HttpStatus.BAD_REQUEST);
      }
      if (!passwordEncoder.matches(password, userDetails.getPassword())) {
         throw new BadRequestException("Invalid password....", HttpStatus.BAD_REQUEST);
      }
      return new UsernamePasswordAuthenticationToken(userDetails, null,
                                                     userDetails.getAuthorities());
   }
}
