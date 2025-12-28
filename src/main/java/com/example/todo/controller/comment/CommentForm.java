package com.example.todo.controller.comment;

import com.example.todo.service.comment.CommentEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentForm(
        @NotBlank(message = "コメントを入力してください")
        @Size(max = 1024, message = "1024文字以内で入力してください")
        String content
) {
    public CommentEntity toEntity(long taskId) {
        return new CommentEntity(null, taskId, content(), null);
    }
}