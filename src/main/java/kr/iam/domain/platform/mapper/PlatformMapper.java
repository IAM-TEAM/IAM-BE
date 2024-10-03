package kr.iam.domain.platform.mapper;

import kr.iam.domain.platform.dto.res.PlatformResDto;
import org.springframework.stereotype.Component;

@Component
public class PlatformMapper {
    public PlatformResDto toPlatformResDto(String link) {
        return PlatformResDto.of(link);
    }
}
