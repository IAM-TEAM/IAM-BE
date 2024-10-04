package kr.iam.domain.episode.dto.res;

import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.domain.EpisodeAdvertisement;
import kr.iam.domain.episode.dto.info.EpisodeADInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record EpisodeInfoResDto(
        Long id,
        String image,
        String title,
        String description,
        String content,
        List<EpisodeADInfo> advertiseInfoList,
        Integer upload
) {
    public static EpisodeInfoResDto of(Episode episode) {
        List<EpisodeAdvertisement> episodeAdvertisementList = episode.getEpisodeAdvertisementList();
        List<EpisodeADInfo> advertiseInfos = episodeAdvertisementList.stream()
                .map(episodeAdvertisement -> EpisodeADInfo.of(episodeAdvertisement.getAdvertise_start(), episodeAdvertisement.getAdvertisement().getId()))
                .toList();

        return EpisodeInfoResDto
                .builder()
                .id(episode.getId())
                .image(episode.getImage())
                .title(episode.getTitle())
                .description(episode.getDescription())
                .content(episode.getContent())
                .advertiseInfoList(advertiseInfos)
                .upload(episode.getUpload())
                .build();
    }
}
