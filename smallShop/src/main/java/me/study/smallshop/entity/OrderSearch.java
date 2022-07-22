package me.study.smallshop.entity;

import lombok.Getter;
import lombok.Setter;

/* 검색 조건을 갖는 클래스 */
@Getter @Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

}
