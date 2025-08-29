package com.ishop.sbinternetshop.repositories;

import com.ishop.sbinternetshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}