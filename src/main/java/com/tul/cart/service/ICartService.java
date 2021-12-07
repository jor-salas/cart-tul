package com.tul.cart.service;

import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;

import java.util.UUID;

public interface ICartService {
    Cart createCart(CartProduct cartProduct);
    Cart addToCart(UUID id, CartProduct cartProduct);
    Cart deleteFromCart(UUID id, UUID cartProduct);
}
