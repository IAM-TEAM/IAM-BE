package kr.iam.domain.advertisement.dto;

import lombok.Builder;
import lombok.Getter;

public class AdvertisementDto {

    @Getter
    @Builder
    public static class AdvertiseInfo {
        private String startTime;
        private Long advertiseId;
    }
}
