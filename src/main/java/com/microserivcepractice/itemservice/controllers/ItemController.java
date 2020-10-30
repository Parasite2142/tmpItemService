package com.microserivcepractice.itemservice.controllers;

import com.microserivcepractice.itemservice.controllers.assemblers.ItemAssembler;
import com.microserivcepractice.itemservice.controllers.services.ItemServiceInterface;
import com.microserivcepractice.itemservice.models.CartItem;
import com.microserivcepractice.itemservice.models.CartItemList;
import com.microserivcepractice.itemservice.models.HateoasWrapper;
import com.microserivcepractice.itemservice.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final RestTemplate web;
    private final ItemServiceInterface itemService;
    private final ItemAssembler itemAssembler;

    @PostMapping("/items")
    public EntityModel<Item> createItem(@RequestBody @Valid Item item) {
        return itemAssembler.toModel(itemService.addItem(item));
    }

    @GetMapping("/items/{id}")
    public EntityModel<Item> getItem(@PathVariable int id) {
        return itemAssembler.toModel(itemService.getItem(id));
    }

    @GetMapping("/items")
    public CollectionModel<EntityModel<Item>> getItems() {
        return itemAssembler.toCollectionModel(itemService.getItemsWithQntGreaterOrEqual(1));
    }

    @GetMapping("/items/all")
    public CollectionModel<EntityModel<Item>> getAllItems() {
        return itemAssembler.toCollectionModel(itemService.getItems());
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        return this.itemService.deleteItem(id);
    }

    @PutMapping("/items/checkout")
    public ResponseEntity<?> checkOut(@RequestBody @Valid HateoasWrapper<CartItemList> cartItemList) {
        return this.itemService.checkOut(cartItemList.getWrappedClass());
    }

    @PatchMapping("/items/{id}")
    public EntityModel<Item> addToItem(@RequestBody Item item) {
        return this.itemAssembler.toModel(this.itemService.addToItem(item));
    }

    // Test methods

    @GetMapping("/test/cart")
    public CartItemList getCartItemList() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(1, web.getForObject("http://localhost:8080/items/1", Item.class), 5));
        list.add(new CartItem(2, web.getForObject("http://localhost:8080/items/2", Item.class), 10));
        return new CartItemList(list);
    }

    @GetMapping("/test/ssss")
    public CollectionModel<? extends EntityModel<?>> getCartItem() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(1, web.getForObject("http://localhost:8080/items/1", Item.class), 5));
        list.add(new CartItem(2, web.getForObject("http://localhost:8080/items/2", Item.class), 10));
        return CollectionModel.of(list.stream().map(ob -> EntityModel.of(ob,
                linkTo(methodOn(ItemController.class).getCartItem()).withSelfRel())).collect(Collectors.toList()));
    }

    @PostMapping("/wrapper/test")
    public HateoasWrapper<CartItemList> getWrapperClass(@RequestBody HateoasWrapper<CartItemList> cartWrapper) {
        return cartWrapper;
    }

}
