package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // TypeQuery : 반환타입이 명확할때 사용
            TypedQuery<Member> query1 = em.createQuery("select m from Member m ", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m ", String.class);

            // Query : 반환타입이 명확하지 않을 때 사용
            Query query3 = em.createQuery("select m.username, m.age from Member m ");

            List<Member> resultList = query1.getResultList();
            for (Member findMember : resultList) {
                System.out.println("member = " + findMember);
            }

            // 표준 JPQL에서는 getSingleResult() 결과값이 하나이상이거나 없으면 Exception 이라 try~ catch 해줘야함
            String singleResult = query2.getSingleResult();

            // 파라미터 바인딩 - 이름기준 권장(위치기준 X)
            Member singleResult2 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                                        .setParameter("username", "member1")
                                        .getSingleResult();
            System.out.println("singleResult = " + singleResult2.getUsername());


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
