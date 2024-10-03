package kr.iam.domain.channel.dto.req;

import java.util.List;
import java.util.Map;

public record ChannelSaveReqDto(
        String username,
        String channelTitle,
        String channelDescription,
        Map<String, List<String>> channelCategories,
        Boolean ageLimit
) {

}