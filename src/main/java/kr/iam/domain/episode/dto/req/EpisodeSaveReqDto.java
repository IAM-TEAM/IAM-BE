package kr.iam.domain.episode.dto.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kr.iam.global.config.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;

public record EpisodeSaveReqDto(
        String image,
        String title,
        String description,
        String content,
        String advertiseStart,
        String advertiseId,
        Boolean age,
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        LocalDateTime reservationTime,
        Integer upload
) {
}