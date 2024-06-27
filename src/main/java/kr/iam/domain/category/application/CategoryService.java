package kr.iam.domain.category.application;

import kr.iam.domain.category.dao.CategoryRepository;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.CategoryDto;
import kr.iam.domain.detail_category.application.DetailCategoryService;
import kr.iam.domain.detail_category.domain.DetailCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.iam.domain.category.dto.CategoryDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final DetailCategoryService detailCategoryService;
    /**
     * 관리자 카테고리 생성
     * @param categorySaveRequestDto
     * @return
     */
    @Transactional
    public Long saveCategory(CategorySaveRequestDto categorySaveRequestDto) {
        Category category = Category.builder()
                .name(categorySaveRequestDto.getMainName())
                .build();

        categorySaveRequestDto.getSubName().forEach(subName -> {
            DetailCategory detailCategory = DetailCategory.builder().name(subName).build();
            category.addDetailCategory(detailCategory);
        });

        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getId();
    }

    /**
     * 전 서버 카테고리 추출
     * @return
     */
    public CategoryResponseDtoList getCategoryList() {
        List<CategoryResponseDto> responseDtoList = categoryRepository.findAllCategory().stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());

        return CategoryResponseDtoList.builder().list(responseDtoList).build();
    }
}
