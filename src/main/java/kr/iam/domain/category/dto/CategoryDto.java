package kr.iam.domain.category.dto;

import kr.iam.domain.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryDto {

    @Getter
    @NoArgsConstructor
    public static class CategorySaveRequestDto {
        private String mainName;
        private List<String> subName;

        @Builder
        public CategorySaveRequestDto(String mainName, List<String> subName) {
            this.mainName = mainName;
            this.subName = subName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryResponseDtoList {
        private List<CategoryResponseDto> list;

        @Builder
        public CategoryResponseDtoList(List<CategoryResponseDto> list) {
            this.list = list;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryResponseDto {
        private Long id;
        private String name;

        @Builder
        public CategoryResponseDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static CategoryResponseDto from(Category category) {
            return CategoryResponseDto.builder().id(category.getId()).name(category.getName()).build();
        }
    }
}
