package com.tul.cart.service;

import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;

import java.util.Optional;
import java.util.UUID;

public interface ICartService {
    Optional<Cart> createCart(CartProduct cartProduct);
    Optional<Cart> getById(UUID id);
    Optional<Cart> addToCart(UUID id, CartProduct cartProduct);
    Optional<Cart> deleteFromCart(UUID id, UUID cartProduct);
    Optional<Cart> checkout(UUID id);
}
