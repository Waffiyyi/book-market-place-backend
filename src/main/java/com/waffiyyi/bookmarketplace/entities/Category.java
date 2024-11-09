package com.waffiyyi.bookmarketplace.entities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Category {
   @Bean
   public List<String> bookCategoriesAndGenre() {
      return Arrays.asList(
        "Classic",
        "Fiction",
        "Thriller",
        "Fantasy",
        "Science Fiction",
        "Biography",
        "Romance",
        "War",
        "Memoir",
        "Cultural",
        "Family Saga",
        "Self-Help",
        "Health & Wellness",
        "Children's Books",
        "Young Adult",
        "Poetry",
        "Graphic Novels",
        "Cooking & Food",
        "Travel",
        "Science & Nature",
        "Religion & Spirituality",
        "Politics & Social Sciences",
        "Arts & Photography",
        "Business & Economics",
        "Reference",
        "Parenting & Family",
        "Humor",
        "Sports & Outdoors",
        "Dystopian",
        "Mystery"
      );
   }
}