package com.bestseller.starbux.controller;

import com.bestseller.starbux.model.Cart;
import com.bestseller.starbux.model.CartItem;
import com.bestseller.starbux.model.OrderInfo;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.service.CartItemService;
import com.bestseller.starbux.service.CartService;
import com.bestseller.starbux.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private ProductService productService;
    private Product productInfo;
    private Product productInfo1;
    private long userId;
    private Set<CartItem> cartItems;
    private CartItem item1;

    @InjectMocks
    @Resource
    private CartController cartController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userId = 1L;
        productInfo = new Product(1L,"Black Coffee", 2, "TOPPING", 2);
        productInfo1 = new Product(2L,"Mocha", 2, "TOPPING", 5);
        cartItems = new HashSet<>();
        item1 = new CartItem(1, 2);
        cartItems.add(item1);
        cartItems.add(new CartItem(2, 2));
    }

    @Test
    public void should_add_new_item_to_cart() {
        CartItem item1 = new CartItem(1L, 2);

        when(productService.getProduct(productInfo.getId())).thenReturn(Optional.of(productInfo));
        ResponseEntity<List<Product>> response = cartController.addToCart(item1, userId);

        assertThat(response.getStatusCodeValue(), equalTo(200));
    }

    @Test
    public void should_update_cart_item() {
        Cart cart = new Cart();

        when(cartService.update(productInfo.getId(), 2, userId)).thenReturn(cart);

        ResponseEntity<List<Product>> response = cartController.updateCartItem(item1, userId);

        assertThat(response.getStatusCodeValue(), equalTo(200));
    }

    @Test
    public void should_delete_cart_item() {
        Cart cart = new Cart();

        when(cartService.delete(productInfo.getId(), userId)).thenReturn(cart);

        ResponseEntity<List<Product>> response = cartController.updateCartItem(item1, userId);

        assertThat(response.getStatusCodeValue(), equalTo(200));
    }

    @Test
    public void should_check_out() {
        OrderInfo order = new OrderInfo();

        when(cartService.checkout(userId)).thenReturn(order);

        ResponseEntity<List<Product>> response = cartController.checkout(userId);

        assertThat(response.getStatusCodeValue(), equalTo(200));
    }
}
