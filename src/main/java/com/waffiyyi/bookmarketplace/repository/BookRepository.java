package com.waffiyyi.bookmarketplace.repository;

import com.waffiyyi.bookmarketplace.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

   @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
     "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
     "OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :query, '%'))")
   List<Book> searchBook(@Param("query") String query);

   List<Book> findAllByCategoriesContaining(String category);
   @Query("SELECT b FROM Book b ORDER BY b.rating DESC")
   Page<Book> findBestsellingBooks(Pageable pageable);

   @Query("SELECT b FROM Book b ORDER BY b.rating DESC, b.price ASC")
   List<Book> findTop10ByOrderByRatingDescPriceAsc();
   @Query("SELECT c FROM Book b JOIN b.categories c GROUP BY c ORDER BY AVG(b.rating) DESC")
   List<String> findTopCategoriesByBooksAndRating();

   List<Book> findByAuthorInAndIdNotIn(Set<String> authors, Set<Long> bookIds);
   List<Book> findByAuthorIn(Set<String> authors);



}
