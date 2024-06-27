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
@NoArgsConstructor
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    @OneToOne(mappedBy = "category", fetch = FetchType.LAZY)
    private Channel channel;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailCategory> detailCategories = new ArrayList<>();

    @Builder
    public Category(Long id, String name, Channel channel, List<DetailCategory> detailCategories) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.detailCategories = new ArrayList<>();
    }

    public void addDetailCategory(DetailCategory detailCategory) {
        this.detailCategories.add(detailCategory);
        detailCategory.setCategory(this);
    }
}
