package com.tul.cart.service;

import com.tul.cart.dao.IProductDao;
import com.tul.cart.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    private IProductDao productDao;

    public ProductService(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product save(final Product product) {
        return productDao.save(product);
    }

    @Override
    public List<Product> getproducts() {
        return productDao.findAll();
    }

    @Override
    public Product get(final UUID id) {
        return productDao.getOne(id);
    }
}
