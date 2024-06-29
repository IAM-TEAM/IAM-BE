package kr.iam.domain.channel.dto;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.CategoryMemberDto;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.detail_category.domain.DetailCategory;
import kr.iam.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static kr.iam.domain.category.dto.CategoryMemberDto.*;

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

    @Getter
    @NoArgsConstructor
    public static class ChannelResponseDto {
        //멤버
        private String username;

        //채널
        private String channelImage;
        private String channelTitle;
        private String channelDescription;
        private String channelCategory;
        private String channelDetailCategory;
        private Boolean ageLimit;

        @Builder
        public ChannelResponseDto(String username, String channelImage,
                                  String channelTitle, String channelDescription,
                                  String channelCategory, String channelDetailCategory, Boolean ageLimit) {
            this.username = username;
            this.channelImage = channelImage;
            this.channelTitle = channelTitle;
            this.channelDescription = channelDescription;
            this.channelCategory = channelCategory;
            this.channelDetailCategory = channelDetailCategory;
            this.ageLimit = ageLimit;
        }

        public static ChannelResponseDto from(Channel channel, String detailedCategory) {
            return ChannelResponseDto
                    .builder()
                    .username(channel.getMember().getName())
                    .channelImage(channel.getImage())
                    .channelTitle(channel.getTitle())
                    .channelDescription(channel.getDescription())
                    .channelCategory(channel.getCategory().getName())
                    .channelDetailCategory(detailedCategory)
                    .ageLimit(channel.getAge())
                    .build();
        }
    }
}
