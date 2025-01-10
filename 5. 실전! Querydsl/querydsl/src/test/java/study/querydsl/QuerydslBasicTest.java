package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext EntityManager em;
    JPAQueryFactory jpaQueryFactory;    // 동시성 문제 상관없음

    @BeforeEach
    public void before() {
        jpaQueryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        // member1 을 찾아라.
        String queryString = "select m from Member m where m.username = :username";

        Member findMember = em.createQuery(queryString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // member1 을 찾아라.
        Member findMember = jpaQueryFactory.select(member)
                                          .from(member)
                                          .where(member.username.eq("member1"))  // querydsl 파라미터 바인딩을 자동으로 해줌
                                          .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        // member1 을 찾아라.
        Member findMember = jpaQueryFactory.select(member)
                                        .from(member)
                                        .where(member.username.eq("member1")
                                                .and(member.age.between(10, 30)))
                                        .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void searchAndParam() {
        // member1 을 찾아라.
        Member findMember = jpaQueryFactory.select(member)
                .from(member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void resultFetch() {
        List<Member> fetch = jpaQueryFactory.selectFrom(member)
                                            .fetch();

        Member fetchOne = jpaQueryFactory.selectFrom(member)
                                            .fetchOne();

        Member fetchFirst = jpaQueryFactory.selectFrom(member)
                                            .fetchFirst();
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = jpaQueryFactory.selectFrom(member)
                                            .where(member.age.eq(100))
                                            .orderBy(member.age.desc(), member.username.asc().nullsLast())  // username = null 객체는 마지막에 넣겠다.
                                            .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging() {
        List<Member> result = jpaQueryFactory.selectFrom(member)
                                            .orderBy(member.username.desc())
                                            .offset(1)
                                            .limit(2)
                                            .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void count() {
        Long totalCount = jpaQueryFactory
                //.select(Wildcard.count) //select count(*)
                .select(member.count()) //select count(member.id)
                .from(member)
                .fetchOne();

        System.out.println("totalCount = " + totalCount);
    }
}
