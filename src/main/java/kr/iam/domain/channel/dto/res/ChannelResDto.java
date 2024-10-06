package kr.iam.domain.channel.dto.res;

import kr.iam.domain.channel.domain.Channel;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
public record ChannelResDto(
        String username,
        String channelImage,
        String channelTitle,
        String channelDescription,
        Map<String, List<String>> channelCategories,
        Boolean ageLimit,
        String rssFeedUrl
) {
    public static ChannelResDto of(Channel channel, Map<String, List<String>> categoryInfo) {
        return ChannelResDto
                .builder()
                .username(channel.getMember().getName())
                .channelImage(channel.getImage())
                .channelTitle(channel.getTitle())
                .channelDescription(channel.getDescription())
                .channelCategories(categoryInfo)
                .ageLimit(channel.getAge())
                .rssFeedUrl(channel.getMember().getRssFeed())
                .build();
    }
}