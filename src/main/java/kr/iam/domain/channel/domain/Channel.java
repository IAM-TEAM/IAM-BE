package kr.iam.domain.channel.domain;

import jakarta.persistence.*;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.channel_advertisement.domain.ChannelAdvertisement;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.notification.domain.Notification;
import kr.iam.domain.member.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
