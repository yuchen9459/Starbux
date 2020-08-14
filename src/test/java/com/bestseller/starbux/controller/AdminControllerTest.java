package com.bestseller.starbux.controller;

import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    @Mock
    ProductService productService;
    Product productInfo;
    Product productInfo1;

    @InjectMocks
    @Resource
    private AdminController adminController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productInfo = new Product(1L, "Black Coffee", 2, "TOPPING", 2);
        productInfo1 = new Product(2L, "Mocha", 2, "TOPPING", 5);
    }

    @Test
    public void should_return_all_products() {
        given_products_exists(Arrays.asList(productInfo, productInfo1));
        ResponseEntity<List<Product>> response = adminController.getProducts();

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody().size(), equalTo(2));
    }

    @Test
    public void should_return_specific_products() {
        given_a_certain_product_exists(productInfo);

        ResponseEntity<Product> response = adminController.getProduct(productInfo.getId());

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody().getName(), equalTo("Black Coffee"));
    }

    public void given_products_exists(List<Product> productList) {
        when(productService.getProducts()).thenReturn(productList);
    }

    public void given_a_certain_product_exists(Product productInfo) {
        when(productService.getProduct(productInfo.getId())).thenReturn(Optional.of(productInfo));
    }

}
