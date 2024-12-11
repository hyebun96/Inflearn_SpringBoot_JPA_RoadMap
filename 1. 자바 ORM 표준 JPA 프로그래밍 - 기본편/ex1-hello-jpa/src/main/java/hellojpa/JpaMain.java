package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 생성
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("HelloA");
//            em.persist(member);

            // 조회
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
