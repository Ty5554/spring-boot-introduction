package com.example.todo.service.accounting;

import java.time.LocalDateTime;

public record OrderEntity(
        Long id,
        int subtotal,
        int tax,
        int totalAmount,
        int payment,
        int changeAmount,
        LocalDateTime orderedAt
) {
}
