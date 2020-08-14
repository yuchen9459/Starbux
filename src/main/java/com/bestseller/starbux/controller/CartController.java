package com.bestseller.starbux.controller;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.model.OrderInfo;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.service.CartItemService;
import com.bestseller.starbux.service.CartService;
import com.bestseller.starbux.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable  long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity addToCart(@RequestBody CartItem cartItem, @PathVariable long userId) {
        Optional<Product> productInfo = productService.getProduct(cartItem.getProductId());
        if(cartItem.getQuantity() <= 0){
            LOGGER.error("When add the item for the first time, the quantity should be positive number");
            throw new BadRequestException("The quantity should be positive");
        }
        Cart updatedCart;
        if (productInfo.isPresent()) {
            updatedCart = cartService.mergeCart(Collections.singleton(cartItem), userId);
            return ResponseEntity.ok(updatedCart);
        } else {
            throw new BadRequestException("The product does not exist");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateCartItem(@RequestBody CartItem cartItem, @PathVariable long userId) {
        Cart updatedCart = cartService.update(cartItem.getProductId(), cartItem.getQuantity(), userId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity deleteCartItem(@PathVariable long productId, @PathVariable long userId) {
        Cart updatedCart = cartService.delete(productId, userId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity checkout(@PathVariable long userId) {
        OrderInfo order = cartService.checkout(userId);
        return ResponseEntity.ok(order);
    }
}
