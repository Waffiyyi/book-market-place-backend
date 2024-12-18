package com.waffiyyi.bookmarketplace.service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

   private final BookRepository bookRepository;
   private final List<String> bookCategoriesAndGenre;
   private final TransactionRepository transactionRepository;
   private RestTemplate restTemplate;

   private static final String API_URL =
     "https://disappointed-donnie-bookmarketplacebackend-58ffda1b.koyeb.app/api/create-book";
   private static final String hardCodeJwt =
     "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MzMyNTQ3MzgsImV4cCI6MTczMzM0MTEzOCwiZW1haWwiOiJmYXNob2xhd2FmaXl5aUBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6IiJ9.Tq4OdD3JKNuI_mINZ6VrUnA_9rjxdnXBKuuVHEaBsLqmcp1rTyQtWGSawD4gBFyBfD0Agapz6ogQ1LohV_RxRw";


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
   @Transactional
   public ResponseEntity<List<String>> getAllBookCategories() {
      return new ResponseEntity<>(bookCategoriesAndGenre, HttpStatus.OK);
   }

   @Override
   @Transactional
   public ResponseEntity<List<String>> getFeaturedCategories() {
      List<String> categories = bookRepository.findTopCategoriesByBooksAndRating();
      if (categories == null) {
         throw new ResourceNotFoundException("No featured categories",
                                             HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(new ArrayList<>(categories), HttpStatus.OK);
   }
   @Transactional
   public List<Book> getFrequentlyBoughtWith(Long userId) {
      Set<Transaction> userTransactions = transactionRepository.findAllByUserId(userId);

      Set<String> authors = userTransactions
        .stream()
        .flatMap(transaction -> transaction.getBooksPurchased().stream())
        .map(Book::getAuthor)
        .collect(Collectors.toSet());
      log.info("User Transactions: " + userTransactions);
      log.info("Authors: " + authors);
      Set<Long> frequentlyBoughtBooks = userTransactions.stream().flatMap(
        transaction -> transaction.getBooksPurchased().stream()).map(Book::getId).collect(
        Collectors.toSet());
      //ideally we should use this if db contains more than one book by same
      // Author
//      return bookRepository.findByAuthorInAndIdNotIn(authors, frequentlyBoughtBooks);
      //using this as db data is not much
      return bookRepository.findByAuthorIn(authors);
   }

   @Override
   public String loadBooks(String jwt) throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      List<Book> books = objectMapper.readValue(
        new File("/Users/macbookpro/Desktop/BookMarketPlace/src/main/resources/bookData.json"),
        new TypeReference<List<Book>>() {});

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(hardCodeJwt);

      RestTemplate restTemplate = new RestTemplate();

      for (Book book : books) {
         log.info(book+"book");
         HttpEntity<Book> requestEntity = new HttpEntity<>(book, headers);
         ResponseEntity<Book> responseEntity = restTemplate.exchange(
           API_URL, HttpMethod.POST, requestEntity, Book.class);
      }

      return "Successfully loaded db with books";
   }


}
