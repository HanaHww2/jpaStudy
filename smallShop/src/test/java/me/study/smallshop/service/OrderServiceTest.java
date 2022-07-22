package me.study.smallshop.service;

import me.study.smallshop.entity.Address;
import me.study.smallshop.entity.Member;
import me.study.smallshop.entity.Order;
import me.study.smallshop.entity.OrderStatus;
import me.study.smallshop.entity.item.Book;
import me.study.smallshop.entity.item.Item;
import me.study.smallshop.exception.NotEnoughStockException;
import me.study.smallshop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    Member member;
    Item item;

    @BeforeEach
    void setUp() {
        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        this.item = book;

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        this.member = member;
    }

    @Test
    @DisplayName("상품 주문이 정확히 수행되는지 확인")
    void orderWell() {
        //Given
        int orderCount = 2;
        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //Then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getStatus())
                .as("상품 주문시 상태는 ORDER 여야 한다.")
                .isEqualTo(OrderStatus.ORDER);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER 여야 한다.");
        assertEquals( 1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
        assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    @DisplayName("상품 주문이 실패했을 때 예외가 잘 발생되는지 확인")
    void orderThrowExceptionWellWhenFailed() {
        //Given
        //When
        int orderCount = 11;
        //Then
        assertThatExceptionOfType(NotEnoughStockException.class)
                .as("재고 수량 부족으로 예외가 발생해야 한다.")
                .isThrownBy(() -> {
                    orderService.order(member.getId(), item.getId(), orderCount);
                }).withMessageMatching("need more stock");
        }

    @Test
    @DisplayName("상품 주문 취소가 잘 동작하는지 확인")
    void cancelOrder() {
        //Given
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //When
        orderService.cancelOrder(orderId);
        //Then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getStatus())
                .as("주문 취소시 상태는 CANCEL 이어야 한다.")
                .isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity())
                .as("주문이 취소된 상품은 취소된 수량만큼 재고가 다시 증가해야 한다.")
                .isEqualTo(10);
    }
}