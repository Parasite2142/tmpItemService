package com.microserivcepractice.itemservice.controllers.assemblers;

import com.microserivcepractice.itemservice.controllers.ItemController;
import com.microserivcepractice.itemservice.models.Item;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {

    @Override
    public EntityModel<Item> toModel(Item entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ItemController.class).
                        getItem(entity.getId())).withSelfRel(),
                linkTo(methodOn(ItemController.class).
                        getItems()).withRel("items"));
    }

    @Override
    public CollectionModel<EntityModel<Item>> toCollectionModel(Iterable<? extends Item> entities) {
        return CollectionModel.of(StreamSupport.
                        stream(entities.spliterator(), false).
                        map(this::toModel).
                        collect(Collectors.toList()),
                linkTo(methodOn(ItemController.class).getItems()).withSelfRel());
    }
}

