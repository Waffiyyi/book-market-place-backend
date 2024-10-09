package com.waffiyyi.bookmarketplace.controller;


import com.waffiyyi.bookmarketplace.dtos.AuthResponse;
import com.waffiyyi.bookmarketplace.dtos.LoginDTO;
import com.waffiyyi.bookmarketplace.entities.User;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.exception.ResourceNotFoundException;
import com.waffiyyi.bookmarketplace.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "This controller manages Auth operations")
@OpenAPIDefinition(info = @Info(title = "AUTH CONTROLLER", version = "1.0",
                                description = "AUTH SERVICE API documentation"))
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  @Operation(summary = "Sign Up", description = "Sign Up")
  @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = AuthResponse.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ResourceNotFoundException.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
  })
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signup(@RequestBody User user) {
    return userService.register(user);
  }

  @Operation(summary = "Login", description = "Login")
  @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = AuthResponse.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ResourceNotFoundException.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginRequest) {
    return userService.loginUser(loginRequest);
  }

   @Operation(summary = "Login", description = "Login")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = User.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ResourceNotFoundException.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @GetMapping("/get-profile")
   public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
      return new ResponseEntity<>(userService.findUserByJWTToken(jwt), HttpStatus.OK);
   }

}
