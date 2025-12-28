package com.example.todo.service.comment;

import java.time.LocalDateTime;

public record CommentEntity(
        Long id,
        Long taskId,
        String content,
        LocalDateTime createdAt
) {
}