package kr.iam.domain.advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.advertisement.dto.req.AdvertisementSaveReqDto;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.domain.EpisodeAdvertisement;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
    private LocalDate startDate;
    private LocalDate endDate;
    private String requirement;
    private Double price;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EpisodeAdvertisement> episodeAdvertisementList = new ArrayList<>();

    public static Advertisement toAdvertisement(AdvertisementSaveReqDto advertisement, String url) {
        return Advertisement.builder()
                .title(advertisement.title())
                .url(url)
                .startDate(advertisement.startDateTime())
                .endDate(advertisement.endDateTime())
                .requirement(advertisement.requirement())
                .price(advertisement.price())
                .content(advertisement.content())
                .build();
    }
}
