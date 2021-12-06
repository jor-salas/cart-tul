package com.tul.cart.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tul.cart.domain.Product;
import com.tul.cart.validations.ValidCartRequest;
import com.tul.cart.validations.ValidProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidCartRequest
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartRequest {
    @NotNull
    @NotBlank
    private String productId;
    @NotNull
    private int quantity;
}
