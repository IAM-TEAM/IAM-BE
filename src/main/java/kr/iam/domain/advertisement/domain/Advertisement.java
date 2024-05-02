package kr.iam.domain.advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode_advertisement.domain.EpisodeAdvertisement;
import lombok.*;

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
    private String url;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "advertisement")
    private List<EpisodeAdvertisement> episodeAdvertisementList = new ArrayList<>();

}
