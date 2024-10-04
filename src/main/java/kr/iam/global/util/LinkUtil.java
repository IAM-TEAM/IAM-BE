package kr.iam.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum LinkUtil {
    LINK("https://hzpodcaster.com"),
    EPISODIC("NewEpisodeManagement");

    private final String link;
}