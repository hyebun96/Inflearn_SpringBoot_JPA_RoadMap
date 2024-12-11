package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 조회할때, 영속성 컨텍스트(1차캐시) 부터 찾기 때문에 없으면 DB를 통해 가져와 1차캐시에 저장
            Member findMember1 = em.find(Member.class, 101L);
            // 2번째 조회할때 영속성 컨텍스트(1차캐시)에 존재하기 때문에 DB조회하지 않는다.
            Member findMember2 = em.find(Member.class, 101L);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
