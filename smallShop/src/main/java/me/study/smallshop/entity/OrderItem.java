package me.study.smallshop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
//@Table(name="ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name="ORDER_ID")
    private Order order;

    private int orderPrice;
    private int count;

    @Builder
    public OrderItem(Item item, Order order, int orderPrice, int count) {
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public void setOrder(Order order) {
        this.order = order;
        // 무한루프 방지
        if (!order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }
 /*   public void setItem(Item item) {
        this.item = item;
        if (!item.getOrderItems().contains(this)) {
            item.getOrderItems().add(this);
        }
    }*/
}
