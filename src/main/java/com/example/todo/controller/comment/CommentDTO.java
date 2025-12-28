package com.example.todo.controller.comment;

import java.time.LocalDateTime;

import com.example.todo.service.comment.CommentEntity;

public record CommentDTO(
        long id,
        String content,
        LocalDateTime createdAt
) {
    public static CommentDTO fromEntity(CommentEntity entity) {
        return new CommentDTO(
                entity.id(),
                entity.content(),
                entity.createdAt()
        );
    }
}