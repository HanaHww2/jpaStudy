package me.study.smallshop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.study.smallshop.entity.item.Item;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ITEM_ID")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORDER_ID")
    private Order order;

    private int orderPrice; // 주문 가격 (할인 등 적용 고려)
    private int count;

    // 생성 메소드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem().builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();
        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    /** 주문 취소 */
    public void cancel() {
        getItem().addStock(count);
    }

    /** 주문상품 전체 가격 조회 */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

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
