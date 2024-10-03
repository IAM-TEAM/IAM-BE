package kr.iam.domain.episode.dto.res;

import kr.iam.domain.episode.dto.info.EpisodeInfo;
import kr.iam.global.domain.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record EpisodePageResDto(
        List<EpisodeInfo> episodeInfos,
        PageInfo pageInfo
) {
    public static EpisodePageResDto of(List<EpisodeInfo> episodeInfos, PageInfo pageInfo) {
        return EpisodePageResDto
                .builder()
                .episodeInfos(episodeInfos)
                .pageInfo(pageInfo)
                .build();
    }
}
