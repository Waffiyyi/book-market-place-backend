package com.waffiyyi.bookmarketplace.controller;


import com.waffiyyi.bookmarketplace.dtos.AddCartItemRequest;
import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.CartItemDTO;
import com.waffiyyi.bookmarketplace.dtos.ErrorResponse;
import com.waffiyyi.bookmarketplace.entities.Book;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.CartItem;
import com.waffiyyi.bookmarketplace.entities.User;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.service.CartService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "This controller manages Cart operations")

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CartController {
   private final CartService cartService;
   private final UserService userService;

   @Operation(summary = "Add item to cart", description = "Used to add item to cart")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = CartItemDTO.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ErrorResponse.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @PutMapping("/cart/add")
   public ResponseEntity<CartItemDTO> addItemToCart(@RequestBody AddCartItemRequest req,
                                                    @RequestHeader("Authorization")
                                                 String jwt) {
      log.info("i enterded contrpller");
      CartItemDTO cartItem = cartService.addItemToCart(req, jwt);
      return new ResponseEntity<>(cartItem, HttpStatus.OK);
   }

   @Operation(summary = "Update cart", description = "Used to update cart")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = CartItemDTO.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ErrorResponse.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @PutMapping("/cart-item/update")
   public ResponseEntity<CartItemDTO> updateCartItem(@RequestBody AddCartItemRequest req) {
      CartItemDTO cartItem = cartService.updateCartItemQuantity(req.getCartItemId(),
                                                             req.getQuantity());
      return new ResponseEntity<>(cartItem, HttpStatus.OK);
   }

   @Operation(summary = "Remove item from cart",
              description = "Used to remove item from cart")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = CartDTO.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ErrorResponse.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @DeleteMapping("/cart-item/remove")
   public ResponseEntity<CartDTO> removeCartItem(@RequestParam Long cartItemId,
                                              @RequestHeader("Authorization")
                                              String jwt) {
      CartDTO cart = cartService.removeItemFromCart(cartItemId, jwt);
      return new ResponseEntity<>(cart, HttpStatus.OK);
   }


   @Operation(summary = "Clear cart",
              description = "Used to clear cart")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = Cart.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ErrorResponse.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @PutMapping("/cart/clear")
   public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) {
      Cart cart = cartService.clearCart(jwt);
      return new ResponseEntity<>(cart, HttpStatus.OK);
   }

   @Operation(summary = "Get cart",
              description = "Used to get  user's cart")
   @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Successful", content =
     @Content(schema = @Schema(implementation = CartDTO.class))),
     @ApiResponse(responseCode = "400", description = "Bad Request", content =
     @Content(schema = @Schema(implementation = BadRequestException.class))),
     @ApiResponse(responseCode = "404", description = "No Record Found", content =
     @Content(schema = @Schema(implementation = ErrorResponse.class))),
     @ApiResponse(responseCode = "500", description = "Internal Server Error!")
   })
   @GetMapping("/cart/user")
   public ResponseEntity<CartDTO> findUserCart(@RequestHeader("Authorization") String jwt) {
      User user = userService.findUserByJWTToken(jwt);
      CartDTO cart = cartService.findCartByUserId(user.getId());
      return new ResponseEntity<>(cart, HttpStatus.OK);
   }

}
