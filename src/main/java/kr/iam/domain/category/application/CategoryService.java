package kr.iam.domain.category.application;

import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.category.dao.CategoryRepository;
import kr.iam.domain.category.domain.Category;
import kr.iam.domain.category.dto.CategoryDto;
import kr.iam.domain.category.dto.CategoryMemberDto;
import kr.iam.domain.channel.application.ChannelService;
import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.detail_category.application.DetailCategoryService;
import kr.iam.domain.detail_category.domain.DetailCategory;
import kr.iam.domain.member.application.MemberService;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import kr.iam.global.util.CookieUtil;
import kr.iam.global.util.RssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.iam.domain.category.dto.CategoryDto.*;
import static kr.iam.domain.category.dto.CategoryMemberDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DetailCategoryService detailCategoryService;
    private final MemberService memberService;
    private final RssUtil rssUtil;
    private final CookieUtil cookieUtil;

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

    /**
     * Main에 대한 Sub List 추출
     * @param name
     * @return
     */
    public CategoryResponseDtoList getCategoryResponseDtoList(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        List<CategoryResponseDto> result = detailCategoryService.findAll(category).stream()
                .map(CategoryResponseDto::fromSub)
                .collect(Collectors.toList());

        return CategoryResponseDtoList.builder().list(result).build();
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }

    /**
     * 카테고리 정보 추출
     * 유저 선택 카테고리 추출 -> 해당 서비스는 유저 정보Dto 서비스로 이전될 예정
     * 아마 RssFeed에서 유저의 모든 정보를 꺼내오고 메인 카테고리 기능만 남지 않을까 싶음
     */
    public CategoryMemberResponseDto getMemberCategoryList(String rssFeedUrl) {
        List<String> categories = rssUtil.getCategories(rssFeedUrl);
        String name = categories.get(0);
        DetailCategory byName = detailCategoryService.findByName(name);
        Category category = byName.getCategory();
        return CategoryMemberResponseDto.builder().mainName(category.getName()).subName(name).build();
    }

    public List<String> getMemberCategory(String rssFeedUrl) {
        List<String> categories = rssUtil.getCategories(rssFeedUrl);
        return categories;
    }
}
