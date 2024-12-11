package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 트랜잭션을 지원하는 수정감지 _ 엔티티 수정
            Member member = em.find(Member.class, 150L);

            member.setName("update A"); // 자바 컬렉션 다루듯 값만 바꿨는데, 알아서 수정해줌

            System.out.println("============");

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
