package com.ishop.sbinternetshop.payload;

import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    // DTO class for Category entity
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long categoryId;
    @ToString.Include
    private String categoryName;

}
