package kr.iam.domain.episode_advertisement.api;

import kr.iam.domain.episode_advertisement.application.EpisodeAdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EpisodeAdvertisementController {

    private EpisodeAdvertisementService episodeAdvertisementService;
}
