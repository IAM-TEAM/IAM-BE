package kr.iam.domain.channel.helper;

import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.channel.error.ChannelError;
import kr.iam.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelHelper {
    private final ChannelRepository channelRepository;

    public Channel findById(Long id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ChannelError.CHANNEL_NOT_FOUND));
    }

    public Channel save(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel findAllInfoByChannelId(Long id) {
        return channelRepository.findAllInfoByChannelId(id)
                .orElseThrow(() -> new EntityNotFoundException(ChannelError.CHANNEL_NOT_FOUND));
    }
}
