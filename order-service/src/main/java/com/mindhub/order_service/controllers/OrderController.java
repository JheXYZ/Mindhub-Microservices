package com.mindhub.order_service.controllers;

import com.mindhub.order_service.dtos.order.NewOrderRequestDTO;
import com.mindhub.order_service.dtos.order.OrderDTO;
import com.mindhub.order_service.dtos.order.PatchOrderRequestDTO;
import com.mindhub.order_service.exceptions.InvalidOrderException;
import com.mindhub.order_service.exceptions.OrderNotFoundException;
import com.mindhub.order_service.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return orderService.getOrderByIdRequest(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrdersRequest();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createNewOrder(@Valid @RequestBody NewOrderRequestDTO newOrderRequestDTO) {
        return orderService.createNewOrderRequest(newOrderRequestDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updatePatchOrder(@PathVariable Long id, @Valid @RequestBody PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException {
        return orderService.patchOrderRequest(id, patchOrderRequestDTO);
    }

}
