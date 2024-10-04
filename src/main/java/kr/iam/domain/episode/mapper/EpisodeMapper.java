package kr.iam.domain.episode.mapper;

import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.dto.info.EpisodeInfo;
import kr.iam.domain.episode.dto.res.EpisodeInfoResDto;
import kr.iam.domain.episode.dto.res.EpisodePageResDto;
import kr.iam.global.domain.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class EpisodeMapper {
    public EpisodeInfoResDto toEpisodeInfoResDto(Episode episode) {
        return EpisodeInfoResDto.of(episode);
    }

    public EpisodePageResDto toEpisodePageResDto(Page<EpisodeInfo> episodeInfos) {
        PageInfo pageInfo = PageInfo.of(episodeInfos);
        return EpisodePageResDto.of(episodeInfos.getContent(), pageInfo);
    }
}
