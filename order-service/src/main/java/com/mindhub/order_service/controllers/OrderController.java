package com.mindhub.order_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.order_service.dtos.order.*;
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
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mindhub.order_service.configs.RabbitMQConfig.ORDER_CONFIRM_ORDER_KEY;
import static com.mindhub.order_service.configs.RabbitMQConfig.ORDER_EXCHANGE;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return orderService.getOrderByIdRequest(id);
    }

    @GetMapping(value = "/{id}", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderByIdFromUser(@PathVariable Long id, @NotNull @RequestParam Long userId) throws OrderNotFoundException {
        return orderService.getOrderByIdFromUserRequest(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrdersRequest();
    }

    @GetMapping(params = "userId")
    public List<OrderDTO> getAllOrdersFromUser(@RequestParam Long userId) {
        return orderService.getAllOrdersByUserIdRequest(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createNewOrder(@Valid @RequestBody NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedValueException, UnexpectedResponseException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException, InvalidOrderException {
        return orderService.createNewOrderRequest(newOrderRequestDTO);
    }

    @PostMapping(params = "userId")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createNewOrderFromUser(@Valid @RequestBody NewOrderRequestFromUser newOrderRequestFromUser, @RequestParam Long userId) throws UserNotFoundException, UnexpectedValueException, InvalidOrderException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException {
        return orderService.createNewOrderFromUserRequest(userId, newOrderRequestFromUser);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmOrderDTO confirmOrder(@PathVariable Long id) throws OrderNotFoundException, UnexpectedValueException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, OrderAlreadyCompletedException, UserNotFoundException {
        OrderForEmailDTO orderForEmailDTO = new OrderForEmailDTO();
        ConfirmOrderDTO confirmOrderDTO = orderService.confirmOrderRequest(id, orderForEmailDTO);
        amqpTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_CONFIRM_ORDER_KEY, orderForEmailDTO);
        return confirmOrderDTO;
    }

    @PostMapping(value = "/{id}/confirm", params = "userId")
    public ConfirmOrderDTO confirmOrderDTOFromUser(@PathVariable Long id, @RequestParam Long userId) throws OrderNotFoundException, UnexpectedValueException, UserNotFoundException, OrderAlreadyCompletedException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException {
        OrderForEmailDTO orderForEmailDTO = new OrderForEmailDTO();
        ConfirmOrderDTO confirmOrderDTO = orderService.confirmOrderRequestFromUserRequest(id, userId, orderForEmailDTO);
        amqpTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_CONFIRM_ORDER_KEY, orderForEmailDTO);
        return confirmOrderDTO;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updatePatchOrder(@PathVariable Long id, @Valid @RequestBody PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException {
        return orderService.patchOrderRequest(id, patchOrderRequestDTO);
    }

}
