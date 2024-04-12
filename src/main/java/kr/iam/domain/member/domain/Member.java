package kr.iam.domain.member.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.Channel.domain.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;

    private String username;
    private String password;
    private String image;
    private String rssFeed;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    //연관관계 주인
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "member")
    private List<MemberPlatform> memberPlatforms = new ArrayList<>();

}
