package kr.iam.global.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum JwtEnum {
    ACCESS_TOKEN_NAME("Authorization"),
    MEMBER_NAME("memberName"),
    MEMBER_ID("memberId"),
    CHANNEL_ID("channelId"),
    MEMBER_ROLE("memberRole");

    private String desc;
}
