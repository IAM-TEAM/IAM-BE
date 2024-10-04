package kr.iam.domain.episode.dto.info;

import kr.iam.domain.episode.domain.Episode;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record EpisodeInfo(
        Long id,
        String image,
        String title,
        String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdDate
) {
    public static EpisodeInfo of(Episode episode) {
        return EpisodeInfo
                .builder()
                .id(episode.getId())
                .image(episode.getImage())
                .title(episode.getTitle())
                .description(episode.getDescription())
                .createdDate(episode.getLastModifiedAt())
                .build();
    }
}
