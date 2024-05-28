package kr.iam.domain.member.dto;

import kr.iam.domain.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDTO {
    private Enum<Role> role;
    private String name;
    private String username;
}
