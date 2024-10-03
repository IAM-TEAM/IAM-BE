package kr.iam.domain.channel.dto.res;

import kr.iam.domain.channel.domain.Channel;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record ChannelResDto(
        String username,
        String channelImage,
        String channelTitle,
        String channelDescription,
        List<String> channelCategories,
        List<String> channelDetailCategories,
        Boolean ageLimit,
        String rssFeedUrl
) {
    public static ChannelResDto of(Channel channel, List<String> detailCategories) {
        List<String> categoryNames = new ArrayList<>();
        channel.getUsingCategories().forEach(usingCategory -> {
            categoryNames.add(usingCategory.getCategory().getName());
        });
        return ChannelResDto
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