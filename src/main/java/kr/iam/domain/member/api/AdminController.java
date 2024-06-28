package kr.iam.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.iam.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class AdminController {

    private final MemberService memberService;

    @Operation(summary = "유저 닉네임 중복 체크", description = "닉네임 중복 체크")
    @GetMapping("/check")
    public boolean checkDuplicate(@RequestParam String username) {
        return memberService.existsMemberByName(username);
    }
}
