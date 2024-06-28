package kr.iam.domain.channel.domain;

import jakarta.persistence.*;
import kr.iam.domain.advertisement.domain.Advertisement;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel.dto.ChannelDto;
import kr.iam.domain.episode.domain.Episode;
import kr.iam.domain.notification.domain.Notification;
import kr.iam.domain.member.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static kr.iam.domain.channel.dto.ChannelDto.*;

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
    private List<Advertisement> advertisementList = new ArrayList<>();

    public void updateChannel(ChannelSaveRequestDto channelSaveRequestDto, Category category) {
        this.title = channelSaveRequestDto.getChannelTitle();
        this.description = channelSaveRequestDto.getChannelDescription();
        this.category = category;
        this.age = channelSaveRequestDto.getAgeLimit();
    }
}
