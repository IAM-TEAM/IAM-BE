package kr.iam.domain.member.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Platform {
    @Id @GeneratedValue
    @Column(name = "platform_id")
    private long id;
    private String name;
    @OneToMany(mappedBy = "platform")
    private List<MemberPlatform> memberPlatforms = new ArrayList<>();
}
