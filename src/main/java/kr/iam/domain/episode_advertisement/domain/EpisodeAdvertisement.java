package kr.iam.domain.episode_advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.episode.domain.Episode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class EpisodeAdvertisement extends BaseTimeEntity {

    @Column(name = "episode_advertisement_id")
    @Id @GeneratedValue
    private Long id;

    private String advertise_start;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    @Builder
    public EpisodeAdvertisement(Long id, String advertise_start, Advertisement advertisement, Episode episode) {
        this.id = id;
        this.advertise_start = advertise_start;
        this.advertisement = advertisement;
        this.episode = episode;
    }

    public static EpisodeAdvertisement of(Episode episode, Advertisement advertisement, String advertiseStart) {
        return EpisodeAdvertisement
                .builder()
                .advertise_start(advertiseStart)
                .episode(episode)
                .advertisement(advertisement)
                .build();
    }
}
