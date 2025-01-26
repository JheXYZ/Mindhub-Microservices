package com.mindhub.order_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindhub.order_service.dtos.order.ConfirmOrderDTO;
import com.mindhub.order_service.dtos.order.NewOrderRequestDTO;
import com.mindhub.order_service.dtos.order.OrderDTO;
import com.mindhub.order_service.dtos.order.PatchOrderRequestDTO;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedValueException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderAlreadyCompletedException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.InsufficientProductStockException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.exceptions.user.UserNotFoundException;
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
    public OrderDTO createNewOrder(@Valid @RequestBody NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedValueException, UnexpectedResponseException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException, InvalidOrderException {
        return orderService.createNewOrderRequest(newOrderRequestDTO);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmOrderDTO confirmOrder(@PathVariable Long id) throws OrderNotFoundException, UnexpectedValueException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, OrderAlreadyCompletedException {
        return orderService.confirmOrderRequest(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updatePatchOrder(@PathVariable Long id, @Valid @RequestBody PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException {
        return orderService.patchOrderRequest(id, patchOrderRequestDTO);
    }

}
