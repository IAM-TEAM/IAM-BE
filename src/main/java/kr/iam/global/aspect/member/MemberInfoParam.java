package kr.iam.global.aspect.member;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MemberInfoParam(
        Long memberId,
        Long channelId
) {
    public static MemberInfoParam of(Long memberId, Long channelId) {
        return MemberInfoParam
                .builder()
                .memberId(memberId)
                .channelId(channelId)
                .build();
    }
}
