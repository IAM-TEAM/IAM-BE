package kr.iam.domain.member.domain;

import jakarta.persistence.*;

@Entity
public class MemberPlatform {
    @Id @GeneratedValue
    @Column(name = "member_platform_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="platform_id")
    private Platform platform;
}
