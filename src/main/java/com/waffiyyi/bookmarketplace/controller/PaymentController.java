package com.waffiyyi.bookmarketplace.controller;

import com.stripe.exception.StripeException;
import com.waffiyyi.bookmarketplace.dtos.CartDTO;
import com.waffiyyi.bookmarketplace.dtos.PaymentResponse;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
   private final PaymentService paymentService;


   @PostMapping("/payment-checkout")
   public PaymentResponse checkout(@RequestBody CartDTO cart) throws StripeException {
      return paymentService.createPaymentLink(cart);
   }
}
