package com.microserivcepractice.itemservice.controllers.services;

import com.microserivcepractice.itemservice.models.CartItemList;
import com.microserivcepractice.itemservice.models.Item;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemServiceInterface {

    Item getItem(int id);

    Item addItem(Item item);

    Item addToItem(int id, int qnt);

    ResponseEntity<?> deleteItem(int id);

    List<Item> getItems();

    ResponseEntity<?> checkOut(CartItemList cartItemList);

    Boolean getValidation(int id, int qnt);
}
