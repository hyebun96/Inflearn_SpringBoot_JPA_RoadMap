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
            member.setType(MemberType.USER);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 서브쿼리의 한계
            // JPA는 WHERE, HAVING 절에서만 서브 쿼리 가능
            // FROM 절은 하이버네이트 6부터 지원
            // SELECT 절은 하이버네이트에서 지원
            String query = "select m from Member m where m.team = (select t from Team t)";
            List resultList = em.createQuery(query)
                                .getResultList();

            System.out.println("result = " + resultList.size());

            em.flush();
            em.clear();


            // JPQL 타입표현
            // ENUM : jpbook, MemberType.Admin(패키지 명 포함)
            String query2 = "select m.username, 'HELLO', TRUE from Member m where m.type = :userType";
            List<Object[]> result2 = em.createQuery(query2)
                                        .setParameter("userType", jpql.MemberType.USER)
                                        .getResultList();

            System.out.println("size = " +result2.size());

            for (Object[] objects : result2) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
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

