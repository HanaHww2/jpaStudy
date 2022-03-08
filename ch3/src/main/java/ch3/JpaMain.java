package ch3;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        /*
        * 엔티티매니저 팩토리 생성
        * - 비용이 많이 드는 작업
        * - 일반적으로 DB 하나당 하나를 생성해서 사용한다.
        * - J2SE(standard edition) 환경에서는 엔티티매니저 팩토리를 생성할 때 커넥션풀도 만든다.
        * - 스프링 프레임워크를 포함하는 J2EE(enterprise edition) 환경에서는 컨테이너가 제공하는 데이터 소스를 사용한다.
        *  */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch3");

        /*
        * 엔티티 매니저 생성
        * - 비용이 거의 들지 않는다.
        * - 엔티티 매니저는 트랜잭션을 시작할 때 커넥션을 얻는다.
        * - 엔티티 매니저는 영속성 컨텍스트에 접근하고 관리한다.
        * */
        EntityManager em = emf.createEntityManager();

        JpaStudy study = new JpaStudy();
        study.treatPersistenceContext(em);

        study.createEntity(em);
        study.searchEntity(em);
        study.registerEntity(em);
        study.modifyEntity(em);
        study.deleteEntity(em);
        study.testDetached(em);
    }
}
