package kr.iam.domain.channel.application;

import kr.iam.domain.channel.dao.ChannelRepository;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;

    public boolean existsChannelById(Long channelId) {
        return channelRepository.existsById(channelId);
    }

    public Channel findByChannelId(Long channelId) {
        Optional<Channel> byId = channelRepository.findById(channelId);
        if (byId.isPresent()) {
            return byId.get();
        }else{
            throw new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND);
        }
    }
}
