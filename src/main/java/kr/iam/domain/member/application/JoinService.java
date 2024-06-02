//package kr.iam.domain.member.application;
//
//import kr.iam.domain.member.dao.MemberRepository;
//import kr.iam.domain.member.domain.Member;
//import kr.iam.domain.member.domain.Role;
//import kr.iam.domain.member.dto.JoinDTO;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class JoinService {
//    private final MemberRepository memberRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.memberRepository = memberRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//
//    public void joinProcess(JoinDTO joinDTO){
//        String username = joinDTO.getUsername();
//        String password = joinDTO.getPassword();
//
//        Boolean isExist = memberRepository.existsByUsername(username);
//
//        if(isExist){
//            return;
//        }
//
//        Member data = Member.builder()
//                .username(username)
//                .password(bCryptPasswordEncoder.encode(password))
//                .role(Role.ADMIN).build();
//
//        memberRepository.save(data);
//
//    }
//}
