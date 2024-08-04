package kr.iam.domain.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public class AdvertisementDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollAdvertisementDto{
        private String title;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate startDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate endDateTime;
        private String requirement;
        private Double price;
        private String content;
    }

    @Builder
    @Getter
    public static class AdvertisementResDto {
        private Page<AdvertisementInfo> advertisementInfos;

        public static AdvertisementResDto from(Page<AdvertisementInfo> advertisementInfos) {
            return AdvertisementResDto
                    .builder()
                    .advertisementInfos(advertisementInfos)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class AdvertisementInfo {
        private Long advertisementId;
        private String title;
        private String url;
        private LocalDate startDate;
        private LocalDate endDate;
        private String requirement;
        private Double price;
        private String content;

        public AdvertisementInfo(Long advertisementId, String title, String url,
                                 LocalDate startDate, LocalDate endDate,
                                 String requirement, Double price, String content) {
            this.advertisementId = advertisementId;
            this.title = title;
            this.url = url;
            this.startDate = startDate;
            this.endDate = endDate;
            this.requirement = requirement;
            this.price = price;
            this.content = content;
        }
    }
}
