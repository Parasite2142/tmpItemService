package com.microserivcepractice.itemservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HateoasWrapper<T> {

    @JsonProperty("_embedded")
    private T wrappedClass;
}
