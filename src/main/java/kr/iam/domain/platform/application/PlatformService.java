package kr.iam.domain.platform.application;

import kr.iam.domain.platform.dao.PlatformRepository;
import kr.iam.domain.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatformService {

    private final PlatformRepository platformRepository;

    public String getPlatform(Long platformId) {
        Optional<Platform> platform = platformRepository.findById(platformId);
        return platform.get().getLink();
    }
}
