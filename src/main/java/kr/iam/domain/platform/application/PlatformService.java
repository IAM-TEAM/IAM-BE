package kr.iam.domain.platform.application;

import kr.iam.domain.platform.domain.Platform;
import kr.iam.domain.platform.dto.res.PlatformResDto;
import kr.iam.domain.platform.helper.PlatformHelper;
import kr.iam.domain.platform.mapper.PlatformMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatformService {

    private final PlatformHelper platformHelper;
    private final PlatformMapper platformMapper;

    public PlatformResDto getPlatform(Long platformId) {
        Platform platform = platformHelper.findById(platformId);
        return platformMapper.toPlatformResDto(platform.getLink());
    }
}
