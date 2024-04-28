package kr.iam.domain.category.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.detail_category.domain.DetailCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    @OneToOne(mappedBy = "category", fetch = FetchType.LAZY)
    private Channel channel;

    @OneToMany(mappedBy = "category")
    private List<DetailCategory> detailCategories = new ArrayList<>();

}
