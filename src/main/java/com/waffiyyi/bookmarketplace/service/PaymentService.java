package com.waffiyyi.bookmarketplace.service;


import com.stripe.exception.StripeException;
import com.waffiyyi.bookmarketplace.dtos.PaymentResponse;
import com.waffiyyi.bookmarketplace.entities.Cart;

public interface PaymentService {
   PaymentResponse createPaymentLink(Cart cart) throws StripeException;
   void handleSuccessfulPayment(Cart cart, double amountPaid);
}
