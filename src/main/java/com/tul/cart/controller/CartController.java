package com.tul.cart.controller;

import com.tul.cart.service.IService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final IService cartService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(final IService cartService, final ModelMapper modelMapper) {
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping
    public String home(){
        return "Home";
    }
}
