package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMainFunction {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("admin1");
            em.persist(member2);

            em.flush();
            em.clear();



            // concat 문자 더하기
            String query = "select concat('A', m.username) from Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();

            for (String s : resultList) {
                System.out.println("name = " + s);
            }


            // substring
            String query2 = "select substring(m.username, 2, 3) from Member m";
            List<String> resultList2 = em.createQuery(query2, String.class).getResultList();

            for (String s : resultList2) {
                System.out.println("substring = " + s);
            }


            // locate : 글자 인덱스번호를 찾아서 반환
            String query3 = "select locate('ber', m.username) from Member m";
            List<Integer> resultList3 = em.createQuery(query3, Integer.class).getResultList();

            for (Integer i : resultList3) {
                System.out.println("locate = " + i);
            }


            // size : 컬렉션의 크기를 보여줌
            String query4 = "select size(t.members) from Team t";
            List<Integer> resultList4 = em.createQuery(query4, Integer.class).getResultList();

            for (Integer i : resultList4) {
                System.out.println("size = " + i);
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
