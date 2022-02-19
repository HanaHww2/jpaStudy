package jpabook.ch2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        /*
        * 엔티티 매니저 팩토리 생성
        * - JPA를 동작시키는 기반 객체 그리고 구현체에 따라서는 디비 커넥션 풀도 생성한다.
        * - 애플리케이션 전체에서 딱 한 번만 생성하고 공유해서 사용한다.
        * - META-INF/persistence.xml 의 설정정보를 사용하여 엔티티 매니저 팩토리를 생성.
        * - 영속성 유닛 이름을 매개변수로 전달한다.
        * */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        /*
        * 엔티티 매니저 생성
        * - JPA의 기능 대부분을 제공(엔티티를 디비에 등록/수정/삭제/조회하는 기능 등)
        * - 내부에 디비 커넥션을 유지하며 디비와 통신하므로 가상의 디비로 간주할 수 있다.
        * - 디비 커넥션과 밀접한 관계가 있으므로, 스레드 간 공유 및 재사용이 불가하다.
        * */
        EntityManager em = emf.createEntityManager();

        /*
        * 트랜잭션 획득
        * - 엔티티 매니저를 통해 트랜잭션 api를 받아온다.
        * - JPA 활용시 항상 트랜잭션 내에서 데이터를 변경해야 한다. 그렇지 않으면 예외가 발생한다.
        * */
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin(); // 트랜잭션 시작
            logic(em); // 비즈니스 로직 실행
            tx.commit(); // 로직 완료시 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback(); // 오류 발생시, 트랜잭션 롤백
        } finally {
            em.close(); // 엔티티 매니저 종료
        }
        emf.close(); // 엔티티 매니저 팩토리 종료
    }

    // 비즈니스 로직
    private static void logic(EntityManager em) {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("하나");
        member.setAge(1);

        // 등록
        em.persist(member);

        // 수정
        member.setAge(20);

        // 한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        /*
        * 목록 조회
        * - 객체지향 쿼리 언어인 JPQL(Java Persistence Query Language)을 활용해
        * 엔티티 객체를 대상으로 하는 검색 쿼리를 생성한다.
        * - 이후, JPA가 JPQL을 분석해 적절한 SQL로 변환한 뒤, 디비에서 데이터를 조회한다.
        * - JPQL은 대소문자를 명확히 구분한다.
        * */
        List<Member> members =
                em.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println("members.size=" + members.size());

        // 삭제
        em.remove(member);
    }
}
