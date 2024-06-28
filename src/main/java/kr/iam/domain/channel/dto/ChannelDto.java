package kr.iam.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChannelDto {

    @Getter
    @NoArgsConstructor
    public static class ChannelSaveRequestDto {
        //멤버
        private String username;

        //채널
        private String channelTitle;
        private String channelDescription;
        private String channelCategory;
        private String channelDetailCategory;
        private Boolean ageLimit;

        @Builder
        public ChannelSaveRequestDto(String username, String channelTitle, String channelDescription, String channelCategory, String channelDetailCategory, Boolean ageLimit) {
            this.username = username;
            this.channelTitle = channelTitle;
            this.channelDescription = channelDescription;
            this.channelCategory = channelCategory;
            this.channelDetailCategory = channelDetailCategory;
            this.ageLimit = ageLimit;
        }
    }

    public static class ChannelResponseDto {
        private String imageUrl;
        private String username;
    }
}
