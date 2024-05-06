package kr.iam.domain.episode.dto;

import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import kr.iam.domain.episode_advertisement.dto.EpisodeAdvertisementDto;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.iam.domain.episode_advertisement.dto.EpisodeAdvertisementDto.*;

public class EpisodeDto {

    @Getter
    @Builder
    public static class EpisodeSaveRequestDto {
        private String image;
        private String title;
        private String description;
        private String content;
        private String advertiseStart;
        private String advertiseId;
        private Boolean age;
        private LocalDateTime reservationTime;
        private Boolean upload;
    }

    @Getter
    @Builder
    public static class EpisodeInfoResponseDto {
        private Long id;
        private String image;
        private String title;
        private String description;
        private String content;
        @Builder.Default
        private List<AdvertiseInfo> advertiseInfoList = new ArrayList<>();
        private Boolean upload;

        public static EpisodeInfoResponseDto of(Episode episode) {
            List<EpisodeAdvertisement> episodeAdvertisementList = episode.getEpisodeAdvertisementList();
            List<AdvertiseInfo> advertiseInfos = episodeAdvertisementList.stream()
                    .map(episodeAdvertisement -> AdvertiseInfo.of(episodeAdvertisement.getAdvertise_start(), episodeAdvertisement.getAdvertisement().getId()))
                    .toList();

            return EpisodeInfoResponseDto
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
}
