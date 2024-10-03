package kr.iam.domain.member.dto;

import kr.iam.domain.member.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MemberDTO(
        Enum<Role> role,
        String memberName,
        Long memberId,
        Long channelId
) {
    public static MemberDTO of(String memberName, Long memberId, Long channelId, Role memberRole) {
        return MemberDTO
                .builder()
                .memberName(memberName)
                .memberId(memberId)
                .channelId(channelId)
                .role(memberRole)
                .build();
    }
}
