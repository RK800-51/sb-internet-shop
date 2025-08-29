package com.ishop.sbinternetshop.payload;

import com.ishop.sbinternetshop.model.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long cartId;
    @ToString.Include
    private Double totalPrice = 0.0;
    private List<ProductDTO> products = new ArrayList<>();
}
