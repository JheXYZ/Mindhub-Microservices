package com.mindhub.order_service.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.order_service.dtos.order.*;
import com.mindhub.order_service.dtos.orderItem.ConfirmOrderItemDTO;
import com.mindhub.order_service.dtos.orderItem.NewOrderItemDTO;
import com.mindhub.order_service.dtos.product.DeductProductDTO;
import com.mindhub.order_service.dtos.product.ProductDTO;
import com.mindhub.order_service.dtos.product.ProductForConfirmOrderDTO;
import com.mindhub.order_service.dtos.user.UserDTO;
import com.mindhub.order_service.exceptions.CustomExceptionsHandler.ErrorResponse;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedValueException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderAlreadyCompletedException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.InsufficientProductStockException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.exceptions.user.UserNotFoundException;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.order.OrderStatus;
import com.mindhub.order_service.models.orderItem.OrderItem;
import com.mindhub.order_service.repositories.OrderRepository;
import com.mindhub.order_service.service.orderItem.OrderItemService;
import com.mindhub.order_service.utils.EndPoints;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemService orderItemService;

    // Crear un mapa de productos para acceso rápido por ID
    private static Map<Long, ProductDTO> mapToIdProduct(List<ProductDTO> products) {
        return products.stream()
                .collect(Collectors.toMap(ProductDTO::id, Function.identity()));
    }

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
    public OrderDTO createNewOrderRequest(NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedResponseException, ProductNotFoundException, InsufficientProductStockException, UnexpectedValueException, JsonProcessingException, InvalidOrderException {
        return new OrderDTO(createNewOrder(newOrderRequestDTO));
    }

    @Override
    @Transactional
    public Order createNewOrder(NewOrderRequestDTO newOrderRequestDTO) throws UserNotFoundException, UnexpectedResponseException, ProductNotFoundException, InsufficientProductStockException, UnexpectedValueException, JsonProcessingException, InvalidOrderException {
        checkForRepeatedProducts(newOrderRequestDTO.products());

        UserDTO user = getUserByEmail(newOrderRequestDTO.userEmail());

        Set<Long> ids = newOrderRequestDTO
                .products().stream()
                .map(NewOrderItemDTO::productId)
                .collect(Collectors.toSet());

        List<ProductDTO> products = getProductsListByIds(ids);

        Map<Long, Integer> requestProductsIdQuantity = new HashMap<>();
        newOrderRequestDTO
                .products()
                .forEach(product ->
                        requestProductsIdQuantity.put(product.productId(), product.quantity()));

        // checks if all products have enough stock and adds up the prices with quantity
        double totalPrice = getTotalPriceWithValidation(products, requestProductsIdQuantity);

        Order order = orderRepository.save(new Order(user.id(), totalPrice, OrderStatus.PENDING));
        List<OrderItem> savedProducts = orderItemService.createNewOrderItemBatch(order, newOrderRequestDTO.products());
        order.setOrderItems(savedProducts);
        return order;
    }

    private void checkForRepeatedProducts(List<NewOrderItemDTO> items) throws InvalidOrderException {
        if (items == null || items.isEmpty()) return;

        Set<Long> seenProducts = new HashSet<>();
        Set<Long> repeatedProducts = new HashSet<>();

        items.forEach(item -> {
            if (!seenProducts.add(item.productId())) // if it couldn't add it, it was already present, so it's repeated
                repeatedProducts.add(item.productId());
        });

        if (!repeatedProducts.isEmpty())
            throw new InvalidOrderException("repeated products were found: " + repeatedProducts);
    }

    private UserDTO getUserByEmail(String email) throws UserNotFoundException, UnexpectedResponseException {
        final String URL = EndPoints.USER_GET_BY_EMAIL(email);

        try {
            return restTemplate.getForObject(URL, UserDTO.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new UserNotFoundException();
            else
                throw new UnexpectedResponseException("an error occurred while obtaining user: (" + exception.getStatusCode().value() + ") " + exception.getMessage());
        }
    }

    private UserDTO getUserById(Long id) throws UserNotFoundException, UnexpectedResponseException {
        final String URL = EndPoints.USER_GET_BY_ID(id);

        try {
            return restTemplate.getForObject(URL, UserDTO.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new UserNotFoundException();
            else
                throw new UnexpectedResponseException("an error occurred while obtaining user: (" + exception.getStatusCode().value() + ") " + exception.getMessage());
        }
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

    @Override
    public ConfirmOrderDTO confirmOrderRequest(Long id, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException, OrderAlreadyCompletedException, UserNotFoundException {
        return confirmOrder(id, orderForEmailDTO);
    }

    @Override
    public ConfirmOrderDTO confirmOrder(Long id, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, OrderAlreadyCompletedException, UserNotFoundException, InsufficientProductStockException {
        Order order = getOrderById(id);
        if (order.getStatus().equals(OrderStatus.COMPLETED))
            throw new OrderAlreadyCompletedException();

        List<ProductDTO> products = getProductsListByIds(new HashSet<>(order.getProducts()));

        List<DeductProductDTO> deductionRequest = order.getOrderItems()
                .stream()
                .map(orderItem ->
                        new DeductProductDTO(orderItem.getProductId(), orderItem.getQuantity()))
                .toList();

        deductStock(deductionRequest);

        order.setTotalPrice(getTotalPrice(products, deductionRequest));
        order.setStatus(OrderStatus.COMPLETED);
        Order savedOrder = orderRepository.save(order);

        setOrderForEmail(orderForEmailDTO, order, products);

        return convertToDTO(savedOrder, products);
    }

    @Override
    public List<OrderDTO> getAllOrdersByUserIdRequest(Long userId) {
        return getAllOrdersByUserId(userId).stream().map(OrderDTO::new).toList();
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .orElse(List.of());
    }

    @Override
    public OrderDTO getOrderByIdFromUserRequest(Long id, Long userId) throws OrderNotFoundException {
        return new OrderDTO(getOrderByIdFromUser(id, userId));
    }

    @Override
    public Order getOrderByIdFromUser(Long id, Long userId) throws OrderNotFoundException {
        return orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public OrderDTO createNewOrderFromUserRequest(Long userId, NewOrderRequestFromUser newOrderRequestFromUser) throws UserNotFoundException, UnexpectedResponseException, UnexpectedValueException, InvalidOrderException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException {
        return new OrderDTO(createNewOrderFromUser(userId, newOrderRequestFromUser));
    }

    @Override
    public Order createNewOrderFromUser(Long userId, NewOrderRequestFromUser newOrderRequestFromUser) throws UserNotFoundException, UnexpectedResponseException, UnexpectedValueException, InvalidOrderException, InsufficientProductStockException, ProductNotFoundException, JsonProcessingException {
        UserDTO user = getUserById(userId);
        return createNewOrder(new NewOrderRequestDTO(user.email(), newOrderRequestFromUser.products()));
    }

    @Override
    public ConfirmOrderDTO confirmOrderRequestFromUserRequest(Long id, Long userId, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UserNotFoundException, OrderAlreadyCompletedException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException {
        return confirmOrderRequestFromUser(id, userId, orderForEmailDTO);
    }

    @Override
    public ConfirmOrderDTO confirmOrderRequestFromUser(Long id, Long userId, OrderForEmailDTO orderForEmailDTO) throws OrderNotFoundException, UnexpectedValueException, UserNotFoundException, OrderAlreadyCompletedException, UnexpectedResponseException, ProductNotFoundException, JsonProcessingException, InsufficientProductStockException {
        if (!orderRepository.existsByIdAndUserId(id, userId))
            throw new OrderNotFoundException();
        return confirmOrder(id, orderForEmailDTO);
    }

    private void setOrderForEmail(OrderForEmailDTO orderForEmailDTO, Order order, List<ProductDTO> products) throws UserNotFoundException, UnexpectedResponseException {
        orderForEmailDTO.setId(order.getId());
        orderForEmailDTO.setTotalPrice(order.getTotalPrice());
        orderForEmailDTO.setUser(getUserById(order.getUserId()));
        orderForEmailDTO.setStatus(order.getStatus().toString());

        Map<Long, ProductDTO> productMap = mapToIdProduct(products);
        List<ConfirmOrderItemDTO> orderItems = new ArrayList<>();
        for (var orderItem : order.getOrderItems())
            orderItems.add(new ConfirmOrderItemDTO(
                    orderItem.getId(),
                    orderItem.getQuantity(),
                    new ProductForConfirmOrderDTO(productMap.get(orderItem.getProductId())))
            );
        orderForEmailDTO.setProducts(orderItems);
    }

    private double getTotalPrice(List<ProductDTO> products, List<DeductProductDTO> prodIdQuantityList) {
        HashMap<Long, Double> prodIdPrice = new HashMap<>();
        products.forEach(prod -> prodIdPrice.put(prod.id(), prod.price()));

        double totalPrice = 0D;
        for (var prodIdQuantity : prodIdQuantityList)
            totalPrice += prodIdPrice.get(prodIdQuantity.id()) * prodIdQuantity.quantity();

        return totalPrice;
    }

    private double toPrecision(double number, int precision) {
        if (precision < 1) return number;
        return Math.round(number * Math.pow(10, precision)) / Math.pow(10, precision);
    }

    // checks if all products have enough stock and adds up the prices with quantity
    private double getTotalPriceWithValidation(List<ProductDTO> products, Map<Long, Integer> requestProductsIdQuantity) throws InsufficientProductStockException {
        double totalPrice = 0D;
        for (var prod : products)
            if (requestProductsIdQuantity.get(prod.id()) <= prod.stock())
                totalPrice += requestProductsIdQuantity.get(prod.id()) * toPrecision(prod.price(), 2);
            else
                throw new InsufficientProductStockException("product with id " + prod.id() + " does not have enough stock. (available: " + prod.stock() + ", requested: " + requestProductsIdQuantity.get(prod.id()) + ", difference: " + (requestProductsIdQuantity.get(prod.id()) - prod.stock()) + ')');
        return totalPrice;
    }

    private void makePatchUpdates(Order order, PatchOrderRequestDTO patchOrder) throws InvalidOrderException {
        if (patchOrder == null || patchOrder.userId() == null && patchOrder.status() == null)
            throw new InvalidOrderException("at least one field must be provided for update");

        if (patchOrder.userId() != null)
            order.setUserId(patchOrder.userId());
        if (patchOrder.status() != null)
            order.setStatus(patchOrder.status());
    }

    private List<ProductDTO> getProductsListByIds(Set<Long> ids) throws UnexpectedValueException, ProductNotFoundException, UnexpectedResponseException, JsonProcessingException {
        String idsFormated = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        final String URL = EndPoints.PRODUCT_GET_ALL_BY_IDS(idsFormated);

        try {
            String response = restTemplate.getForObject(URL, String.class);
            return objectMapper.readValue(response, new TypeReference<List<ProductDTO>>() {
            });
        } catch (JsonProcessingException exception) {
            throw new UnexpectedValueException(exception.getMessage());
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                ErrorResponse errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), ErrorResponse.class);
                throw new ProductNotFoundException(errorResponse.errors().get(0));
            } else
                throw new UnexpectedResponseException("an error occurred while obtaining products: " + exception.getLocalizedMessage());
        }
    }

    private void deductStock(List<DeductProductDTO> deductProductDTO) throws ProductNotFoundException, JsonProcessingException, UnexpectedResponseException, InsufficientProductStockException {
        final String URL = EndPoints.PRODUCT_PATCH_STOCK;
        try {
            String response = restTemplate.patchForObject(URL, deductProductDTO, String.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                ErrorResponse errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), ErrorResponse.class);
                throw new ProductNotFoundException(errorResponse.errors().get(0));
            } else if (exception.getStatusCode().equals(HttpStatus.BAD_REQUEST) && exception.getMessage().contains("does not have enough stock")) {
                ErrorResponse errorResponse = objectMapper.readValue(exception.getResponseBodyAsString(), ErrorResponse.class);
                throw new InsufficientProductStockException(errorResponse.errors().get(0));
            } else
                throw new UnexpectedResponseException("an error occurred while deducting stock from products: " + exception.getLocalizedMessage());
        }
    }

    private ConfirmOrderDTO convertToDTO(Order order, List<ProductDTO> products) {
        Map<Long, ProductDTO> productMap = mapToIdProduct(products);

        // Convertir los OrderItem a ConfirmOrderItemDTO
        List<ConfirmOrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(orderItem -> {
                    ProductForConfirmOrderDTO productDTO = new ProductForConfirmOrderDTO(productMap.get(orderItem.getProductId()));
                    return new ConfirmOrderItemDTO(orderItem.getId(), orderItem.getQuantity(), productDTO);
                })
                .collect(Collectors.toList());

        // Crear y devolver el OrderDTO
        return new ConfirmOrderDTO(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                toPrecision(order.getTotalPrice(), 2),
                itemDTOs
        );
    }

}
