package com.bestseller.starbux.service;

import com.bestseller.starbux.exception.errors.BadRequestException;
import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    @Resource
    private ProductService productService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_delete_product_when_it_exists() {
        long id = 1L;
        Product productInfo = new Product(1L,"Black Coffee", 2, "TOPPING", 2);

        when(productRepository.findById(id)).thenReturn(java.util.Optional.of(productInfo));
        doNothing().when(productRepository).deleteById(id);

        productService.deleteProduct(id);
    }

    @Test
    public void should_throw_exception_when_product_id_does_not_exist() {
        long id = 2L;

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.deleteProduct(id));

        assertThat(exception.getMessage(), is("The product id is invalid"));
    }
}
