package kr.iam.domain.Channel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailCategory {
    @Id @GeneratedValue
    @Column(name = "detail_category_id")
    private long id;
    private String name;

    //연관관계 주인
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
