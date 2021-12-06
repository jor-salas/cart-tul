package com.tul.cart.validations;

import com.tul.cart.dto.CartRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CartValidator implements ConstraintValidator<ValidCartRequest, CartRequest> {
    public void initialize(ValidCartRequest constraint) {
    }

    public boolean isValid(CartRequest newCartRequest, ConstraintValidatorContext context) {
        return newCartRequest.getQuantity() > 0;
    }

}

