package com.example.todo.repository.comment;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.todo.service.comment.CommentEntity;

@Mapper
public interface CommentRepository {

    @Select("""
            SELECT id, task_id, content, created_at
            FROM comments
            WHERE task_id = #{taskId}
            ORDER BY created_at DESC, id DESC
            """)
    List<CommentEntity> selectByTaskId(@Param("taskId") long taskId);

    @Insert("""
            INSERT INTO comments (task_id, content)
            VALUES (#{comment.taskId}, #{comment.content})
            """)
    void insert(@Param("comment") CommentEntity comment);

    @Delete("DELETE FROM comments WHERE id = #{commentId} AND task_id = #{taskId}")
    void delete(@Param("taskId") long taskId, @Param("commentId") long commentId);
}