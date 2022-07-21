package me.study.smallshop.service;

import me.study.smallshop.entity.Member;
import me.study.smallshop.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트에서는 항상 콜백을 수행한다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void ifJoinWell() {
        // Given
        Member member = new Member();
        member.setName("kim");
        // When
        Long saveId = memberService.join(member);
        // Then
        assertThat(member).isEqualTo(memberRepository.findOne(saveId));
    }

    @Test
    @DisplayName("중복 예외 발생이 잘 동작시키는지")
    void throwDuplicatedExceptionWell() {
        // Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        // When
        memberService.join(member1);
        // Then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    memberService.join(member2);
                }).withMessageMatching("이미 존재하는 회원입니다.");
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}