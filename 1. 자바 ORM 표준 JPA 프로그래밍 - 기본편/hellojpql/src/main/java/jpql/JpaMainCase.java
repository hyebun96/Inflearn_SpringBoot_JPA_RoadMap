package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMainCase {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setTeam(team);
            member2.setAge(70);
            member2.setType(MemberType.USER);
            em.persist(member2);

            em.flush();
            em.clear();



            // 기본 조건식
            String query =  "select case when m.age <= 10 then '학생요금'" +
                            "            when m.age >= 60 then '경로요금'" +
                            "            else '일반요금' end " +
                            "from Member m";

            List<String> resultList = em.createQuery(query, String.class).getResultList();

            for (String s : resultList) {
                System.out.println("요금 = " + s);
            }

            em.flush();
            em.clear();



            // COALESCE
            String query2 = "select coalesce(m.username, '이름없는 회원') " +
                            "from Member m";

            List<String> resultList2 = em.createQuery(query2, String.class).getResultList();

            for (String s : resultList2) {
                System.out.println("회원이름 = " + s);
            }

            em.flush();
            em.clear();



            // NULLIF
            String query3 = "select nullif(m.type, '관리자') " +
                            "from Member m";

            List<String> resultList3 = em.createQuery(query3, String.class).getResultList();

            for (String s : resultList3) {
                System.out.println("관리자 = " + s);
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

