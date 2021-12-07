package com.tul.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tul.cart.validations.ValidCartRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidCartRequest
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private int quantity;
}
