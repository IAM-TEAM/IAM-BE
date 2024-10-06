package kr.iam.domain.category.helper;

import kr.iam.domain.category.dao.CategoryRepository;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.info.CategoryInfo;
import kr.iam.domain.category.error.CategoryError;
import kr.iam.global.error.exception.EntityNotFoundException;
import kr.iam.global.util.RssUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryHelper {
    private final CategoryRepository categoryRepository;
    private final RssUtil rssUtil;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(CategoryError.CATEGORY_NOT_FOUND));
    }

    public List<CategoryInfo> findCategoryInfos() {
        return categoryRepository.findAllCategory().stream().map(CategoryInfo::of).collect(Collectors.toList());
    }

    public Map<String, List<String>> getMemberCategory(String rssFeedUrl) {
        return rssUtil.getCategories(rssFeedUrl);
    }
}