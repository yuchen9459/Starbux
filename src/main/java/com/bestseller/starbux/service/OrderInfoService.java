package com.bestseller.starbux.service;

import com.bestseller.starbux.exception.errors.NotFoundException;
import com.bestseller.starbux.lib.Pair;
import com.bestseller.starbux.model.OrderInfo;
import com.bestseller.starbux.model.Product;
import com.bestseller.starbux.repository.OrderInfoRepository;
import com.bestseller.starbux.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderInfoService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderInfoRepository orderInfoRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoService.class);

    public Map<Long, Long> getOrdersPerUser() {
        Map<Long, Long> map = new HashMap<>();
        List<OrderInfo> orderList = orderInfoRepository.findAll();
        if (orderList.isEmpty()) {
            LOGGER.warn("No orders can be found in the database");
            throw new NotFoundException("No orders can be found");
        }
        orderList.stream().forEach(orderInfo -> {
            long count = map.containsKey(orderInfo.getUserId()) ? 1 : 0;
            map.put(orderInfo.getUserId(), count + 1);
        });
        return map;
    }

    public Pair<String, Long> getMostSoldTopping() {
        List<Product> productsInfo = productRepository.findAll();
        if (productsInfo.isEmpty()) {
            LOGGER.warn("None of the products have been sold");
            throw new NotFoundException("None of the products have been sold");
        }
        String productName = "";
        Long salesNum = Long.MIN_VALUE;
        for (Product product : productsInfo) {
            if (product.getSalesNum() > salesNum) {
                salesNum = product.getSalesNum();
                productName = product.getName();
            }
        }
        return new Pair<>(productName, salesNum);
    }
}
