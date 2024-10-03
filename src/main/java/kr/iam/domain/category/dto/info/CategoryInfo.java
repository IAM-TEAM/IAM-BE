package kr.iam.domain.category.dto.info;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.domain.DetailCategory;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CategoryInfo(
    Long id,
    String name
) {
    public static CategoryInfo of(Category category) {
        return CategoryInfo.builder().id(category.getId()).name(category.getName()).build();
    }

    public static CategoryInfo ofSub(DetailCategory detailCategory) {
        return CategoryInfo.builder().id(detailCategory.getId()).name(detailCategory.getName()).build();
    }
}