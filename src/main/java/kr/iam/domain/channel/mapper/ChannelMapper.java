package kr.iam.domain.channel.mapper;

import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.dto.res.ChannelResDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChannelMapper {
    public ChannelResDto toChannelResDto(Channel channel, List<String> detailCategories) {
        return ChannelResDto.of(channel, detailCategories);
    }
}
