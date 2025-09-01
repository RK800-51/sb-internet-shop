package com.ishop.sbinternetshop.service;

import com.ishop.sbinternetshop.exceptions.APIException;
import com.ishop.sbinternetshop.exceptions.ResourceNotFoundException;
import com.ishop.sbinternetshop.model.Cart;
import com.ishop.sbinternetshop.model.CartItem;
import com.ishop.sbinternetshop.model.Product;
import com.ishop.sbinternetshop.payload.CartDTO;
import com.ishop.sbinternetshop.payload.ProductDTO;
import com.ishop.sbinternetshop.repositories.CartItemRepository;
import com.ishop.sbinternetshop.repositories.CartRepository;
import com.ishop.sbinternetshop.repositories.ProductRepository;
import com.ishop.sbinternetshop.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository,
                           CartItemRepository cartItemRepository, ModelMapper modelMapper, AuthUtil authUtil) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // find existing cart or create one associated with particular user
        Cart userCart = createCart();
        // retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // perform validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(userCart.getCartId(), productId);
        if (cartItem != null) {
            throw new APIException("Product" + product.getProductName() + "already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + "is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        // create cart item

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(userCart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        // save cart item
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());

        userCart.setTotalPrice(userCart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(userCart);

        // return updated cart information
        CartDTO userCartDTO = modelMapper.map(userCart, CartDTO.class);
        List<CartItem> cartItems = userCart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        userCartDTO.setProducts((productDTOStream.toList()));

        return userCartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.size() == 0) {
            throw new APIException("No cart exists");
        }
        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream().map(cartItem ->
                modelMapper.map(cartItem.getProduct(), ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();

        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(ci -> ci.getProduct().setQuantity(ci.getQuantity()));
        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(cartItem ->
                modelMapper.map(cartItem.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }
}
