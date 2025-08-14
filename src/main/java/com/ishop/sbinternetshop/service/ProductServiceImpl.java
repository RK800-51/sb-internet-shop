package com.ishop.sbinternetshop.service;

import com.ishop.sbinternetshop.exceptions.APIException;
import com.ishop.sbinternetshop.exceptions.ResourceNotFoundException;
import com.ishop.sbinternetshop.model.Category;
import com.ishop.sbinternetshop.model.Product;
import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.payload.ProductResponse;
import com.ishop.sbinternetshop.repositories.CategoryRepository;
import com.ishop.sbinternetshop.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository,
                              ModelMapper modelMapper, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDto.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            Product product = modelMapper.map(productDto, Product.class);
            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();

            product.setCategory(category);
            product.setImage("default.png");
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exists");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        if (products.isEmpty()) {
            throw new APIException("Products not exist");
        }
        List<ProductDTO> content = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(!pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDto) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDto, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        productFromDB.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }


    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
        List<Product> productsByCategory = pageProducts.getContent();

        if (productsByCategory.isEmpty()) {
            throw new APIException("Products with this category not exist");
        }

        List<ProductDTO> content = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(!pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);
        List<Product> productsByCategory = pageProducts.getContent();
        if (productsByCategory.isEmpty()) {
            throw new APIException("No products containing this keyword");
        }

        List<ProductDTO> content = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(!pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String fileName = fileService.uploadImage(path, image);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
