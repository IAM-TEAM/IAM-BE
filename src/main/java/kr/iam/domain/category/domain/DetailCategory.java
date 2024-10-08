package kr.iam.domain.category.domain;

import jakarta.persistence.*;
import kr.iam.global.domain.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCategory extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "detail_category_id")
    private long id;
    private String name;

    //연관관계 주인
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
