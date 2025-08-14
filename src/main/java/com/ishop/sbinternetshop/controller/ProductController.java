package com.ishop.sbinternetshop.controller;

import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;
import com.ishop.sbinternetshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/admin/categories/{categoryId}/product", method = RequestMethod.POST)
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProductDTO =  productService.addProduct(categoryId, productDto);

        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/public/products", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/categories/{categoryId}/products", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.getAllProductsByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/public/products/keyword/{keyword}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.getProductsByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }
    @RequestMapping(value = "/admin/products/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDto,
                                                    @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/products/{productId}", method =  RequestMethod.DELETE)
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }
}
