package com.waffiyyi.bookmarketplace.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AddCartItemRequest {
    private Long cartItemId;
    private Long bookId;
    private int quantity;
}
