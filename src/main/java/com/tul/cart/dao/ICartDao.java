package com.tul.cart.dao;

import com.tul.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICartDao extends JpaRepository<Cart, UUID> { }
