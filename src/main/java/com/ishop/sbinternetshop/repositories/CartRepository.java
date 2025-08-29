package com.ishop.sbinternetshop.repositories;

import com.ishop.sbinternetshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
