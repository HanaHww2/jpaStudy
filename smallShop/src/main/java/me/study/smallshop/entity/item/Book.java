package me.study.smallshop.entity.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;

@Getter @Setter
@DiscriminatorValue("B")
public class Book {
    private String author;
    private String isbn;
}
