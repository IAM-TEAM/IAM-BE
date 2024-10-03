package kr.iam.domain.platform.dto.res;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlatformResDto(
        String link
) {
    public static PlatformResDto of(String link) {
        return PlatformResDto.builder().link(link).build();
    }
}
