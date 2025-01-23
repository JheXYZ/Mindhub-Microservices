package com.mindhub.order_service.controllers;

import com.mindhub.order_service.dtos.orderItem.NewOrderItemRequestDTO;
import com.mindhub.order_service.dtos.orderItem.OrderItemDTO;
import com.mindhub.order_service.dtos.orderItem.PatchOrderItemRequestDTO;
import com.mindhub.order_service.exceptions.InvalidOrderException;
import com.mindhub.order_service.exceptions.OrderItemNotFoundException;
import com.mindhub.order_service.exceptions.OrderNotFoundException;
import com.mindhub.order_service.service.orderItem.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemService.getAllOrderItemsRequest();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDTO getOrderItemById(@PathVariable Long id) throws OrderItemNotFoundException {
        return orderItemService.getOrderItemByIdRequest(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemDTO createNewOrderItem(@Valid @RequestBody NewOrderItemRequestDTO newOrderItem) throws OrderNotFoundException {
        return orderItemService.createNewOrderItemRequest(newOrderItem);
    }

    @PatchMapping("/{id}")
    public OrderItemDTO patchOrderItem(@PathVariable Long id, @Valid @RequestBody PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException {
        return orderItemService.patchOrderItemRequest(id, patchOrderItem);
    }

}
