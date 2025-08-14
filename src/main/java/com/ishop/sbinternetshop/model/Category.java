package com.ishop.sbinternetshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = "categories")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    // Category entity class
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long categoryId;
    @NotBlank
    @Size(min = 5, message = "Category name must contain at least 5 characters")
    @ToString.Include
    private String categoryName;

    @OneToMany(mappedBy = "category",  cascade = CascadeType.ALL)
    private List<Product> products;
}
