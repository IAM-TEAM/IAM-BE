package kr.iam.domain.category.helper;

import kr.iam.domain.category.dao.DetailCategoryRepository;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.domain.DetailCategory;
import kr.iam.domain.category.error.CategoryError;
import kr.iam.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DetailCategoryHelper {
    private final DetailCategoryRepository detailCategoryRepository;

    public DetailCategory findByName(String name) {
        return detailCategoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(CategoryError.CATEGORY_NOT_FOUND));
    }

    public List<DetailCategory> findAll(Category category) {
        return detailCategoryRepository.findAllByCategory(category);
    }
}
