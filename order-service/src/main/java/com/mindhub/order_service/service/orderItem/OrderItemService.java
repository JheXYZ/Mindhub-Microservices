package com.mindhub.order_service.service.orderItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindhub.order_service.dtos.orderItem.*;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderItemNotFoundException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.orderItem.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO createNewOrderItemRequest(NewOrderItemRequestDTO newOrderItemRequestDTO) throws OrderNotFoundException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException;

    OrderItem createNewOrderItem(NewOrderItemRequestDTO newOrderItemRequestDTO) throws OrderNotFoundException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException;

    List<OrderItem> createNewOrderItemBatch(Order order, List<NewOrderItemDTO> newOrderItemDTOList);

    OrderItemDTO getOrderItemByIdRequest(Long id) throws OrderItemNotFoundException;

    OrderItem getOrderItemById(Long id) throws OrderItemNotFoundException;

    List<OrderItemDTO> getAllOrderItemsRequest();

    List<OrderItem> getAllOrderItems();

    OrderItemDTO patchOrderItemRequest(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException;

    OrderItem patchOrderItem(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException;
}
