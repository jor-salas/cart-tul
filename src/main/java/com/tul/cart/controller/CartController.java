package com.tul.cart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @RequestMapping
    public String home(){
        return "Home";
    }
}
