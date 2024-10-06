package kr.iam.domain.channel.mapper;

import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.dto.res.ChannelResDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ChannelMapper {
    public ChannelResDto toChannelResDto(Channel channel, Map<String, List<String>> categoryInfo) {
        return ChannelResDto.of(channel, categoryInfo);
    }
}
