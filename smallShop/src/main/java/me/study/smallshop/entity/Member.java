package me.study.smallshop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMBER_ID")
    private Long id;

    private String name;

//    private String city;
//    private String street;
//    private String zipcode;

    /*
    * 값 타입은 해당 타입이 속한 엔티티의 테이블에 매핑된다.
    * */
    @Embedded // ~와 값 타입 클래스에 붙는 @Embeddable 중 하나는 생략해도 된다.
    private Address address;

    @OneToMany(mappedBy="member")
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }
//    @Builder
//    public Member(String name, String city, String street, String zipcode) {
//        this.name = name;
//        this.city = city;
//        this.street = street;
//        this.zipcode = zipcode;
//    }
}
