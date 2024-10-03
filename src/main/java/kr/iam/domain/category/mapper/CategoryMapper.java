package kr.iam.domain.category.mapper;

import kr.iam.domain.category.dto.info.CategoryInfo;
import kr.iam.domain.category.dto.res.CategoryListResDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    public CategoryListResDto toCategoryListResDto(List<CategoryInfo> categoryInfos) {
        return CategoryListResDto.of(categoryInfos);
    }
}
