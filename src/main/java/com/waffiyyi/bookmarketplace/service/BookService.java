package com.waffiyyi.bookmarketplace.service;

import com.waffiyyi.bookmarketplace.entities.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
   Book createBook(Book req);

   Book getBook(Long id);

   List<Book> getAllAvailableBooks();

   List<Book> searchBook(String query);

   List<Book> filterByCategory(String query);

   List<Book> findBestsellingBooks();

   ResponseEntity<List<String>> getAllBookCategories();
   ResponseEntity<List<String>> getFeaturedCategories();
    List<Book> getFrequentlyBoughtWith(Long userid);


}