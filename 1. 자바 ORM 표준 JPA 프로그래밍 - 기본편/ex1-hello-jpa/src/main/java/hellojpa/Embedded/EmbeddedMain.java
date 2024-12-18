package hellojpa.Embedded;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class EmbeddedMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            // 1. 임베디드 타입
            Address address = new Address("city", "street", "zip");
            Period period = new Period(LocalDateTime.now(), LocalDateTime.now());

            Member member = new Member();
//            member.setUsername("member1");
//            member.setAddress(address);
//            member.setPeriod(period);

            Address copyAddress = new Address("new city", address.getStreet(), address.getZipcode());

            Member member1 = new Member();
//            member1.setUsername("member2");
//            member1.setAddress(copyAddress);
//            member1.setPeriod(period);

            // 값타입 공유 참조
            // 임베디드 타입 같은 값 타입을 공유하고 있기때문에 member만 바꿔도 member2까지 영향을 줌
            // -> 부작용 (side effect) 발생
//            member.getAddress().setCity("new city");

            em.persist(member);
            em.persist(member1);


            em.clear();
            em.flush();




            // 2. 값 타입과 불변 객체 - Setter 없애고 생성자할때만 값을 셋팅한다.
            Member member2 = new Member();

            Address address1 = new Address("new city", address.getStreet(), address.getZipcode());
//            member3.setAddress(address1);

            em.persist(member2);

            em.clear();
            em.flush();



            // 3. 값타입 컬렉션
            System.out.println("============ 값타입 컬렉션 ============");
            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setHomeAddress(new Address("homeCity", "street", "10000"));

            member3.getFavoriteFoods().add("치킨");
            member3.getFavoriteFoods().add("피자");
            member3.getFavoriteFoods().add("초밥");

            member3.getAddressHistory().add(new Address("old1", "street", "10000"));
            member3.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member3);

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
