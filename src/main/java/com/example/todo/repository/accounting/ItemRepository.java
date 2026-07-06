package com.example.todo.repository.accounting;

import com.example.todo.service.accounting.ItemEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemRepository {

    @Select("SELECT id, name, price FROM items ORDER BY id")
    List<ItemEntity> selectAll();

    @Select("SELECT id, name, price FROM items WHERE id = #{id}")
    Optional<ItemEntity> selectById(long id);

    @Insert("INSERT INTO items (name, price) VALUES (#{name}, #{price})")
    void insert(ItemEntity entity);

    @Update("UPDATE items SET name = #{name}, price = #{price} WHERE id = #{id}")
    void update(ItemEntity entity);

    @Delete("DELETE FROM items WHERE id = #{id}")
    void delete(long id);
}
