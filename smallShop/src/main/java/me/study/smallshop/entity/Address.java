package me.study.smallshop.entity;

import lombok.*;

import javax.persistence.Embeddable;

/* 공유 참조로 인한 부작용을 막기 위해 불변객체로 만드는 것이 권장된다. */
@Getter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Embeddable /* 값 타입 클래스 생성 */
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
