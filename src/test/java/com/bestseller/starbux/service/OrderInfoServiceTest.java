//package com.bestseller.starbux.service;
//
//import com.bestseller.starbux.model.OrderInfo;
//import com.bestseller.starbux.model.Product;
//import com.bestseller.starbux.repository.OrderInfoRepository;
//import com.bestseller.starbux.repository.ProductRepository;
//import com.bestseller.starbux.lib.Pair;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Mockito.when;
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//public class OrderInfoServiceTest {
//    @Mock
//    private OrderInfoRepository orderInfoRepository;
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    @Resource
//    private OrderInfoService orderInfoService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void should_get_order_per_user() {
//        OrderInfo orderInfo = new OrderInfo(1, 1, null, 1, 1);
//        OrderInfo orderInfo1 = new OrderInfo(2, 2, null, 2, 2);
//        List<OrderInfo> orderList = Arrays.asList(orderInfo, orderInfo1);
//
//        when(orderInfoRepository.findAll()).thenReturn(orderList);
//        Map<Long, Long> res = orderInfoService.getOrdersPerUser();
//
//        assertThat(res.size(), equalTo(2));
//        assertThat(res.get((long)1), equalTo((long)1));
//    }
//
//    @Test
//    public void should_get_most_sold_topping() {
//        Product productInfo = new Product(1L,"Black Coffee", 2, "TOPPING", 2);
//        Product productInfo1 = new Product(2L,"Mocha", 2, "TOPPING", 5);
//        List<Product> productsList = Arrays.asList(productInfo, productInfo1);
//
//        when(productRepository.findAll()).thenReturn(productsList);
//        Pair<String, Long> res = orderInfoService.getMostSoldTopping();
//        assertThat(res.getKey(), equalTo("Mocha"));
//        assertThat(res.getValue(), equalTo(5L));
//    }
//}
