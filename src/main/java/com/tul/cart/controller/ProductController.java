package com.tul.cart.controller;

import com.tul.cart.domain.Product;
import com.tul.cart.dto.ProductRequest;
import com.tul.cart.dto.ProductResponse;
import com.tul.cart.service.IProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(final IProductService productService, final ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(
            value = "Insert Product",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @PostMapping("create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product productStored = productService.save(product);
        ProductResponse productResponse = modelMapper.map(productStored, ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

    @ApiOperation(
            value = "Get list products",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @GetMapping("list")
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.getproducts();
        return ResponseEntity.ok(products);
    }

    @ApiOperation(
            value = "Get product by id",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request"),
            @ApiResponse(code = SC_NO_CONTENT, message = "Producto inexistente")
    })
    @GetMapping
    public ResponseEntity<ProductResponse> get(@RequestParam UUID id){
        Optional<Product> product = productService.get(id);

        if(!product.isPresent())
            return ResponseEntity.noContent().build();

        ProductResponse productResponse = modelMapper.map(product.get(), ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

    @ApiOperation(
            value = "Delete product",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @DeleteMapping
    public void delete(@RequestParam UUID id){
        productService.delete(id);
    }

    @ApiOperation(
            value = "Modify product",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_NO_CONTENT, message = "Producto inexistente")
    })
    @PutMapping("modify")
    public ResponseEntity<ProductResponse> put(@Valid @RequestBody ProductRequest productRequest, @RequestParam UUID id){
        Product product = modelMapper.map(productRequest, Product.class);
        Optional<Product> productStored = productService.modify(id,product);

        if(!productStored.isPresent())
            return ResponseEntity.noContent().build();

        ProductResponse response = modelMapper.map(productStored.get(), ProductResponse.class);
        return ResponseEntity.ok(response);
    }
}
