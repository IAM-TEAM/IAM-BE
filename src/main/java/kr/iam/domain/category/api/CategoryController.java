package kr.iam.domain.category.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.dto.CategoryDto;
import kr.iam.domain.category.dto.CategoryMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.iam.domain.category.dto.CategoryDto.*;
import static kr.iam.domain.category.dto.CategoryMemberDto.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 리스트 전부 추출(초기 생성 용)
     * @return
     */
    @Operation(summary = "카테고리 리스트", description = "카테고리 전부 추출(채널 상관 없이)")
    @GetMapping
    public ResponseEntity<CategoryResponseDtoList> getCategoryList() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    /**
     * 관리자 용 카테고리 생성 API
     * @param categorySaveRequestDto
     * @return
     */
    @Operation(summary = "카테고리 생성", description = "카테고리 생성(관리자 용)")
    @PostMapping("/admin")
    public ResponseEntity<String> setCategory(@RequestBody CategorySaveRequestDto categorySaveRequestDto) {
        return ResponseEntity.ok(categoryService.saveCategory(categorySaveRequestDto) + " created");
    }

    /**
     * Main 카테고리 클릭 시 서브 카테고리 리스트 출력
     * @param name
     * @return
     */
    @Operation(summary = "하위 카테고리 추출", description = "하위 카테고리 추출")
    @GetMapping("/sub")
    public ResponseEntity<CategoryResponseDtoList> getSubCategoryList(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getCategoryResponseDtoList(name));
    }

    @Operation(summary = "Member 카테고리 추출",
            description = "Member의 채널 카테고리 추출(RSS Feed에서 값을 꺼내올 지 아니면 ChannelId를 통해서 가져올 지 고민중")
    @GetMapping("/member")
    public ResponseEntity<CategoryMemberResponseDto> getCategoryByMemberId(HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.getMemberCategoryList(request));
    }
}
