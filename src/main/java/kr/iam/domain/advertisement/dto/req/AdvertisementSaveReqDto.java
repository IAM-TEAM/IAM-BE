package kr.iam.domain.advertisement.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


public record AdvertisementSaveReqDto(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate startDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate endDateTime,
        String requirement,
        Double price,
        String content
        ) {
}