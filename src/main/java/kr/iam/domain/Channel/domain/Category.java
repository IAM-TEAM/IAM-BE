package kr.iam.domain.Channel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    @OneToOne(mappedBy = "category", fetch = FetchType.LAZY)
    private Channel channel;

    @OneToMany(mappedBy = "category")
    private List<DetailCategory> detailCategories = new ArrayList<>();

}
