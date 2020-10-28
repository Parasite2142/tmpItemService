package com.microserivcepractice.itemservice.controllers.services.impl;

import com.microserivcepractice.itemservice.controllers.services.ItemServiceInterface;
import com.microserivcepractice.itemservice.models.CartItem;
import com.microserivcepractice.itemservice.models.CartItemList;
import com.microserivcepractice.itemservice.models.Item;
import com.microserivcepractice.itemservice.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService implements ItemServiceInterface {

    private final ItemRepository itemRepository;
    private static final String NOT_FOUND_MSG = "Item not found id = ";


    public Item getItem(int id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MSG + id));
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsWithQntGreaterOrEqual(int qnt) {
        return this.itemRepository.findAllByStockGreaterThanEqual(qnt);
    }

    public Item addItem(Item item) {
        synchronized (this) {
            return itemRepository.save(item);
        }
    }

    public Item addToItem(Item item) {
        synchronized (this) {
            Item updatableItem = this.itemRepository.findById(item.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            updatableItem.setStock(updatableItem.getStock() + item.getStock());
            return this.itemRepository.save(item);
        }
    }

    public ResponseEntity<?> deleteItem(int id) {
        synchronized (this) {
            Item deleteItem = this.itemRepository.findById(id).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            NOT_FOUND_MSG + id));
            deleteItem.setStock(0);
            itemRepository.save(deleteItem);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> checkOut(CartItemList itemList) {
        synchronized (this) {
            itemList.getCartItemList().forEach(this::updateItemQnt);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateItemQnt(CartItem cartItem) {
        if (this.itemRepository.updateItem(
                cartItem.getItem().getId(),
                cartItem.getQnt()) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not enough items of id = " + cartItem.getItem().getId());
        }
    }

    public Item getValidatedItem(int id, int qnt) {
        Item item = this.itemRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Item with id = %d doesn't exist", id)));
        if (item.getStock() - qnt < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "not enough items in the stock item id = " + id);
        }
        return item;
    }
    
}
