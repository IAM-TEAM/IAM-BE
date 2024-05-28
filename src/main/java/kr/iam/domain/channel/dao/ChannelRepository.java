package kr.iam.domain.channel.dao;

import kr.iam.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    boolean existsById(Long channelId);
}
