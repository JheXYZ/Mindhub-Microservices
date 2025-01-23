package com.mindhub.order_service.service.order;

import com.mindhub.order_service.dtos.order.NewOrderRequestDTO;
import com.mindhub.order_service.dtos.order.OrderDTO;
import com.mindhub.order_service.dtos.order.PatchOrderRequestDTO;
import com.mindhub.order_service.dtos.orderItem.NewOrderItemDTO;
import com.mindhub.order_service.exceptions.InvalidOrderException;
import com.mindhub.order_service.exceptions.OrderNotFoundException;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.orderItem.OrderItem;
import com.mindhub.order_service.repositories.OrderRepository;
import com.mindhub.order_service.service.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public OrderDTO getOrderByIdRequest(Long id) throws OrderNotFoundException {
        return new OrderDTO(getOrderById(id));
    }

    @Override
    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<OrderDTO> getAllOrdersRequest() {
        return getAllOrders().stream().map(OrderDTO::new).toList();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderDTO createNewOrderRequest(NewOrderRequestDTO newOrderRequestDTO) {
        return new OrderDTO(createNewOrder(newOrderRequestDTO));
    }

    @Override
    public Order createNewOrder(NewOrderRequestDTO newOrderRequestDTO) {
        Order order = orderRepository.save(new Order(newOrderRequestDTO.userId(), newOrderRequestDTO.status()));
        List<OrderItem> products = orderItemService.createNewOrderItemBatch(order, newOrderRequestDTO.products());
        order.setProducts(products);
        return order;
    }

    @Override
    public OrderDTO patchOrderRequest(Long id, PatchOrderRequestDTO patchOrderRequestDTO) throws OrderNotFoundException, InvalidOrderException {
        return new OrderDTO(patchOrder(id, patchOrderRequestDTO));
    }

    @Override
    public Order patchOrder(Long id, PatchOrderRequestDTO patchOrder) throws OrderNotFoundException, InvalidOrderException {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        makePatchUpdates(order, patchOrder);
        return orderRepository.save(order);
    }

    private void makePatchUpdates(Order order, PatchOrderRequestDTO patchOrder) throws InvalidOrderException {
        if (patchOrder == null || patchOrder.userId() == null && patchOrder.status() == null)
            throw new InvalidOrderException("at least one field must be provided for update");

        if (patchOrder.userId() != null)
            order.setUserId(patchOrder.userId());
        if (patchOrder.status() != null)
            order.setStatus(patchOrder.status());
    }

}
