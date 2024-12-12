package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 영속
            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            // detch : 영속성컨텍스트가 더이상 관리하지 않아서 update되지 않음
            // em.detach(member);

            // clear : 영속컨텍스트 전부 지워짐
            em.clear();

            // 다시 초기화되어서 DB에서 값 다시 가져옴
            Member member1 = em.find(Member.class, 150L);

            System.out.println("========");

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
