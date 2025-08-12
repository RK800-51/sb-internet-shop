package com.ishop.sbinternetshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    // DTO class for Category entity
    private Long categoryId;
    private String categoryName;

}
