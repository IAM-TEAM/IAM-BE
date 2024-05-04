package kr.iam.domain.member.application;

import kr.iam.domain.member.dao.MemberRepository;
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

    public Boolean existsMemberById(Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
