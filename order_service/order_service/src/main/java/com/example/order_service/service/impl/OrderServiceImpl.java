package com.example.order_service.service.impl;

import com.example.order_service.client.ProductClient;
import com.example.order_service.common.OrderStatus;
import com.example.order_service.dto.OrderDTO;
import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.dto.request.ProductFilter;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.events.OrderCreatedEvent;
import com.example.order_service.exception.ApplicationException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final OrderItemRepository orderItemRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate ;


    @Override
    public Order create(OrderDTO orderDTO) {
        List<String> productIds = orderDTO.getOrderItems().stream().map(item -> item.getProductId()).distinct().toList();

        log.info("Fetching product details for productIds: {}", productIds);

        List<ProductDTO> products = productClient.getProductsByIds(new ProductFilter(productIds)); // lấy ra thông tin của list Product ở đây
        log.info("Received product details: {}", products);

        Map<String, ProductDTO> productPriceMap = new HashMap<>();

        products.forEach(product -> {
            productPriceMap.put(product.getId(), product);
        });

        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setStatus(OrderStatus.New.name());
        order.setTotalAmount(0);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created with id: {}", savedOrder.getId());

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            ProductDTO productDTO = productPriceMap.get(orderItemDTO.getProductId());
            if (productDTO == null) {
                throw new ApplicationException("Product not found with id: " + orderItemDTO.getProductId() + "not existed");
            }
            if (orderItemDTO.getQuantity() > productDTO.getStock()) {
                throw new ApplicationException("Product with id: " + orderItemDTO.getProductId() + " not enough stock");
            }
            Integer price = productDTO.getPrice();
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProductId(orderItemDTO.getProductId());
            item.setQuantity(orderItemDTO.getQuantity());
            item.setPrice(price);

            orderItems.add(item);
            totalAmount += (price * orderItemDTO.getQuantity());
        }

        productClient.lockProducts(orderDTO.getOrderItems());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setOrderItems(orderItems);

//        return orderRepository.save(savedOrder); // nếu làm đồng bộ thì có thể return luôn từ bước này do đã lockProducts
        Order createdOrder = orderRepository.save(savedOrder);

        // do createdOrder đã có orderItems rồi nên đoạn này của anh huấn comment đi
//        OrderCreatedEvent orderCreatedEvent = orderMapper.toEvent(createdOrder) ;
//        orderCreatedEvent.setOrderItems(orderItems);

        kafkaTemplate.send("order_created", createdOrder) ; // gửi message sang kafka topic "order_created" với payload là createdOrder
        log.info("Published order created event to Kafka for order id: {}", createdOrder.getId());

        return  createdOrder ;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteById(String id) {
        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {

        }
        return "Deleted successfully";
    }

    @Override
    @Transactional
    public void changeStatus(Order order) {
        Order orderExisted = orderRepository.getById(order.getId())  ;
        orderExisted.setStatus(OrderStatus.Processing.name());
        orderRepository.save(orderExisted) ;
    }


}
