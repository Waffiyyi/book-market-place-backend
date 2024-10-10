package com.waffiyyi.bookmarketplace.service.serviceImpl;


import com.waffiyyi.bookmarketplace.dtos.AddCartItemRequest;
import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.CartItemDTO;
import com.waffiyyi.bookmarketplace.entities.Book;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.entities.CartItem;
import com.waffiyyi.bookmarketplace.entities.User;
import com.waffiyyi.bookmarketplace.exception.ResourceNotFoundException;
import com.waffiyyi.bookmarketplace.repository.BookRepository;
import com.waffiyyi.bookmarketplace.repository.CartItemRepository;
import com.waffiyyi.bookmarketplace.repository.CartRepository;
import com.waffiyyi.bookmarketplace.service.CartService;
import com.waffiyyi.bookmarketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
   private final CartRepository cartRepository;
   private final UserService userService;
   private final CartItemRepository cartItemRepository;
   private final BookRepository bookRepository;

   @Override
   public CartItemDTO addItemToCart(AddCartItemRequest req, String jwt) {
      User user = userService.findUserByJWTToken(jwt);
      Book book = bookRepository.findById(req.getBookId())
                                .orElseThrow(
                                  () -> new ResourceNotFoundException("Book not found",
                                                                      HttpStatus.BAD_REQUEST));

      Cart cart = cartRepository.findByCustomerId(user.getId());

      log.info("User: {}", user);
      log.info("Book: {}", book);
      log.info("Cart: {}", cart.getItems());

      for (CartItem cartItem : cart.getItems()) {
         if (cartItem.getBook().equals(book)) {
            log.info("Updating existing cart item: {}", cartItem);
            cartItem.setQuantity(cartItem.getQuantity() + req.getQuantity());
            cartItem.setTotalPrice(
              cartItem.getBook().getPrice() * cartItem.getQuantity());
            cartItemRepository.save(cartItem);
            return CartMapper.toCartItemDTO(cartItem);
         }
      }

      CartItem newCartItem = new CartItem();
      newCartItem.setBook(book);
      newCartItem.setQuantity(req.getQuantity());
      newCartItem.setTotalPrice(book.getPrice() * req.getQuantity());
      newCartItem.setCart(cart);
      cart.getItems().add(newCartItem);

      log.info("Adding new cart item: {}", newCartItem);
      cartItemRepository.save(newCartItem);
      cartRepository.save(cart);

      return CartMapper.toCartItemDTO(newCartItem);
   }

   @Override
   public CartItemDTO updateCartItemQuantity(Long cartItemId, int quantity) {
      CartItem cartItem = cartItemRepository.findById(cartItemId)
                                            .orElseThrow(
                                              () -> new ResourceNotFoundException(
                                                "Cart item not found",
                                                HttpStatus.NOT_FOUND));

      cartItem.setQuantity(quantity);
      cartItem.setTotalPrice(cartItem.getBook().getPrice() * quantity);

      CartItem updatedCartItem = cartItemRepository.save(cartItem);
      log.info("updated cart item" + updatedCartItem.getCart().getItems());
      return CartMapper.toCartItemDTO(updatedCartItem);
   }

   @Override
   public CartDTO removeItemFromCart(Long cartItemId, String jwt) {
      User user = userService.findUserByJWTToken(jwt);
      Cart cart = cartRepository.findByCustomerId(user.getId());

      CartItem cartItem = cartItemRepository.findById(cartItemId)
                                            .orElseThrow(
                                              () -> new ResourceNotFoundException(
                                                "Cart item not found",
                                                HttpStatus.NOT_FOUND));

      cart.getItems().remove(cartItem);
      Cart savedCart = cartRepository.save(cart);

      return CartMapper.toDTO(savedCart);
   }

   @Override
   public Double calculateCartTotal(Cart cart) {
      double total = 0.0;

      for (CartItem cartItem : cart.getItems()) {
         total += cartItem.getBook().getPrice() * cartItem.getQuantity();
      }

      return total;
   }

   @Override
   public Cart findCartById(Long id) {
      return cartRepository.findById(
        id).orElseThrow(
        () -> new ResourceNotFoundException(
          "Cart not found with id " + id,
          HttpStatus.NOT_FOUND));
   }

   @Override
   public CartDTO findCartByUserId(Long userId) {
      Cart cart = cartRepository.findByCustomerId(userId);
      cart.setTotal(calculateCartTotal(cart));
      return CartMapper.toDTO(cart);
   }

   @Override
   public Cart clearCart(String jwt) {
      User user = userService.findUserByJWTToken(jwt);
      Cart cart = cartRepository.findByCustomerId(user.getId());
      cart.getItems().clear();
      return cartRepository.save(cart);
   }
}
