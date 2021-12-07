package com.tul.cart.service;

import com.tul.cart.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductService {
    Product save(Product product);
    List<Product> getproducts();
    Optional<Product> get(UUID id);
    List<Product> delete(UUID id);
    Optional<Product> modify(UUID id, Product product);
}
