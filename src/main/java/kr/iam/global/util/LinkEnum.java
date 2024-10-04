package kr.iam.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LinkEnum {
    LINK("hzpodcaster.com"),
    EPISODIC("NewEpisodeManagement");

    private final String link;
}
