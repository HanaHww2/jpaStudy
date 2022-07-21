package me.study.smallshop.service;

import lombok.AllArgsConstructor;
import me.study.smallshop.entity.Member;
import me.study.smallshop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 디폴트로 언체크 예외만 롤백한다. 체크 예외 발생에도 롤백을 하려면 옵션을 변경하면 된다.
@AllArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;

    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicatedMember(Member member) {
        List<Member> findMembers =
                memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
