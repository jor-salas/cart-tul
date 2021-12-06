package com.tul.cart.service;

import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import com.tul.cart.domain.Product;

import java.util.List;
import java.util.UUID;

public interface ICartService {
    CartProduct save(CartProduct cartProduct);
    Cart save(Cart cart);
    Cart getById(UUID id);
}
