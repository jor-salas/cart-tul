package com.tul.cart.dao;

import com.tul.cart.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductCartDao extends JpaRepository<CartProduct, UUID> { }
