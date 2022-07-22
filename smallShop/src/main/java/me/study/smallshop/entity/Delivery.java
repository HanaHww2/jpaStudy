package me.study.smallshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

//    private String city;
//    private String street;
//    private String zipcode;

    @Embedded // @Embeddable과 둘 중 하나만 적용해도 된다.
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery() {}
    public Delivery(Address address) {
        this.address = address;
    }
}
