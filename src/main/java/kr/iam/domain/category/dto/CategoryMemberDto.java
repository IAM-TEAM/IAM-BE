package kr.iam.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryMemberDto {

    @Getter
    @NoArgsConstructor
    public static class CategoryMemberResponseDto {
        private String mainName;
        private String subName;

        @Builder
        public CategoryMemberResponseDto(String mainName, String subName) {
            this.mainName = mainName;
            this.subName = subName;
        }
    }
}
