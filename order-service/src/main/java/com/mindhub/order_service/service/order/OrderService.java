package com.mindhub.order_service.service.order;

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
import com.mindhub.order_service.models.order.Order;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderByIdRequest(Long id) throws OrderNotFoundException;

    Order getOrderById(Long id) throws OrderNotFoundException;

    List<OrderDTO> getAllOrdersRequest();

    List<Order> getAllOrders();

    OrderDTO createNewOrderRequest(NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, InsufficientProductStockException, JsonProcessingException, InvalidOrderException;

    Order createNewOrder(NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedResponseException, UnexpectedValueException, ProductNotFoundException, InsufficientProductStockException, JsonProcessingException, InvalidOrderException;

    OrderDTO patchOrderRequest(Long id, PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException;

    Order patchOrder(Long id, PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException;

    ConfirmOrderDTO confirmOrderRequest(Long id) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException, OrderAlreadyCompletedException;

    ConfirmOrderDTO confirmOrder(Long id) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException, OrderAlreadyCompletedException;
}
