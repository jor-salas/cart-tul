package com.tul.cart.service;

import com.tul.cart.constants.Status;
import com.tul.cart.dao.ICartDao;
import com.tul.cart.dao.IProductCartDao;
import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import com.tul.cart.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.DoubleAccumulator;

@Service
public class CartService implements ICartService {

    private IProductCartDao productCartDao;
    private ICartDao cartDao;
    private IProductService productService;

    public CartService(ICartDao cartDao, IProductCartDao productCartDao, IProductService productService) {
        this.cartDao = cartDao;
        this.productCartDao = productCartDao;
        this.productService = productService;
    }

    @Override
    public Optional<Cart> createCart(final CartProduct cartProduct) {
        Optional<Product> product = productService.get(cartProduct.getProductId());

        if(!product.isPresent())
            return Optional.empty();

        CartProduct cartProductStored = productCartDao.save(cartProduct);
        Cart cart = Cart.builder()
                .cartProducts(Collections.singletonList(cartProductStored))
                .totalAmount(calculateTotalAmount(cartProductStored))
                .status(Status.PENDING)
                .build();

        return Optional.of(this.save(cart));
    }

    @Override
    public Optional<Cart> addToCart(UUID id, CartProduct cartProduct) {
        Optional<Product> product = productService.get(cartProduct.getProductId());

        if(!product.isPresent())
            return Optional.empty();

        CartProduct cartProductStored = this.save(cartProduct);
        Optional<Cart> cart = this.getById(id);

        if(!cart.isPresent())
            return Optional.empty();

        cart.get().getCartProducts().add(cartProductStored);
        cart.get().setTotalAmount(calculateTotalAmount(cart.get().getCartProducts()));
        return Optional.of(this.save(cart.get()));
    }

    @Override
    public Optional<Cart> deleteFromCart(UUID id, UUID cartProductId) {
        Optional<Cart> cart = cartDao.findById(id);

        if(!cart.isPresent())
            return Optional.empty();

        List<CartProduct> cartProductList = cart.get().getCartProducts();
        boolean removed = cartProductList.removeIf(i -> i.getId().equals(cartProductId));

        if (!removed)
            return Optional.empty();

        cart.get().setTotalAmount(calculateTotalAmount(cartProductList));
        return Optional.of(cartDao.save(cart.get()));
    }

    @Override
    public Optional<Cart> checkout(UUID id) {
        Optional<Cart> cart = cartDao.findById(id);

        if(!cart.isPresent())
            return Optional.empty();

        cart.get().setStatus(Status.COMPLETE);
        List<CartProduct> cartProductList = cart.get().getCartProducts();
        cart.get().setTotalAmount(calculateTotalAmount(cartProductList));
        return Optional.of(cartDao.save(cart.get()));
    }

    public Optional<Cart> getById(final UUID id) {
        return cartDao.findById(id);
    }

    public Cart save(final Cart cart) {
        return cartDao.save(cart);
    }

    public CartProduct save(final CartProduct cartProduct) {
        return productCartDao.save(cartProduct);
    }

    private Double calculateTotalAmount(CartProduct cartProduct){
        Optional<Product> product = productService.get(cartProduct.getProductId());

        if(product.isPresent()){
            Double price = product.get().getPrice();
            boolean discount = product.get().isDiscount();
            return discount ? price/2*cartProduct.getQuantity() : price*cartProduct.getQuantity();
        }

        return 0.0;
    }

    private Double calculateTotalAmount(List<CartProduct> cartProducts){
        DoubleAccumulator acumulator = new DoubleAccumulator(Double::sum, 0L);

        for(CartProduct product : cartProducts){
            Double total = calculateTotalAmount(product);
            acumulator.accumulate(total);
        }

        return acumulator.doubleValue();
    }
}
