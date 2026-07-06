package com.example.todo.repository.accounting;

import com.example.todo.service.accounting.OrderItemEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemRepository {

    @Select("SELECT id, order_id, item_name, item_price, quantity, subtotal FROM order_items WHERE order_id = #{orderId} ORDER BY id")
    List<OrderItemEntity> selectByOrderId(long orderId);

    @Insert("INSERT INTO order_items (order_id, item_name, item_price, quantity, subtotal) VALUES (#{orderId}, #{itemName}, #{itemPrice}, #{quantity}, #{subtotal})")
    void insert(OrderItemEntity entity);
}
