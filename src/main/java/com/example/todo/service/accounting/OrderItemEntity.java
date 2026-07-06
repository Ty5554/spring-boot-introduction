package com.example.todo.service.accounting;

public record OrderItemEntity(
        Long id,
        long orderId,
        String itemName,
        int itemPrice,
        int quantity,
        int subtotal
) {
}
