package com.bestseller.starbux.controller;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.lib.Pair;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.service.CartItemService;
import com.bestseller.starbux.service.OrderInfoService;
import com.bestseller.starbux.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderInfoService orderInfoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id) {
        Optional<Product> product = productService.getProduct(id);
        if(!product.isPresent()) {
            LOGGER.error(String.format("The request product id <%d> does not exist", id));
            throw new NotFoundException("The request product does not exist");
        }
        return ResponseEntity.ok().body(product.get());
    }

    @PostMapping("/product/add")
    public ResponseEntity addProduct(@RequestBody Product productInfo) {
        Product productNameExist = productService.getProductByName(productInfo.getName());
        if (productNameExist != null) {
            throw new BadRequestException("The product already exists");
        }
        return ResponseEntity.ok(productService.addProduct(productInfo));
    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity updateProduct(@PathVariable("id") long id, @RequestBody Product productInfo) {
        if(productInfo.getId() == null) {
            LOGGER.error("The product id in the body is missing");
            throw new BadRequestException("The product id in the body is missing");
        }
        if (id != productInfo.getId()) {
            LOGGER.error("Ids are not match");
            throw new BadRequestException("Ids are not match");
        }
        return ResponseEntity.ok(productService.updateProduct(productInfo));
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(String.format("Product with id <%d> is deleted", id));
    }

    @GetMapping("/reports/user-orders")
    public ResponseEntity getUserOrders() {
        Map<Long, Long> userOrders = orderInfoService.getOrdersPerUser();
        return ResponseEntity.ok(userOrders);
    }

    @GetMapping("/reports/most-sold-toppings")
    public ResponseEntity getMostSoldTopping() {
        Pair<String, Long> mostSoldTopping = orderInfoService.getMostSoldTopping();
        return ResponseEntity.ok(mostSoldTopping);
    }
}
