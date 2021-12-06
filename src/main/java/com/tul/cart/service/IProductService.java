package com.tul.cart.service;

import com.tul.cart.domain.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product save(Product product);
    List<Product> getproducts();
    Product get(UUID id);
    void delete(UUID id);
}
