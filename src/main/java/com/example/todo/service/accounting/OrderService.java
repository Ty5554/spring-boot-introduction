package com.example.todo.service.accounting;

import com.example.todo.repository.accounting.OrderItemRepository;
import com.example.todo.repository.accounting.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public List<OrderEntity> findAll() {
        return orderRepository.selectAll();
    }

    public Optional<OrderEntity> findById(long id) {
        return orderRepository.selectById(id);
    }

    public List<OrderItemEntity> findOrderItems(long orderId) {
        return orderItemRepository.selectByOrderId(orderId);
    }

    @Transactional
    public OrderEntity checkout(List<OrderItemEntity> orderItems, int payment) {
        int subtotal = orderItems.stream().mapToInt(OrderItemEntity::subtotal).sum();
        int tax = (int) Math.floor(subtotal * 0.1);
        int totalAmount = subtotal + tax;
        int changeAmount = payment - totalAmount;

        OrderEntity order = new OrderEntity(null, subtotal, tax, totalAmount, payment, changeAmount, null);
        orderRepository.insert(order);
        long orderId = orderRepository.selectLastInsertId();

        for (OrderItemEntity item : orderItems) {
            OrderItemEntity withOrderId = new OrderItemEntity(
                    null, orderId, item.itemName(), item.itemPrice(), item.quantity(), item.subtotal()
            );
            orderItemRepository.insert(withOrderId);
        }

        return orderRepository.selectById(orderId).orElseThrow();
    }
}
