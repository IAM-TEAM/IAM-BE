package kr.iam.domain.advertisement.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class AdvertisementDto {

    @Builder
    @Getter
    public static class EnrollAdvertisementDto{
        private String title;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String requirement;
        private Double price;
        private String content;


    }
}
