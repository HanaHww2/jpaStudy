package me.study.smallshop.entity.item;

import lombok.*;
import me.study.smallshop.entity.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 단일 테이블 전략
// 단일 테이블 전략인 경우, 필수 사용
// 생략하면, 디폴트 값(DTYPE)으로 설정
@DiscriminatorColumn(name = "DTYPE")
@Entity
public abstract class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
