package com.waffiyyi.bookmarketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.waffiyyi.bookmarketplace.entities.Cart;
import com.waffiyyi.bookmarketplace.repository.CartRepository;
import com.waffiyyi.bookmarketplace.repository.TransactionRepository;
import com.waffiyyi.bookmarketplace.service.CartService;
import com.waffiyyi.bookmarketplace.service.PaymentService;
import com.waffiyyi.bookmarketplace.service.serviceImpl.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

   private final TransactionRepository transactionRepository;
   private final PaymentService paymentService;
   private final CartRepository cartRepository;
   private final CartService cartService;
   private final ObjectMapper objectMapper = new ObjectMapper();
   @Value("${stripe.signing.key}")
   private String signingKey;

   @PostMapping
   public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                   @RequestHeader("Stripe-Signature") String sigHeader) {
      log.info("Received Stripe signature header: " + sigHeader);
      log.info("Webhook initiated with payload: " + payload);

      Event event;
      try {
         event = Webhook.constructEvent(payload, sigHeader, signingKey);
      } catch (SignatureVerificationException e) {
         log.error("Invalid Stripe signature: " + e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
      } catch (Exception e) {
         log.error("Failed to parse webhook event: " + e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
      }

      String eventType = event.getType();
      log.info("Stripe event type: " + eventType);

      String clientReferenceId = extractClientReferenceId(payload);
      if (clientReferenceId == null || !cartRepository.existsById(Long.valueOf(clientReferenceId))) {
         log.info("Ignoring event; clientReferenceId does not match a known Cart ID.");
         return ResponseEntity.ok("Event ignored");
      }

      switch (eventType) {
         case "checkout.session.completed":
         case "checkout.session.async_payment_succeeded":
            handleSuccessfulPayment(payload);
            break;
         default:
            log.warn("Unhandled event type: " + eventType);
            break;
      }

      return ResponseEntity.ok("Webhook received successfully");
   }

   private void handleSuccessfulPayment(String payload) {
      try {
         Map<String, Object> eventMap = objectMapper.readValue(payload, new TypeReference<>() {});
         Map<String, Object> dataMap = (Map<String, Object>) eventMap.get("data");
         Map<String, Object> objectMap = (Map<String, Object>) dataMap.get("object");

         log.info("Session map: " + objectMap);

         String clientReferenceId = (String) objectMap.get("client_reference_id");
         Long cartId = Long.valueOf(clientReferenceId);
         Double amountPaid = Double.valueOf(objectMap.get("amount_total").toString()) / 100;

         Cart cart = cartRepository.findById(cartId).orElse(null);
         log.info("Cart passed to transaction: " + cart);
         if (cart != null) {
            paymentService.handleSuccessfulPayment(CartMapper.toDTO(cart), amountPaid);
            cart.getItems().clear();
            cartRepository.save(cart);
            log.info("Transaction saved successfully with amount: " + amountPaid);
         } else {
            log.error("Cart not found for id " + cartId);
         }
      } catch (JsonProcessingException ex) {
         throw new RuntimeException(ex);
      } catch (Exception e) {
         log.error("Error handling successful payment: " + e.getMessage(), e);
      }
   }

   private String extractClientReferenceId(String payload) {
      try {
         Map<String, Object> eventMap = objectMapper.readValue(payload, new TypeReference<>() {});
         Map<String, Object> dataMap = (Map<String, Object>) eventMap.get("data");
         Map<String, Object> objectMap = (Map<String, Object>) dataMap.get("object");
         return (String) objectMap.get("client_reference_id");
      } catch (Exception e) {
         log.error("Failed to extract clientReferenceId: " + e.getMessage(), e);
         return null;
      }
   }
}