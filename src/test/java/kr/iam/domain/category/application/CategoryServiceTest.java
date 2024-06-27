package kr.iam.domain.category.application;

import kr.iam.domain.category.dao.CategoryRepository;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.CategoryDto;
import kr.iam.domain.detail_category.application.DetailCategoryService;
import kr.iam.domain.detail_category.domain.DetailCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.iam.domain.category.dto.CategoryDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @MockBean
    DetailCategoryService detailCategoryService;

    @MockBean
    CategoryRepository categoryRepository;

    @Test
    void 카테고리_저장() {
        //given
        String name = "test";
        List<String> subNames = List.of("test1", "test2");
        CategorySaveRequestDto requestDto =
                CategorySaveRequestDto.builder().mainName(name).subName(subNames).build();
        Category category = Category.builder().name(requestDto.getMainName()).build();
        category.addDetailCategory(DetailCategory.builder().name(requestDto.getSubName().get(0)).build());
        category.addDetailCategory(DetailCategory.builder().name(requestDto.getSubName().get(1)).build());

        Category saved = Category.builder().id(1L).name(requestDto.getMainName()).build();
        saved.addDetailCategory(DetailCategory.builder().name(requestDto.getSubName().get(0)).build());
        saved.addDetailCategory(DetailCategory.builder().name(requestDto.getSubName().get(1)).build());
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        //when
        Long categoryId = categoryService.saveCategory(requestDto);

        //then
        assertEquals(1L, categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void 카테고리_추출() {
        //given
        List<Category> tmp = List.of(Category.builder().id(1L).build(), Category.builder().id(2L).build());
        when(categoryRepository.findAllCategory()).thenReturn(tmp);

        //when
        CategoryResponseDtoList result = categoryService.getCategoryList();

        //then
        assertThat(result).isNotNull();
        assertThat(result.getList().size()).isEqualTo(2);
    }

    @Test
    void Sub카테고리_리스트_추출() {
        //given
        String name = "test";
        Category category = Category.builder().id(1L).name(name).build();
        List<CategoryResponseDto> result = List.of(CategoryResponseDto.builder().name("sub1").build(),
                CategoryResponseDto.builder().name("sub2").build());
        List<DetailCategory> subList = List.of(DetailCategory.builder().name("sub1").build(),
                DetailCategory.builder().name("sub2").build());
        when(categoryRepository.findByName(name)).thenReturn(Optional.ofNullable(category));
        when(detailCategoryService.findAll(category)).thenReturn(subList);

        //when
        CategoryResponseDtoList categoryResponseDtoList = categoryService.getCategoryResponseDtoList(name);

        //then
        assertThat(categoryResponseDtoList).isNotNull();
        assertThat(categoryResponseDtoList.getList().size()).isEqualTo(2);
    }
}