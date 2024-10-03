package kr.iam.domain.episode.dto.info;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record EpisodeADInfo(
        String startTime,
        Long adId
) {
    public static EpisodeADInfo of(String startTime, Long adId) {
        return EpisodeADInfo
                .builder().startTime(startTime).adId(adId).build();
    }
}
