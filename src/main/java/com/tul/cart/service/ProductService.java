package com.tul.cart.service;

import com.tul.cart.dao.IProductDao;
import com.tul.cart.domain.Product;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
    public Optional<Product> get(final UUID id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> delete(UUID id) {
        Optional<Product> product = this.get(id);

        if(!product.isPresent())
            return new ArrayList<>();

        productDao.deleteById(id);
        return this.getproducts();
    }

    @Override
    public Optional<Product> modify(UUID id, Product product) {
        Optional<Product> productUpdate = Optional.of(productDao.getOne(id));
        if(productUpdate.isPresent()){
            productUpdate.get().setName(product.getName());
            productUpdate.get().setSku(product.getSku());
            productUpdate.get().setDescription(product.getDescription());
            productUpdate.get().setPrice(product.getPrice());
            productUpdate.get().setDiscount(product.isDiscount());
            return Optional.of(productDao.save(productUpdate.get()));
        }

        return Optional.empty();
    }

    @Override
    public void updateStock(UUID id, int quantity){
        Optional<Product> product = this.get(id);
        int sku = product.get().getSku();
        int finalStock = sku-quantity;
        product.get().setSku(finalStock);
        this.save(product.get());
    }
}
