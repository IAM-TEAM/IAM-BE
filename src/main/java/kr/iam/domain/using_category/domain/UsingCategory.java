package kr.iam.domain.using_category.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.channel.domain.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UsingCategory extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "using_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static UsingCategory createUsingCategory(Channel channel, Category category) {
        return UsingCategory
                .builder()
                .channel(channel)
                .category(category)
                .build();
    }
}
