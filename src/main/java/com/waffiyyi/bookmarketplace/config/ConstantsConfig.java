package com.waffiyyi.bookmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ConstantsConfig {
   @Bean
   public List<String> bookCategoriesAndGenre() {
      return Arrays.asList(
        "Fiction",
        "Non-Fiction",
        "Mystery & Thriller",
        "Fantasy",
        "Science Fiction",
        "Biography",
        "Romance",
        "Historical Fiction",
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
        "Sports & Outdoors"
      );
   }
}