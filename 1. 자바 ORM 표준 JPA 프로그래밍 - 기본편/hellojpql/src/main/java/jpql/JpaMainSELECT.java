package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMainSELECT {

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

            em.flush();
            em.clear();

            // List 값 모두 영속성 컨텍스트에 관리가 된다.
            List<Member> result = em.createQuery("select m from Member m ", Member.class)
                                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            // 조인은 명시적으로 하는 것을 권장
            // why? 명시적으로 해야 유지보수에 용이
            // TypedQuery<Team> result2 = em.createQuery("select m.team from Member m", Team.class);
            TypedQuery<Team> result2 = em.createQuery("select t from Member m join Team t", Team.class);
            System.out.println("result = " + result2);


            // 임베디드 타입 프로젝션
            List<Address> resultList = em.createQuery("select o.address from Order o", Address.class)
                                            .getResultList();
            for (Address address : resultList) {
                System.out.println("address = " + address);
            }

            // 스칼라 타입 프로젝션
            List resultList2 = em.createQuery("select distinct m.username, m.age from Member m").getResultList();

           // 스칼라 타입 조회  - Object[]로 조회
            Object o = resultList2.get(0);
            Object[] result3 = (Object[]) o;
            System.out.println("username = " + result3[0]);
            System.out.println("age = " + result3[1]);

            // 스칼라 타입 조회 - new 로 조회
            List<MemberDTO> resultList3 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m").getResultList();
            for (MemberDTO memberDTO : resultList3) {
                System.out.println("memberDTO" + memberDTO);
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
