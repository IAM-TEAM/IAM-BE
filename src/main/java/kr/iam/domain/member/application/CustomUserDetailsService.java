//package kr.iam.domain.member.application;
//
//import kr.iam.domain.member.dao.MemberRepository;
//import kr.iam.domain.member.domain.Member;
//import kr.iam.domain.member.dto.CustomUserDetails;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    public CustomUserDetailsService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member memberData = memberRepository.findByUsername(username);
//
//        if(memberData != null){
//            return new CustomUserDetails(memberData);
//        }
//
//
//        return null;
//    }
//}
