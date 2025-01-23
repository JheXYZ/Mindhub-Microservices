package com.mindhub.order_service.service.orderItem;

import com.mindhub.order_service.dtos.orderItem.*;
import com.mindhub.order_service.exceptions.InvalidOrderException;
import com.mindhub.order_service.exceptions.OrderItemNotFoundException;
import com.mindhub.order_service.exceptions.OrderNotFoundException;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.orderItem.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO createNewOrderItemRequest(NewOrderItemRequestDTO newOrderItemRequestDTO) throws OrderNotFoundException;

    OrderItem createNewOrderItem(NewOrderItemRequestDTO newOrderItemRequestDTO) throws OrderNotFoundException;

    List<OrderItem> createNewOrderItemBatch(Order order, List<NewOrderItemDTO> newOrderItemDTOList);

    OrderItemDTO getOrderItemByIdRequest(Long id) throws OrderItemNotFoundException;

    OrderItem getOrderItemById(Long id) throws OrderItemNotFoundException;

    List<OrderItemDTO> getAllOrderItemsRequest();

    List<OrderItem> getAllOrderItems();

    OrderItemDTO patchOrderItemRequest(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException;

    OrderItem patchOrderItem(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException;
}
