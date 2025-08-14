package com.ishop.sbinternetshop.service;


import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDto);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String s, Integer pageNumber, Integer pageSize, String sortBy, String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDto);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
