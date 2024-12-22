package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.List;

public class JpaMainFetchJoin {

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


            // Member 만 호출했을 경우 : Lazy라 지연로딩이 일어나고 필요에 따라 DB 조회
            String query = "select m from Member m";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // 회원1, 팀A(SQL)
                // 회원2, 팀A(1차캐시)
                // 회원3, 팀B(SQL)

                // 최악의 경우는 팀이 다 다른 소속이면 무수하게 많은 쿼리를 조회함,
                // 회원 100명 -> N + 1
            }

            em.flush();
            em.clear();




            // Fetch join : 성능최적화. Team 까지 한번에 끌어와
            System.out.println("================= fetch join =================");
            String query2 = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

            for (Member member : result2) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            em.flush();
            em.clear();





            // 컬렉션 fetch join
            // distinct 추가하지 않아도 hibernate 6에서 중복제거됨
            System.out.println("================= 컬렉션 fetch join =================");
            String query3 = "select t from Team t join fetch t.members";
            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();

            for (Team team : result3) {
                System.out.println("team = " + team.getName() + ", " + team.getMembers());
            }

            em.flush();
            em.clear();




            // 패치조인의 한계
            // fetch join 은 별칭을 사용할 수 없음. -> 가급적 사용하지 X
            // fetch join 은 페이징을 할 수 없음.

            // fetch join을 하지 않아도, 아래와 같은 방법을 사용하면 LAZY가아닌 즉시로딩 할 수 있음
            // 1.Team.java에서 @BatchSize(size = 100)
            // 2. persistence.xml에 설정해주면 페이징하고 member 가 올 수 있음
            System.out.println("================= fetch join 한계 =================");
            String query4 = "select t from Team t";
            List<Team> result4 = em.createQuery(query4, Team.class)
                                    .setFirstResult(0)
                                    .setMaxResults(2)
                                    .getResultList();

            System.out.println("size = " + result4.size());

            for (Team team : result4) {
                System.out.println("team = " + team.getName() + ", " + team.getMembers());
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
