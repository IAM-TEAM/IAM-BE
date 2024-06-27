package kr.iam.domain.detail_category.application;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.detail_category.dao.DetailCategoryRepository;
import kr.iam.domain.detail_category.domain.DetailCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class DetailCategoryServiceTest {

    @Autowired
    DetailCategoryService detailCategoryService;

    @MockBean
    DetailCategoryRepository detailCategoryRepository;

    @Test
    void 상세_카테고리_이름으로_찾기() {
        //given
        String subName = "sub1";
        DetailCategory detailCategory = DetailCategory.builder().name(subName).build();
        when(detailCategoryRepository.findByName(subName)).thenReturn(Optional.ofNullable(detailCategory));

        //when
        DetailCategory byName = detailCategoryService.findByName(subName);

        //then
        assertThat(byName.getName()).isEqualTo(subName);
        verify(detailCategoryRepository, times(1)).findByName(subName);
    }

    @Test
    void 카테고리로_상세_카테고리_모두_찾기() {
        //given
        Category category = Category.builder().id(1L).name("main").build();
        List<DetailCategory> detailCategories = List.of(DetailCategory.builder().name("sub1").build(),
                DetailCategory.builder().name("sub2").build());
        when(detailCategoryRepository.findAllByCategory(category)).thenReturn(detailCategories);

        //when
        List<DetailCategory> result = detailCategoryService.findAll(category);

        //then
        assertThat(result).hasSize(2);
    }
}