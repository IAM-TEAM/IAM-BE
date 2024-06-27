package kr.iam.domain.category.api;

import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.iam.domain.category.dto.CategoryDto.*;

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
    @GetMapping
    public ResponseEntity<CategoryResponseDtoList> getCategoryList() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    /**
     * 관리자 용 카테고리 생성 API
     * @param categorySaveRequestDto
     * @return
     */
    @PostMapping("/admin")
    public ResponseEntity<String> setCategory(@RequestBody CategorySaveRequestDto categorySaveRequestDto) {
        return ResponseEntity.ok(categoryService.saveCategory(categorySaveRequestDto) + " created");
    }
}
