package kr.iam.domain.category.domain;

import jakarta.persistence.*;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.global.domain.BaseTimeEntity;
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
