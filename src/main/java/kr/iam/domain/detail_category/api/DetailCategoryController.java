package kr.iam.domain.detail_category.api;

import kr.iam.domain.category.application.CategoryService;
import kr.iam.domain.detail_category.application.DetailCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.iam.domain.category.dto.CategoryDto.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Slf4j
public class DetailCategoryController {

    private final DetailCategoryService detailCategoryService;
}
