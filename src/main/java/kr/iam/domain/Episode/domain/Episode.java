package kr.iam.domain.Episode.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.Channel.domain.Channel;

import java.time.LocalDateTime;

@Entity
public class Episode extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "episode_id")
    private long id;
    private String title;
    private String description;
    private String image;
    private String url;
    private int limit; // 연령제한
    private LocalDateTime reservation;

    //연관관계 주인
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;
}
