package com.bestseller.starbux.repository;

import com.bestseller.starbux.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
}
