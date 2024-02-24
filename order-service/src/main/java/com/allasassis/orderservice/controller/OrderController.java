package com.allasassis.orderservice.controller;

import com.allasassis.orderservice.dto.Order;
import com.allasassis.orderservice.dto.OrderEvent;
import com.allasassis.orderservice.publisher.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public ResponseEntity createOrder(@RequestBody Order order) {
        order.setId(UUID.randomUUID().toString());
        orderProducer.sendMessage(new OrderEvent("PENDING", "Order is in pending status!", order));
        return ResponseEntity.status(201).body("Order sent to RabbitMQ!");
    }
}
