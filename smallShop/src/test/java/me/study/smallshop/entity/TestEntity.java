package me.study.smallshop.entity;


import me.study.smallshop.entity.item.Item;
import me.study.smallshop.entity.item.Movie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
public class TestEntity {

    @Autowired
    private TestEntityManager em;
    private Member member;
    private Item item;

    @BeforeEach
    void setUpData() {
/*        EntityTransaction tx = em.getTransaction();
        tx.begin();*/

        this.item = Movie.builder()
                .name("그래비티")
                .price(10000)
                .stockQuantity(100)
                .build();
        em.persist(item);

        this.member = em.find(Member.class, 1L);
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .build();
        order.setMember(member);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(10000)
                .count(10)
                .build();
        order.addOrderItem(orderItem);

        em.persist(orderItem);
        em.persist(order);
/*        tx.commit();*/
    }

    @Test
    void getOrderedMemberByOrderId() {
        Long orderId = 1L;
        Order order = em.find(Order.class, orderId);
        Member member = order.getMember();
        Assertions.assertThat(member).isEqualTo(this.member);
    }
    @Test
    void getOrderedItemByOrderId() {
        Long orderId = 2L;
        // TODO : orderId 2로 두번째 setUp()이 진행된다.
        // orderId 1L인 경우의 데이터는 존재하지 않아서 null을 반환한다.
        // h2는 매번 초기화되는데, em은 초기화되지 않는 것일지
        // flush(test에서는 persistFlush와 같은 메소드를 제공한다고 한다.)를 사용하지 않아서인지 확인해볼 예정이다.
        Order order = em.find(Order.class, orderId);
        OrderItem orderItem = order.getOrderItems().get(0);
        Item item = orderItem.getItem();
        Assertions.assertThat(item).isEqualTo(this.item);
    }

    @Test
    void chkCascadeAll() {

        Delivery delivery = new Delivery();
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();

        Order order = new Order();
        order.setDelivery(delivery);
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);
        em.persist(order);
    }

}
