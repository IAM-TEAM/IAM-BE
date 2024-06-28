package kr.iam.domain.member.application;

import kr.iam.domain.member.dao.MemberRepository;
import kr.iam.domain.member.domain.Member;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member updateMember(Long memberId, String username) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        member.setName(username);
        return member;
    }

    public String getRssFeedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return member.getRssFeed();
    }

    public Boolean existsMemberById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public boolean existsMemberByName(String name) {
        return memberRepository.existsByName(name);
    }
}
