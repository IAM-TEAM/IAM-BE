package kr.iam.domain.advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.advertisement.dto.AdvertisementDto;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kr.iam.domain.advertisement.dto.AdvertisementDto.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "advertisement_id")
    private long id;
    private String title;
    private String url;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String requirement;
    private Double price;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EpisodeAdvertisement> episodeAdvertisementList = new ArrayList<>();

    public static Advertisement toAdvertisement(EnrollAdvertisementDto advertisement, String url) {
        return Advertisement.builder()
                .title(advertisement.getTitle())
                .url(url)
                .startDate(advertisement.getStartDateTime())
                .endDate(advertisement.getEndDateTime())
                .requirement(advertisement.getRequirement())
                .price(advertisement.getPrice())
                .content(advertisement.getContent())
                .build();
    }
}
