package com.ishop.sbinternetshop.service;

import com.ishop.sbinternetshop.exceptions.ResourceNotFoundException;
import com.ishop.sbinternetshop.model.Category;
import com.ishop.sbinternetshop.model.Product;
import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;
import com.ishop.sbinternetshop.repositories.CategoryRepository;
import com.ishop.sbinternetshop.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository,
                              ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDto, Product.class);
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();

        product.setCategory(category);
        product.setImage("default.png");
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> content = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {
        // get the existing product from DB
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDto, Product.class);
        // update the product info with the one in request body
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        productFromDB.setSpecialPrice(specialPrice);

        // save to DB
        Product savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }


    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> productsByCategory = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> content = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> productsByCategory = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> content = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        return productResponse;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete, ProductDTO.class);
    }
}
