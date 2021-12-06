package com.tul.cart.service;

import com.tul.cart.dao.ICartDao;
import com.tul.cart.dao.IProductCartDao;
import com.tul.cart.domain.Cart;
import com.tul.cart.domain.CartProduct;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService implements ICartService {

    private IProductCartDao productCartDao;
    private ICartDao cartDao;

    public CartService(ICartDao cartDao, IProductCartDao productCartDao) {
        this.cartDao = cartDao;
        this.productCartDao = productCartDao;
    }

    @Override
    public CartProduct save(final CartProduct cartProduct) {
        return productCartDao.save(cartProduct);
    }

    @Override
    public Cart save(final Cart cart) {
        return cartDao.save(cart);
    }

    @Override
    public Cart getById(final UUID id) {
        return cartDao.getOne(id);
    }
}
