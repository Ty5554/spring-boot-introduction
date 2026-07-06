package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.OrderEntity;

import java.time.format.DateTimeFormatter;

public record OrderDTO(
        long id,
        String subtotal,
        String tax,
        String totalAmount,
        String payment,
        String changeAmount,
        String orderedAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public static OrderDTO fromEntity(OrderEntity entity) {
        return new OrderDTO(
                entity.id(),
                String.format("%,d", entity.subtotal()),
                String.format("%,d", entity.tax()),
                String.format("%,d", entity.totalAmount()),
                String.format("%,d", entity.payment()),
                String.format("%,d", entity.changeAmount()),
                entity.orderedAt() != null ? entity.orderedAt().format(FORMATTER) : ""
        );
    }
}
