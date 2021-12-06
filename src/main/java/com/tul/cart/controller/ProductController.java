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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
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
            value = "Insert Client",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @PostMapping("create")
    public ResponseEntity create(@Valid @RequestBody ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product productStored = productService.save(product);
        ProductResponse productResponse = modelMapper.map(productStored, ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

    @ApiOperation(
            value = "Insert Client",
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
            value = "Insert Client",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @GetMapping
    public ResponseEntity get(@RequestParam UUID id){
        Product product = productService.get(id);
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

    @ApiOperation(
            value = "Insert Client",
            response = ProductResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @DeleteMapping
    public ResponseEntity delete(@RequestParam UUID id){
        //Product product = productService.delete(id);
        ProductResponse productResponse = modelMapper.map("", ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

}
