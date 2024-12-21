package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMainJoin {
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

            String query = "select m from Member m join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                                    .setFirstResult(0)
                                    .setMaxResults(10)
                                    .getResultList();

            System.out.println("result = " + result.size());

            // 조인 대상 필터링
            String query2 = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> result2 = em.createQuery(query2, Member.class)
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

