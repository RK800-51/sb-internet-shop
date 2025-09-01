package com.ishop.sbinternetshop.service;

import com.ishop.sbinternetshop.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);
}
