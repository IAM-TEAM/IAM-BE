package kr.iam.domain.episode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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

        public void of(String image, String content) {
            this.image = image;
            this.content = content;
        }
    }
}
