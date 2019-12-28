package com.zhacky.ninjapos.repository;

import com.zhacky.ninjapos.model.sales.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}
