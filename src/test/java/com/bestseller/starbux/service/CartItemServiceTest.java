package com.bestseller.starbux.service;

import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.repository.CartItemRepository;
import com.bestseller.starbux.repository.CartRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


public class CartItemServiceTest {
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    CartRepository cartRepository;

    @InjectMocks
    @Resource
    private CartItemService cartItemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_update_cart() {
        long userId = 1L;
        CartItem item1 = new CartItem(1, 2);
        CartItem item2 = new CartItem(2, 2);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(item1);
        cartItems.add(item2);
        Cart cart = new Cart();
        cart.setItems(cartItems);
        cart.setUserId(userId);

        when(cartRepository.findCartByUserId(userId)).thenReturn(cart);
        cartItemService.update(2, 2, userId);

        verify(cartRepository, times(1)).findCartByUserId(userId);
        verify(cartItemRepository).save(item2);
    }
}
