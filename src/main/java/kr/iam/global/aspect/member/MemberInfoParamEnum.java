package kr.iam.global.aspect.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MemberInfoParamEnum {
    MEMBER_ID("memberId"),
    CHANNEL_ID("channelId");

    private final String desc;
}
