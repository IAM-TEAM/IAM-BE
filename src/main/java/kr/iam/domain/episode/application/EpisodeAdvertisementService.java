package kr.iam.domain.episode.application;

import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.advertisement.helper.AdvertisementHelper;
import kr.iam.domain.episode.dao.EpisodeAdvertisementRepository;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.episode.domain.EpisodeAdvertisement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpisodeAdvertisementService {

    private final EpisodeAdvertisementRepository episodeAdvertisementRepository;
    private final AdvertisementHelper advertisementHelper;

    @Transactional
    public void saveEpisodeAdvertisement(Episode episode, List<String> advertiseIds, List<String> startTimes) {
        IntStream.range(0, advertiseIds.size())
                .mapToObj(i -> {
                    Advertisement advertisement = advertisementHelper.findByAdvertiseId(Long.valueOf(advertiseIds.get(i)));
                    String startTime = startTimes.get(i);
                    return EpisodeAdvertisement.of(episode, advertisement, startTime);
                })
                .forEach(episodeAdvertisementRepository::save);
    }
}
