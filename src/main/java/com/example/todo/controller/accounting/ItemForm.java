package com.example.todo.controller.accounting;

import com.example.todo.service.accounting.ItemEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemForm(
        @NotBlank @Size(max = 256) String name,
        @Min(1) int price
) {
    public ItemEntity toEntity() {
        return new ItemEntity(null, name, price);
    }

    public ItemEntity toEntity(long id) {
        return new ItemEntity(id, name, price);
    }

    public static ItemForm fromEntity(ItemEntity entity) {
        return new ItemForm(entity.name(), entity.price());
    }
}
