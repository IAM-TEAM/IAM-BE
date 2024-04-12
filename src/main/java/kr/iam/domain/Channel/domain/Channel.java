package kr.iam.domain.Channel.domain;

import jakarta.persistence.*;
import kr.iam.domain.Advertisement.domain.Advertisement;
import kr.iam.domain.Advertisement.domain.ChannelAdvertisement;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.Episode.domain.Episode;
import kr.iam.domain.Notification.domain.Notification;
import kr.iam.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channel extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "channel_id")
    private Long id;
    private String title;
    private String description;
    private String image;

    //연관관계 주인
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "channel", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "channel")
    private List<Episode> episodes = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<ChannelAdvertisement> channelAdvertisements = new ArrayList<>();

}
