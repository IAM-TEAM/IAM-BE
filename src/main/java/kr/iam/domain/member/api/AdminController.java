package kr.iam.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.iam.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class AdminController {

    private final MemberService memberService;

    @Operation(summary = "유저 닉네임 중복 체크", description = "닉네임 중복 체크")
    @GetMapping("/check")
    public boolean checkDuplicate(@RequestParam String username) {
        return memberService.checkDuplicatedName(username);
    }
}
