package com.mindhub.order_service.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindhub.order_service.dtos.order.*;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedValueException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderAlreadyCompletedException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.InsufficientProductStockException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.exceptions.user.UserNotFoundException;
import com.mindhub.order_service.models.order.Order;
import jakarta.validation.Valid;

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

    ConfirmOrderDTO confirmOrderRequest(Long id, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException, OrderAlreadyCompletedException, UserNotFoundException;

    ConfirmOrderDTO confirmOrder(Long id, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException, OrderAlreadyCompletedException, UserNotFoundException;

    List<OrderDTO> getAllOrdersByUserIdRequest(Long userId);

    List<Order> getAllOrdersByUserId(Long userId);

    OrderDTO getOrderByIdFromUserRequest(Long id, Long userId) throws OrderNotFoundException;

    Order getOrderByIdFromUser(Long id, Long userId) throws OrderNotFoundException;

    OrderDTO createNewOrderFromUserRequest(Long userId, NewOrderRequestFromUser newOrderRequestFromUser) throws UserNotFoundException, UnexpectedResponseException, UnexpectedValueException, InvalidOrderException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException;

    Order createNewOrderFromUser(Long userId, NewOrderRequestFromUser newOrderRequestFromUser) throws UserNotFoundException, UnexpectedResponseException, UnexpectedValueException, InvalidOrderException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException;

    ConfirmOrderDTO confirmOrderRequestFromUserRequest(Long id, Long userId, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UserNotFoundException, OrderAlreadyCompletedException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException;

    ConfirmOrderDTO confirmOrderRequestFromUser(Long id, Long userId, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UserNotFoundException, OrderAlreadyCompletedException, InsufficientProductStockException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException;
}
