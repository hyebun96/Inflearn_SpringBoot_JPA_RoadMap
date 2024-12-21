package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMainSubQuery {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(1);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 서브쿼리의 한계
            // JPA는 WHERE, HAVING 절에서만 서브 쿼리 가능
            // FROM 절은 불가능
            // SELECT 절은 하이버네이트에서 지원
            String query = "select (select avg(m1)from Member m1) from Member m join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result = " + result.size());

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

