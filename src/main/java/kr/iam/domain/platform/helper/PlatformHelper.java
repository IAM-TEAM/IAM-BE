package kr.iam.domain.platform.helper;

import kr.iam.domain.platform.dao.PlatformRepository;
import kr.iam.domain.platform.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlatformHelper {
    public final PlatformRepository platformRepository;

    public Platform findById(Long id) {
        return platformRepository.findById(id).orElse(null);
    }
}
