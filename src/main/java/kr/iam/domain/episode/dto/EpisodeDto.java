package kr.iam.domain.episode.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import kr.iam.global.config.CustomLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kr.iam.domain.episode_advertisement.dto.EpisodeAdvertisementDto.*;

public class EpisodeDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EpisodeSaveRequestDto {
        private String image;
        private String title;
        private String description;
        private String content;
        private String advertiseStart;
        private String advertiseId;
        private Boolean age;
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime reservationTime;
        private Integer upload;
    }

    @Getter
    @Builder
    public static class EpisodeListInfoDto {
        private Long id;
        private String image;
        private String title;
        private String description;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdDate;

        public static EpisodeListInfoDto of(Episode episode) {
            return EpisodeListInfoDto
                    .builder()
                    .id(episode.getId())
                    .image(episode.getImage())
                    .title(episode.getTitle())
                    .description(episode.getDescription())
                    .createdDate(episode.getModifiedDate())
                    .build();
        }
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
        private Integer upload;

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
