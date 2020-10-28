package com.microserivcepractice.itemservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartWrapper {

    @JsonProperty("_embedded")
    private CartItemList cartItemList;
}
