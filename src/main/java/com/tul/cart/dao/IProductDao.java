package com.tul.cart.dao;

import com.tul.cart.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductDao extends JpaRepository<Product, UUID> { }
