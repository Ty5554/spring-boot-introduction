package com.example.todo.service.accounting;

import com.example.todo.repository.accounting.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemEntity> findAll() {
        return itemRepository.selectAll();
    }

    public Optional<ItemEntity> findById(long id) {
        return itemRepository.selectById(id);
    }

    @Transactional
    public void create(ItemEntity entity) {
        itemRepository.insert(entity);
    }

    @Transactional
    public void update(ItemEntity entity) {
        itemRepository.update(entity);
    }

    @Transactional
    public void delete(long id) {
        itemRepository.delete(id);
    }
}
