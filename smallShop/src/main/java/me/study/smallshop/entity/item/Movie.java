package me.study.smallshop.entity.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor @AllArgsConstructor
@DiscriminatorValue("M")
public class Movie extends Item {
    private String director;
    private String actor;

    @Builder
    public Movie(String name, int price, int stockQuantity, String director, String actor) {
        super.setName(name);
        super.setPrice(price);
        super.setStockQuantity(stockQuantity);
        this.director = director;
        this.actor = actor;
    }
}
