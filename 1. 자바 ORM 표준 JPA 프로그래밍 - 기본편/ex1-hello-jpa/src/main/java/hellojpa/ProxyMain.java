package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProxyMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

//            Member member = em.find(Member.class, 1L);
//            printMember(member);    // 멤버만 출력하는 비즈니스 로직
//            printMemberAndTeam(member); // 멤버와 팀을 같이 출력하는 비즈니스 로직

            Member member = new Member();
            member.setUsername("hello");
            em.persist(member);

            em.flush();
            em.clear();



            // 1. 프록시

            // getReference : 사용되는 시점에 갖고옴
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember = " + findMember.getClass());        //  class hellojpa.Member$HibernateProxy$AMqJ20Ba
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember instance = " + (findMember instanceof Member));

            em.flush();
            em.clear();



            // 2. 영속성 컨텍스트 안에 이미 엔티티가 존재하는 경우, 실제 객체 반환

            Member m1 = em.find(Member.class, member.getId());
            System.out.println("m1 = " + m1.getClass());

            Member reference = em.getReference(Member.class, member.getId());
            System.out.println("m1 = " + m1.getClass());
            System.out.println("m1 instance = " + (m1 instanceof Member));

            // 영속성 컨텍스트 안에 실제 엔티티가 있으면 em.getReference() 해도 실제 객체 반환하기 때문에 true
            System.out.println("m1 === reference : " + (m1 == reference));

            em.flush();
            em.clear();



            // 3. 프록시 객체 호출하고 실제 객체 호출하는 경우, 실제객체 찾아도 프록시로 옴

            Member reference2 = em.getReference(Member.class, member.getId());
            System.out.println("reference2 = " + reference2.getClass());    // Proxy

            Member m2 = em.find(Member.class, member.getId());      // Proxy
            System.out.println("m2 = " + m2.getClass());

            System.out.println("m2 == reference2 : " + (m2 == reference2));

            em.flush();
            em.clear();



            // 4.

            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("refMember = " + refMember.getClass());    // Proxy

            em.detach(refMember);       //   더이상 영속성컨텍스트가 관리 안함

            System.out.println("refMember = " + refMember.getUsername());   // could not initialize proxy


            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
