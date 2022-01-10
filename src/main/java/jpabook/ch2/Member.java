package jpabook.ch2;

import javax.persistence.*;

@Entity // 엔티티 클래스
@Table(name="MEMBER") // 매핑할 테이블 정보
public class Member {
    @Id // primary key, 식별자 필드 지정
    @Column(name="ID") // 매핑할 컬럼 정보
    private String id;

    @Column(name="NAME")
    private String username;

    // 매핑 정보가 없는 경우, 필드명으로 매핑
    private Integer age;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
