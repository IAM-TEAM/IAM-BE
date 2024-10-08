package kr.iam.domain.channel.domain;

import jakarta.persistence.*;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.category.domain.UsingCategory;
import kr.iam.domain.channel.dto.req.ChannelSaveReqDto;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.member.domain.Member;
import kr.iam.domain.notification.domain.Notification;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;
    private String title;
    private String description;
    private String image;
    private Boolean age;

    //연관관계 주인
//    @OneToOne
//    @JoinColumn(name = "category_id")
//    private Category category;
    @OneToMany(mappedBy = "channel", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private List<UsingCategory> usingCategories = new ArrayList<>();

    @OneToOne(mappedBy = "channel", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "channel")
    private List<Episode> episodes = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Advertisement> advertisementList = new ArrayList<>();

    public void updateChannel(ChannelSaveReqDto channelSaveReqDto, List<UsingCategory> usingCategories,
                              String imageUrl) {
        if(imageUrl != null) {
            this.image = imageUrl;
        }
        this.title = channelSaveReqDto.channelTitle();
        this.description = channelSaveReqDto.channelDescription();
        this.usingCategories.addAll(usingCategories);
        this.age = channelSaveReqDto.ageLimit();
    }
}
