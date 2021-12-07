package com.tul.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tul.cart.constants.Status;
import com.tul.cart.controller.CartController;
import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import com.tul.cart.dto.CartRequest;
import com.tul.cart.service.ICartService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest {

    private static final String CART_PATH = "/api/cart?id=";
    private static final String CART_CREATE_PATH = "/api/cart/createCart";
    private static final String CART_ADD_PRODUCT_PATH = "/api/cart/addProduct";
    private static final String CART_DELETE_PRODUCT_PATH = "/api/cart/deleteCartProduct";
    private static final String CART_CHECKOUT_PATH = "/api/cart/checkout";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    ICartService cartService;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    private CartController cartController;
    private MockMvc mock;

    @BeforeAll
    static void setUpForAllTests() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        mock = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void post_cart_invalid_json() throws Exception {
        mock.perform(post(CART_CREATE_PATH)
                        .content("invalid_json")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_cart_null_attributes() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setQuantity(1);
        mock.perform(post(CART_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_cart_invalid_attribute() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(id);
        cartRequest.setQuantity(-1);
        mock.perform(post(CART_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_cart_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(id);
        cartRequest.setQuantity(1);
        Cart cart = Cart.builder().build();
        when(cartService.createCart(any())).thenReturn(Optional.of(cart));
        mock.perform(post(CART_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    void get_cart_null() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        when(cartService.getById(id)).thenReturn(Optional.empty());
        mock.perform(get(CART_PATH + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_cart_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad797");
        CartProduct productOne = CartProduct.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799"))
                .quantity(1)
                .build();
        CartProduct productTwo = CartProduct.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798"))
                .quantity(2)
                .build();
        Cart cart = Cart.builder().id(id).cartProducts(Arrays.asList(productOne, productTwo)).totalAmount(1000.0).status(Status.PENDING).build();
        when(cartService.getById(id)).thenReturn(Optional.of(cart));
        mock.perform(get(CART_PATH + id))
                .andExpect(status().isOk());
    }

    @Test
    void add_product_cart_null() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798");
        UUID productId = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad797");

        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(productId);
        cartRequest.setQuantity(1);

        CartProduct product = CartProduct.builder()
                .productId(productId)
                .quantity(1)
                .build();
        when(cartService.addToCart(id,product)).thenReturn(Optional.empty());
        mock.perform(post(CART_ADD_PRODUCT_PATH + "?id=" + id)
                .content(OBJECT_MAPPER.writeValueAsString(cartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void add_product_cart_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798");
        UUID productId = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad797");

        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(productId);
        cartRequest.setQuantity(1);

        CartProduct product = CartProduct.builder()
                .productId(productId)
                .quantity(1)
                .build();
        Cart cart = Cart.builder().id(id).cartProducts(Collections.singletonList(product)).totalAmount(1000.0).status(Status.COMPLETE).build();
        when(cartService.addToCart(any(),any())).thenReturn(Optional.of(cart));
        mock.perform(post(CART_ADD_PRODUCT_PATH + "?id=" + id)
                        .content(OBJECT_MAPPER.writeValueAsString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void delete_product_from_cart_null() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        UUID idProduct = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798");
        when(cartService.deleteFromCart(id,idProduct)).thenReturn(Optional.empty());
        mock.perform(delete(CART_DELETE_PRODUCT_PATH + "?cartId=" + id + "&productId=" + idProduct ))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_product_from_cart_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        UUID idProduct = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798");
        CartProduct product = CartProduct.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798"))
                .quantity(1)
                .build();
        Cart cart = Cart.builder().id(id).cartProducts(Collections.singletonList(product)).totalAmount(1000.0).status(Status.COMPLETE).build();
        when(cartService.deleteFromCart(id,idProduct)).thenReturn(Optional.of(cart));
        mock.perform(delete(CART_DELETE_PRODUCT_PATH + "?cartId=" + id + "&productId=" + idProduct ))
                .andExpect(status().isOk());
    }

    @Test
    void checkout_cart_not_found() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        CartProduct product = CartProduct.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798"))
                .quantity(1)
                .build();
        Cart cart = Cart.builder().id(id).cartProducts(Collections.singletonList(product)).totalAmount(1000.0).status(Status.COMPLETE).build();
        when(cartService.checkout(id)).thenReturn(Optional.empty());
        mock.perform(post(CART_CHECKOUT_PATH + "?cartId=" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkout_cart_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        CartProduct product = CartProduct.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798"))
                .quantity(1)
                .build();
        Cart cart = Cart.builder().id(id).cartProducts(Collections.singletonList(product)).totalAmount(1000.0).status(Status.COMPLETE).build();
        when(cartService.checkout(id)).thenReturn(Optional.of(cart));
        mock.perform(post(CART_CHECKOUT_PATH + "?cartId=" + id))
                .andExpect(status().isOk());
    }
}
