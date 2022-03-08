package ch3;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class JpaStudy {

    /* 영속성 컨텍스트 생명주기 */
    public void treatPersistenceContext(EntityManager em) {

        /*엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.*/
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /* 비영속(new/transient)
         * 순수한 객체 상태, 영속성 컨텍스트나 DB와 전혀 관련이 없는 상태
         * */
        Member member = new Member();
        member.setId("member");
        member.setUsername("하나");
        member.setAge(1);

        /* 영속(managed)
         * 영속 컨텍스트가 관리하는 엔티티
         * 엔티티 매니저를 통해 영속 컨텍스트 내부 1차 캐시에 회원 엔티티를 저장한 상태
         * 아직 db에 회원 엔티티를 저장하지 않은 상태.
         * */
        em.persist(member);

        tx.commit();
        tx.begin();

        /* 준영속(detached)
         * 회원 엔티티를 영속성 컨텍스트에서 분리한 상태
         * */
        em.detach(member);
        // em.close(); /*영속성 컨텍스트를 닫는다*/
        // em.clear(); /*영속성 컨텍스트를 초기화*/

        Member memberMerged = em.merge(member); // 다시 영속성 컨텍스트에 등록

        /* 삭제(removed)
         * 엔티티를 영속성 컨텍스트와 데이터베이스에서 삭제
         * */
        em.remove(memberMerged);

        tx.commit();
    }

    /* 이하 메소드들을 실행하기 위해 엔티티 미리 생성 */
    public void createEntity(EntityManager em) {

        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("하나1");
        member1.setAge(20);

        Member member2 = new Member();
        member2.setId("member2");
        member2.setUsername("하나2");
        member2.setAge(30);

        Member member3 = new Member();
        member3.setId("member3");
        member3.setUsername("하나3");
        member3.setAge(40);

        /*엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.*/
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        tx.commit();

        em.detach(member3);
    }

    /*
     * 영속성 컨텍스트의 특징
     * - 식별자 값으로 엔티티를 구분하므로, 식별자 값이 필수적이다.
     * - 트랜잭션을 커밋하는 순간 영속성 컨텍스트에 저장된 엔티티 데이터를 데이터베이스에 반영한다. flush()
     * - 장점 : 1차 캐시, 동일성 보장, 트랜잭션을 지원하는 쓰기 지연, 변경 감지, 지연 로딩
     * */

    /*
     * Entity 조회 - 1차 캐시, 동일성
     * - 1차 캐시는 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 애플리케이션 차원에서 제공한다.
     * */
    public void searchEntity(EntityManager em) {

        /*
         * 1차 캐시(영속성 컨텍스트 내부에 존재)의 키는 식별자 값(db의 기본키와 매핑)
         * 아래의 find 메소드는 먼저 영속성 컨텍스트의 1차 캐시(메모리)에서 엔티티를 찾는다.
         * - 1차 캐시를 사용해 엔티티를 메모리에 저장하므로, 성능상 이점이 있다.
         * */
        Member findMember2 = em.find(Member.class, "member2");
        System.out.println("findMember=" + findMember2.getUsername() + ", age=" + findMember2.getAge());

        /*
         * 호출하려는 엔티티가 1차 캐시에 없다면, db에서 조회해서 엔티티를 생성한다.
         * 생성한 엔티티는 1차 캐시에 저장되고, 영속 상태의 엔티티로 반환된다.
         * */
        Member findMember3 = em.find(Member.class, "member3");
        System.out.println("findMember=" + findMember3.getUsername() + ", age=" + findMember3.getAge());

        /*
         * 영속 엔티티는 동일성을 보장한다.
         * 동일한 키로 엔티티를 반복 호출하면,
         * 엔티티 매니저는 영속성 컨텍스트의 1차 캐시에 있는 같은 인스턴스를 반환한다.
         * */
        Member a = em.find(Member.class, "member1");
        Member b = em.find(Member.class, "member1");
        System.out.println(a == b); // true, 동일성을 갖는다.
    }

    /*
     * Entity 등록 - 트랜잭션을 지원하는 쓰기 지연
     * - DB에서 커밋 직전에 한꺼번에 SQL을 전달해 데이터를 동기화하는 방식으로 성능 최적화가 가능하다.
     * */
    public void registerEntity(EntityManager em) {

        /*엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.*/
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member memberA = new Member();
        memberA.setId("memberA");
        memberA.setUsername("하나A");
        memberA.setAge(11);

        Member memberB = new Member();
        memberB.setId("memberB");
        memberB.setUsername("하나B");
        memberB.setAge(22);

        /*
         * 1차 캐시에 회원 엔티티들을 저장하면서,
         * 엔티티 등록 쿼리를 만들어 쓰기 지연 SQL 저장소에 보관한다.
         * */
        em.persist(memberA);
        em.persist(memberB);

        /*
         * 트랜잭션을 지원하는 쓰기 지연 Transactional write-behind
         * 트랜잭션을 커밋하는 순간 영속성 컨텍스트의 flush한다.
         * (flush : 영속성 컨텍스트의 변경 내용을 db에 동기화하는 작업)
         * 구체적으로는 쓰기 지연 SQL 저장소에 모인 쿼리를 db에 전송한다.
         * 이렇게 동기화가 진행된 이후, 실제 데이터베이스의 트랜잭션을 커밋한다.
         * */
        tx.commit();
    }

    /*
     * Entity 수정 - 변경 감지 dirty checking
     * - SQL 수정 쿼리를 이용하는 경우 비즈니스 로직이 SQL에 의존적이게 되는 점을 JPA 변경 감지 기능으로 해결한다.
     * - 엔티티에서 일어난 변경사항을 데이터베이스에 자동으로 반영하는 기능을 제공한다.
     * - 영속성 컨텍스트에 엔티티를 보관할 때, 최초 상태를 복사해 스냅샷을 만들어 두고,
     * 플러시 시점에 스냅샷과 엔티티를 비교해 변경된 엔티티를 찾는다.
     * - JPA는 엔티티의 모든 필드를 업데이트하는 수정 쿼리 사용을 기본 전략으로 갖는다.
     * -- 모든 필드를 사용해 DB에 전달하는 데이터 전송량이 증가하는 단점이 있지만,
     * -- 수정 쿼리가 항상 동일하기에, 애플리케이션 로딩 시점에 수정 쿼리를 생성해서 재사용할 수 있다.
     * -- DB에 동일한 쿼리를 보내므로, DB는 이전에 한 번 파싱된 쿼리를 재사용할 수 있다.
     * - 엔티티의 크기가 너무 크다면, 수정된 데이터만 사용해 동적으로 수정 쿼리를 생성하는 전략을 선택할 수 있다.
     * -- 컬럼이 30개 이상인 경우를 말하는데, 동적 쿼리 사용보다는 테이블의 책임을 먼저 잘 분리할 필요가 있다.
     * -- @org.hibernate.annotation.DynamicUpdate (하이버네이트의 확장 기능)
     * */
    public void modifyEntity(EntityManager em) {

        /*엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.*/
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /* 영속 엔티티 조회 */
        Member memberA = em.find(Member.class, "memberA");

        /* 영속 엔티티 수정 */
        memberA.setUsername("hi");
        memberA.setAge(33);

        /*
         * 트랜잭션 커밋
         * - 엔티티 매니저 내부에서 플러시 flush()를 호출
         * - 영속 상태 엔티티 중 변경된 엔티티가 있다면, 수정 쿼리를 생성해 쓰기 지연 SQL 저장소에 등록
         * - 쓰기 지연 저장소의 SQL을 DB로 전달하여 동기화
         * - DB 트랜잭션 커밋
         * */
        tx.commit();
    }

    /*
     * Entity 삭제 - 변경 감지
     * */
    public void deleteEntity(EntityManager em) {

        /*엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.*/
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /* 삭제할 엔티티 조회 */
        Member memberB = em.find(Member.class, "memberB");

        /*
         * 엔티티 등록과 유사하다.
         * 영속성 컨텍스트에서 엔티티를 제거하고,(엔티티는 가비지 컬렉션의 대상이 된다.)
         * 삭제 쿼리를 쓰기 지연 SQL 저장소에 등록한다.
         * */
        em.remove(memberB);

        /*
         * 내부적으로 flush 호출하여 db와 동기화
         * */
        tx.commit();
    }

    /*
    * 플러시하는 방법
    * - 직접 em.flush() 호출
    * - 트랜잭션 커밋 시 내부적으로 플러시 호출
    * - JPQL 쿼리 실행시 플러시 자동 호출
    * -- DB에서 어떤 쿼리를 실행하기 이전에는 영속성 컨텍스트를 플러시해서 변경 내용을 DB에 반영해야 한다.
    * -- 식별자 기준으로 조회하는 find() 메소드를 호출할 때는 플러시가 실행되지 않는다. (아마 1차 캐시를 활용하므로)
    *
    * 플러시 모드 옵션
    * - 엔티티 매니저에 플러시 모드를 직접 지정할 수 있다.
    * - javax.persistence.FlushModeType.AUTO : 커밋이나 쿼리를 실행할 대 플러시(기본값)
    * - javax.persistence.FlushModeType.COMMIT : 커밋할 때만 플러시
    * -- em.setFlushMode(FlushModeType.COMMIT)
     * -- 성능 최적화를 위해 사용할 수 있다.
    * */

    /*
    * 준영속
    * - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리된 것
    * -- 거의 비영속 상태에 가깝다. 1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩 불가
    * -- 식별자 값을 반드시 가지고 있다. (원래 영속 상태였으므로)
    * - 개발자가 직접 엔티티를 준영속 상태로 만드는 일은 드물다.
    * - 3가지 방법
    * -- em.detach(entity); 특정 엔티티를 준영속 상태로 전환
    * -- em.clear(); 영속성 컨텍스트를 완전히 초기화
    * -- 영속성 컨텍스트의 모든 엔티티가 준영속 상태가 된다.
    * -- 영속성 컨텍스트를 제거하고 새롭게 만든 것과 같다.
    * -- em.close(); 영속성 컨텍스트 종료
    * -- 영속성 컨텍스트의 모든 엔티티가 준영속 상태가 된다.
    * */
    public void testDetached(EntityManager em) {

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = em.find(Member.class, "memberA");
        member.setAge(100);

        /*
        * 준영속 상태로 변환
        * - 1차 캐시와 쓰기 지연 SQL 저장소에 존재하는 해당 엔티티를 관리하기 위한 모든 정보가 제거된다.
        * */
        em.detach(member);
        tx.commit();
    }

    /*
    * 병합 merge()
    * 준영속 상태의 엔티티를 받아 새로운 영속 상태의 엔티티를 반환한다.
    * */

    /* 준영속 병합 */
    /* 비영속 병합 */
}
