package com.tul.cart.service;

import com.tul.cart.constants.Status;
import com.tul.cart.dao.ICartDao;
import com.tul.cart.dao.IProductCartDao;
import com.tul.cart.dao.IProductDao;
import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import com.tul.cart.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.DoubleAccumulator;

@Service
public class CartService implements ICartService {

    private IProductCartDao productCartDao;
    private IProductDao productDao;
    private ICartDao cartDao;
    private IProductService productService;

    public CartService(ICartDao cartDao, IProductCartDao productCartDao, IProductService productService) {
        this.cartDao = cartDao;
        this.productCartDao = productCartDao;
        this.productService = productService;
    }

    @Override
    public Cart createCart(final CartProduct cartProduct) {
        CartProduct cartProductStored = productCartDao.save(cartProduct);
        Cart cart = Cart.builder()
                .cartProducts(Collections.singletonList(cartProductStored))
                .totalAmount(calculateTotalAmount(cartProductStored))
                .status(Status.PENDING)
                .build();

        return this.save(cart);
    }

    @Override
    public Cart addToCart(UUID id, CartProduct cartProduct) {
        CartProduct cartProductStored = this.save(cartProduct);
        Cart cart = this.getById(id);
        cart.getCartProducts().add(cartProductStored);
        cart.setTotalAmount(calculateTotalAmount(cart.getCartProducts()));
        return this.save(cart);
    }

    @Override
    public Cart deleteFromCart(UUID id, UUID cartProductId) {
        Cart cart = cartDao.getOne(id);
        List<CartProduct> cartProductList = cart.getCartProducts();
        cartProductList.removeIf(i -> i.getId().equals(cartProductId));
        cart.setTotalAmount(calculateTotalAmount(cartProductList));
        return cartDao.save(cart);
    }

    public Cart getById(final UUID id) {
        return cartDao.getOne(id);
    }

    public Cart save(final Cart cart) {
        return cartDao.save(cart);
    }

    public CartProduct save(final CartProduct cartProduct) {
        return productCartDao.save(cartProduct);
    }

    private Double calculateTotalAmount(CartProduct cartProduct){
        Product product = productService.get(cartProduct.getProductId());
        Double price = product.getPrice();
        boolean discount = product.isDiscount();

        return discount ? price/2*cartProduct.getQuantity() : price*cartProduct.getQuantity();
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
