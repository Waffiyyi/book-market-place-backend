package com.waffiyyi.bookmarketplace.controller;

import com.waffiyyi.bookmarketplace.dtos.ErrorResponse;
import com.waffiyyi.bookmarketplace.entities.Book;
import com.waffiyyi.bookmarketplace.entities.User;
import com.waffiyyi.bookmarketplace.exception.BadRequestException;
import com.waffiyyi.bookmarketplace.repository.BookRepository;
import com.waffiyyi.bookmarketplace.service.BookService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Book", description = "This controller manages Book operations")

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
   private final BookService bookService;
   private final BookRepository bookRepository;
   private final UserService userService;

   @Operation(summary = "Create Book", description = "Used to create book")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @PostMapping("/create-book")
   public ResponseEntity<Book> createBook(@RequestBody Book req) {
      return new ResponseEntity<>(bookService.createBook(req), HttpStatus.CREATED);
   }

   @Operation(summary = "Get Book", description = "Used to get a book")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/get-book/{bookId}")
   public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
      return new ResponseEntity<>(bookService.getBook(bookId), HttpStatus.OK);
   }

   @Operation(summary = "Get Book", description = "Used to get all available books")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/get-all-book")
   public ResponseEntity<List<Book>> getAllAvailableBooks() {
      return new ResponseEntity<>(bookService.getAllAvailableBooks(), HttpStatus.OK);
   }


   @Operation(summary = "Search Book", description = "Used to get search for books")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/search-book")
   public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
      return new ResponseEntity<>(bookService.searchBook(query), HttpStatus.OK);
   }

   @Operation(summary = "Search Book", description = "Used to get search for books")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/filter-book")
   public ResponseEntity<List<Book>> filterBooks(@RequestParam String query) {
      return new ResponseEntity<>(bookService.filterByCategory(query), HttpStatus.OK);
   }

   @Operation(summary = "Get Book", description = "Used to get top best selling books")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/best-selling-book")
   public ResponseEntity<List<Book>> getBestSellingBooks() {
      return new ResponseEntity<>(bookService.findBestsellingBooks(), HttpStatus.OK);
   }


   @Operation(summary = "Get Book Category",
              description = "Used to get all Book Categories")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = String.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/all-book-categories")
   public ResponseEntity<List<String>> getBookCategory() {
      return bookService.getAllBookCategories();
   }

   @Operation(summary = "Get Featured Books", description = "Used to get featured books")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/featured-books")
   public ResponseEntity<List<Book>> featuredBooks() {
      return new ResponseEntity<>(bookRepository.findTop10ByOrderByRatingDescPriceAsc(),
                                  HttpStatus.OK);
   }

   @Operation(summary = "Get Featured Categories",
              description = "Used to get featured categores")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = String.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/featured-categories")
   public ResponseEntity<List<String>> featuredCategories() {
      return bookService.getFeaturedCategories();
   }

   @Operation(summary = "Get Books",
              description = "Used to get books by authors users frequently buy with")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = Book.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @GetMapping("/frequently-bought-with")
   public ResponseEntity<List<Book>> getFrequentlyBoughtWith(
     @RequestHeader("Authorization") String jwt) {
      User user = userService.findUserByJWTToken(jwt);
      return new ResponseEntity<>(bookService.getFrequentlyBoughtWith(user.getId()),
                                  HttpStatus.OK);
   }

   @Operation(summary = "Load Book",
              description = "Used to load book data in the database")
   @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful",
                                       content = @Content(schema = @Schema(
                                         implementation = String.class))), @ApiResponse(
     responseCode = "400", description = "Bad Request", content = @Content(
     schema = @Schema(implementation = BadRequestException.class))), @ApiResponse(
     responseCode = "404", description = "No Record Found", content = @Content(
     schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(
     responseCode = "500", description = "Internal Server Error!")})
   @PostMapping("/load-books")
   public String loadBooks(@RequestHeader("Authorization") String jwt) throws
                             IOException {
      return bookService.loadBooks(jwt);
   }
}
