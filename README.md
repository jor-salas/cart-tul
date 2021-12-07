# cart-tul
Developed by Jorge Manuel Salas

## Documentation
- [Swagger Documentation](http://localhost:8080/swagger-ui/)

## Endpoints

### POST Product
```
curl --location --request POST 'http://localhost:8080/api/product' \
 --header 'Content-Type: application/json' \
 --data-raw '{
        "description": "Bicicleta mountain bike",
        "discount": true,
        "name": "Bicicleta",
        "price": 2000,
        "sku": 2
}'
 ```

### GET Product
- [Try Now](http://localhost:8080/api/product?id=1234)

 ```
curl --location --request GET 'http://localhost:8080/api/product?id=1234'
  ```

### GET list products
- [Try Now](http://localhost:8080/api/product/list)
```
curl --location --request GET 'http://localhost:8080/api/product/list'
```

### DELETE product
```
curl --location --request DELETE 'http://localhost:8080/api/product?id=1234'
```

### PUT product
```
curl --location --request PUT 'http://localhost:8080/api/product/modify' \
 --header 'Content-Type: application/json' \
 --data-raw '{
        "description": "Bicicleta mountain bike",
        "discount": false,
        "name": "Bicicleta",
        "price": 3000,
        "sku": 3
}'
```

### POST create cart
```
curl --location --request POST 'http://localhost:8080/api/cart/createCart' \
 --header 'Content-Type: application/json' \
 --data-raw '{
        "product_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "quantity": 2
}'
 ```

### POST add Product to cart
```
curl --location --request POST 'http://localhost:8080/api/cart/addProduct?id=1234' \
 --header 'Content-Type: application/json' \
 --data-raw '{
        "product_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "quantity": 3
}'
 ```

### DELETE Product to cart
```
curl --location --request DELETE 'http://localhost:8080/api/cart?cartId=1234&productId=1234
 ```

### POST checkout cart
```
curl --location --request POST 'http://localhost:8080/api/cart/checkout?id=1234'
 ```


## Technologies
- Java 8
- SpringBoot
- ModelMapper
- JUnit
- Swagger
- JPA
- H2
- Gradle

## Considerations
- An in-memory database is used (H2), so it is necessary to load at least one product to obtain results