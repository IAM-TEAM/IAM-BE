package kr.iam.domain.member.application;

import kr.iam.domain.member.dao.MemberRepository;
import kr.iam.domain.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @Test
    void 닉네임_중복_체크() {
        //given
        String name = "lee";
        when(memberRepository.existsByName(name)).thenReturn(true);

        //when
        Boolean result = memberRepository.existsByName(name);

        //then
        assertTrue(result);
        verify(memberRepository, times(1)).existsByName(name);
    }
}