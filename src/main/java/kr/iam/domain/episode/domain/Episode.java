package kr.iam.domain.episode.domain;

import jakarta.persistence.*;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.episode.dto.req.EpisodeSaveReqDto;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class Episode extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "episode_id")
    private long id;

    private String title;
    private String description;
    private String image;
    private String content;
    private boolean limitAge; // 연령제한
    private LocalDateTime reservation;
    private Integer upload;

    //연관관계 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EpisodeAdvertisement> episodeAdvertisementList = new ArrayList<>();

    @Builder
    public Episode(long id, String title, String description, String image, String content,
                   boolean limitAge, LocalDateTime reservation, Channel channel, Integer upload) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.content = content;
        this.limitAge = limitAge;
        this.reservation = reservation;
        this.upload = upload;
        this.channel = channel;
    }

    public static Episode of(EpisodeSaveReqDto requestDto, Channel channel, String imageUrl,
                             String content, LocalDateTime uploadTime) {
        return Episode
                .builder()
                .image(imageUrl)
                .title(requestDto.title())
                .description(requestDto.description())
                .content(content)
                .limitAge(requestDto.age())
                .reservation(uploadTime)
                .upload(requestDto.upload())
                .channel(channel)
                .build();
    }
}
