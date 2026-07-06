package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.OrderItemEntity;

public record OrderItemDTO(
        String itemName,
        String itemPrice,
        int quantity,
        String subtotal
) {
    public static OrderItemDTO fromEntity(OrderItemEntity entity) {
        return new OrderItemDTO(
                entity.itemName(),
                String.format("%,d", entity.itemPrice()),
                entity.quantity(),
                String.format("%,d", entity.subtotal())
        );
    }
}
