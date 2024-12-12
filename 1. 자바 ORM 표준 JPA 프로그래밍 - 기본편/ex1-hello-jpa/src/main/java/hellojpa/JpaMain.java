package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member(200L, "member200");
            em.persist(member);

            // 플러시
            em.flush();

            System.out.println("========");

            // flush 되는 순간에 현 Entity와 스냅샷을 비교하여 다르면 update해줌
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
