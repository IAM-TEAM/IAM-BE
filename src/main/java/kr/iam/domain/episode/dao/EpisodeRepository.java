package kr.iam.domain.episode.dao;

import kr.iam.domain.episode.domain.Episode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    @Query("select e from Episode e left join fetch e.episodeAdvertisementList ea where e.id = :episodeId")
    Optional<Episode> findByIdAndEpisodeAdvertisement(Long episodeId);
}
