package com.microserivcepractice.itemservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Min(0)
    private int qnt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public int getPrice() {
        return this.qnt * this.item.getPrice();
    }
}
