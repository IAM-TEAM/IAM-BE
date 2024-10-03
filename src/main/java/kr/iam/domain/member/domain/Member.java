package kr.iam.domain.member.domain;

import jakarta.persistence.*;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.platform.domain.UsingPlatform;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    //private String password;
    @Setter
    private String name;
    @Setter
    private String email;
    private String image;
    private String rssFeed;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    //연관관계 주인
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "member")
    private List<UsingPlatform> usingPlatforms = new ArrayList<>();

}
