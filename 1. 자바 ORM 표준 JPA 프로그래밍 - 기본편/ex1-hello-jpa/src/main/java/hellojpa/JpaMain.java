package hellojpa;

import jakarta.persistence.*;

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
           member.setTeam(team);        // 단방향 연관관계 설정, 참조 저장
           em.persist(member);

           // DB에서 갖고오는거 보고싶으면
           em.flush();
           em.clear();

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam = " + findTeam.getName());

            // 수정
            Team team100 = new Team();
            team.setName("Team100");
            em.persist(team100);

            Team newTeam = em.find(Team.class, 2L);
            findMember.setTeam(newTeam);

            System.out.println("findTeam = " + findTeam.getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
