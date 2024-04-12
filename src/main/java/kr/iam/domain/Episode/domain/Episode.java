package kr.iam.domain.Episode.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.Channel.domain.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Episode extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "episode_id")
    private long id;
    private String title;
    private String description;
    private String image;
    private String url;
    private int limitAge; // 연령제한
    private LocalDateTime reservation;

    //연관관계 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Builder
    public Episode(long id, String title, String description, String image, String url, int limitAge, LocalDateTime reservation, Channel channel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.url = url;
        this.limitAge = limitAge;
        this.reservation = reservation;
        this.channel = channel;
    }
}
