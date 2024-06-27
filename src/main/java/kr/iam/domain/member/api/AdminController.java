package kr.iam.domain.member.api;

import kr.iam.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/check")
    public boolean checkDuplicate(@RequestParam String username) {
        return memberService.existsMemberByName(username);
    }
}
