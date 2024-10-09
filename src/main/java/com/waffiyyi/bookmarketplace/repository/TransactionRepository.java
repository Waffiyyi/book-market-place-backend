package com.waffiyyi.bookmarketplace.repository;

import com.waffiyyi.bookmarketplace.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   List<Transaction> findByBooksPurchased_Author(String author);
   Set<Transaction> findAllByUserId(Long id);

}
