package com.tul.cart.validations;

import com.tul.cart.dto.ProductRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductValidator implements ConstraintValidator<ValidProductRequest, ProductRequest> {
    public void initialize(ValidProductRequest constraint) {
    }

    public boolean isValid(ProductRequest newProductRequest, ConstraintValidatorContext context) {
        final Double price = newProductRequest.getPrice();
        return  newProductRequest.getPrice() > 0;
    }

}

