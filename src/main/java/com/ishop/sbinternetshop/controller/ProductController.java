package com.ishop.sbinternetshop.controller;

import com.ishop.sbinternetshop.config.AppConstants;
import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;
import com.ishop.sbinternetshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/admin/categories/{categoryId}/product", method = RequestMethod.POST)
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDto,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProductDTO =  productService.addProduct(categoryId, productDto);

        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/public/products", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/categories/{categoryId}/products", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder                                                         ) {
        ProductResponse productResponse = productService.getAllProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/products/keyword/{keyword}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }
    @RequestMapping(value = "/admin/products/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDto,
                                                    @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/products/{productId}", method =  RequestMethod.DELETE)
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }
    @RequestMapping(value = "/products/{productId}/image", method = RequestMethod.PUT)
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProductDto = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);

    }
}
