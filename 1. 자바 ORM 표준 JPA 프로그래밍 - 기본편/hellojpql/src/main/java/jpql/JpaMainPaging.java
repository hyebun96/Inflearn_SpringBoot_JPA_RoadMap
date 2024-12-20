package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMainPaging {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            // 페이징 쿼리
            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                                .setFirstResult(1)
                                .setMaxResults(10)
                                .getResultList();

            System.out.println("result.size = " + result.size());
            for (Member m : result) {
                System.out.println(m);
            }

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}

