package kr.iam.domain.platform.domain;

import jakarta.persistence.*;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String link;
    @OneToMany(mappedBy = "platform")
    private List<UsingPlatform> usingPlatforms = new ArrayList<>();
}
