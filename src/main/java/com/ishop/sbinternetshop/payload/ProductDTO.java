package com.ishop.sbinternetshop.payload;

import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long productId;
    @ToString.Include
    private String productName;
    private String image;
    private String description;
    private Integer quantity; // quantity of product added to the cart, not in stock
    private double price;
    private double discount;
    private double specialPrice;
}
