package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;


    @Test
    void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());

        // Given
        Member member = new Member("winter");
        Member savedMember = memberRepository.save(member);

        // When
        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        // Then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("memeber1");
        Member member2 = new Member("memeber2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member!!!");

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findHelloBy();
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");

        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findByUsernameList();
        assertThat(usernameList.size()).isEqualTo(2);
        assertThat(usernameList.get(0)).isEqualTo("AAA");
        assertThat(usernameList.get(1)).isEqualTo("BBB");
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();
        assertThat(usernameList.size()).isEqualTo(1);
        assertThat(usernameList.get(0).getId()).isEqualTo(m1.getId());
        assertThat(usernameList.get(0).getUsername()).isEqualTo("AAA");
        assertThat(usernameList.get(0).getTeamName()).isEqualTo("teamA");
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        assertThat(usernameList.size()).isEqualTo(2);
        assertThat(usernameList.get(0)).isEqualTo(m1);
        assertThat(usernameList.get(1)).isEqualTo(m2);
        assertThat(usernameList.get(0).getUsername()).isEqualTo("AAA");
        assertThat(usernameList.get(1).getUsername()).isEqualTo("BBB");
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.findListByUsername("AAA");     // 값이 없는 경우 empty 반환
        Member findMember = memberRepository.findMemberByUsername("AAA");            // 값이 없는 경우 null 반환
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");   // 값이 없는 경우 empty 반환

        assertThat(usernameList.size()).isEqualTo(1);
        assertThat(usernameList.get(0)).isEqualTo(m1);
        assertThat(findMember).isEqualTo(m1);
        assertThat(optionalMember.get()).isEqualTo(m1);
    }

    @Test
    public void paging() {
        // Given
        Team teamA = teamRepository.save(new Team("teamA"));
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamA));
        memberRepository.save(new Member("member3", 10, teamA));
        memberRepository.save(new Member("member4", 10, teamA));
        memberRepository.save(new Member("member5", 10, teamA));

        // 인터페이스 만으로 페이징 가능
        // SpringJPA 는 페이지 0번부터 시작함
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // When
        System.out.println("---> paging <---");
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // slice는 totalCount안나오고 +1 하나해서 있는지만 봄
        System.out.println("---> slice: totalPage 안나옴 <---");
        Slice<Member> slicePage = memberRepository.findSliceByAge(age, pageRequest);

        // count는 join할 필요가 없으니 처리
        System.out.println("---> count: totalPage join 안하고 <---");
        Page<Member> count = memberRepository.findCountByAge(age, pageRequest);

        System.out.println("--->  Page<Member> -> Page<MemberDto> <---");
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));
        System.out.println(toMap.getTotalPages());

        // Then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);            // find Elements 개수
        assertThat(content.get(0).getAge()).isEqualTo(10);  //Elements(0) 번 조회
        assertThat(page.getNumber()).isEqualTo(0);          // 현재 페이지 번호
        assertThat(page.getTotalElements()).isEqualTo(5L);  // 전체 Elements 개수
        assertThat(page.getTotalPages()).isEqualTo(2);      // 전체 페이지 수
        assertThat(page.isFirst()).isTrue();                          // 현재 슬라이스가 첫페이지 인지
        assertThat(page.hasNext()).isTrue();                          // 다음 페이지 있는지

        assertThat(slicePage.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        // Given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 33));

        // When
        int resultCount = memberRepository.bulkAgePlus(20);
//        em.flush(); // 영속성 컨텍스트를 날려버림. DB에서 값을 갖고옴
//        em.clear();

        Member member = memberRepository.findByUsername("member5").get(0);
        System.out.println(member); // em.flush(), em.clear() 없다면 영속성 컨텍스트에 남아있는 값으로 출력하기 때문에 반영이 안되기 전 값으로 나옴

        // Then
        assertThat(resultCount).isEqualTo(3);
    }
}