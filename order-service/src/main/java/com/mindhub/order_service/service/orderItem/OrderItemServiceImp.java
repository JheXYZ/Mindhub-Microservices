package com.mindhub.order_service.service.orderItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mindhub.order_service.dtos.orderItem.NewOrderItemDTO;
import com.mindhub.order_service.dtos.orderItem.NewOrderItemRequestDTO;
import com.mindhub.order_service.dtos.orderItem.OrderItemDTO;
import com.mindhub.order_service.dtos.orderItem.PatchOrderItemRequestDTO;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderItemNotFoundException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.orderItem.OrderItem;
import com.mindhub.order_service.repositories.OrderItemRepository;
import com.mindhub.order_service.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class OrderItemServiceImp implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderItemDTO createNewOrderItemRequest(NewOrderItemRequestDTO newOrderItemRequestDTO) throws OrderNotFoundException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException {
        return new OrderItemDTO(createNewOrderItem(newOrderItemRequestDTO));
    }

    @Override
    public OrderItem createNewOrderItem(NewOrderItemRequestDTO newOrderItemRequest) throws OrderNotFoundException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException {
        Order order = orderRepository.findById(newOrderItemRequest.orderId())
                .orElseThrow(OrderNotFoundException::new);
        return orderItemRepository.save(
                new OrderItem(
                        order,
                        newOrderItemRequest.productId(),
                        newOrderItemRequest.quantity())
        );
    }

    @Override
    public List<OrderItem> createNewOrderItemBatch(Order order, List<NewOrderItemDTO> newOrderItemDTOList) {
        return orderItemRepository.saveAll(
                newOrderItemDTOList.stream()
                        .map(newOrderItemDTO ->
                                new OrderItem(
                                        order,
                                        newOrderItemDTO.productId(),
                                        newOrderItemDTO.quantity()
                                ))
                        .toList()
        );
    }

    @Override
    public OrderItemDTO getOrderItemByIdRequest(Long id) throws OrderItemNotFoundException {
        return new OrderItemDTO(getOrderItemById(id));
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws OrderItemNotFoundException {
        return orderItemRepository.findById(id)
                .orElseThrow(OrderItemNotFoundException::new);
    }

    @Override
    public List<OrderItemDTO> getAllOrderItemsRequest() {
        return getAllOrderItems().stream().map(OrderItemDTO::new).toList();
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItemDTO patchOrderItemRequest(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException {
        return new OrderItemDTO(patchOrderItem(id, patchOrderItem));
    }

    @Override
    public OrderItem patchOrderItem(Long id, PatchOrderItemRequestDTO patchOrderItem) throws OrderItemNotFoundException, OrderNotFoundException, InvalidOrderException {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(OrderItemNotFoundException::new);

        makePatchUpdates(orderItem, patchOrderItem);
        return orderItem;
    }

    private void makePatchUpdates(OrderItem orderItem, PatchOrderItemRequestDTO patchOrderItem) throws InvalidOrderException, OrderNotFoundException {
        if (patchOrderItem == null || patchOrderItem.orderId() == null && patchOrderItem.productId() == null && patchOrderItem.quantity() == null)
            throw new InvalidOrderException("at least one field must be provided for update");

        if (patchOrderItem.orderId() != null && !orderItem.getOrder().getId().equals(patchOrderItem.orderId()))
            orderItem.setOrder(
                    orderRepository.findById(patchOrderItem.orderId())
                            .orElseThrow(OrderNotFoundException::new));
        if (patchOrderItem.quantity() != null)
            orderItem.setQuantity(patchOrderItem.quantity());
        if (patchOrderItem.productId() != null)
            orderItem.setProductId(patchOrderItem.productId());
    }
}
