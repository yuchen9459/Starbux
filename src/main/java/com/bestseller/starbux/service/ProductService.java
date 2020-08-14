package com.bestseller.starbux.service;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(long id) {
        return productRepository.findById(id);
    }

    public Product getProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        if(productRepository.findById(id).isPresent()){
            productRepository.deleteById(id);
        }
        else{
            LOGGER.error("The product with id <%id> is not in the database");
            throw new BadRequestException("The product id is invalid");
        }
    }
}
