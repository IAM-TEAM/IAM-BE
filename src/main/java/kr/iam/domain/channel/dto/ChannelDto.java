package kr.iam.domain.channel.dto;

import kr.iam.domain.channel.domain.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ChannelDto {

    @Getter
    @NoArgsConstructor
    public static class ChannelSaveRequestDto {
        //멤버
        private String username;

        //채널
        private String channelTitle;
        private String channelDescription;
        private List<String> channelCategories;
        private List<String> channelDetailCategories;
        private Boolean ageLimit;

        @Builder
        public ChannelSaveRequestDto(String username, String channelTitle, String channelDescription,
                                     List<String> channelCategories, List<String> channelDetailCategories, Boolean ageLimit) {
            this.username = username;
            this.channelTitle = channelTitle;
            this.channelDescription = channelDescription;
            this.channelCategories = channelCategories;
            this.channelDetailCategories = channelDetailCategories;
            this.ageLimit = ageLimit;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChannelResponseDto {
        //멤버
        private String username;

        //채널
        private String channelImage;
        private String channelTitle;
        private String channelDescription;
        private List<String> channelCategories;
        private List<String> channelDetailCategories;
        private Boolean ageLimit;
        private String rssFeedUrl;

        @Builder
        public ChannelResponseDto(String username, String channelImage,
                                  String channelTitle, String channelDescription,
                                  List<String> channelCategories, List<String> channelDetailCategories,
                                  Boolean ageLimit, String rssFeedUrl) {
            this.username = username;
            this.channelImage = channelImage;
            this.channelTitle = channelTitle;
            this.channelDescription = channelDescription;
            this.channelCategories = channelCategories;
            this.channelDetailCategories = channelDetailCategories;
            this.ageLimit = ageLimit;
            this.rssFeedUrl = rssFeedUrl;
        }

        public static ChannelResponseDto from(Channel channel, List<String> detailCategories) {
            List<String> categoryNames = new ArrayList<>();
            channel.getUsingCategories().forEach(usingCategory -> {
                categoryNames.add(usingCategory.getCategory().getName());
            });
            return ChannelResponseDto
                    .builder()
                    .username(channel.getMember().getName())
                    .channelImage(channel.getImage())
                    .channelTitle(channel.getTitle())
                    .channelDescription(channel.getDescription())
                    .channelCategories(categoryNames)
                    .channelDetailCategories(detailCategories)
                    .ageLimit(channel.getAge())
                    .rssFeedUrl(channel.getMember().getRssFeed())
                    .build();
        }
    }
}
