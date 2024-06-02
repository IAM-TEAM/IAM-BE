package kr.iam.domain.episode_advertisement.dto;

import lombok.Builder;
import lombok.Getter;

public class EpisodeAdvertisementDto {

    @Getter
    @Builder
    public static class AdvertiseInfo {
        private String startTime;
        private Long advertiseId;

        public static AdvertiseInfo of(String startTime, Long advertiseId) {
            return AdvertiseInfo
                    .builder()
                    .startTime(startTime)
                    .advertiseId(advertiseId)
                    .build();
        }
    }
}
