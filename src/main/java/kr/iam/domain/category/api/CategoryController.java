package kr.iam.domain.category.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.dto.req.CategorySaveReqDto;
import kr.iam.global.domain.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
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
    public ResponseEntity<SuccessResponse<?>> getCategoryList() {
        return SuccessResponse.ok(categoryService.getCategoryList());
    }

    /**
     * 관리자 용 카테고리 생성 API
     * @param reqDto
     * @return
     */
    @Operation(summary = "카테고리 생성", description = "카테고리 생성(관리자 용)")
    @PostMapping("/admin")
    public ResponseEntity<SuccessResponse<?>> setCategory(@RequestBody CategorySaveReqDto reqDto) {
        return SuccessResponse.ok(categoryService.saveCategory(reqDto));
    }

    /**
     * Main 카테고리 클릭 시 서브 카테고리 리스트 출력
     * @param name
     * @return
     */
    @Operation(summary = "하위 카테고리 추출", description = "하위 카테고리 추출")
    @GetMapping("/sub")
    public ResponseEntity<SuccessResponse<?>> getSubCategoryList(@RequestParam String name) {
        return SuccessResponse.ok(categoryService.getCategoryResponseDtoList(name));
    }
}
