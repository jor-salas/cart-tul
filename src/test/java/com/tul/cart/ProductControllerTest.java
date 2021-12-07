package com.tul.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tul.cart.controller.ProductController;
import com.tul.cart.domain.Product;
import com.tul.cart.dto.ProductRequest;
import com.tul.cart.service.IProductService;
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

import java.util.ArrayList;
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

public class ProductControllerTest {

    private static final String PRODUCT_PATH = "/api/product";
    private static final String PRODUCT_CREATE_PATH = "/api/product/create";
    private static final String PRODUCT_LIST_PATH = "/api/product/list";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    IProductService productService;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    private ProductController productController;
    private MockMvc mock;

    @BeforeAll
    static void setUpForAllTests() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        mock = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void post_product_invalid_json() throws Exception {
        mock.perform(post(PRODUCT_CREATE_PATH)
                        .content("invalid_json")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_product_null_attributes() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("name");
        productRequest.setDescription("description");
        productRequest.setPrice(2000.0);
        mock.perform(post(PRODUCT_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_product_invalid_attribute() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("name");
        productRequest.setDescription("description");
        productRequest.setPrice(-1.0);
        productRequest.setSku(1);
        productRequest.setDiscount(true);
        mock.perform(post(PRODUCT_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_product_ok() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("name");
        productRequest.setDescription("description");
        productRequest.setPrice(1000.0);
        productRequest.setSku(1);
        productRequest.setDiscount(true);
        when(productService.save(any(Product.class))).thenReturn(new Product());
        mock.perform(post(PRODUCT_CREATE_PATH)
                        .content(OBJECT_MAPPER.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    void get_products_empty() throws Exception {
        when(productService.getproducts()).thenReturn(Collections.emptyList());
        mock.perform(get(PRODUCT_LIST_PATH))
                .andExpect(status().isNoContent());
    }

    @Test
    void get_products_ok() throws Exception {
        Product productOne = Product.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799"))
                .name("name1")
                .description("description1")
                .price(2000.0)
                .sku(2)
                .discount(true)
                .build();
        Product productTwo = Product.builder()
                .productId(UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad798"))
                .name("name2")
                .description("description2")
                .price(3000.0)
                .sku(1)
                .discount(false)
                .build();
        when(productService.getproducts()).thenReturn(Arrays.asList(productOne, productTwo));
        mock.perform(get(PRODUCT_LIST_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is("f5ec459a-edae-47e2-a6d7-0bc281aad799")))
                .andExpect(jsonPath("$[0].name", is("name1")))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[0].price", is(2000.0)))
                .andExpect(jsonPath("$[0].sku", is(2)))
                .andExpect(jsonPath("$[0].discount", is(true)))
                .andExpect(jsonPath("$[1].productId", is("f5ec459a-edae-47e2-a6d7-0bc281aad798")))
                .andExpect(jsonPath("$[1].name", is("name2")))
                .andExpect(jsonPath("$[1].description", is("description2")))
                .andExpect(jsonPath("$[1].price", is(3000.0)))
                .andExpect(jsonPath("$[1].sku", is(1)))
                .andExpect(jsonPath("$[1].discount", is(false)));

    }

    @Test
    void get_product_null() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        when(productService.get(id)).thenReturn(Optional.empty());
        mock.perform(get(PRODUCT_PATH + "?id=" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_product_ok() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        Product product = Product.builder()
                .productId(id)
                .name("name1")
                .description("description1")
                .price(2000.0)
                .sku(2)
                .discount(true)
                .build();
        when(productService.get(id)).thenReturn(Optional.of(product));
        mock.perform(get(PRODUCT_PATH + "?id=" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.price", is(2000.0)));
    }

    @Test
    void delete_product_null() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        when(productService.get(id)).thenReturn(Optional.empty());
        mock.perform(delete(PRODUCT_PATH + "?id=" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_product_ok_empty_list() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        when(productService.get(id)).thenReturn(Optional.of(Product.builder().build()));
        when(productService.delete(id)).thenReturn(new ArrayList<>());
        mock.perform(delete(PRODUCT_PATH + "?id=" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_product_ok_list_with_items() throws Exception {
        UUID id = UUID.fromString("f5ec459a-edae-47e2-a6d7-0bc281aad799");
        Product product = Product.builder()
                .productId(id)
                .name("name1")
                .description("description1")
                .price(2000.0)
                .sku(2)
                .discount(true)
                .build();
        when(productService.get(id)).thenReturn(Optional.of(Product.builder().build()));
        when(productService.delete(id)).thenReturn(Collections.singletonList(product));
        mock.perform(delete(PRODUCT_PATH + "?id=" + id))
                .andExpect(status().isOk());
    }
}
