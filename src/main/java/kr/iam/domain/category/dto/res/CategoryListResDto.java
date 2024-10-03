package kr.iam.domain.category.dto.res;

import kr.iam.domain.category.dto.info.CategoryInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CategoryListResDto(
        List<CategoryInfo> categoryInfos
) {
    public static CategoryListResDto of(List<CategoryInfo> categoryInfos) {
        return CategoryListResDto.builder().categoryInfos(categoryInfos).build();
    }
}
