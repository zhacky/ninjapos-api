package com.zhacky.ninjapos.controller;

import com.zhacky.ninjapos.model.Product;
import com.zhacky.ninjapos.model.sales.LineItem;
import com.zhacky.ninjapos.model.sales.Order;
import com.zhacky.ninjapos.service.SalesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SalesController {

    @Autowired
    SalesService salesService;

    public SalesController(@Qualifier("UserSalesService") SalesService salesService) {
        this.salesService = salesService;
    }

    @ApiOperation(value = "Retrieve all orders", authorizations = { @Authorization(value = "JWT")})
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {

        List<Order> orders = salesService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrder(@RequestBody Order order) {

        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> addLineItemsToOrder(@PathVariable Long orderId, @RequestBody LineItem item) {

        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/orders/{orderId}/products/")
    public ResponseEntity<?> addProductItemToOrder(@RequestBody Product product, @PathVariable Long orderId) {


        return ResponseEntity.ok("Ok");
    }

}
