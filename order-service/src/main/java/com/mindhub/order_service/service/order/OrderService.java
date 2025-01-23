package com.mindhub.order_service.service.order;

import com.mindhub.order_service.dtos.order.NewOrderRequestDTO;
import com.mindhub.order_service.dtos.order.OrderDTO;
import com.mindhub.order_service.dtos.order.PatchOrderRequestDTO;
import com.mindhub.order_service.exceptions.InvalidOrderException;
import com.mindhub.order_service.exceptions.OrderNotFoundException;
import com.mindhub.order_service.models.order.Order;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderByIdRequest(Long id) throws OrderNotFoundException;

    Order getOrderById(Long id) throws OrderNotFoundException;

    List<OrderDTO> getAllOrdersRequest();

    List<Order> getAllOrders();

    OrderDTO createNewOrderRequest(NewOrderRequestDTO newOrderRequestDTO);

    Order createNewOrder(NewOrderRequestDTO newOrderRequestDTO);

    OrderDTO patchOrderRequest(Long id, PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException;

    Order patchOrder(Long id, PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException;
}
