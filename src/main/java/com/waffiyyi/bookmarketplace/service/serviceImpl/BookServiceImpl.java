package com.waffiyyi.bookmarketplace.service.serviceImpl;

import com.waffiyyi.bookmarketplace.entities.Book;
import com.waffiyyi.bookmarketplace.entities.Transaction;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.exception.ResourceNotFoundException;
import com.waffiyyi.bookmarketplace.repository.BookRepository;
import com.waffiyyi.bookmarketplace.repository.TransactionRepository;
import com.waffiyyi.bookmarketplace.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

   private final BookRepository bookRepository;
   private final List<String> bookCategoriesAndGenre;
   private final TransactionRepository transactionRepository;

   @Override
   public Book createBook(Book req) {

      Book createdBook = Book
        .builder()
        .author(req.getAuthor())
        .image(req.getImage())
        .price(req.getPrice())
        .description(req.getDescription())
        .dateReleased(req.getDateReleased())
        .categories(req.getCategories())
        .rating(req.getRating())
        .title(req.getTitle())
        .genre(req.getGenre())
        .build();
      return bookRepository.save(createdBook);

   }

   @Override
   public Book getBook(Long id) {
      return bookRepository.findById(id).orElseThrow(
        () -> new BadRequestException("Book does not exist", HttpStatus.BAD_REQUEST));
   }

   @Override
   public List<Book> getAllAvailableBooks() {
      return bookRepository.findAll();
   }

   @Override
   public List<Book> searchBook(String query) {
      return bookRepository.searchBook(query);
   }

   @Override
   public List<Book> filterByCategory(String query) {
      return bookRepository.findAllByCategoriesContaining(query);
   }

   @Override
   public List<Book> findBestsellingBooks() {
      Pageable pageable = PageRequest.of(0, 10);
      return bookRepository.findBestsellingBooks(pageable).getContent();
   }

   @Override
   public ResponseEntity<List<String>> getAllBookCategories() {
      return new ResponseEntity<>(bookCategoriesAndGenre, HttpStatus.OK);
   }

   @Override
   public ResponseEntity<List<String>> getFeaturedCategories() {
      List<String> categories = bookRepository.findTopCategoriesByBooksAndRating();
      if (categories == null) {
         throw new ResourceNotFoundException("No featured categir",
                                             HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(new ArrayList<>(categories), HttpStatus.OK);
   }

   public List<Book> getFrequentlyBoughtWith(Long userId) {
      Set<Transaction> userTransactions = transactionRepository.findAllByUserId(userId);

      Set<String> authors = userTransactions
        .stream()
        .flatMap(transaction -> transaction.getBooksPurchased().stream())
        .map(Book::getAuthor)
        .collect(Collectors.toSet());

      Set<Long> frequentlyBoughtBooks = userTransactions.stream().flatMap(
        transaction -> transaction.getBooksPurchased().stream()).map(Book::getId).collect(
        Collectors.toSet());
      return bookRepository.findByAuthorInAndIdNotIn(authors, frequentlyBoughtBooks);
   }




}
