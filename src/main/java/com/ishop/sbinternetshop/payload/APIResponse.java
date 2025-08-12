package com.ishop.sbinternetshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    // standard exception response class for project API
    public String message;
    private boolean status;
}
