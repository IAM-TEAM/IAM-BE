package kr.iam.domain.member.helper;

import kr.iam.domain.member.dao.MemberRepository;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.member.error.MemberError;
import kr.iam.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberHelper {
    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MemberError.MEMBER_NOT_FOUND));
    }

    public boolean existsMemberByName(String name) {
        return memberRepository.existsByName(name);
    }

    @Transactional
    public Member updateMember(Long memberId, String username) {
        Member member = findById(memberId);
        member.setName(username);
        return member;
    }
}
