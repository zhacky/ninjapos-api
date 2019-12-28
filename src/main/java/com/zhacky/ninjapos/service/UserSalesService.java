package com.zhacky.ninjapos.service;

import com.zhacky.ninjapos.model.sales.Order;
import com.zhacky.ninjapos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Qualifier("UserSalesService")
@Service
public class UserSalesService implements SalesService {

    @Autowired
    OrderRepository orderRepository;

    public UserSalesService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }
}
