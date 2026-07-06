package com.example.todo.repository.accounting;

import com.example.todo.service.accounting.OrderEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderRepository {

    @Select("SELECT id, subtotal, tax, total_amount, payment, change_amount, ordered_at FROM orders ORDER BY ordered_at DESC")
    List<OrderEntity> selectAll();

    @Select("SELECT id, subtotal, tax, total_amount, payment, change_amount, ordered_at FROM orders WHERE id = #{id}")
    Optional<OrderEntity> selectById(long id);

    @Insert("INSERT INTO orders (subtotal, tax, total_amount, payment, change_amount) VALUES (#{subtotal}, #{tax}, #{totalAmount}, #{payment}, #{changeAmount})")
    void insert(OrderEntity entity);

    @Select("SELECT MAX(id) FROM orders")
    long selectLastInsertId();
}
