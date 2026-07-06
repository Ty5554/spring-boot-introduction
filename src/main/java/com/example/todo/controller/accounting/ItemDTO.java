package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.ItemEntity;

public record ItemDTO(long id, String name, int price, String formattedPrice) {

    public static ItemDTO fromEntity(ItemEntity entity) {
        String formatted = String.format("%,d", entity.price());
        return new ItemDTO(entity.id(), entity.name(), entity.price(), formatted);
    }
}
