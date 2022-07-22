package me.study.smallshop.service;

import lombok.AllArgsConstructor;
import me.study.smallshop.entity.item.Item;
import me.study.smallshop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;

    // 상품 리포지토리에 단순 위임만 하는 방식
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
