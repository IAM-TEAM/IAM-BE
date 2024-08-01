package kr.iam.domain.category.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.detail_category.domain.DetailCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

//    @OneToOne(mappedBy = "category", fetch = FetchType.LAZY)
//    private Channel channel;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailCategory> detailCategories = new ArrayList<>();

    public void addDetailCategory(DetailCategory detailCategory) {
        this.detailCategories.add(detailCategory);
        detailCategory.setCategory(this);
    }
}
