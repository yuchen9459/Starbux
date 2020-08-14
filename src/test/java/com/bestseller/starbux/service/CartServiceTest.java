package com.bestseller.starbux.service;

import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.repository.CartItemRepository;
import com.bestseller.starbux.repository.CartRepository;
import com.bestseller.starbux.repository.OrderInfoRepository;
import com.bestseller.starbux.repository.ProductRepository;
import com.bestseller.starbux.lib.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderInfoRepository orderInfoRepository;

    @InjectMocks
    @Resource
    private CartService cartService;

    private double priceOriginal;
    private double priceDiscount;
    private long userId;
    private Cart newCart;
    private Set<CartItem> cartItems;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        priceDiscount = 0;
        priceOriginal = 0;
        userId = 1L;
        CartItem item1 = new CartItem(1L,2);
        CartItem item2 = new CartItem(2L, 2);
        cartItems = new HashSet<>();
        cartItems.add(item1);
        cartItems.add(item2);
        newCart = new Cart(cartItems, userId);
    }

    @Test
    public void should_create_cart() {
        CartService cartServiceMock = spy(cartService);
        doReturn(new Pair(priceOriginal, priceDiscount)).when(cartServiceMock).calculatePrice(cartItems);

        cartServiceMock.createCart(cartItems, userId);

        verify(cartRepository).save(any());
    }

    @Test
    public void should_update_cart() {
        long itemId = 1;
        int quantity = 1;

        CartService cartServiceMock = spy(cartService);
        doReturn(new Pair(priceOriginal, priceDiscount)).when(cartServiceMock).calculatePrice(cartItems);
        when(cartItemService.update(itemId, quantity, userId)).thenReturn(newCart);

        cartServiceMock.update(itemId, quantity, userId);

        verify(cartRepository).save(any());
    }

    @Test
    public void should_delete_item() {
        long productId = 1L;

        CartService cartServiceMock = spy(cartService);
        doReturn(new Pair(priceOriginal, priceDiscount)).when(cartServiceMock).calculatePrice(cartItems);
        when(cartRepository.findCartByUserId(userId)).thenReturn(newCart);
        cartServiceMock.delete(productId, userId);

        verify(cartItemRepository, times(1)).deleteById(any());
    }
}
