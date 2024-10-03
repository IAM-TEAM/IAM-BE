package kr.iam.domain.advertisement.dto.info;

import java.time.LocalDate;

public record AdvertisementInfo(
        Long advertisementId,
        String title,
        String url,
        LocalDate startDate,
        LocalDate endDate,
        String requirement,
        Double price,
        String content
) {
}