package kr.iam.domain.episode.dao;

import kr.iam.domain.episode.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
