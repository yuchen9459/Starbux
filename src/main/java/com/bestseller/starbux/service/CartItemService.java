package com.bestseller.starbux.service;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.repository.CartItemRepository;
import com.bestseller.starbux.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class CartItemService {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartRepository cartRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartItemService.class);

    @Transactional
    public Cart update(long itemId, Integer quantity, long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) {
            LOGGER.error(String.format("The user with id <%d> does not exist", userId));
            throw new NotFoundException("The user does not exist");
        }
        Set<CartItem> cartItems = cart.getItems();

        Optional<CartItem> op = cartItems.stream().filter(e -> itemId == e.getProductId()).findFirst();
        if(!op.isPresent()) {
            throw new BadRequestException(String.format("The item with product id <%d> is not in this cart", itemId));
        }
        op.ifPresent(cartItem -> {
            cartItem.setQuantity(quantity + cartItem.getQuantity());
            if(cartItem.getQuantity() <= 0) {
                cartItems.remove(cartItem);
                cart.setItems(cartItems);
                cartItemRepository.deleteById(cartItem.getCartItemId());
            } else {
                cartItemRepository.save(cartItem);
            }
        });
        return cart;
    }
}
