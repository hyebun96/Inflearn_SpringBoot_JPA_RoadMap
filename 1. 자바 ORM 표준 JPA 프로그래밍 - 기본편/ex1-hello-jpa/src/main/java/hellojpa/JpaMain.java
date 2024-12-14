package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();   // 일관적인 단위를 할때마다 EntityManager을 생성해주어야함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 저장
           Team team = new Team();
           team.setName("TeamA");
           em.persist(team);        // pk값이 영속상태에 들어감

           Member member = new Member();
           member.setUsername("member1");
            // member.setTeam(team);        // 단방향 연관관계 설정, 참조 저장
           em.persist(member);

           // DB에서 갖고오는거 보고싶으면
           em.flush();
           em.clear();

            Member findMember = em.find(Member.class, member.getId());
            // Team findTeam = findMember.getTeam();

            // System.out.println("findTeam = " + findTeam.getName());

            // 수정
            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            // findMember.setTeam(teamB);

            //System.out.println("findTeam = " + findMember.getTeam().getName());

            // team 양방향으로 찾기
            Team findTeam2 = em.find(Team.class, team.getId());
            List<Member> members = findTeam2.getMembers();

            for(Member member1 : members) {
                System.out.println(member1  + "=>" + member1.getUsername());
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
