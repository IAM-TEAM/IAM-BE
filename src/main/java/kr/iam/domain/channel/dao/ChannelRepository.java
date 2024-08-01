package kr.iam.domain.channel.dao;

import kr.iam.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    boolean existsById(Long channelId);

    @Query("select c from Channel c join fetch c.member join fetch c.usingCategories uc join fetch uc.category where c.id = :channelId")
    Optional<Channel> findAllInfoByChannelId(Long channelId);
}
