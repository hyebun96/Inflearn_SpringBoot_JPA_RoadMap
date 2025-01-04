package study.data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        // Given
        Member member = new Member("winter");

        // When
        Member findMember = memberJpaRepository.save(member);

        // Then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void testFindMember() {
        // Given
        Member member = new Member("winter");
        Member savedMember = memberJpaRepository.save(member);

        // When
        Member findMember = memberJpaRepository.find(savedMember.getId());

        // Then
        assertThat(findMember).isEqualTo(savedMember);
    }
}