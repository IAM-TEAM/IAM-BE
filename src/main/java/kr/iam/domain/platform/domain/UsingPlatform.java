package kr.iam.domain.platform.domain;

import jakarta.persistence.*;
import kr.iam.domain.member.domain.Member;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UsingPlatform extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "using_platform_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="platform_id")
    private Platform platform;
}
