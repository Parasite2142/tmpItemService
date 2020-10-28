package com.microserivcepractice.itemservice.controllers.services;

import com.microserivcepractice.itemservice.models.CartItemList;
import com.microserivcepractice.itemservice.models.Item;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemServiceInterface {

    Item getItem(int id);

    Item addItem(Item item);

    Item addToItem(Item item);

    ResponseEntity<?> deleteItem(int id);

    List<Item> getItems();

    ResponseEntity<?> checkOut(CartItemList cartItemList);

    Item getValidatedItem(int id, int qnt);

    List<Item> getItemsWithQntGreaterOrEqual(int qnt);
}
