package kr.iam.domain.category.application;

import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.domain.DetailCategory;
import kr.iam.domain.category.dto.info.CategoryInfo;
import kr.iam.domain.category.dto.req.CategorySaveReqDto;
import kr.iam.domain.category.dto.res.CategoryListResDto;
import kr.iam.domain.category.helper.CategoryHelper;
import kr.iam.domain.category.helper.DetailCategoryHelper;
import kr.iam.domain.category.mapper.CategoryMapper;
import kr.iam.global.util.RssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryHelper categoryHelper;
    private final CategoryMapper categoryMapper;
    private final DetailCategoryHelper detailCategoryHelper;
    private final RssUtil rssUtil;

    /**
     * 관리자 카테고리 생성
     * @param categorySaveReqDto
     * @return
     */
    @Transactional
    public Long saveCategory(CategorySaveReqDto categorySaveReqDto) {
        Category category = Category.builder()
                .name(categorySaveReqDto.mainName())
                .build();

        categorySaveReqDto.subNames().forEach(subName -> {
            DetailCategory detailCategory = DetailCategory.builder().name(subName).build();
            category.addDetailCategory(detailCategory);
        });

        Category savedCategory = categoryHelper.save(category);
        return savedCategory.getId();
    }

    /**
     * 전 서버 카테고리 추출
     * @return
     */
    public CategoryListResDto getCategoryList() {
        List<CategoryInfo> categoryInfos = categoryHelper.findCategoryInfos();
        return categoryMapper.toCategoryListResDto(categoryInfos);
    }

    /**
     * Main에 대한 Sub List 추출
     * @param name
     * @return
     */
    public CategoryListResDto getCategoryResponseDtoList(String name) {
        Category category = categoryHelper.findByName(name);

        List<CategoryInfo> result = detailCategoryHelper.findAll(category).stream()
                .map(CategoryInfo::ofSub)
                .collect(Collectors.toList());

        return categoryMapper.toCategoryListResDto(result);
    }

    /**
     * 카테고리 정보 추출
     * 유저 선택 카테고리 추출 -> 해당 서비스는 유저 정보Dto 서비스로 이전될 예정
     * 아마 RssFeed에서 유저의 모든 정보를 꺼내오고 메인 카테고리 기능만 남지 않을까 싶음
     */
//    public CategoryMemberResponseDto getMemberCategoryList(String rssFeedUrl) {
//        List<String> categories = rssUtil.getCategories(rssFeedUrl);
//        String name = categories.get(0);
//        DetailCategory byName = detailCategoryHelper.findByName(name);
//        Category category = byName.getCategory();
//        return CategoryMemberResponseDto.builder().mainName(category.getName()).subName(name).build();
//    }
}
