package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaOther {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();


            // 벌크연산
            // 모든 회원의 나이를 20살로 변경한크다면

            // [문제] 벌크연산 주의! -> 영속성컨텍스트를 거치지 않고 바로 DB에 들어간다
            // [해결방법1] 벌크연산을 선수행
            // [해결방법2] 연산후, 영속성컨텍스트 초기화
            System.out.println("================= 벌크연산 =================");
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            Member findMember3 = em.find(Member.class, member1.getId());
            System.out.println("findMember3의 나이" + findMember3.getAge());

            // FLUSH 호출 -> commit, query, flush
            // FLUSH는 영속성 컨텍스트의 값을 DB에 반영하는것.



            // 엔티티 값 직접사용, id값 사용과 SQL은 동일
            System.out.println("================= 엔티티값 직접사용 =================");
            String query1 = "select m from Member m where m = :member";
            String query2 = "select m from Member m where m.id = :id";

            Member findMember1 = em.createQuery(query1, Member.class)
                                .setParameter("member", member1)
                                .getSingleResult();

            Member findMember2 = em.createQuery(query2, Member.class)
                    .setParameter("id", member1.getId())
                    .getSingleResult();

            System.out.println("findMember1 = " + findMember1);
            System.out.println("findMember2 = " + findMember2);

            em.flush();
            em.clear();



            // 엔티티 직접사용 - 외래키 값또한 동일
            String query3 = "select m from Member m where m.team = :team";
            String query4 = "select m from Member m where m.team = :id";

            Member findTeam3 = em.createQuery(query3, Member.class)
                    .setParameter("team", teamB)
                    .getSingleResult();

            Member findTeam4 = em.createQuery(query4, Member.class)
                    .setParameter("id", member3.getTeam())
                    .getSingleResult();

            System.out.println("findTeam1 = " + findTeam3.getTeam());
            System.out.println("findTeam2 = " + findTeam4.getTeam());

            em.flush();
            em.clear();



            // Named 쿼리 - 어노테이션 : 쿼리 재 활용
            Member findMember = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getSingleResult();

            System.out.println("findMember = " + findMember);

            em.flush();
            em.clear();

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
