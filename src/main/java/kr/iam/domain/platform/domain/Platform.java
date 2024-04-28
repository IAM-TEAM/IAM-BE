package kr.iam.domain.platform.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.using_platform.domain.UsingPlatform;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Platform extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "platform_id")
    private long id;
    private String name;
    @OneToMany(mappedBy = "platform")
    private List<UsingPlatform> usingPlatforms = new ArrayList<>();
}
