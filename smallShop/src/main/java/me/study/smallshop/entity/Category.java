package me.study.smallshop.entity;

import lombok.Getter;
import lombok.Setter;
import me.study.smallshop.entity.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Category extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;
    private String name;

    /* 카테고리 엔티티가 연관관계의 주인이 된다. */
    /* 실무에서는 @ManyToMany를 잘 사용하지 않는다. 연결 테이블에 필드를 추가할 수 없다는 한계가 존재하기 때문 */
    //TODO CategoryItem이라는 연결 엔티티를 따로 만들어서 다대일로 매핑을 변경할 것
    @ManyToMany
    @JoinTable(name="CATEGORY_ITEM",
            joinColumns = @JoinColumn(name="CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name="ITEM_ID"))
    private List<Item> items = new ArrayList<>();

    /* 카테고리의 계층 구조를 위한 필드들 */
    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }
    public void addItem(Item item) {
        items.add(item);
    }
}
