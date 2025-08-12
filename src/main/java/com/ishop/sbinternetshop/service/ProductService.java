package com.ishop.sbinternetshop.service;


import com.ishop.sbinternetshop.model.Product;
import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
    ProductResponse getAllProducts();
    ProductResponse getAllProductsByCategory(Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);
}
