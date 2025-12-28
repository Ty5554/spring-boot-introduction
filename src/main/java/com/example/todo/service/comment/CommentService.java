package com.example.todo.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.repository.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentEntity> findByTaskId(long taskId) {
        return commentRepository.selectByTaskId(taskId);
    }

    @Transactional
    public void create(CommentEntity entity) {
        commentRepository.insert(entity);
    }

    @Transactional
    public void delete(long taskId, long commentId) {
        commentRepository.delete(taskId, commentId);
    }
}