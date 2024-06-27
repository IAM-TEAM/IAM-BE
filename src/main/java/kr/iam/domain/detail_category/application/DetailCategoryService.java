package kr.iam.domain.detail_category.application;

import kr.iam.domain.detail_category.dao.DetailCategoryRepository;
import kr.iam.domain.detail_category.domain.DetailCategory;
import kr.iam.global.exception.BusinessLogicException;
import kr.iam.global.exception.code.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.iam.domain.category.dto.CategoryDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailCategoryService {

    private final DetailCategoryRepository detailCategoryRepository;

    public DetailCategory findByName(String subName) {
        return detailCategoryRepository.findByName(subName)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }
}
