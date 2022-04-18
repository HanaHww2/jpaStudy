package me.study.smallshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMBER_ID")
    private Long id;

    private String name;
    private String city;
    private String street;
    private String zipcode;
}
