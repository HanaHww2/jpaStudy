package me.study.smallshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name="ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
