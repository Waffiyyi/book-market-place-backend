package com.waffiyyi.bookmarketplace.service.serviceImpl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.CartItemDTO;
import com.waffiyyi.bookmarketplace.dtos.PaymentResponse;
import com.waffiyyi.bookmarketplace.entities.*;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.exception.UserNotFoundException;
import com.waffiyyi.bookmarketplace.repository.TransactionRepository;
import com.waffiyyi.bookmarketplace.repository.UserRepository;
import com.waffiyyi.bookmarketplace.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
   private final TransactionRepository transactionRepository;
   private final UserRepository userRepository;
   @Value("${stripe.secret.key}")
   private String stripeSecretKey;
   @Value("${app.redirect.success}")
   private String successURL;
   @Value("${app.redirect.fail}")
   private String cancelURL;

   @Override
   public PaymentResponse createPaymentLink(CartDTO cart) throws StripeException {
      Stripe.apiKey = stripeSecretKey;
      log.info("cart" + cart);
      if (cart.getItems() == null || cart.getItems().isEmpty()) {
         throw new BadRequestException("Cart is empty or has no items.",
                                       HttpStatus.BAD_REQUEST);
      }
      log.info("cart that created payment" + cart);
      log.info("cartitems that created payment " + cart.getItems());
      List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

      for (CartItemDTO item : cart.getItems()) {
         long unitAmount = (long) (item.getBook().getPrice() * 100);

         SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem
           .builder()
           .setQuantity((long) item.getQuantity())
           .setPriceData(SessionCreateParams.LineItem.PriceData
                           .builder()
                           .setCurrency("usd")
                           .setUnitAmount(unitAmount)
                           .setProductData(
                             SessionCreateParams.LineItem.PriceData.ProductData
                               .builder()
                               .setName("BookMarketPlace")
                               .build())
                           .build())
           .build();

         lineItems.add(lineItem);
      }

      SessionCreateParams params = SessionCreateParams
        .builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successURL + cart.getId())
        .setCancelUrl(cancelURL)
        .setClientReferenceId(String.valueOf(cart.getId()))
        .addAllLineItem(lineItems)
        .build();

      Session session = Session.create(params);

      PaymentResponse response = new PaymentResponse();
      response.setPayment_url(session.getUrl());
      return response;
   }

   @Override
   public void handleSuccessfulPayment(CartDTO cart, double amountPaid) {
      Transaction transaction = new Transaction();
      User user =
        userRepository.findById(cart.getUserId()).orElseThrow(
          () -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
      transaction.setAmountOfPurchase(amountPaid);

      List<Book> booksPurchased = cart.getItems().stream()
                                      .map(CartItemDTO::getBook)
                                      .filter(
                                        Objects::nonNull)
                                      .collect(Collectors.toList());

      transaction.setBooksPurchased(booksPurchased);
      transaction.setDatePurchased(new Date());
      transaction.setUser(user);
      log.info("Books purchased: " + booksPurchased);
      transactionRepository.save(transaction);
   }
}
