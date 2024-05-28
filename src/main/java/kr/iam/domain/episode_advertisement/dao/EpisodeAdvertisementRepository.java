package kr.iam.domain.episode_advertisement.dao;

import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeAdvertisementRepository extends JpaRepository<EpisodeAdvertisement, Long> {
}
