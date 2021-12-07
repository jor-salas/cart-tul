package com.tul.cart.controller;

import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import com.tul.cart.dto.CartRequest;
import com.tul.cart.dto.CartResponse;
import com.tul.cart.dto.CheckoutResponse;
import com.tul.cart.dto.ProductResponse;
import com.tul.cart.service.ICartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(final ICartService cartService, final ModelMapper modelMapper) {
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(
            value = "Create cart",
            response = CartResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Inexistent product")
    })
    @PostMapping("createCart")
    public ResponseEntity create(@Valid @RequestBody CartRequest cartRequest) {
        CartProduct cartProduct = modelMapper.map(cartRequest, CartProduct.class);
        Optional<Cart> cart = cartService.createCart(cartProduct);

        if(!cart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inexistent product");
        }

        CartResponse cartResponse = modelMapper.map(cart.get(), CartResponse.class);
        return ResponseEntity.ok(cartResponse);
    }

    @ApiOperation(
            value = "Get cart",
            response = CartResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Inexistent product")
    })
    @GetMapping
    public ResponseEntity get(@RequestParam UUID id) {
        Optional<Cart> cart = cartService.getById(id);

        if(!cart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inexistent cart");
        }

        CartResponse cartResponse = modelMapper.map(cart.get(), CartResponse.class);
        return ResponseEntity.ok(cartResponse);
    }

    @ApiOperation(
            value = "add product to cart",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @PostMapping("addProduct")
    public ResponseEntity add(@Valid @RequestBody CartRequest cartRequest, @RequestParam UUID id) {
        CartProduct cartProduct = modelMapper.map(cartRequest, CartProduct.class);
        Optional<Cart> cart = cartService.addToCart(id, cartProduct);

        if(!cart.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inexistent cart or product");

        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        return ResponseEntity.ok(cartResponse);
    }

    @ApiOperation(
            value = "delete product from cart",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @DeleteMapping("deleteCartProduct")
    public ResponseEntity delete(@RequestParam UUID cartId, @RequestParam UUID productId) {
        Optional<Cart> cart = cartService.deleteFromCart(cartId, productId);

        if(!cart.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inexistent cart or product");

        CartResponse cartResponse = modelMapper.map(cart.get(), CartResponse.class);
        return ResponseEntity.ok(cartResponse);
    }

    @ApiOperation(
            value = "checkout cart",
            response = CheckoutResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Inexistent cart")
    })
    @PostMapping("checkout")
    public ResponseEntity checkout(@RequestParam UUID cartId) {
        Optional<Cart> cart = cartService.checkout(cartId);

        if(!cart.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inexistent cart");

        CheckoutResponse checkoutResponse = modelMapper.map(cart, CheckoutResponse.class);
        return ResponseEntity.ok(checkoutResponse);
    }
}
