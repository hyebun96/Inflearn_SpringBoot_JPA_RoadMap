package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() throws Exception{
       // Given
        Member member = new Member();
        member.setName("kim");

        // When
        Long savedId = memberService.join(member);

        // Then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // When
        memberService.join(member1);

        // Then
        // 예외가 발생해야 한다.
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        }, "예외가 발생해야 한다.");
    }

}