package kr.iam.domain.episode.helper;

import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dao.EpisodeRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.error.EpisodeError;
import kr.iam.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EpisodeHelper {
    private final EpisodeRepository episodeRepository;

    public void save(Episode episode) {
        episodeRepository.save(episode);
    }

    public void delete(Episode episode) {
        episodeRepository.delete(episode);
    }

    public Episode findById(Long id) {
        return episodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EpisodeError.EPISODE_NOT_FOUND));
    }

    public Episode findByIdAndEpisodeAdvertisement(Long id) {
        return episodeRepository.findByIdAndEpisodeAdvertisement(id)
                .orElseThrow(() -> new EntityNotFoundException(EpisodeError.EPISODE_NOT_FOUND));
    }

    public Page<Episode> findByUploadAndChannel(int upload, Channel channel, Pageable pageable) {
        return episodeRepository.findByUploadAndChannel(upload, channel, pageable);
    }
}
