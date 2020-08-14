package com.bestseller.starbux.service;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.lib.Pair;
import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.model.OrderInfo;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.repository.CartItemRepository;
import com.bestseller.starbux.repository.CartRepository;
import com.bestseller.starbux.repository.OrderInfoRepository;
import com.bestseller.starbux.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderInfoRepository orderInfoRepository;
    @Autowired
    CartItemService cartItemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    public Cart getCart(long userId) {
        return cartRepository.findCartByUserId(userId);
    }

    public Pair<Double, Double> calculatePrice(Set<CartItem> cartItems) {
        double sumPriceDiscount;
        double sumPriceOriginal = 0;
        double lowestPrice = 0;
        int drinkAmount = 0;

        for (CartItem item : cartItems) {
            Product productInfo = productRepository.getOne(item.getProductId());
            drinkAmount += (productInfo.getCategoryType().equals("DRINK") ? 1 : 0);
            sumPriceOriginal += productInfo.getPrice() * item.getQuantity();
            lowestPrice = Math.min(lowestPrice, productInfo.getPrice());
        }

        if (drinkAmount > 3) {
            sumPriceDiscount = sumPriceOriginal - Math.min(lowestPrice, sumPriceOriginal * 0.25);
        } else if (sumPriceOriginal > 12) {
            sumPriceDiscount = sumPriceOriginal * 0.75;
        } else {
            sumPriceDiscount = sumPriceOriginal;
        }

        return new Pair(sumPriceOriginal, sumPriceDiscount);
    }

    @Transactional
    public Cart createCart(Collection<CartItem> cartItems, long userId) {
        Cart newCart = new Cart(new HashSet<>(cartItems), userId);
        Pair<Double, Double> price = calculatePrice(new HashSet<>(cartItems));
        newCart.setPriceOriginal(price.getKey());
        newCart.setPriceDiscount(price.getValue());
        for (CartItem cartItem : cartItems) {
            cartItem.setCart(newCart);
        }
        LOGGER.info(String.format("New cart for user with id <%d> was created.", userId));
        return cartRepository.save(newCart);
    }

    @Transactional
    public Cart mergeCart(Collection<CartItem> cartItems, long userId) {
        if (cartRepository.findCartByUserId(userId) == null) {
            return createCart(cartItems, userId);
        } else {
            return mergeLocalCart(cartItems, userId);
        }
    }

    @Transactional
    public Cart mergeLocalCart(Collection<CartItem> cartItems, long userId) {
        Cart finalCart = cartRepository.findCartByUserId(userId);
        if (finalCart == null) {
            LOGGER.error(String.format("User with <%d> does not exist", userId));
            throw new NotFoundException("User does not exist");
        }
        cartItems.forEach(cartItem -> {
            Set<CartItem> set = finalCart.getItems();
            Optional<CartItem> old = set.stream().filter(e -> e.getProductId() == cartItem.getProductId()).findFirst();
            CartItem sumProduct;
            if (old.isPresent()) {
                sumProduct = old.get();
                sumProduct.setQuantity(cartItem.getQuantity() + sumProduct.getQuantity());
            } else {
                sumProduct = cartItem;
                sumProduct.setCart(finalCart);
                finalCart.getItems().add(sumProduct);
            }
            cartRepository.save(finalCart);
        });
        Pair<Double, Double> price = calculatePrice(new HashSet<>(finalCart.getItems()));
        finalCart.setPriceOriginal(price.getKey());
        finalCart.setPriceDiscount(price.getValue());
        return finalCart;
    }

    @Transactional
    public Cart update(long itemId, Integer quantity, long userId) {
        Cart updatedCart = cartItemService.update(itemId, quantity, userId);
        Pair<Double, Double> price = calculatePrice(new HashSet<>(updatedCart.getItems()));
        updatedCart.setPriceOriginal(price.getKey());
        updatedCart.setPriceDiscount(price.getValue());
        LOGGER.info(String.format("Cart for user with id <%d> was updated", userId));
        return cartRepository.save(updatedCart);
    }

    @Transactional
    public Cart delete(long productId, long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) {
            LOGGER.error(String.format("User with id <%id> does not exist", userId));
            throw new NotFoundException("User does not exist");
        }
        Optional<CartItem> cartItem = cart.getItems().stream().filter(e -> productId == e.getProductId()).findFirst();
        if (cartItem.isPresent()) {
            cartItem.get().setCart(null);
            cart.getItems().remove(cartItem.get());
            cartItemRepository.deleteById(cartItem.get().getCartItemId());
        } else {
            LOGGER.error(String.format("The item with id <%d> is not in the cart with id <%d>", productId, cart.getCartId()));
            throw new BadRequestException("The item is not in this cart");
        }
        Pair<Double, Double> price = calculatePrice(new HashSet<>(cart.getItems()));
        cart.setPriceOriginal(price.getKey());
        cart.setPriceDiscount(price.getValue());
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public OrderInfo checkout(long userId) {
        Cart userCart = cartRepository.findCartByUserId(userId);
        if (userCart == null) {
            LOGGER.error(String.format("User with id <%d> does not exist", userId));
            throw new NotFoundException("User does not exist");
        }

        // Transfer info from cart to order
        OrderInfo order = new OrderInfo(userId);
        order.setPriceOriginal(userCart.getPriceOriginal());
        order.setPriceDiscount(userCart.getPriceDiscount());
        order.setItems(userCart.getItems());
        userCart.getItems().forEach(cartItem -> {
            cartItem.setCart(null);
            cartItem.setOrder(order);
            cartItemRepository.save(cartItem);
        });
        // Clear cart
        userCart.setItems(null);
        userCart.setPriceDiscount(0);
        userCart.setPriceOriginal(0);
        orderInfoRepository.save(order);
        // Sum the sales num
        order.getItems().forEach(cartItem -> {
            Product productInfo = productRepository.findById(cartItem.getProductId()).get();
            productInfo.setSalesNum(productInfo.getSalesNum() + cartItem.getQuantity());
        });
        return order;
    }
}
