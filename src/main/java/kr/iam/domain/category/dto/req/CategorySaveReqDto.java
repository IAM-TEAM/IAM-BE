package kr.iam.domain.category.dto.req;

import java.util.List;

public record CategorySaveReqDto(
        String mainName,
        List<String> subNames
) {
}
